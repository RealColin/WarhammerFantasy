package realcolin.whmod.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.AttackIndicatorStatus;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Gui.class)
public abstract class GuiMixin {
    @ModifyExpressionValue(
            method = "extractCrosshair",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/OptionInstance;get()Ljava/lang/Object;"
            )
    )
    private Object disableCrosshairAttackIndicator(Object original) {
        return AttackIndicatorStatus.OFF;
    }

    @ModifyExpressionValue(
            method = "extractItemHotbar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/OptionInstance;get()Ljava/lang/Object;"
            )
    )
    private Object disableHotbarAttackIndicator(Object original) {
        return AttackIndicatorStatus.OFF;
    }
}
