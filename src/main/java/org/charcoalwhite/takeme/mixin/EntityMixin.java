package org.charcoalwhite.takeme.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.Entity.PositionUpdater;
import net.minecraft.util.math.Vec3d;
import org.charcoalwhite.takeme.TakeMe;
import org.charcoalwhite.takeme.api.EntityApi;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Entity.class)
public abstract class EntityMixin implements EntityApi{
	@Overwrite
    public void updatePassengerPosition(Entity passenger, PositionUpdater positionUpdater) {
        Vec3d vec3d = this.getPassengerRidingPos(passenger);
        Vec3d vec3d2 = passenger.getVehicleAttachmentPos((Entity)(Object)this);
		Vec3d vec3d3 = this.hasCommandTag(TakeMe.HOLDING) ? passenger.getVehicleHoldingOffset((Entity)(Object)this) : Vec3d.ZERO;
        positionUpdater.accept(passenger, vec3d.x - vec3d2.x + vec3d3.x, vec3d.y - vec3d2.y + vec3d3.y, vec3d.z - vec3d2.z + vec3d3.z);
    }

    @Shadow
    public abstract Vec3d getVehicleAttachmentPos(Entity vehicle);

    @Shadow
    public abstract Vec3d getPassengerRidingPos(Entity passenger);

    @Shadow
    public abstract boolean hasCommandTag(String commandTag);
}
