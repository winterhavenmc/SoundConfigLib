package com.winterhavenmc.util.soundconfig.sounds;


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
