package realcolin.whmod.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin {

    @Inject(method = "cannotAttackWithItem", at = @At("HEAD"), cancellable = true)
    private void disableVanillaAttackCooldown(ItemStack stack, int tolerance, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
