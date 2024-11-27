package io.github.mituba3328.decibelslayer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import javax.sound.sampled.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class VoiceVolumeBarMod {
    private static float currentVolume = 0.0f;
    private static boolean isRunning = true;
    private static Thread monitoringThread;

    public static void initialize() {
        AudioFormat format = new AudioFormat(44100, 16, 1, true, true);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        SoundLevelDetector.initialize(info);


        if (SoundLevelDetector.isInitialized()) {
            isRunning = true;
            monitoringThread = new Thread(() -> {
                while (isRunning) {
                    try {
                        currentVolume = SoundLevelDetector.getSoundLevelAsPercentage();
                    } catch (Exception e) {
                        System.out.println("Error in sound monitoring thread: " + e.getMessage());
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        System.out.println("Thread interrupted: " + e.getMessage());
                    }
                }
                System.out.println("Sound monitoring thread has stopped.");
            });
            monitoringThread.start();
        } else {
            System.out.println("Failed to initialize the microphone. Sound monitoring will not start.");
        }

        // HUDにバーを描画
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null) {
                if (SoundLevelDetector.isInitialized()) {
                    renderVolumeBar(drawContext, currentVolume);
                } else {
                    System.out.println("Microphone is not initialized. HUD bar will not be rendered.");
                }
            }
        });

        // ワールド切断時の処理
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            stopMonitoringThread(); // スレッドの停止
            SoundLevelDetector.shutdown(); // マイクリソースの解放
        });
    }

    public static void initialize(DataLine.Info customInfo) {
        // マイクを初期化
        AudioFormat format = new AudioFormat(44100, 16, 1, true, true);
        SoundLevelDetector.initialize(customInfo);

        // 初期化完了後に音量取得スレッドを開始
        if (SoundLevelDetector.isInitialized()) {
            isRunning = true;
            monitoringThread = new Thread(() -> {
                while (isRunning) {
                    try {
                        currentVolume = SoundLevelDetector.getSoundLevelAsPercentage();
                    } catch (Exception e) {
                        System.out.println("Error in sound monitoring thread: " + e.getMessage());
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(100); // 100msごとに更新
                    } catch (InterruptedException e) {
                        System.out.println("Thread interrupted: " + e.getMessage());
                    }
                }
                System.out.println("Sound monitoring thread has stopped.");
            });
            monitoringThread.start();
        } else {
            System.out.println("Failed to initialize the microphone. Sound monitoring will not start.");
        }

        // HUDにバーを描画
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null) {
                if (SoundLevelDetector.isInitialized()) {
                    renderVolumeBar(drawContext, currentVolume);
                } else {
                    System.out.println("Microphone is not initialized. HUD bar will not be rendered.");
                }
            }
        });

        // ワールド切断時の処理
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            stopMonitoringThread(); // スレッドの停止
            SoundLevelDetector.shutdown(); // マイクリソースの解放
        });
    }

    public static void stopMonitoringThread() {
        if (monitoringThread != null && monitoringThread.isAlive()) {
            isRunning = false; // スレッドを停止
            SoundLevelDetector.shutdown();
            try {
                monitoringThread.join(); // スレッド終了を待機
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void renderVolumeBar(DrawContext context, float volume) {
        MinecraftClient client = MinecraftClient.getInstance();
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();
    
        int barWidth = screenWidth - 40; // 横幅を画面全体から少しマージンを残す
        int barHeight = 5; // 高さを薄く
        int x = 20; // 左端からのマージン
        int y = screenHeight - 35; // 経験値バーの上に配置
    
        int filledWidth = (int) (barWidth * volume / 150); // 音量に応じた長さ
    
        // 音量に応じた色
        int color;
        if (volume > 0.75f) {
            color = 0xFFFF0000; // 赤
        } else if (volume > 0.5f) {
            color = 0xFFFFFF00; // 黄色
        } else {
            color = 0xFF00FF00; // 緑
        }
    
        // 背景バー
        context.fill(x, y, x + barWidth, y + barHeight, 0xFF555555); // 灰色
    
        // 音量を示すバー
        context.fill(x, y, x + filledWidth, y + barHeight, color);
    }
}
