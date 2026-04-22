package realcolin.whmod.item.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.jspecify.annotations.NonNull;
import realcolin.whmod.faction.Faction;

import java.util.List;

public record FactionShapelessCraftingRecipeDisplay(Faction faction, List<SlotDisplay> ingredients, SlotDisplay result, SlotDisplay craftingStation) implements RecipeDisplay {
    public static final MapCodec<FactionShapelessCraftingRecipeDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(
            (i) -> i.group(
                    Faction.CODEC.fieldOf("faction").forGetter(FactionShapelessCraftingRecipeDisplay::faction),
                    SlotDisplay.CODEC.listOf().fieldOf("ingredients").forGetter(FactionShapelessCraftingRecipeDisplay::ingredients),
                    SlotDisplay.CODEC.fieldOf("result").forGetter(FactionShapelessCraftingRecipeDisplay::result),
                    SlotDisplay.CODEC.fieldOf("crafting_station").forGetter(FactionShapelessCraftingRecipeDisplay::craftingStation)
            ).apply(i, FactionShapelessCraftingRecipeDisplay::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, FactionShapelessCraftingRecipeDisplay> STREAM_CODEC =
            StreamCodec.composite(
                    Faction.STREAM_CODEC, FactionShapelessCraftingRecipeDisplay::faction,
                    SlotDisplay.STREAM_CODEC.apply(ByteBufCodecs.list()), FactionShapelessCraftingRecipeDisplay::ingredients,
                    SlotDisplay.STREAM_CODEC, FactionShapelessCraftingRecipeDisplay::result,
                    SlotDisplay.STREAM_CODEC, FactionShapelessCraftingRecipeDisplay::craftingStation,
                    FactionShapelessCraftingRecipeDisplay::new);



    @Override
    public @NonNull Type<? extends RecipeDisplay> type() {
        return WHRecipeDisplays.FACTION_SHAPELESS.get();
    }
}
