package dev.su5ed.mffs.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public abstract class BaseButton extends AbstractButton {
    private final Runnable onPress;

    public BaseButton(int x, int y, int width, int height, Runnable onPress) {
        super(x, y, width, height, Component.empty());

        this.onPress = onPress;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();

        int i = getTextureY();
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        guiGraphics.blit(WIDGETS_LOCATION, getX(), getY(), 0, i, this.width / 2, this.height);
        guiGraphics.blit(WIDGETS_LOCATION, getX() + this.width / 2, getY(), 200 - this.width / 2, i, this.width / 2, this.height);

        renderFg(guiGraphics, minecraft, mouseX, mouseY, partialTick);
    }

    protected abstract void renderFg(GuiGraphics guiGraphics, Minecraft minecraft, int mouseX, int mouseY, float partialTick);

    @Override
    public void onPress() {
        this.onPress.run();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {}
}
