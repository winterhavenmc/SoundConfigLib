/*
 * Copyright (c) 2022 Tim Savage.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.winterhavenmc.util.soundconfig;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import java.util.Collection;


/**
 * An interface that facilitates loading a custom sound configuration file and provides methods to play sounds in game
 */
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
	boolean isValidBukkitSoundName(final String name);


	/**
	 * Test string is valid sound config key in sounds.yml file
	 * @param key the string to test
	 * @return true if passed string is a valid key in sounds.yml file; false if not
	 */
	boolean isValidSoundConfigKey(final String key);


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
	String getBukkitSoundName(final String key);

}
