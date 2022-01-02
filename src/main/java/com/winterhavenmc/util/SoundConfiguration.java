package com.winterhavenmc.util;

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

	/**
	 * Test string is valid bukkit sound name
	 * @param name the string to test
	 * @return true if passed string is a member of bukkit sounds enum; false if not
	 */
	boolean isValidBukkitSoundName(String name);


	/**
	 * Test string is valid sound config key in sounds.yml file
	 * @param key the string to test
	 * @return true if passed string is a valid key in sounds.yml file; false if not
	 */
	boolean isValidSoundConfigKey(String key);


	/**
	 * Get String Collection of configuration keys
	 * @return Collection of String containing config file sound keys
	 */
	Collection<String> getSoundConfigKeys();

	/**
	 * Get bukkit sound name for sound config file key
	 * @param key sound config file key
	 * @return String - the bukkit sound name for key
	 */
	String getBukkitSoundName(String key);


	}
