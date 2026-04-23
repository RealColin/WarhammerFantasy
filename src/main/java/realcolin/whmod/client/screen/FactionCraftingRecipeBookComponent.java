package realcolin.whmod.client.screen;

import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.recipebook.GhostSlots;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.network.chat.Component;
import net.minecraft.recipebook.PlaceRecipeHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.AbstractCraftingMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import org.jspecify.annotations.NonNull;
import realcolin.whmod.faction.Faction;
import realcolin.whmod.item.WHItems;
import realcolin.whmod.item.recipe.FactionShapedCraftingRecipeDisplay;
import realcolin.whmod.item.recipe.FactionShapelessCraftingRecipeDisplay;
import realcolin.whmod.item.recipe.WHRecipeBookCategories;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FactionCraftingRecipeBookComponent extends RecipeBookComponent<AbstractCraftingMenu> {
    private static final WidgetSprites FILTER_BUTTON_SPRITES = new WidgetSprites(Identifier.withDefaultNamespace("recipe_book/filter_enabled"), Identifier.withDefaultNamespace("recipe_book/filter_disabled"), Identifier.withDefaultNamespace("recipe_book/filter_enabled_highlighted"), Identifier.withDefaultNamespace("recipe_book/filter_disabled_highlighted"));
    private static final Component ONLY_CRAFTABLES_TOOLTIP = Component.translatable("gui.recipebook.toggleRecipes.craftable");

    private final Faction faction;

    public FactionCraftingRecipeBookComponent(AbstractCraftingMenu menu, Faction faction) {
        super(menu, makeTabs(faction));
        this.faction = faction;
    }

    private static List<TabInfo> makeTabs(Faction faction) {
        Item item;
        switch (faction) {
            case EMPIRE -> item = WHItems.IMPERIAL_SWORD.get();
            case DWARFS -> item = Items.IRON_INGOT;
            default -> item = Items.GOLDEN_AXE;
        }

        return List.of(
                new TabInfo(new ItemStack(Items.COMPASS), Optional.empty(), WHSearchRecipeBookCategory.CRAFTING),
                new TabInfo(item, WHRecipeBookCategories.FACTION_CRAFTING.get())
        );
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
        ghostSlots.setResult((this.menu).getResultSlot(), contextMap, recipeDisplay.result());
        Objects.requireNonNull(recipeDisplay);
        switch (recipeDisplay) {
            case FactionShapedCraftingRecipeDisplay shaped -> {
                var inputSlots = menu.getInputGridSlots();
                PlaceRecipeHelper.placeRecipe(
                        menu.getGridWidth(),
                        menu.getGridHeight(),
                        shaped.width(),
                        shaped.height(),
                        shaped.ingredients(),
                        (ingredient, gridIndex, _, _) -> {
                            var slot = inputSlots.get(gridIndex);
                            ghostSlots.setInput(slot, contextMap, ingredient);
                        }
                );
            }

            case FactionShapelessCraftingRecipeDisplay shapeless -> {
                var inputSlots = menu.getInputGridSlots();
                int slotCount = Math.min(shapeless.ingredients().size(), inputSlots.size());

                for (int i = 0; i < slotCount; i++) {
                    ghostSlots.setInput(inputSlots.get(i), contextMap, shapeless.ingredients().get(i));
                }
            }


            default -> System.out.println("A");
        }

    }

    private boolean canDisplay(RecipeDisplay display) {
        boolean ret;
        switch (display) {
            case FactionShapedCraftingRecipeDisplay shaped -> ret = this.faction == shaped.faction();
            case FactionShapelessCraftingRecipeDisplay shapeless -> ret = this.faction == shapeless.faction();
            default -> ret = false;
        }

        return ret;
    }
}
