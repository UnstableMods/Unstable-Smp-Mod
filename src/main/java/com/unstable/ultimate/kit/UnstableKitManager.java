package com.unstable.ultimate.kit;

import net.minecraft.block.entity.BannerPattern;
import net.minecraft.block.entity.BannerPatterns;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BannerPatternsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.equipment.trim.ArmorTrim;
import net.minecraft.item.equipment.trim.ArmorTrimMaterial;
import net.minecraft.item.equipment.trim.ArmorTrimMaterials;
import net.minecraft.item.equipment.trim.ArmorTrimPattern;
import net.minecraft.item.equipment.trim.ArmorTrimPatterns;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;

import java.util.List;

public class UnstableKitManager {

    public static void applyKit(ServerPlayerEntity player, String kitId) {
        player.getInventory().clear();
        MinecraftServer server = player.getEntityWorld().getServer();
        if (server == null) return;

        switch (kitId.toLowerCase()) {
            case "spoke" -> applySpokeKit(player, server);
            case "wemmbu" -> applyWemmbuKit(player, server);
            case "flame" -> applyFlameKit(player, server);
            case "parrot" -> applyParrotKit(player, server);
            case "jaden" -> applyJadenKit(player, server);
            case "mafia" -> applyMafiaKit(player, server);
            default -> applyWemmbuKit(player, server);
        }
        player.playerScreenHandler.sendContentUpdates();
    }

    private static void applySpokeKit(ServerPlayerEntity player, MinecraftServer server) {
        ItemStack helm = createTrimmedArmor(server, Items.NETHERITE_HELMET, ArmorTrimPatterns.FLOW, ArmorTrimMaterials.AMETHYST);
        ItemStack chest = createTrimmedArmor(server, Items.NETHERITE_CHESTPLATE, ArmorTrimPatterns.SNOUT, ArmorTrimMaterials.REDSTONE);
        ItemStack legs = createTrimmedArmor(server, Items.NETHERITE_LEGGINGS, ArmorTrimPatterns.FLOW, ArmorTrimMaterials.AMETHYST);
        ItemStack boots = createTrimmedArmor(server, Items.NETHERITE_BOOTS, ArmorTrimPatterns.FLOW, ArmorTrimMaterials.AMETHYST);

        enchant(server, boots, Enchantments.DEPTH_STRIDER, 3);
        enchant(server, boots, Enchantments.SOUL_SPEED, 3);
        enchant(server, boots, Enchantments.FEATHER_FALLING, 4);
        equipArmor(player, helm, chest, legs, boots);

        ItemStack sword = new ItemStack(Items.NETHERITE_SWORD);
        enchant(server, sword, Enchantments.SHARPNESS, 5);
        enchant(server, sword, Enchantments.UNBREAKING, 3);

        player.getInventory().setStack(0, sword);
        player.getInventory().setStack(1, new ItemStack(Items.ENDER_PEARL, 16));
        player.getInventory().setStack(2, new ItemStack(Items.TOTEM_OF_UNDYING, 1));
        player.getInventory().setStack(3, new ItemStack(Items.ENCHANTED_GOLDEN_APPLE, 16));
        player.getInventory().setStack(4, new ItemStack(Items.ELYTRA));
        player.getInventory().setStack(5, new ItemStack(Items.FIREWORK_ROCKET, 64));
        player.getInventory().setStack(8, createBannerShield(server, DyeColor.BLACK,
                List.of(
                        new BannerPatternsComponent.Layer(entry(server, BannerPatterns.CIRCLE), DyeColor.RED),
                        new BannerPatternsComponent.Layer(entry(server, BannerPatterns.TRIANGLES_BOTTOM), DyeColor.RED)
                ), "§cSpoke / Mafia Shield"));
    }

    private static void applyWemmbuKit(ServerPlayerEntity player, MinecraftServer server) {
        ItemStack helm = createTrimmedArmor(server, Items.NETHERITE_HELMET, ArmorTrimPatterns.SILENCE, ArmorTrimMaterials.AMETHYST);
        ItemStack chest = createTrimmedArmor(server, Items.NETHERITE_CHESTPLATE, ArmorTrimPatterns.SILENCE, ArmorTrimMaterials.AMETHYST);
        ItemStack legs = createTrimmedArmor(server, Items.NETHERITE_LEGGINGS, ArmorTrimPatterns.SILENCE, ArmorTrimMaterials.AMETHYST);
        ItemStack boots = createTrimmedArmor(server, Items.NETHERITE_BOOTS, ArmorTrimPatterns.SILENCE, ArmorTrimMaterials.AMETHYST);
        equipArmor(player, helm, chest, legs, boots);

        ItemStack gambit = new ItemStack(Items.MACE);
        gambit.set(DataComponentTypes.CUSTOM_NAME, Text.literal("§5§lGambit"));
        enchant(server, gambit, Enchantments.DENSITY, 5);
        enchant(server, gambit, Enchantments.WIND_BURST, 3);
        enchant(server, gambit, Enchantments.UNBREAKING, 3);

        player.getInventory().setStack(0, gambit);
        player.getInventory().setStack(1, new ItemStack(Items.WIND_CHARGE, 64));
        player.getInventory().setStack(2, new ItemStack(Items.ENDER_PEARL, 16));
        player.getInventory().setStack(3, new ItemStack(Items.ENCHANTED_GOLDEN_APPLE, 16));
        player.getInventory().setStack(4, new ItemStack(Items.TOTEM_OF_UNDYING, 1));
        player.getInventory().setStack(5, new ItemStack(Items.ELYTRA));
        player.getInventory().setStack(8, createBannerShield(server, DyeColor.PURPLE,
                List.of(
                        new BannerPatternsComponent.Layer(entry(server, BannerPatterns.HALF_HORIZONTAL), DyeColor.PURPLE),
                        new BannerPatternsComponent.Layer(entry(server, BannerPatterns.PIGLIN), DyeColor.BLACK)
                ), "§5Wemmbu Banner Shield"));
    }

    private static void applyFlameKit(ServerPlayerEntity player, MinecraftServer server) {
        ItemStack helm = createTrimmedArmor(server, Items.NETHERITE_HELMET, ArmorTrimPatterns.WILD, ArmorTrimMaterials.GOLD);
        ItemStack chest = createTrimmedArmor(server, Items.NETHERITE_CHESTPLATE, ArmorTrimPatterns.WILD, ArmorTrimMaterials.GOLD);
        ItemStack legs = createTrimmedArmor(server, Items.NETHERITE_LEGGINGS, ArmorTrimPatterns.WILD, ArmorTrimMaterials.GOLD);
        ItemStack boots = createTrimmedArmor(server, Items.NETHERITE_BOOTS, ArmorTrimPatterns.WILD, ArmorTrimMaterials.GOLD);
        equipArmor(player, helm, chest, legs, boots);

        ItemStack sword = new ItemStack(Items.NETHERITE_SWORD);
        enchant(server, sword, Enchantments.SHARPNESS, 5);
        enchant(server, sword, Enchantments.FIRE_ASPECT, 2);

        ItemStack axe = new ItemStack(Items.NETHERITE_AXE);
        enchant(server, axe, Enchantments.SHARPNESS, 5);
        enchant(server, axe, Enchantments.EFFICIENCY, 5);

        player.getInventory().setStack(0, sword);
        player.getInventory().setStack(1, axe);
        player.getInventory().setStack(2, new ItemStack(Items.FISHING_ROD));
        player.getInventory().setStack(3, new ItemStack(Items.BOW));
        player.getInventory().setStack(4, new ItemStack(Items.ARROW, 64));
        player.getInventory().setStack(5, new ItemStack(Items.ENCHANTED_GOLDEN_APPLE, 16));
        player.getInventory().setStack(6, new ItemStack(Items.ENDER_PEARL, 16));
        player.getInventory().setStack(8, new ItemStack(Items.SHIELD));
    }

    private static void applyParrotKit(ServerPlayerEntity player, MinecraftServer server) {
        ItemStack helm = createTrimmedArmor(server, Items.NETHERITE_HELMET, ArmorTrimPatterns.SPIRE, ArmorTrimMaterials.GOLD);
        ItemStack chest = createTrimmedArmor(server, Items.NETHERITE_CHESTPLATE, ArmorTrimPatterns.SPIRE, ArmorTrimMaterials.GOLD);
        ItemStack legs = createTrimmedArmor(server, Items.NETHERITE_LEGGINGS, ArmorTrimPatterns.SPIRE, ArmorTrimMaterials.GOLD);
        ItemStack boots = createTrimmedArmor(server, Items.NETHERITE_BOOTS, ArmorTrimPatterns.SPIRE, ArmorTrimMaterials.GOLD);
        equipArmor(player, helm, chest, legs, boots);

        ItemStack sword = new ItemStack(Items.NETHERITE_SWORD);
        enchant(server, sword, Enchantments.SHARPNESS, 5);

        ItemStack spyglass = new ItemStack(Items.SPYGLASS);
        spyglass.set(DataComponentTypes.CUSTOM_NAME, Text.literal("§6Parrot's Spyglass"));

        player.getInventory().setStack(0, sword);
        player.getInventory().setStack(1, spyglass);
        player.getInventory().setStack(2, new ItemStack(Items.MACE));
        player.getInventory().setStack(3, new ItemStack(Items.ENCHANTED_GOLDEN_APPLE, 16));
        player.getInventory().setStack(4, new ItemStack(Items.ENDER_PEARL, 16));
    }

    private static void applyJadenKit(ServerPlayerEntity player, MinecraftServer server) {
        ItemStack helm = createTrimmedArmor(server, Items.NETHERITE_HELMET, ArmorTrimPatterns.RIB, ArmorTrimMaterials.IRON);
        ItemStack chest = createTrimmedArmor(server, Items.NETHERITE_CHESTPLATE, ArmorTrimPatterns.RIB, ArmorTrimMaterials.IRON);
        ItemStack legs = createTrimmedArmor(server, Items.NETHERITE_LEGGINGS, ArmorTrimPatterns.RIB, ArmorTrimMaterials.IRON);
        ItemStack boots = createTrimmedArmor(server, Items.NETHERITE_BOOTS, ArmorTrimPatterns.RIB, ArmorTrimMaterials.IRON);
        equipArmor(player, helm, chest, legs, boots);

        ItemStack crucible = new ItemStack(Items.MACE);
        crucible.set(DataComponentTypes.CUSTOM_NAME, Text.literal("§c§lCrucible"));
        enchant(server, crucible, Enchantments.BREACH, 4);
        enchant(server, crucible, Enchantments.FIRE_ASPECT, 2);

        ItemStack treasure = new ItemStack(Items.TRIDENT);
        treasure.set(DataComponentTypes.CUSTOM_NAME, Text.literal("§b§lTreasure"));
        enchant(server, treasure, Enchantments.RIPTIDE, 3);
        enchant(server, treasure, Enchantments.IMPALING, 5);

        player.getInventory().setStack(0, crucible);
        player.getInventory().setStack(1, treasure);
        player.getInventory().setStack(2, new ItemStack(Items.WATER_BUCKET));
        player.getInventory().setStack(3, new ItemStack(Items.ENCHANTED_GOLDEN_APPLE, 16));
        player.getInventory().setStack(4, new ItemStack(Items.ENDER_PEARL, 16));
    }

    private static void applyMafiaKit(ServerPlayerEntity player, MinecraftServer server) {
        ItemStack helm = createTrimmedArmor(server, Items.NETHERITE_HELMET, ArmorTrimPatterns.EYE, ArmorTrimMaterials.DIAMOND);
        ItemStack chest = createTrimmedArmor(server, Items.NETHERITE_CHESTPLATE, ArmorTrimPatterns.EYE, ArmorTrimMaterials.DIAMOND);
        ItemStack legs = createTrimmedArmor(server, Items.NETHERITE_LEGGINGS, ArmorTrimPatterns.EYE, ArmorTrimMaterials.DIAMOND);
        ItemStack boots = createTrimmedArmor(server, Items.NETHERITE_BOOTS, ArmorTrimPatterns.EYE, ArmorTrimMaterials.DIAMOND);
        equipArmor(player, helm, chest, legs, boots);

        ItemStack mace = new ItemStack(Items.MACE);
        enchant(server, mace, Enchantments.DENSITY, 5);

        player.getInventory().setStack(0, mace);
        player.getInventory().setStack(1, new ItemStack(Items.ENDER_PEARL, 16));
        player.getInventory().setStack(2, new ItemStack(Items.TOTEM_OF_UNDYING, 2));
        player.getInventory().setStack(3, new ItemStack(Items.GOLDEN_APPLE, 32));
    }

    public static ItemStack createTrimmedArmor(
            MinecraftServer server,
            Item armorItem,
            RegistryKey<ArmorTrimPattern> patternKey,
            RegistryKey<ArmorTrimMaterial> materialKey
    ) {
        ItemStack stack = new ItemStack(armorItem);
        var registryManager = server.getRegistryManager();
        RegistryEntry<ArmorTrimPattern> pattern = registryManager.getOrThrow(RegistryKeys.TRIM_PATTERN).getOrThrow(patternKey);
        RegistryEntry<ArmorTrimMaterial> material = registryManager.getOrThrow(RegistryKeys.TRIM_MATERIAL).getOrThrow(materialKey);
        stack.set(DataComponentTypes.TRIM, new ArmorTrim(material, pattern));
        enchant(server, stack, Enchantments.PROTECTION, 4);
        enchant(server, stack, Enchantments.UNBREAKING, 3);
        return stack;
    }

    public static ItemStack createBannerShield(
            MinecraftServer server,
            DyeColor baseColor,
            List<BannerPatternsComponent.Layer> layers,
            String customName
    ) {
        ItemStack shield = new ItemStack(Items.SHIELD);
        shield.set(DataComponentTypes.BASE_COLOR, baseColor);
        shield.set(DataComponentTypes.BANNER_PATTERNS, new BannerPatternsComponent(layers));
        shield.set(DataComponentTypes.CUSTOM_NAME, Text.literal(customName));
        return shield;
    }

    private static RegistryEntry<BannerPattern> entry(MinecraftServer server, RegistryKey<BannerPattern> key) {
        return server.getRegistryManager().getOrThrow(RegistryKeys.BANNER_PATTERN).getOrThrow(key);
    }

    private static void enchant(MinecraftServer server, ItemStack stack, RegistryKey<Enchantment> enchantment, int level) {
        RegistryEntry<Enchantment> entry = server.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(enchantment);
        stack.addEnchantment(entry, level);
    }

    private static void equipArmor(ServerPlayerEntity player, ItemStack helm, ItemStack chest, ItemStack legs, ItemStack boots) {
        player.getInventory().setStack(39, helm);
        player.getInventory().setStack(38, chest);
        player.getInventory().setStack(37, legs);
        player.getInventory().setStack(36, boots);
    }
}
