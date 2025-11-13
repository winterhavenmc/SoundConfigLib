/*
 * Copyright (c) 2025 Tim Savage.
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

public sealed interface SoundEntry permits ValidSoundEntry, InvalidSoundEntry
{
	String key();

	static SoundEntry of(String key,
	                     boolean enabled,
	                     boolean playerOnly,
	                     String bukkitSoundName,
	                     float volume,
	                     float pitch)
	{
		if (key == null) return new InvalidSoundEntry("ø", "The key was null.");
		else if (key.isBlank()) return new InvalidSoundEntry("BLANK", "The key was blank.");
		else if (bukkitSoundName == null) return new InvalidSoundEntry(key, "The sound name was null.");
		else if (bukkitSoundName.isBlank()) return new InvalidSoundEntry(key, "The sound name was blank.");
		else return new ValidSoundEntry(key, enabled, playerOnly, bukkitSoundName, volume, pitch);
	}
}
