/*
 * Copyright (c) 2024-2025 Tim Savage.
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

package com.winterhavenmc.library.soundconfig;

/**
 * A class that contains the key and fields of a sound entry in the sound configuration
 * @param key {@code String} the key for the sound entry
 * @param enabled {@code boolean} if the sound entry is enabled for playback
 * @param playerOnly {@code boolean} if the sound entry should be played for an individual or for a location
 * @param bukkitSoundName {@code String} the bukkit Sound constant name, formerly an enum but now contained in the bukkit registry
 * @param volume {@code float} the volume of playback for the sound entry
 * @param pitch {@code float} the pitch of playback for the sound entry
 */
public record ValidSoundEntry(String key,
                              boolean enabled,
                              boolean playerOnly,
                              String bukkitSoundName,
                              float volume,
                              float pitch) implements SoundEntry { }
