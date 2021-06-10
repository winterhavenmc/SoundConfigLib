package com.winterhaven_mc.util;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import org.bukkit.Location;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SoundConfigTests {

    private ServerMock server;
    private PluginMain plugin;
    private PlayerMock player;

    @BeforeAll
    public void setUp() {
        // Start the mock server
        server = MockBukkit.mock();

        server.setPlayers(1);

        player = server.getPlayer(0);

        // start the mock plugin
        plugin = MockBukkit.load(PluginMain.class);
    }

    @AfterAll
    public void tearDown() {
        // Stop the mock server
        MockBukkit.unmock();
    }

    @Nested
    class Mocking {
        @Test
        @DisplayName("Mock server is not null.")
        void MockServerNotNull() {
            Assertions.assertNotNull(server);
        }

        @Test
        @DisplayName("Mock plugin is not null.")
        void MockPluginNotNull() {
            Assertions.assertNotNull(plugin);
        }

        @Test
        @DisplayName("SoundConfig is not null.")
        void soundConfigNotNull() {
            Assertions.assertNotNull(plugin.soundConfig);
        }

        @Test
        @DisplayName("Test for valid sound file name.")
        void ValidSoundFileName() {
            String soundFileName = "sounds.yml";
            Assertions.assertEquals(soundFileName, plugin.soundConfig.getSoundFileName(),
                    "soundFileName equals '" + soundFileName + "'");
        }
    }

    @Nested
    class PlaySounds {

        @Test
        @DisplayName("Play sounds for player. Assert sounds played matches sound.yml config sounds.")
        void playSoundsForPlayer() {

            EnumMap<SoundId, Boolean> soundsPlayed = new EnumMap<>(SoundId.class);

            for (SoundId soundId : SoundId.values()) {
                plugin.soundConfig.playSound(player, soundId);
                soundsPlayed.put(soundId, true);
            }

            for (String soundName : plugin.soundConfig.getConfigSounds()) {
                Assertions.assertTrue(soundsPlayed.containsKey(SoundId.valueOf(soundName)));
            }
        }

        @Test
        @DisplayName("Play sounds for location. Assert sounds played matches sound.yml config sounds.")
        void playSoundsForLocation() {

            EnumMap<SoundId, Boolean> soundsPlayed = new EnumMap<>(SoundId.class);

            WorldMock world = server.addSimpleWorld("world");
            for (SoundId soundId : SoundId.values()) {
                plugin.soundConfig.playSound(new Location(world, 0, 0, 0), soundId);
                soundsPlayed.put(soundId, true);
            }

            for (String soundName : plugin.soundConfig.getConfigSounds()) {
                Assertions.assertTrue(soundsPlayed.containsKey(SoundId.valueOf(soundName)),
                        "soundId is in sounds played");
            }
        }
    }

    @Nested
    class MatchSoundNames {

        @Test
        @DisplayName("Match all enum sounds against config sounds.")
        void MatchAllEnumSounds() {

            // collection of enum sound names
            Collection<String> configSounds = plugin.soundConfig.getConfigSounds();

            for (SoundId soundId : SoundId.values()) {
                Assertions.assertTrue(configSounds.contains(soundId.toString()),
                        soundId + " is in sounds played");
            }
        }


        @Test
        @DisplayName("Match all config sounds against enum sounds.")
        void MatchAllConfigSounds() {

            // collection of sound config keys
            Collection<String> configSoundNames = plugin.soundConfig.getConfigSounds();

            // collection of enum sound names
            Collection<String> enumSoundNames = new HashSet<>();

            // create list of enum sound name strings
            for (SoundId soundId : SoundId.values()) {
                enumSoundNames.add(soundId.toString());
            }

            // check each config sound name is contained in enum sound names collection
            for (String configSoundName : configSoundNames) {
                Assertions.assertTrue(enumSoundNames.contains(configSoundName));
            }
        }
    }

    @Test
    @DisplayName("Test SoundConfig reload method.")
    void reload() {
        plugin.soundConfig.reload();
        Assertions.assertNotNull(plugin.soundConfig);
    }

}
