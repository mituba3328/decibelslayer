package io.github.mituba3328.decibelslayer;

import javax.sound.sampled.*;
import java.util.ArrayList;
import java.util.List;

public class AudioDeviceManager {

    public static List<Mixer.Info> getRecordingDevices() {
        List<Mixer.Info> devices = new ArrayList<>();
        for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
            if (mixer.getTargetLineInfo().length > 0) {
                devices.add(mixerInfo);
            }
        }
        return devices;
    }

    public static TargetDataLine getTargetDataLine(Mixer.Info mixerInfo) throws LineUnavailableException {
        Mixer mixer = AudioSystem.getMixer(mixerInfo);
        Line.Info[] targetLines = mixer.getTargetLineInfo();
        if (targetLines.length > 0) {
            return (TargetDataLine) mixer.getLine(targetLines[0]);
        } else {
            throw new LineUnavailableException("No TargetDataLine available for the selected device.");
        }
    }

    public static void printCurrentRecordingDevice(TargetDataLine microphone) {
        if (microphone != null && microphone.isOpen()) {
            Line.Info lineInfo = microphone.getLineInfo();
            for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
                Mixer mixer = AudioSystem.getMixer(mixerInfo);
                for (Line.Info targetLineInfo : mixer.getTargetLineInfo()) {
                    if (targetLineInfo.equals(lineInfo)) {
                        System.out.println("Current Recording Device:");
                        System.out.println(" - Mixer: " + mixerInfo.getName());
                        System.out.println("   Description: " + mixerInfo.getDescription());
                        return;
                    }
                }
            }
            System.out.println("Current recording device not found in the mixer list.");
        } else {
            System.out.println("Microphone is not initialized or not open.");
        }
    }
    public static List<String> getAvailableMicrophones() {
        List<String> microphones = new ArrayList<>();
        Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        for (Mixer.Info mixerInfo : mixers) {
            microphones.add(mixerInfo.getName());
        }
        return microphones;
    }
    public static TargetDataLine.Info getTargetDataLineInfo(String deviceName) {
        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();

        for (Mixer.Info mixerInfo : mixerInfos) {
            if (mixerInfo.getName().equals(deviceName)) {
                Mixer mixer = AudioSystem.getMixer(mixerInfo);
                Line.Info[] targetLines = mixer.getTargetLineInfo();
                for (Line.Info targetLineInfo : targetLines) {
                    if (targetLineInfo instanceof DataLine.Info) {
                        return (TargetDataLine.Info) targetLineInfo;
                    }
                }
            }
        }

        throw new IllegalArgumentException("指定されたデバイスが見つかりません: " + deviceName);
    }
}
