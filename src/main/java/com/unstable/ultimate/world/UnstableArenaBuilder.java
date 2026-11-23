package com.unstable.ultimate.world;

import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class UnstableArenaBuilder {

    public static void buildSpawnAndArena(ServerWorld world, BlockPos center) {
        int radius = 32;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                double dist = Math.sqrt(dx * dx + dz * dz);
                BlockPos floorPos = center.add(dx, -1, dz);

                if (dist <= 20) {
                    world.setBlockState(floorPos, ((dx + dz) % 4 == 0) ? Blocks.CRYING_OBSIDIAN.getDefaultState() : Blocks.OBSIDIAN.getDefaultState());
                } else if (dist <= radius) {
                    world.setBlockState(floorPos, (Math.abs(dx) + Math.abs(dz)) % 3 == 0 ? Blocks.CRACKED_STONE_BRICKS.getDefaultState() : Blocks.DEEPSLATE_TILES.getDefaultState());
                }

                for (int y = 0; y <= 24; y++) {
                    world.setBlockState(center.add(dx, y, dz), Blocks.AIR.getDefaultState());
                }
            }
        }

        for (int angle = 0; angle < 360; angle += 15) {
            double rad = Math.toRadians(angle);
            int px = (int) Math.round(Math.cos(rad) * 22);
            int pz = (int) Math.round(Math.sin(rad) * 22);
            int height = 10 + ((Math.abs(px * 7 + pz * 13)) % 14);

            for (int h = 0; h < height; h++) {
                BlockPos pillarPos = center.add(px, h, pz);
                if (h == height - 1) {
                    world.setBlockState(pillarPos, Blocks.GRASS_BLOCK.getDefaultState());
                } else if (h > height - 4) {
                    world.setBlockState(pillarPos, Blocks.DIRT.getDefaultState());
                } else {
                    world.setBlockState(pillarPos, (h % 3 == 0) ? Blocks.AMETHYST_BLOCK.getDefaultState() : Blocks.STONE.getDefaultState());
                }
            }
        }

        buildFloatingIsland(world, center.add(0, 9, 0), 4);
        buildFloatingIsland(world, center.add(-12, 13, 8), 3);
        buildFloatingIsland(world, center.add(12, 13, -8), 3);
    }

    private static void buildFloatingIsland(ServerWorld world, BlockPos pos, int r) {
        for (int dx = -r; dx <= r; dx++) {
            for (int dz = -r; dz <= r; dz++) {
                if (dx * dx + dz * dz <= r * r) {
                    world.setBlockState(pos.add(dx, 0, dz), Blocks.SCULK.getDefaultState());
                    world.setBlockState(pos.add(dx, -1, dz), Blocks.DEEPSLATE.getDefaultState());
                }
            }
        }
    }
}
