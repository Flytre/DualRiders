package net.flytre.dual_riders.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;

@Mixin(HorseBaseEntity.class)
public abstract class HorseBaseEntityMixin extends AnimalEntity {

    @Shadow
    private float lastAngryAnimationProgress;

    public HorseBaseEntityMixin(EntityType<? extends AnimalEntity> type, World world) {
        super(type, world);
    }

    @Inject(method = "updatePassengerPosition", at = @At("HEAD"), cancellable = true)
    public void dual_riders$setPosition(Entity passenger, CallbackInfo ci) {
        if (!this.hasPassenger(passenger))
            return;
        float lengthwiseOffset = 0.0F;
        double heightwiseOffset = this.getMountedHeightOffset() + passenger.getHeightOffset();
        if (this.getPassengerList().size() > 1) {
            lengthwiseOffset = getPassengerList().indexOf(passenger) == 0 ? 0.2F : -0.6F;
            if (passenger instanceof AnimalEntity)
                lengthwiseOffset += 0.2F;
        }


        if (this.lastAngryAnimationProgress <= 0.0) {
            //Take the lengthwise offset and rotate it around the Y axis by - yaw as radians - pi/2
            Vec3d vec3d = (new Vec3d(lengthwiseOffset, 0.0D, 0.0D)).rotateY(-this.getYaw() * 0.017453292F - 1.5707964F);
            passenger.setPosition(this.getX() + vec3d.x, this.getY() + heightwiseOffset, this.getZ() + vec3d.z);
        } else {
            float zMultiplier = MathHelper.sin(this.bodyYaw * ((float) Math.PI / 180));
            float xMultiplier = MathHelper.cos(this.bodyYaw * ((float) Math.PI / 180));
            float animationHorizontalOffset = 0.7F * this.lastAngryAnimationProgress - lengthwiseOffset;
            float animationVerticalOffset = 0.15F * this.lastAngryAnimationProgress;
            passenger.setPosition(this.getX() + (double) (animationHorizontalOffset * zMultiplier), this.getY() + heightwiseOffset + (double) animationVerticalOffset, this.getZ() - (double) (animationHorizontalOffset * xMultiplier));
            if (passenger instanceof LivingEntity) {
                ((LivingEntity) passenger).bodyYaw = this.bodyYaw;
            }
        }
        ci.cancel();
    }

    @Unique
    public boolean hasPassenger(Entity passenger) {
        Iterator<Entity> var2 = this.getPassengerList().iterator();

        Entity entity;
        do {
            if (!var2.hasNext()) {
                return false;
            }
            entity = var2.next();
        } while (!entity.equals(passenger));

        return true;
    }
}
