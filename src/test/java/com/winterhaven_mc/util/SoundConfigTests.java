package com.winterhaven_mc.util;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import org.bukkit.Sound;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SoundConfigTests {

    private ServerMock server;
    private WorldMock world;
    private PlayerMock player;
    private PluginMain plugin;

    @BeforeAll
    public void setUp() {
        // Start the mock server
        server = MockBukkit.mock();

        server.setPlayers(1);

        world = server.addSimpleWorld("world");
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
    @DisplayName("Test mock objects are setup.")
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


    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class SoundConfig {

        Set<String> enumKeyStrings = new HashSet<>();
        Set<String> allBukkitSoundNames = new HashSet<>();

        public SoundConfig() {
            for (SoundId soundId : SoundId.values()) {
                this.enumKeyStrings.add(soundId.name());
            }

            // all bukkit sounds
            for (Sound sound : Sound.values()) {
                this.allBukkitSoundNames.add(sound.name());
            }
        }

        @SuppressWarnings("unused")
        Set<String> SoundConfigFileKeys() {
            return plugin.soundConfig.getSoundNames();
        }

        @ParameterizedTest
        @DisplayName("file config key is contained in enum.")
        @MethodSource("SoundConfigFileKeys")
        void ConfigFileKeyNotNull(String key) {
            Assertions.assertNotNull(key);
            Assertions.assertTrue(enumKeyStrings.contains(key));
            System.out.println("soundConfig file key '" + key + "' contained in SoundId Enum");
        }

        @ParameterizedTest
        @EnumSource(SoundId.class)
        @DisplayName("enum member soundId is contained in getConfig() keys.")
        void FileKeysContainsEnumValue(SoundId soundId) {
            Assertions.assertTrue(plugin.soundConfig.getYamlSounds().getKeys(false).toString().contains(soundId.name()));
            System.out.println("Enum value '" + soundId.name() + "' contained in sounds.yml");
        }

        @ParameterizedTest
        @EnumSource(SoundId.class)
        @DisplayName("enum member soundId maps to a valid bukkit sound name.")
        void ValidBukkitSoundName(SoundId soundId) {
            Assertions.assertTrue(allBukkitSoundNames.contains(soundId.getBukkitSoundName()));
            System.out.println("Enum value '" + soundId.name() + "' maps to valid bukkit sound name.");
        }


        @Nested
        @DisplayName("Play all sounds.")
        class PlaySounds {

            @Nested
            @DisplayName("Play all sounds in SoundId for player")
            class PlayerSounds {

                private final EnumMap<SoundId, Boolean> soundsPlayed = new EnumMap<>(SoundId.class);

                @ParameterizedTest
                @EnumSource(SoundId.class)
                @DisplayName("play sound for player")
                void SoundConfigPlaySoundForPlayer(SoundId soundId) {
                    plugin.soundConfig.playSound(player, soundId);
                    soundsPlayed.put(soundId, true);
                    Assertions.assertTrue(soundsPlayed.containsKey(soundId));
                }
            }

            @Nested
            @DisplayName("Play all sounds in SoundId at world location")
            class WorldSounds {

                private final EnumMap<SoundId, Boolean> soundsPlayed = new EnumMap<>(SoundId.class);

                @ParameterizedTest
                @EnumSource(SoundId.class)
                @DisplayName("play sound for location")
                void SoundConfigPlaySoundForPlayer(SoundId soundId) {
                    plugin.soundConfig.playSound(world.getSpawnLocation(), soundId);
                    soundsPlayed.put(soundId, true);
                    Assertions.assertTrue(soundsPlayed.containsKey(soundId));
                }
            }
        }

    }

    @Test
    @DisplayName("Test SoundConfig reload method.")
    void reload() {
        plugin.soundConfig.reload();
        Assertions.assertNotNull(plugin.soundConfig, "soundConfig not null after reload.");
    }

}
