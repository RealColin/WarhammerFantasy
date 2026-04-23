package realcolin.whmod.item.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;
import realcolin.whmod.faction.Faction;

import java.util.List;

public class FactionShapedCraftingRecipe implements FactionCraftingRecipe {

    private final Faction faction;
    private final ShapedRecipePattern pattern;
    private final ItemStackTemplate result;
    private PlacementInfo info;


    public static final MapCodec<FactionShapedCraftingRecipe> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Faction.CODEC.fieldOf("faction").forGetter(FactionShapedCraftingRecipe::faction),
                    ShapedRecipePattern.MAP_CODEC.forGetter(r -> r.pattern),
                    ItemStackTemplate.CODEC.fieldOf("result").forGetter(r -> r.result)
            ).apply(instance, FactionShapedCraftingRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, FactionShapedCraftingRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    Faction.STREAM_CODEC, FactionShapedCraftingRecipe::faction,
                    ShapedRecipePattern.STREAM_CODEC, FactionShapedCraftingRecipe::pattern,
                    ItemStackTemplate.STREAM_CODEC, FactionShapedCraftingRecipe::result,
                    FactionShapedCraftingRecipe::new
            );

    public FactionShapedCraftingRecipe(Faction faction, ShapedRecipePattern pattern, ItemStackTemplate result) {
        this.faction = faction;
        this.pattern = pattern;
        this.result = result;
    }

    @Override
    public boolean matches(@NonNull CraftingInput craftingInput, @NonNull Level level) {
        return this.pattern.matches(craftingInput);
    }

    @Override
    public @NonNull ItemStack assemble(@NonNull CraftingInput craftingInput) {
        return result.create();
    }

    // TODO what for?
    @Override
    public boolean showNotification() {
        return false;
    }

    // TODO what for?
    @Override
    public @NonNull String group() {
        return "";
    }

    @Override
    public @NonNull RecipeType<? extends Recipe<CraftingInput>> getType() {
        return WHRecipes.FACTION_CRAFTING.get();
    }

    @Override
    public @NonNull PlacementInfo placementInfo() {
        if (info == null) {
            System.out.println("Pattern ingredients: " + this.pattern.ingredients());
            this.info = PlacementInfo.createFromOptionals(this.pattern.ingredients());
        }

        return this.info;
    }

    @Override
    public @NonNull RecipeBookCategory recipeBookCategory() {
        return WHRecipeBookCategories.FACTION_CRAFTING.get();
    }

    @Override
    public @NonNull RecipeSerializer<? extends FactionCraftingRecipe> getSerializer() {
        return WHRecipes.SHAPED_FACTION_CRAFTING.get();
    }

    @Override
    public Faction faction() {
        return faction;
    }

    public ShapedRecipePattern pattern() {
        return pattern;
    }

    public ItemStackTemplate result() {
        return result;
    }

    @Override
    public @NonNull List<RecipeDisplay> display() {
        // TODO get item for faction-specific crafting table

        return List.of(new FactionShapedCraftingRecipeDisplay(
                faction,
                this.pattern.width(),
                this.pattern.height(),
                this.pattern.ingredients().stream().map(
                        (e) -> e.map(Ingredient::display).orElse(SlotDisplay.Empty.INSTANCE)).toList(),
                new SlotDisplay.ItemStackSlotDisplay(this.result), new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)));
    }
}
