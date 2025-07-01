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

package com.winterhavenmc.library.soundconfig.sounds;


/**
 * Enum that contains entries for configured sounds
 */
public enum SoundId {

	ENABLED_SOUND("ENTITY_VILLAGER_NO", true, true, 1, 1),
	DISABLED_SOUND("ENTITY_VILLAGER_NO", false, true, 1, 1),
	WORLD_SOUND("ENTITY_VILLAGER_NO", true, false, 1,1);

	final boolean enabled;
	final boolean playerOnly;
	final String bukkitSoundName;
	final double volume;
	final double pitch;

	SoundId(final String bukkitSoundName,
			final boolean enabled,
			final boolean playerOnly,
			final double volume,
			final double pitch) {
		this.bukkitSoundName = bukkitSoundName;
		this.enabled = enabled;
		this.playerOnly = playerOnly;
		this.volume = volume;
		this.pitch = pitch;
	}

	@SuppressWarnings("unused")
	public String getBukkitSoundName() {
		return bukkitSoundName;
	}

}
