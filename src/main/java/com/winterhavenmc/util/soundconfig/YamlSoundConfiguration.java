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
import org.bukkit.Registry;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.*;


/**
 * A class that implements SoundConfiguration interface
 */
public class YamlSoundConfiguration implements SoundConfiguration {

	// reference to plugin main class
	private final Plugin plugin;

	// configuration object for sounds file
	private final YamlConfiguration soundsConfig;

	// sound file name
	private final String soundFileName = "sounds.yml";

	/**
	 * Class constructor
	 *
	 * @param plugin reference to plugin main class
	 */
	public YamlSoundConfiguration(final Plugin plugin) {

		// set reference to main class
		this.plugin = plugin;

		// get file handle to sounds.yml file in plugin data directory
		File soundFile = new File(plugin.getDataFolder(), soundFileName);

		// install sounds.yml if not already present and resource exists
		// this is only wrapped in a conditional to prevent log message when file already exists
		if (!soundFile.exists() && plugin.getResource(soundFileName) != null) {
			plugin.saveResource(soundFileName, false);
			//TODO: remove this log message when testing allows
			plugin.getLogger().info(soundFileName + " was installed by call to plugin.saveResource() in YamlSoundConfiguration constructor.");
			plugin.getLogger().info("Path: " + soundFile.getPath());
			if (soundFile.exists()) {
				plugin.getLogger().info("file has been successfully copied from resource.");
			}
			else {
				plugin.getLogger().severe("file was not copied from resource.");
			}
		}

		// instantiate sounds configuration object
		this.soundsConfig = new YamlConfiguration();

		// load values from sounds.yml in root of plugin data directory
		try {
			this.soundsConfig.load(soundFile);
		}
		catch (FileNotFoundException e) {
			plugin.getLogger().severe(e.getLocalizedMessage());
		}
		catch (IOException | InvalidConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Set<String> getKeys() {
		return this.soundsConfig.getKeys(false);
	}

	@Override
	public boolean isValidBukkitSoundName(final String key) {
		Set<String> soundNames = new HashSet<>();
		for (Sound sound : Sound.values()) {
			soundNames.add(sound.name());
		}
		return soundNames.contains(key);
	}

	boolean isRegistrySound(final String name) {
		return Registry.SOUNDS.match(name) != null;
	}

	@Override
	public boolean isValidSoundConfigKey(final String key) {
		return this.soundsConfig.getKeys(false).contains(key);
	}


	/**
	 * Get bukkit sound name for sound config file key
	 * @param key sound config file key
	 * @return String - the bukkit sound name for key
	 */
	@Override
	public String getBukkitSoundName(final String key) {
		return this.soundsConfig.getString(key + ".sound");
	}


	/**
	 * Load sound configuration from yaml file in plugin data folder
	 */
	@Override
	public void reload() {
		// get File object for sound file
		File soundFile = new File(plugin.getDataFolder().getPath(), soundFileName);

		// copy resource to plugin data directory if it does not already exist there
		if (!soundFile.exists()) {
			plugin.saveResource(soundFileName, false);
		}
		try {
			soundsConfig.load(soundFile);
		} catch (IOException | InvalidConfigurationException e) {
			throw new RuntimeException(e);
		}
	}


	/**
	 * Play sound effect for player
	 *
	 * @param sender  the command sender (player) to play sound
	 * @param soundId the sound identifier enum member
	 */
	@Override
	public void playSound(final CommandSender sender, final Enum<?> soundId) {

		// if sound effects are configured false, do nothing and return
		if (!plugin.getConfig().getBoolean("sound-effects")) {
			return;
		}

		// if sender is not a player do nothing and return
		if (!(sender instanceof Player player)) {
			return;
		}

		// if sound is set to enabled in sounds file
		if (soundsConfig.getBoolean(soundId + ".enabled")) {

			// get player only setting from config file
			boolean playerOnly = soundsConfig.getBoolean(soundId + ".player-only");

			// get sound name from config file
			String soundName = soundsConfig.getString(soundId + ".sound");

			// get sound volume from config file
			float volume = (float) soundsConfig.getDouble(soundId + ".volume");

			// get sound pitch from config file
			float pitch = (float) soundsConfig.getDouble(soundId + ".pitch");

			if (soundName == null) {
				soundName = "";
			}

			// check that sound name is valid
			if (Registry.SOUNDS.match(soundName) != null) {
//			if (validBukkitSoundNames.contains(soundName)) {

				// if sound is set player only, use player.playSound()
				if (playerOnly) {
					player.playSound(player.getLocation(), Objects.requireNonNull(Registry.SOUNDS.match(soundName)), volume, pitch);
				}
				// else use world.playSound() so other players in vicinity can hear
				else {
					player.getWorld().playSound(player.getLocation(), Objects.requireNonNull(Registry.SOUNDS.match(soundName)), volume, pitch);
				}
			}
			else {
				plugin.getLogger().warning("An error occurred while trying to play the sound '"
						+ soundName + "'. You probably need to update the sound name in your "
						+ soundFileName + " file.");
			}
		}
	}


	/**
	 * Play sound effect for location
	 *
	 * @param location the location at which to play sound
	 * @param soundId  the sound identifier enum member
	 */
	@Override
	public void playSound(final Location location, final Enum<?> soundId) {

		// if location is null, do nothing and return
		if (location == null) {
			return;
		}

		// if sound effects are configured false, do nothing and return
		if (!plugin.getConfig().getBoolean("sound-effects")) {
			return;
		}

		// if sound is set to enabled in sounds file
		if (soundsConfig.getBoolean(soundId.toString() + ".enabled")) {

			// get sound name from config file
			String soundName = soundsConfig.getString(soundId + ".sound");

			// get sound volume from config file
			float volume = (float) soundsConfig.getDouble(soundId + ".volume");

			// get sound pitch from config file
			float pitch = (float) soundsConfig.getDouble(soundId + ".pitch");

			// check that sound name is valid
			if (soundName != null && Registry.SOUNDS.match(soundName) != null) {

				// else use world.playSound() so other players in vicinity can hear
				if (location.getWorld() != null) {
					location.getWorld().playSound(location, Objects.requireNonNull(Registry.SOUNDS.match(soundName)), volume, pitch);
				}
			}
			else {
				plugin.getLogger().warning("An error occurred while trying to play the sound '"
						+ soundName + "'. You probably need to update the sound name in your "
						+ soundFileName + " file.");
			}
		}
	}

}
