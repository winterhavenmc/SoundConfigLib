package com.winterhaven_mc.util;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import org.bukkit.Location;
import org.junit.jupiter.api.*;

import java.util.Collection;

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


    @Test
    @DisplayName("Test mock server is not null.")
    void MockServerNotNull() {
        Assertions.assertNotNull(server);
    }

    @Test
    @DisplayName("Test mock plugin is not null.")
    void MockPluginNotNull() {
        Assertions.assertNotNull(plugin);
    }

    @Test
    @DisplayName("Test soundConfig is not null.")
    void soundConfigNotNull() {
        Assertions.assertNotNull(plugin.soundConfig);
    }

    @Test
    @DisplayName("Play sounds. Assert more than one sound played from SoundId enum.")
    void playSoundsForPlayer() {
        int count = 0;
        for (SoundId soundId : SoundId.values()) {
            plugin.soundConfig.playSound(player, soundId);
            count += 1;
        }
        Assertions.assertTrue(count > 1);
    }

    @Test
    @DisplayName("Play sounds. Assert more than one sound played from SoundId enum.")
    void playSoundsForLocation() {
        int count = 0;
        WorldMock world = server.addSimpleWorld("world");
        for (SoundId soundId : SoundId.values()) {
            plugin.soundConfig.playSound(new Location(world, 0, 0, 0), soundId);
            count += 1;
        }
        Assertions.assertTrue(count > 1);
    }

    @Test
    @DisplayName("Test for valid sound file name.")
    void ValidSoundFileName() {
        Assertions.assertEquals("sounds.yml", plugin.soundConfig.getSoundFileName());
    }

    @Test
    @DisplayName("Count configured sounds.")
    void MatchSounds() {
        Collection<String> configSounds = plugin.soundConfig.getConfigSounds();
        for (SoundId soundId : SoundId.values()) {
            Assertions.assertTrue(configSounds.contains(soundId.toString()));
        }
    }

    @Test
    @DisplayName("Test reload method.")
    void reload() {
        plugin.soundConfig.reload();
        Assertions.assertNotNull(plugin.soundConfig);
    }

}
