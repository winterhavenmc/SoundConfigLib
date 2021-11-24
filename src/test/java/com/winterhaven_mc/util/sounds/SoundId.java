package com.winterhaven_mc.util.sounds;


/**
 * Enum that contains entries for configured sounds
 */
public enum SoundId {

    COMMAND_FAIL("ENTITY_VILLAGER_NO", true, true, 1, 1),
    COMMAND_INVALID("ENTITY_WANDERING_TRADER_AMBIENT", true, true, 1, 1),
    COMMAND_SUCCESS_GIVE_SENDER("ENTITY_ARROW_SHOOT", true, true, 0.5, 1),
    COMMAND_SUCCESS_DESTROY("ENTITY_ITEM_PICKUP", true, true, 1, 1),
    COMMAND_SUCCESS_GIVE_TARGET("ENTITY_ITEM_PICKUP", true, true, 1, 1),
    TELEPORT_CANCELLED("ENTITY_GENERIC_EXTINGUISH_FIRE", true, true, 1, 1),
    TELEPORT_DENIED_PERMISSION("BLOCK_NOTE_BLOCK_BASS", true, true, 1, 1),
    TELEPORT_DENIED_WORLD_DISABLED("BLOCK_NOTE_BLOCK_BASS", true, true, 1, 1),
    TELEPORT_CANCELLED_NO_ITEM("ENTITY_DONKEY_ANGRY", true, true, 1, 1),
    TELEPORT_WARMUP("BLOCK_PORTAL_TRAVEL", true, true, 0.3, 1),
    TELEPORT_SUCCESS_DEPARTURE("ENTITY_ENDERMAN_TELEPORT", true, true, 1, 1),
    TELEPORT_SUCCESS_ARRIVAL("ENTITY_ENDERMAN_TELEPORT", true, true, 1, 1),

    CHEST_DENIED_ACCESS("ENTITY_VILLAGER_NO",true, true, 1, 1),
    CHEST_BREAK("BLOCK_WOOD_BREAK",true, true,2, 1),
    INVENTORY_ADD_ITEM("ENTITY_ITEM_PICKUP",true, true,1, 1),

    COMMAND_SUCCESS_DELETE("ENTITY_IRON_GOLEM_HURT", true, true, 1, 1),
    COMMAND_SUCCESS_FORGET("BLOCK_BEACON_DEACTIVATE",true, true, 1, 1),
    COMMAND_SUCCESS_SET("ENTITY_PLAYER_LEVELUP", true, true, 1, 1.25),
    ACTION_DISCOVERY("ENTITY_PLAYER_LEVELUP", true, true, 1, 1.5),
    ;

    protected boolean enabled;
    protected boolean playerOnly;
    protected String bukkitSoundName;
    protected double volume;
    protected double pitch;

    SoundId(String bukkitSoundName, boolean enabled, boolean playerOnly, double volume, double pitch) {
        this.bukkitSoundName = bukkitSoundName;
        this.enabled = enabled;
        this.playerOnly = playerOnly;
        this.volume = volume;
        this.pitch = pitch;
    }

    public String getBukkitSoundName() {
        return bukkitSoundName;
    }

}