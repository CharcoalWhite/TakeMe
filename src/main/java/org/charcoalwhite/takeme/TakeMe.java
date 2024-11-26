package org.charcoalwhite.takeme;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TakeMe implements ModInitializer {
	public static final String MOD_ID = "takeme";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("[TakeMe] *HeavyHeavyHeavy-*");

		UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			if (entity.isPlayer()) {
				PlayerEntity player2 = (PlayerEntity) entity;
				if (!player2.hasPassengers()) {
					if (player.hasPassengers()) {
						if (player.hasPassenger(player2)) {
							if (player.isSneaking() && player.getPitch() < 0) {
								player2.stopRiding();
							}
						} else {
							PlayerEntity player3 = (PlayerEntity) player.getFirstPassenger();
							if (player3.squaredDistanceTo(player2) < 1) {
								player3.startRiding(player2, true);
							} else if (player3.squaredDistanceTo(player2.getEyePos()) < 1) {
								player3.startRiding(player2, true);
							}
						}
					} else {
						if (player.squaredDistanceTo(player2) < 1) {
							if (player.isSneaking()) {
								player2.startRiding(player, true);
							} else {
								player2.startRiding(player, true);
							}
						} else if (player.squaredDistanceTo(player2.getEyePos()) < 1) {
							player.startRiding(player2, true);
						}
					}
				}
			}
			return ActionResult.PASS;
		});
	}
}