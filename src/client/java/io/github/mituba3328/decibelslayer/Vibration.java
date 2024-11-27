package io.github.mituba3328.decibelslayer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.event.GameEvent;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.world.ServerWorld;

public class Vibration {
    static int tickCounter = 0;
    static final float ALERT_THRESHOLD = 80.0f; // 閾値：80%以上で振動を発生

    public static void alertWardenIfTooLoud() {
        if (SoundLevelDetector.soundLevel >= ALERT_THRESHOLD) {
            // 音量が閾値を超えたときのギミック
        }
    }

    public static void onTick() {
        final int TICKS_PER_SECOND = 10;

        if (MinecraftClient.getInstance().world != null) {
            tickCounter++;

            if (tickCounter >= TICKS_PER_SECOND) {
                tickCounter = 0; // カウンターをリセット
                alertWardenIfTooLoud();
            }
        }
    }
}
