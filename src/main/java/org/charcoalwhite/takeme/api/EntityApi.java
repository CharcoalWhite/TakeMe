package org.charcoalwhite.takeme.api;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public interface EntityApi {
    default Vec3d getVehicleHoldingOffset(Entity vehicle) {
        return Vec3d.ZERO;
    }
}
