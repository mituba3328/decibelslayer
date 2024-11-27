package io.github.mituba3328.decibelslayer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class DecibelSlayerMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> Vibration.onTick());
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            VoiceVolumeBarMod.initialize();
        });
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            VoiceVolumeBarMod.stopMonitoringThread();
        });
    }
}
