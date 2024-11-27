package io.github.mituba3328.decibelslayer;

import java.util.List;

import net.minecraft.client.MinecraftClient;
import io.github.mituba3328.decibelslayer.CalibrateSettingScreen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.gui.widget.SliderWidget;


public class ModSettingsScreen extends Screen {
    private String selectedMicrophone;
    private Screen parent;
    public ModSettingsScreen(Text title, Screen parent) {
        super(title);
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        // マイクの設定
        List<String> microphones = AudioDeviceManager.getAvailableMicrophones();

        this.addDrawableChild(CyclingButtonWidget.builder((String value) -> Text.of(value))
            .values(microphones.toArray(new String[0]))
            .initially(microphones.get(0))
            .build(
                this.width / 2 - 100, this.height / 6 + 120, 200, 20,
                Text.of("使用するマイク："),
                (button, value) -> this.selectedMicrophone = value
            )
        );

        this.addDrawableChild(ButtonWidget.builder(
                Text.of("感度調整"),
                button -> {
                    MinecraftClient.getInstance().setScreen(new CalibrateSettingScreen(Text.of("感度調整"), this));
                }
            ).dimensions(
                this.width / 2 - 100, this.height / 6 + 150, 200, 20
            ).build()
        );

        this.addDrawableChild(ButtonWidget.builder(
                Text.of("保存して終了"),
                button -> {
                    if (this.saveSettings()) {
                        this.client.setScreen(this.parent);
                    } else {
                        this.client.getToastManager().add(
                                SystemToast.create(this.client, SystemToast.Type.NARRATOR_TOGGLE, Text.of("オーディオエラー"), Text.of("そのデバイスは使用できません"))
                        );
                    }
                }
            ).dimensions(
                this.width / 2 - 100, this.height / 6 + 200, 200, 20
            ).build()
        );
    }
    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }
    private boolean saveSettings() {
        try {
            VoiceVolumeBarMod.stopMonitoringThread();
            var info = AudioDeviceManager.getTargetDataLineInfo(this.selectedMicrophone);
            VoiceVolumeBarMod.initialize(info);
            System.out.println("Settings saved: " + this.selectedMicrophone);
            return true;
        } catch (Exception e) {
            return false;
        }
    }    
}