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
    @Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);

		// Minecraft doesn't have a "label" widget, so we'll have to draw our own text.
		// We'll subtract the font height from the Y position to make the text appear above the button.
		// Subtracting an extra 10 pixels will give the text some padding.
		// textRenderer, text, x, y, color, hasShadow
		context.drawText(this.textRenderer, "\"静かな状態を計測\"で静かな状態を3秒間計測します.\n\"うるさい状態を計測\"でうるさい状態を3秒間計測します.", this.width / 2, this.height / 6 - this.textRenderer.fontHeight + 200, 0xFFFFFFFF, true);
	}
}