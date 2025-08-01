package io.github.gaming32.bingo.client.icons;

import io.github.gaming32.bingo.data.icons.IndicatorIcon;
import net.minecraft.client.gui.GuiGraphics;

public class IndicatorIconRenderer implements IconRenderer<IndicatorIcon> {
    @Override
    public void render(IndicatorIcon icon, GuiGraphics graphics, int x, int y) {
        IconRenderers.getRenderer(icon.base()).render(icon.base(), graphics, x, y);
        graphics.pose().pushMatrix();
        graphics.pose().scale(0.5f, 0.5f);
        IconRenderers.getRenderer(icon.indicator()).render(icon.indicator(), graphics, x * 2, y * 2 + 16);
        graphics.pose().popMatrix();
    }
}
