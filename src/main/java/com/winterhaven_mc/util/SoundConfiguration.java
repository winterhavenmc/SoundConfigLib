package com.winterhaven_mc.util;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import java.util.Collection;


public interface SoundConfiguration {


	/**
	 * Load sound configuration
	 */
	void reload();


	/**
	 * Play sound effect for player
	 *
	 * @param sender  the command sender (player) to play sound
	 * @param soundId the sound identifier enum member
	 */
	void playSound(final CommandSender sender, final Enum<?> soundId);

	/**
	 * Play sound effect for location
	 *
	 * @param location the location at which to play sound
	 * @param soundId  the sound identifier enum member
	 */
	void playSound(final Location location, final Enum<?> soundId);

	Collection<String> getSoundNames();

	/**
	 * Get valid bukkit sound names for current server
	 * @return Collection of String of valid sound names
	 */
	Collection<String> getValidSoundNames();

	/**
	 * Get bukkit sound name for sound config file key
	 * @param key sound config file key
	 * @return String - the bukkit sound name for key
	 */
	String getBukkitSoundName(String key);


	}
