package com.unstable.ultimate.bot;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public class UnstablePvpBotEntity extends PathAwareEntity {
    private static final List<String> MODES = List.of("mace", "sword", "crystal", "uhc");
    private static final List<String> TIERS = List.of("LT5", "HT5", "LT4", "HT4", "LT3", "HT3", "LT2", "HT2", "LT1", "HT1");

    private UUID targetPlayerUuid;
    private String pvpMode = "mace";
    private String tier = "HT1";
    private String kitName = "wemmbu";
    private int attackCooldownTicks = 0;

    public UnstablePvpBotEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createBotAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.MAX_HEALTH, 20.0)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.28)
                .add(EntityAttributes.ATTACK_DAMAGE, 7.0)
                .add(EntityAttributes.FOLLOW_RANGE, 32.0);
    }

    public void configureBot(UUID ownerUuid, String mode, String tier, String kit) {
        this.targetPlayerUuid = ownerUuid;
        this.pvpMode = MODES.contains(mode.toLowerCase()) ? mode.toLowerCase() : "mace";
        this.tier = TIERS.contains(tier.toUpperCase()) ? tier.toUpperCase() : "HT1";
        this.kitName = kit;
        this.setCustomName(Text.literal("§c[" + this.tier + "] §fUnstable Bot §7(" + this.pvpMode + ")"));
        this.setCustomNameVisible(true);
        applyTierStatsAndLoadout();
    }

    private void applyTierStatsAndLoadout() {
        int tierIndex = Math.max(0, TIERS.indexOf(this.tier));
        double speed = 0.24 + (tierIndex * 0.012);
        double damage = 4.0 + (tierIndex * 0.75);

        var speedAttr = this.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED);
        var dmgAttr = this.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE);
        if (speedAttr != null) speedAttr.setBaseValue(speed);
        if (dmgAttr != null) dmgAttr.setBaseValue(damage);

        this.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.NETHERITE_HELMET));
        this.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.NETHERITE_CHESTPLATE));
        this.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.NETHERITE_LEGGINGS));
        this.equipStack(EquipmentSlot.FEET, new ItemStack(Items.NETHERITE_BOOTS));

        switch (pvpMode) {
            case "mace" -> this.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.MACE));
            case "crystal" -> this.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.END_CRYSTAL));
            case "uhc" -> this.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.FISHING_ROD));
            default -> this.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.NETHERITE_SWORD));
        }
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.25, false));
        this.goalSelector.add(2, new LookAtEntityGoal(this, PlayerEntity.class, 16.0f));
        this.goalSelector.add(3, new LookAroundGoal(this));

        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true,
                (entity, world) -> targetPlayerUuid == null || entity.getUuid().equals(targetPlayerUuid)));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getEntityWorld().isClient()) return;

        if (attackCooldownTicks > 0) {
            attackCooldownTicks--;
        }

        LivingEntity target = this.getTarget();
        if (target != null && target.isAlive() && this.distanceTo(target) < 3.6f && attackCooldownTicks == 0) {
            this.tryAttack(target);
            int tierIndex = Math.max(0, TIERS.indexOf(this.tier));
            this.attackCooldownTicks = Math.max(4, 14 - tierIndex);
        }
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (!this.getEntityWorld().isClient() && hand == Hand.MAIN_HAND) {
            int nextModeIdx = (MODES.indexOf(this.pvpMode) + 1) % MODES.size();
            int nextTierIdx = (TIERS.indexOf(this.tier) + 1) % TIERS.size();
            configureBot(player.getUuid(), MODES.get(nextModeIdx), TIERS.get(nextTierIdx), this.kitName);
            player.sendMessage(Text.literal("§d[Bot Config] §fMode: §b" + this.pvpMode + " §f| Tier: §e" + this.tier), false);
            return ActionResult.SUCCESS;
        }
        return super.interactMob(player, hand);
    }
}
