package realcolin.whmod.item.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.jspecify.annotations.NonNull;
import realcolin.whmod.faction.Faction;

import java.util.List;

public record FactionShapedCraftingRecipeDisplay(Faction faction, int width, int height, List<SlotDisplay> ingredients, SlotDisplay result, SlotDisplay craftingStation) implements RecipeDisplay {
    public static final MapCodec<FactionShapedCraftingRecipeDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(
            (i) -> i.group(
                    Faction.CODEC.fieldOf("faction").forGetter(FactionShapedCraftingRecipeDisplay::faction),
                    Codec.INT.fieldOf("width").forGetter(FactionShapedCraftingRecipeDisplay::width),
                    Codec.INT.fieldOf("height").forGetter(FactionShapedCraftingRecipeDisplay::height),
                    SlotDisplay.CODEC.listOf().fieldOf("ingredients").forGetter(FactionShapedCraftingRecipeDisplay::ingredients),
                    SlotDisplay.CODEC.fieldOf("result").forGetter(FactionShapedCraftingRecipeDisplay::result),
                    SlotDisplay.CODEC.fieldOf("crafting_station").forGetter(FactionShapedCraftingRecipeDisplay::craftingStation))
                    .apply(i, FactionShapedCraftingRecipeDisplay::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, FactionShapedCraftingRecipeDisplay> STREAM_CODEC =
            StreamCodec.composite(
                    Faction.STREAM_CODEC, FactionShapedCraftingRecipeDisplay::faction,
                    ByteBufCodecs.VAR_INT, FactionShapedCraftingRecipeDisplay::width,
                    ByteBufCodecs.VAR_INT, FactionShapedCraftingRecipeDisplay::height,
                    SlotDisplay.STREAM_CODEC.apply(ByteBufCodecs.list()), FactionShapedCraftingRecipeDisplay::ingredients,
                    SlotDisplay.STREAM_CODEC, FactionShapedCraftingRecipeDisplay::result,
                    SlotDisplay.STREAM_CODEC, FactionShapedCraftingRecipeDisplay::craftingStation,
                    FactionShapedCraftingRecipeDisplay::new);

    public boolean isEnabled(@NonNull FeatureFlagSet enabledFeatures) {
        return this.ingredients.stream().allMatch((e) -> e.isEnabled(enabledFeatures));
    }

    @Override
    public @NonNull Type<? extends RecipeDisplay> type() {
        return WHRecipeDisplays.FACTION_SHAPED.get();
    }
}
