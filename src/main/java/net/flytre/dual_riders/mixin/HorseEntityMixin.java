package net.flytre.dual_riders.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(HorseEntity.class)
public class HorseEntityMixin extends HorseBaseEntity {

    protected HorseEntityMixin(EntityType<? extends HorseBaseEntity> entityType, World world) {
        super(entityType, world);
    }

    //Use the join interaction unless the horse is full at 2 passengers and not just 1
    @Redirect(method = "interactMob", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/HorseEntity;hasPassengers()Z"))
    public boolean dual_riders$dualPassengers(HorseEntity horseEntity) {
        return this.getPassengerList().size() >= 2;
    }
}
