package net.flytre.dual_riders.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    public abstract List<Entity> getPassengerList();

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "canAddPassenger", at = @At("HEAD"), cancellable = true)
    public void dual_riders$doubleHorse(Entity passenger, CallbackInfoReturnable<Boolean> cir) {
        if ((Entity) (Object) this instanceof AbstractHorseEntity) {
            cir.setReturnValue(this.getPassengerList().size() < 2);
        }
    }
}
