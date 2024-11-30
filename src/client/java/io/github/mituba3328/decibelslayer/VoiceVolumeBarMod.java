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
            stopMonitoringThread();
            SoundLevelDetector.shutdown();
        });
    }

    public static void initialize(DataLine.Info customInfo) {
        // マイクを初期化
        AudioFormat format = new AudioFormat(44100, 16, 1, true, true);
        SoundLevelDetector.initialize(customInfo);

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
            stopMonitoringThread();
            SoundLevelDetector.shutdown();
        });
    }

    public static void stopMonitoringThread() {
        if (monitoringThread != null && monitoringThread.isAlive()) {
            isRunning = false;
            SoundLevelDetector.shutdown();
            try {
                monitoringThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void renderVolumeBar(DrawContext context, float volume) {
        MinecraftClient client = MinecraftClient.getInstance();
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();
    
        int barWidth = screenWidth - 40;
        int barHeight = 5;
        int x = 20;
        int y = screenHeight - 35;
    
        float normalizedVolume = Math.min(volume / 100.0f, 1.0f);
    
        int filledWidth = (int) (barWidth * normalizedVolume);

        int color = getVolumeColor(normalizedVolume);

        context.fill(x, y, x + barWidth, y + barHeight, 0xFF555555);
    
        context.fill(x, y, x + filledWidth, y + barHeight, color);
    }

    private static int getVolumeColor(float volume) {
        int red = (int) (255 * volume);
        int green = (int) (255 * (1 - volume));
        int blue = 0;
    
        return (0xFF << 24) | (red << 16) | (green << 8) | blue;
    }
}
