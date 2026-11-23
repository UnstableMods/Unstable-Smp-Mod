package com.unstable.ultimate.gui;

import com.unstable.ultimate.kit.UnstableKitManager;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.equipment.trim.ArmorTrimMaterial;
import net.minecraft.item.equipment.trim.ArmorTrimMaterials;
import net.minecraft.item.equipment.trim.ArmorTrimPattern;
import net.minecraft.item.equipment.trim.ArmorTrimPatterns;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.RegistryKey;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import java.util.List;

public class UnstableMenuHandler {

    public record CosmeticaSelectPayload(String usernameOrCape) implements CustomPayload {
        public static final Id<CosmeticaSelectPayload> ID = new Id<>(Identifier.of("unstable_ultimate", "cosmetica_select"));
        public static final PacketCodec<PacketByteBuf, CosmeticaSelectPayload> CODEC = PacketCodec.of(
                (value, buf) -> buf.writeString(value.usernameOrCape()),
                buf -> new CosmeticaSelectPayload(buf.readString())
        );

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    public static void register() {
        PayloadTypeRegistry.playS2C().register(CosmeticaSelectPayload.ID, CosmeticaSelectPayload.CODEC);
    }

    public static void openMainGui(ServerPlayerEntity player, int tab) {
        SimpleInventory inv = new SimpleInventory(54);
        MinecraftServer server = player.getEntityWorld().getServer();
        if (server == null) return;

        inv.setStack(1, namedItem(Items.NETHERITE_SWORD, "§c§lUnstable Kits"));
        inv.setStack(3, namedItem(Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE, "§d§lArmor Trim Gallery"));
        inv.setStack(5, namedItem(Items.SHIELD, "§5§lShield & Banner Gallery"));
        inv.setStack(7, namedItem(Items.PLAYER_HEAD, "§b§lCosmetica Skin & Cape"));

        if (tab == 0) {
            inv.setStack(19, namedItem(Items.ENDER_PEARL, "§dSpokeIsHere Kit"));
            inv.setStack(20, namedItem(Items.MACE, "§5Wemmbu (Gambit) Kit"));
            inv.setStack(21, namedItem(Items.NETHERITE_SWORD, "§cFlameFrags Duelist Kit"));
            inv.setStack(22, namedItem(Items.SPYGLASS, "§6ParrotX2 King Kit"));
            inv.setStack(23, namedItem(Items.TRIDENT, "§bJaden_MAN Pirate Kit"));
            inv.setStack(24, namedItem(Items.POTION, "§8Invisible Mafia Kit"));
        } else if (tab == 1) {
            inv.setStack(19, UnstableKitManager.createTrimmedArmor(server, Items.NETHERITE_CHESTPLATE, ArmorTrimPatterns.SILENCE, ArmorTrimMaterials.AMETHYST));
            inv.setStack(20, UnstableKitManager.createTrimmedArmor(server, Items.NETHERITE_CHESTPLATE, ArmorTrimPatterns.SNOUT, ArmorTrimMaterials.REDSTONE));
            inv.setStack(21, UnstableKitManager.createTrimmedArmor(server, Items.NETHERITE_CHESTPLATE, ArmorTrimPatterns.SPIRE, ArmorTrimMaterials.GOLD));
            inv.setStack(22, UnstableKitManager.createTrimmedArmor(server, Items.NETHERITE_CHESTPLATE, ArmorTrimPatterns.RIB, ArmorTrimMaterials.IRON));
        } else if (tab == 2) {
            inv.setStack(19, UnstableKitManager.createBannerShield(server, DyeColor.PURPLE, List.of(), "§5Wemmbu Shield"));
            inv.setStack(20, UnstableKitManager.createBannerShield(server, DyeColor.BLACK, List.of(), "§cMafia / Red-Black Shield"));
            inv.setStack(21, UnstableKitManager.createBannerShield(server, DyeColor.WHITE, List.of(), "§fClownPierce / Circus Shield"));
            inv.setStack(22, UnstableKitManager.createBannerShield(server, DyeColor.YELLOW, List.of(), "§eHazard Shield"));
        } else if (tab == 3) {
            inv.setStack(19, namedItem(Items.PLAYER_HEAD, "§bApply Cosmetica: Wemmbu"));
            inv.setStack(20, namedItem(Items.PLAYER_HEAD, "§dApply Cosmetica: SpokeIsHere"));
            inv.setStack(21, namedItem(Items.PLAYER_HEAD, "§cApply Cosmetica: FlameFrags"));
            inv.setStack(22, namedItem(Items.PLAYER_HEAD, "§6Apply Cosmetica: ParrotX2"));
        }

        player.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, playerInv, p) ->
                new GenericContainerScreenHandler(ScreenHandlerType.GENERIC_9X6, syncId, playerInv, inv, 6) {
                    @Override
                    public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity clicker) {
                        if (!(clicker instanceof ServerPlayerEntity sp)) return;
                        if (slotIndex < 54) {
                            handleGuiClick(sp, tab, slotIndex);
                            return;
                        }
                        super.onSlotClick(slotIndex, button, actionType, clicker);
                    }
                }, Text.literal("§d§lUnstable SMP Hub")));
    }

    private static void handleGuiClick(ServerPlayerEntity player, int tab, int slot) {
        if (slot == 1) { openMainGui(player, 0); return; }
        if (slot == 3) { openMainGui(player, 1); return; }
        if (slot == 5) { openMainGui(player, 2); return; }
        if (slot == 7) { openMainGui(player, 3); return; }

        MinecraftServer server = player.getEntityWorld().getServer();
        if (server == null) return;

        if (tab == 0) {
            String kit = switch (slot) {
                case 19 -> "spoke";
                case 20 -> "wemmbu";
                case 21 -> "flame";
                case 22 -> "parrot";
                case 23 -> "jaden";
                case 24 -> "mafia";
                default -> null;
            };
            if (kit != null) {
                UnstableKitManager.applyKit(player, kit);
                player.closeHandledScreen();
                player.sendMessage(Text.literal("§d[Unstable] §fEquipped kit: §e" + kit.toUpperCase()), false);
            }
        } else if (tab == 1) {
            TrimChoice choice = switch (slot) {
                case 19 -> new TrimChoice(ArmorTrimPatterns.SILENCE, ArmorTrimMaterials.AMETHYST);
                case 20 -> new TrimChoice(ArmorTrimPatterns.SNOUT, ArmorTrimMaterials.REDSTONE);
                case 21 -> new TrimChoice(ArmorTrimPatterns.SPIRE, ArmorTrimMaterials.GOLD);
                case 22 -> new TrimChoice(ArmorTrimPatterns.RIB, ArmorTrimMaterials.IRON);
                default -> null;
            };
            if (choice != null) {
                for (int armorSlot = 36; armorSlot <= 39; armorSlot++) {
                    ItemStack piece = player.getInventory().getStack(armorSlot);
                    if (!piece.isEmpty()) {
                        ItemStack trimmed = UnstableKitManager.createTrimmedArmor(
                                server,
                                piece.getItem(),
                                choice.pattern(),
                                choice.material()
                        );
                        player.getInventory().setStack(armorSlot, trimmed);
                    }
                }
                player.playerScreenHandler.sendContentUpdates();
                player.sendMessage(Text.literal("§d[Unstable] §fApplied Armor Trim to your equipped armor!"), false);
            }
        } else if (tab == 2) {
            ItemStack shield = switch (slot) {
                case 19 -> UnstableKitManager.createBannerShield(server, DyeColor.PURPLE, List.of(), "§5Wemmbu Shield");
                case 20 -> UnstableKitManager.createBannerShield(server, DyeColor.BLACK, List.of(), "§cMafia Shield");
                case 21 -> UnstableKitManager.createBannerShield(server, DyeColor.WHITE, List.of(), "§fCircus Shield");
                case 22 -> UnstableKitManager.createBannerShield(server, DyeColor.YELLOW, List.of(), "§eHazard Shield");
                default -> ItemStack.EMPTY;
            };
            if (!shield.isEmpty()) {
                player.getInventory().insertStack(shield);
                player.playerScreenHandler.sendContentUpdates();
            }
        } else if (tab == 3) {
            String target = switch (slot) {
                case 19 -> "Wemmbu";
                case 20 -> "SpokeIsHere";
                case 21 -> "FlameFrags";
                case 22 -> "ParrotX2";
                default -> null;
            };
            if (target != null) {
                ServerPlayNetworking.send(player, new CosmeticaSelectPayload(target));
                player.sendMessage(Text.literal("§b[Cosmetica] §fRequesting skin & cape for §e" + target + "§f..."), false);
            }
        }
    }

    private record TrimChoice(RegistryKey<ArmorTrimPattern> pattern, RegistryKey<ArmorTrimMaterial> material) {}

    private static ItemStack namedItem(Item item, String name) {
        ItemStack stack = new ItemStack(item);
        stack.set(net.minecraft.component.DataComponentTypes.CUSTOM_NAME, Text.literal(name));
        return stack;
    }
}
