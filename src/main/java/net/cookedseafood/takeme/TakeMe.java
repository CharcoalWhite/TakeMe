package net.cookedseafood.takeme;

import net.cookedseafood.takeme.command.TakeMeCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TakeMe implements ModInitializer {
	public static final String MOD_ID = "takeme";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static boolean mainHandFilterMode = false;
	public static boolean offHandFilterMode = false;
	public static Set<String> mainHandFilterItems = Stream.of("minecraft:air").collect(Collectors.toUnmodifiableSet());
	public static Set<String> offHandFilterItems = mainHandFilterItems;
	public static final byte VERSION_MAJOR = 1;
	public static final byte VERSION_MINOR = 1;
	public static final byte VERSION_PATCH = 0;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("[TakeMe] *HeavyHeavyHeavy-*");

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> TakeMeCommand.register(dispatcher, registryAccess));

		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			TakeMeCommand.executeReload(server.getCommandSource());
		});

		UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			if (!entity.isPlayer()) {
				return ActionResult.PASS;
			}

			if (mainHandFilterMode == mainHandFilterItems.contains(player.getMainHandStack().getRegistryEntry().getIdAsString())
				|| offHandFilterMode == offHandFilterItems.contains(player.getOffHandStack().getRegistryEntry().getIdAsString())
			) {
				return ActionResult.PASS;
			}

			PlayerEntity usedPlayer = (PlayerEntity) entity;
			if (!player.isSneaking()) {
				if (!usedPlayer.hasPassengers()) {
					player.startRiding(usedPlayer, true);
				}
				return ActionResult.PASS;
			}

			int pitch = (int) player.getPitch();
			if (!player.hasPassengers()) {
				if (!(pitch == -90) && !usedPlayer.hasPassengers()) {
					usedPlayer.startRiding(player, true);
				}
				return ActionResult.PASS;
			}

			if (player.hasPassenger(usedPlayer)) {
				if (pitch == -90) {
					usedPlayer.stopRiding();
				}
				return ActionResult.PASS;
			}

			Entity passengerEntity = player.getFirstPassenger();
			if (!passengerEntity.isPlayer()) {
				return ActionResult.PASS;
			}

			PlayerEntity passengerPlayer = (PlayerEntity) passengerEntity;
			if (!usedPlayer.hasPassengers()) {
				passengerPlayer.startRiding(usedPlayer, true);
			}
			return ActionResult.PASS;
		});
	}
}
