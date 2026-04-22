package realcolin.whmod.client.screen;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.jspecify.annotations.NonNull;
import realcolin.whmod.WHMod;
import realcolin.whmod.menu.FactionCraftingMenu;

public class FactionCraftingScreen extends AbstractRecipeBookScreen<FactionCraftingMenu> {
    private final Identifier TEXTURE_LOCATION;

    public FactionCraftingScreen(FactionCraftingMenu menu, Inventory inventory, Component title) {
        super(menu, new FactionCraftingRecipeBookComponent(menu, menu.getFaction()), inventory, title);
        var faction = menu.getFaction();
        TEXTURE_LOCATION = Identifier.fromNamespaceAndPath(WHMod.MOD_ID, "textures/gui/container/" + faction.id() + "_crafting_table.png");
    }

    protected void init() {
        super.init();
        this.titleLabelX = 29;
    }

    @Override
    protected @NonNull ScreenPosition getRecipeBookButtonPosition() {
        return new ScreenPosition(this.leftPos + 5, this.height / 2 - 49);
    }

    public void extractBackground(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        int xo = this.leftPos;
        int yo = (this.height - this.imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE_LOCATION, xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
    }
}
