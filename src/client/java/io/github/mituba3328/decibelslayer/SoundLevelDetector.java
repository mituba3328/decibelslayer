package io.github.mituba3328.decibelslayer;

import javax.sound.sampled.*;

public class SoundLevelDetector {

    private static TargetDataLine microphone;
    private static AudioFormat format;
    private static byte[] buffer;
    private static boolean initialized = false;
    public static double soundLevel = 0.0f;

    // 最小・最大RMS値
    private static double minRms = 0.1; // デフォルト値（静寂の基準）
    private static double maxRms = 1.0; // デフォルト値（最大音量の基準）

    public static void initialize(TargetDataLine.Info selectedMicrophoneInfo) {
        try {
            format = new AudioFormat(44100.0f, 16, 1, true, true);
            microphone = (TargetDataLine) AudioSystem.getLine(selectedMicrophoneInfo);
            microphone.open(format);
            buffer = new byte[88200]; // バッファサイズ

            microphone.start();
            microphone.read(buffer, 0, buffer.length);

            initialized = true;
            System.out.println("Microphone initialized successfully.");
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            initialized = false;
            System.out.println("Failed to initialize the microphone.");
        }
    }

    public static float getSoundLevelAsPercentage() {
        if (!isInitialized()) {
            System.out.println("Microphone is not initialized or running.");
            return 0.0f;
        }

        float percentage = 0.0f;
        try {
            microphone.read(buffer, 0, buffer.length); // 音声データを読み取る

            // RMSで計算
            long sum = 0;
            for (int i = 0; i < buffer.length; i += 2) {
                int sample = (buffer[i + 1] << 8) | (buffer[i] & 0xFF); // 16ビット音声データ取得
                sum += sample * sample;
            }
            double rms = Math.sqrt(sum / (buffer.length / 2.0));

            // パーセンテージで計算
            if (rms >= minRms && rms <= maxRms) {
                percentage = (float) ((rms - minRms) / (maxRms - minRms) * 100.0);
            } else if (rms < minRms) {
                percentage = 0.0f; // 静寂以下
            } else if (rms > maxRms) {
                percentage = 100.0f; // 最大音量以上
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Current sound level (%): " + percentage);
        soundLevel = percentage;
        return percentage;
    }

    public static void calibrateQuiet() {
        if (!isInitialized()) {
            System.out.println("Microphone is not initialized. Cannot calibrate.");
            return;
        }

        System.out.println("静かにしてください...");
        try {
            // 一定時間測定して最小のRMSを記録
            int durationSeconds = 3;
            double minDetectedRms = Double.MAX_VALUE;

            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < durationSeconds * 1000) {
                microphone.read(buffer, 0, buffer.length); // 音声データを読み取る

                long sum = 0;
                for (int i = 0; i < buffer.length; i += 2) {
                    int sample = (buffer[i + 1] << 8) | (buffer[i] & 0xFF);
                    sum += sample * sample;
                }
                double rms = Math.sqrt(sum / (buffer.length / 2.0));

                if (rms < minDetectedRms) {
                    minDetectedRms = rms;
                }
            }

            if (minDetectedRms > 0) {
                minRms = minDetectedRms;
                System.out.println("Calibration completed. Quiet RMS: " + minRms);
            } else {
                System.out.println("No valid samples for calibration.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void calibrateLoud() {
        if (!isInitialized()) {
            System.out.println("Microphone is not initialized. Cannot calibrate.");
            return;
        }

        System.out.println("できるだけ大きな声を出してください！！");
        try {
            // 一定時間測定して最大のRMSを記録
            int durationSeconds = 3;
            double maxDetectedRms = 0.0;

            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < durationSeconds * 1000) {
                microphone.read(buffer, 0, buffer.length); // 音声データを読み取る

                long sum = 0;
                for (int i = 0; i < buffer.length; i += 2) {
                    int sample = (buffer[i + 1] << 8) | (buffer[i] & 0xFF);
                    sum += sample * sample;
                }
                double rms = Math.sqrt(sum / (buffer.length / 2.0));

                if (rms > maxDetectedRms) {
                    maxDetectedRms = rms;
                }
            }

            if (maxDetectedRms > 0) {
                maxRms = maxDetectedRms;
                System.out.println("Calibration completed. Loud RMS: " + maxRms);
            } else {
                System.out.println("No valid samples for calibration.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void shutdown() {
        if (microphone != null) {
            microphone.stop();
            microphone.close();
            System.out.println("Microphone shutdown successfully.");
        }
        initialized = false;
    }

    public static boolean isInitialized() {
        return initialized && microphone != null;
    }
}
