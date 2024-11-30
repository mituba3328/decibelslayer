package io.github.mituba3328.decibelslayer;

import java.util.List;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.gui.widget.SliderWidget;


public class CalibrateSettingScreen extends Screen {
    private Screen parent;
    public CalibrateSettingScreen(Text title, Screen parent) {
        super(title);
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        this.addDrawableChild(ButtonWidget.builder(
                Text.of("静かな状態を計測"),
                button -> {
                    SoundLevelDetector.calibrateQuiet();
                }
            ).dimensions(
                this.width / 2 - 100, this.height / 6 + 150, 200, 20
            ).build()
        );

        this.addDrawableChild(ButtonWidget.builder(
                Text.of("うるさい状態を計測"),
                button -> {
                    SoundLevelDetector.calibrateLoud();
                }
            ).dimensions(
                this.width / 2 - 100, this.height / 6 + 120, 200, 20
            ).build()
        );
        this.addDrawableChild(ButtonWidget.builder(
                Text.of("保存して終了"),
                button -> {
                        this.client.setScreen(this.parent);
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
}