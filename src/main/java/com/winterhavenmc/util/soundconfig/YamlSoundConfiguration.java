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
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;


/**
 * A class that implements SoundConfiguration interface
 */
@SuppressWarnings("unused")
public class YamlSoundConfiguration implements SoundConfiguration {

	// reference to plugin main class
	private final JavaPlugin plugin;

	// ConfigAccessor for sounds file
	private YamlConfiguration sounds;

	// default sound file name
	private final String soundFileName = "sounds.yml";

	// Set of valid sound enum names as strings
	private final Collection<String> validBukkitSoundNames = new HashSet<>();


	/**
	 * Class constructor
	 *
	 * @param plugin reference to plugin main class
	 */
	public YamlSoundConfiguration(final JavaPlugin plugin) {

		// set reference to main class
		this.plugin = plugin;

		// instantiate custom sound manager
		this.sounds = new YamlConfiguration();

		// populate valid sound names set
		for (Sound sound : Sound.values()) {
			validBukkitSoundNames.add(sound.name());
		}

		// load sounds
		this.reload();
	}


	@Override
	public boolean isValidBukkitSoundName(final String name) {
		return validBukkitSoundNames.contains(name);
	}


	@Override
	public boolean isValidSoundConfigKey(final String key) {
		return getSoundConfigKeys().contains(key);
	}


	/**
	 * Get bukkit sound name for sound config file key
	 * @param key sound config file key
	 * @return String - the bukkit sound name for key
	 */
	@Override
	public String getBukkitSoundName(final String key) {
		return this.sounds.getString(key + ".sound");
	}


	/**
	 * Get sound file name
	 * @return String - sound file name
	 */
	public String getSoundFileName() {
		return soundFileName;
	}


	/**
	 * get configuration keys as collection of String
	 * @return Collection of String - configuration keys
	 */
	@Override
	public Collection<String> getSoundConfigKeys() {
		return sounds.getKeys(false);
	}


	/**
	 * Load sound configuration from yaml file
	 */
	@Override
	public final void reload() {

		// reinstall sound file if necessary; this will not overwrite an existing file
		// note: manually check for file existence to prevent log message when file already exists
		if (!new File(plugin.getDataFolder() + File.separator + soundFileName).exists()) {
			plugin.saveResource(soundFileName, false);
		}

		// get file object for sound file
		File soundFile = new File(plugin.getDataFolder() + File.separator + soundFileName);

		// create new YamlConfiguration object
		YamlConfiguration newSoundConfig = new YamlConfiguration();

		// try to load sound file into new YamlConfiguration object
		try {
			newSoundConfig.load(soundFile);
			plugin.getLogger().info("Sound file " + soundFileName + " successfully loaded.");
		}
		catch (FileNotFoundException e) {
			plugin.getLogger().severe("Sound file " + soundFileName + " does not exist.");
		}
		catch (IOException e) {
			plugin.getLogger().severe("Sound file " + soundFileName + " could not be read.");
		}
		catch (InvalidConfigurationException e) {
			plugin.getLogger().severe("Sound file " + soundFileName + " is not valid yaml.");
		}

		// Set defaults to embedded resource file

		// get input stream reader for embedded resource file
		Reader defaultConfigStream = new InputStreamReader(
				Objects.requireNonNull(plugin.getResource(soundFileName)), StandardCharsets.UTF_8);

		// load embedded resource stream into Configuration object
		Configuration defaultConfig = YamlConfiguration.loadConfiguration(defaultConfigStream);

		// set Configuration object as defaults for messages configuration
		newSoundConfig.setDefaults(defaultConfig);

		// set class field to new YamlConfiguration object
		this.sounds = newSoundConfig;
	}


	/**
	 * Play sound effect for player
	 *
	 * @param sender  the command sender (player) to play sound
	 * @param soundId the sound identifier enum member
	 */
	@Override
	public final void playSound(final CommandSender sender, final Enum<?> soundId) {

		// if sound effects are configured false, do nothing and return
		if (!plugin.getConfig().getBoolean("sound-effects")) {
			return;
		}

		// if sender is not a player do nothing and return
		if (!(sender instanceof Player player)) {
			return;
		}

		// if sound is set to enabled in sounds file
		if (sounds.getBoolean(soundId + ".enabled")) {

			// get player only setting from config file
			boolean playerOnly = sounds.getBoolean(soundId + ".player-only");

			// get sound name from config file
			String soundName = sounds.getString(soundId + ".sound");

			// get sound volume from config file
			float volume = (float) sounds.getDouble(soundId + ".volume");

			// get sound pitch from config file
			float pitch = (float) sounds.getDouble(soundId + ".pitch");

			// check that sound name is valid
			if (validBukkitSoundNames.contains(soundName)) {

				// if sound is set player only, use player.playSound()
				if (playerOnly) {
					player.playSound(player.getLocation(), Sound.valueOf(soundName), volume, pitch);
				}
				// else use world.playSound() so other players in vicinity can hear
				else {
					player.getWorld().playSound(player.getLocation(), Sound.valueOf(soundName), volume, pitch);
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
	public final void playSound(final Location location, final Enum<?> soundId) {

		// if location is null, do nothing and return
		if (location == null) {
			return;
		}

		// if sound effects are configured false, do nothing and return
		if (!plugin.getConfig().getBoolean("sound-effects")) {
			return;
		}

		// if sound is set to enabled in sounds file
		if (sounds.getBoolean(soundId.toString() + ".enabled")) {

			// get sound name from config file
			String soundName = sounds.getString(soundId + ".sound");

			// get sound volume from config file
			float volume = (float) sounds.getDouble(soundId + ".volume");

			// get sound pitch from config file
			float pitch = (float) sounds.getDouble(soundId + ".pitch");

			// check that sound name is valid
			if (validBukkitSoundNames.contains(soundName)) {

				// else use world.playSound() so other players in vicinity can hear
				if (location.getWorld() != null) {
					location.getWorld().playSound(location, Sound.valueOf(soundName), volume, pitch);
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
