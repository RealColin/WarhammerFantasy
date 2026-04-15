package realcolin.whmod.client.renderer;

import com.geckolib.model.DefaultedItemGeoModel;
import com.geckolib.renderer.GeoArmorRenderer;
import com.geckolib.renderer.base.GeoRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import org.jspecify.annotations.NonNull;
import realcolin.whmod.item.WHArmorItem;

import java.util.List;

public class WHArmorRenderer<R extends HumanoidRenderState & GeoRenderState> extends GeoArmorRenderer<WHArmorItem, @NonNull R> {
    public WHArmorRenderer(Identifier modelId) {
        super(new DefaultedItemGeoModel<>(modelId));
    }


    @Override
    public @NonNull List<ArmorSegment> getSegmentsForSlot(R renderState, EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> List.of(ArmorSegment.HEAD);
            case CHEST -> List.of(ArmorSegment.CHEST, ArmorSegment.LEFT_ARM, ArmorSegment.RIGHT_ARM);
            case LEGS -> List.of(ArmorSegment.CHEST, ArmorSegment.LEFT_LEG, ArmorSegment.RIGHT_LEG);
            case FEET -> List.of(ArmorSegment.LEFT_FOOT, ArmorSegment.RIGHT_FOOT);
            default -> List.of();
        };
    }

    @Override
    public @NonNull String getBoneNameForSegment(R renderState, @NonNull ArmorSegment segment) {
        EquipmentSlot currentSlot = renderState.getGeckolibData(CURRENT_SLOT);

        // I don't think this should happen but if something weird happens uhh remove this
        if (currentSlot == null) return "";

        return switch (segment) {
            case HEAD -> "armorHead";
            case CHEST -> currentSlot.equals(EquipmentSlot.LEGS) ? "armorWaist" : "armorBody";
            case LEFT_ARM -> "armorLeftArm";
            case RIGHT_ARM -> "armorRightArm";
            case LEFT_LEG -> "armorLeftLeg";
            case RIGHT_LEG -> "armorRightLeg";
            case LEFT_FOOT -> "armorLeftBoot";
            case RIGHT_FOOT -> "armorRightBoot";
        };
    }

}
