package com.unstable.ultimate;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.unstable.ultimate.bot.UnstablePvpBotEntity;
import com.unstable.ultimate.gui.UnstableMenuHandler;
import com.unstable.ultimate.world.UnstableArenaBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class UnstableUltimateMod implements ModInitializer {
    public static final String MOD_ID = "unstable_ultimate";

    public static final RegistryKey<EntityType<?>> PVP_BOT_KEY = RegistryKey.of(
            RegistryKeys.ENTITY_TYPE,
            Identifier.of(MOD_ID, "pvp_bot")
    );

    public static final EntityType<UnstablePvpBotEntity> PVP_BOT = Registry.register(
            Registries.ENTITY_TYPE,
            PVP_BOT_KEY,
            EntityType.Builder.create(UnstablePvpBotEntity::new, SpawnGroup.MONSTER)
                    .dimensions(0.6f, 1.8f)
                    .build(PVP_BOT_KEY)
    );

    @Override
    public void onInitialize() {
        FabricDefaultAttributeRegistry.register(PVP_BOT, UnstablePvpBotEntity.createBotAttributes());
        UnstableMenuHandler.register();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("kit")
                    .executes(ctx -> {
                        ServerPlayerEntity player = ctx.getSource().getPlayerOrThrow();
                        UnstableMenuHandler.openMainGui(player, 0);
                        return 1;
                    }));

            dispatcher.register(CommandManager.literal("dobuild")
                    .requires(src -> src.hasPermissionLevel(2))
                    .executes(ctx -> {
                        ServerWorld world = ctx.getSource().getWorld();
                        BlockPos pos = BlockPos.ofFloored(ctx.getSource().getPosition());
                        UnstableArenaBuilder.buildSpawnAndArena(world, pos);
                        ctx.getSource().sendFeedback(() -> Text.literal("§d[Unstable] §fGenerated Spawn & Farlands PvP Arena!"), true);
                        return 1;
                    })
                    .then(CommandManager.argument("pos", BlockPosArgumentType.blockPos())
                            .executes(ctx -> {
                                ServerWorld world = ctx.getSource().getWorld();
                                BlockPos pos = BlockPosArgumentType.getBlockPos(ctx.getSource(), "pos");
                                UnstableArenaBuilder.buildSpawnAndArena(world, pos);
                                ctx.getSource().sendFeedback(() -> Text.literal("§d[Unstable] §fGenerated Arena at " + pos.toShortString()), true);
                                return 1;
                            })));

            dispatcher.register(CommandManager.literal("bot")
                    .executes(ctx -> spawnBot(ctx.getSource().getWorld(), ctx.getSource().getPlayerOrThrow(), "mace", "HT1"))
                    .then(CommandManager.argument("mode", StringArgumentType.word())
                            .executes(ctx -> spawnBot(ctx.getSource().getWorld(), ctx.getSource().getPlayerOrThrow(),
                                    StringArgumentType.getString(ctx, "mode"), "HT1"))
                            .then(CommandManager.argument("tier", StringArgumentType.word())
                                    .executes(ctx -> spawnBot(ctx.getSource().getWorld(), ctx.getSource().getPlayerOrThrow(),
                                            StringArgumentType.getString(ctx, "mode"),
                                            StringArgumentType.getString(ctx, "tier"))))));
        });
    }

    private static int spawnBot(ServerWorld world, ServerPlayerEntity owner, String mode, String tier) {
        UnstablePvpBotEntity bot = new UnstablePvpBotEntity(PVP_BOT, world);
        bot.refreshPositionAndAngles(owner.getX() + 3.0, owner.getY(), owner.getZ() + 3.0, owner.getYaw(), 0.0f);
        bot.configureBot(owner.getUuid(), mode, tier, "wemmbu");
        world.spawnEntity(bot);
        owner.sendMessage(Text.literal("§d[Unstable Bot] §fSpawned §e" + tier.toUpperCase() + " §fbot in §b" + mode.toUpperCase() + " §fmode. Right-click it to edit kit/tier!"), false);
        return 1;
    }
}
