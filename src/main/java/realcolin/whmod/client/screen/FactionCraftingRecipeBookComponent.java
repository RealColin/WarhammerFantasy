package realcolin.whmod.client.screen;

import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.recipebook.GhostSlots;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.AbstractCraftingMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import org.jspecify.annotations.NonNull;
import realcolin.whmod.faction.Faction;
import realcolin.whmod.item.recipe.FactionShapedCraftingRecipeDisplay;
import realcolin.whmod.item.recipe.WHRecipeBookCategories;

import java.util.List;
import java.util.Optional;

public class FactionCraftingRecipeBookComponent extends RecipeBookComponent<AbstractCraftingMenu> {
    private static final WidgetSprites FILTER_BUTTON_SPRITES = new WidgetSprites(Identifier.withDefaultNamespace("recipe_book/filter_enabled"), Identifier.withDefaultNamespace("recipe_book/filter_disabled"), Identifier.withDefaultNamespace("recipe_book/filter_enabled_highlighted"), Identifier.withDefaultNamespace("recipe_book/filter_disabled_highlighted"));
    private static final Component ONLY_CRAFTABLES_TOOLTIP = Component.translatable("gui.recipebook.toggleRecipes.craftable");
    private static final List<RecipeBookComponent.TabInfo> TABS = List.of(
            new RecipeBookComponent.TabInfo(new ItemStack(Items.COMPASS), Optional.empty(), WHSearchRecipeBookCategory.CRAFTING),
            new RecipeBookComponent.TabInfo(Items.IRON_AXE, Items.GOLDEN_SWORD, WHRecipeBookCategories.FACTION_CRAFTING.get()),
            new RecipeBookComponent.TabInfo(Items.BRICKS, RecipeBookCategories.CRAFTING_BUILDING_BLOCKS),
            new RecipeBookComponent.TabInfo(Items.LAVA_BUCKET, Items.APPLE, RecipeBookCategories.CRAFTING_MISC),
            new RecipeBookComponent.TabInfo(Items.REDSTONE, RecipeBookCategories.CRAFTING_REDSTONE));

    private final Faction faction;

    public FactionCraftingRecipeBookComponent(AbstractCraftingMenu menu, Faction faction) {
        super(menu, TABS);
        this.faction = faction;
    }

    @Override
    protected @NonNull WidgetSprites getFilterButtonTextures() {
        return FILTER_BUTTON_SPRITES;
    }

    @Override
    protected boolean isCraftingSlot(@NonNull Slot slot) {
        return (this.menu).getResultSlot() == slot || (this.menu).getInputGridSlots().contains(slot);
    }

    @Override
    protected void selectMatchingRecipes(RecipeCollection recipeCollection, @NonNull StackedItemContents stackedItemContents) {
        recipeCollection.selectRecipes(stackedItemContents, this::canDisplay);
    }

    @Override
    protected @NonNull Component getRecipeFilterName() {
        return ONLY_CRAFTABLES_TOOLTIP;
    }

    @Override
    protected void fillGhostRecipe(@NonNull GhostSlots ghostSlots, @NonNull RecipeDisplay recipeDisplay, @NonNull ContextMap contextMap) {

    }

    // TODO implement pls
    private boolean canDisplay(RecipeDisplay display) {
        boolean ret;
        switch (display) {
            case FactionShapedCraftingRecipeDisplay shaped -> ret = this.faction == shaped.faction();

            default -> ret = false;
        }

        return ret;
    }
}
