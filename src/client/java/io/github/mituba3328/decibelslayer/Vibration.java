package io.github.mituba3328.decibelslayer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.event.GameEvent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.world.ServerWorld;

public class Vibration {
    static int tickCounter = 0;
    static final float ALERT_THRESHOLD = 40.0f; // 閾値：80%以上で振動を発生

    public static void spawnCreeperIfTooLoud() {
        if (SoundLevelDetector.soundLevel >= ALERT_THRESHOLD) {
            MinecraftClient client = MinecraftClient.getInstance();
    
            if (client.world != null && client.player != null) {
                IntegratedServer server = client.getServer();
                if (server != null) {
                    ServerWorld serverWorld = server.getWorld(client.player.getWorld().getRegistryKey());
                    if (serverWorld != null) {
                        BlockPos playerPos = client.player.getBlockPos();

                        CreeperEntity creeper = EntityType.CREEPER.create(serverWorld);
    
                        if (creeper != null) {
                            creeper.refreshPositionAndAngles(
                                playerPos.getX() + 0.5,
                                playerPos.getY(),
                                playerPos.getZ() + 0.5,
                                client.player.getYaw(),
                                0
                            );
    
                            serverWorld.spawnEntity(creeper);
                        }
                    }
                }
            }
        }
    }

    public static void onTick() {
        final int TICKS_PER_SECOND = 10;

        if (MinecraftClient.getInstance().world != null) {
            tickCounter++;

            if (tickCounter >= TICKS_PER_SECOND) {
                tickCounter = 0; // カウンターをリセット
                spawnCreeperIfTooLoud();
            }
        }
    }
}
