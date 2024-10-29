package org.charcoalwhite.takeme.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.charcoalwhite.takeme.api.PlayerEntityApi;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements PlayerEntityApi {
    @Override
    public Vec3d getVehicleHoldingOffset(Entity vehicle) {
        if (vehicle.isPlayer()) {
            return new Vec3d(Math.sin(vehicle.getBodyYaw()), -1, Math.cos(vehicle.getBodyYaw()));
        }
        return Vec3d.ZERO;
    }
}
