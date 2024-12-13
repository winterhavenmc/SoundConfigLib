package com.winterhavenmc.util.soundconfig.sounds;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.winterhavenmc.util.soundconfig.PluginMain;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

@Tag("SoundConfigTests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SoundConfigTests {

    private ServerMock server;
    private WorldMock world;
    private PlayerMock player;
    private PluginMain plugin;

    private Plugin plugin = mock(Plugin.class);

//    @BeforeAll
//    Beforeall method can be used to initialize properties files, database, etc. The method is static and executes once before running all tests.

//    @AfterAll
//    This is a static method and runs once after executing all test methods.


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
    class MockingTests {
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
    class SoundKeyTests {

        Set<String> enumKeyStrings = new HashSet<>();

        public SoundKeyTests() {
            for (SoundId soundId : SoundId.values()) {
                this.enumKeyStrings.add(soundId.name());
            }
        }

        @SuppressWarnings("unused")
        Collection<String> SoundConfigFileKeys() {
            return plugin.soundConfig.getSoundConfigKeys();
        }

        @ParameterizedTest
        @EnumSource(SoundId.class)
        @DisplayName("enum member soundId is contained in getConfig() keys.")
        void FileKeysContainsEnumValue(SoundId soundId) {
            Assertions.assertTrue(plugin.soundConfig.isValidSoundConfigKey(soundId.name()));
            System.out.println("Enum value '" + soundId.name() + "' contained in sounds.yml");
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
        @MethodSource("SoundConfigFileKeys")
        @DisplayName("Test sound file key has valid bukkit sound name")
        void SoundConfigFileHasValidBukkitSound(String key) {
            Assertions.assertTrue(plugin.soundConfig.isValidBukkitSoundName(plugin.soundConfig.getBukkitSoundName(key)));
            System.out.println("File key: " + key + " has valid bukkit sound name: " + plugin.soundConfig.getBukkitSoundName(key));
        }


        @Nested
        @DisplayName("Play all sounds.")
        class PlaySoundTests {

            @Nested
            @DisplayName("Play all sounds in SoundId for player")
            class PlayerSoundTests {

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
            class WorldSoundTests {

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
