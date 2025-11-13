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

import com.winterhavenmc.library.soundconfig.Server.MockServer;
import com.winterhavenmc.library.soundconfig.sounds.SoundId;
import org.bukkit.*;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SoundConfigurationTests {

	// resource as input stream
	InputStream SOUNDS_RESOURCE;

	// temporary plugin data directory for testing
	File tempDataDirectory;

	// real plugin configuration object
	FileConfiguration configuration = new YamlConfiguration();

	// mocks
	Plugin plugin = mock(Plugin.class, "mockPlugin");
	Player player = mock(Player.class, "mockPlayer");
	World world = mock(World.class, "mockWorld");

	// real location
	Location location = new Location(world, 0.0, 0.0, 0.0);

	// real uuid for player
	private final static UUID playerUid = new UUID(0, 1);

	// real uuid for world
	private final static UUID worldUid = new UUID(1, 1);

	// real sound configuration
	private SoundConfiguration soundConfiguration;


	@BeforeEach
	public void setUp() throws IOException, InvalidConfigurationException {

		// get sound.yml resource as a stream
		SOUNDS_RESOURCE = Thread.currentThread().getContextClassLoader().getResourceAsStream("sounds.yml");

		// create temporary data directory for mock plugin
		tempDataDirectory = createTempDataDir();

		// load real configuration from string
		configuration.loadFromString("sound-effects: true");

		// setup mock server
		MockServer.setup();

		// responses for mock plugin
		when(plugin.getLogger()).thenReturn(Logger.getLogger("Mock Plugin"));
		when(plugin.getServer()).thenReturn(MockServer.getInstance());
		when(plugin.getConfig()).thenReturn(configuration);
		when(plugin.getDataFolder()).thenReturn(tempDataDirectory);
		when(plugin.getResource("sounds.yml")).thenReturn(SOUNDS_RESOURCE);
		doAnswer(invocation -> {
				installResource("sounds.yml");
				return null;
			}).when(plugin).saveResource("sounds.yml", false);

		// responses for mock player
		when(player.getName()).thenReturn("player_one");
		when(player.getUniqueId()).thenReturn(playerUid);
		when(player.getLocation()).thenReturn(location);

		// responses for mock world
		when(world.getName()).thenReturn("world");
		when(world.getUID()).thenReturn(worldUid);

		soundConfiguration = new YamlSoundConfiguration(plugin);
	}


	@AfterEach
	public void tearDown() {
		soundConfiguration = null;
		tempDataDirectory = null;
	}

	@Nested
	class MockingSetupTests {

		@Test
		void MockPluginNotNull() {
			assertNotNull(plugin);
		}

		@Test
		void soundConfigNotNull() {
			SoundConfiguration soundConfiguration = new YamlSoundConfiguration(plugin);
			assertNotNull(soundConfiguration);
		}

		@Test
		void canAccessSoundResourceTest() {
			assertNotNull(SOUNDS_RESOURCE, "the real 'sounds.yml' resource is null.");
		}

		@Test
		void getResourceIsMockedTest() {
			assertNotNull(plugin.getResource("sounds.yml"), "the mock 'sounds.yml' resource is null.");
			verify(plugin, atLeast(1)).getResource(any(String.class));
		}

		@Test
		void getSoundConfigFileKeysTest() {
			String[] keys = soundKeyProvider();
			assertNotNull(keys, "sound config keys test array is null");
			assertTrue(keys.length > 0, "sound config keys test array is empty");
			assertTrue(Arrays.asList(keys).contains("ENABLED_SOUND"),
					"sound config keys test array is missing expected value");
		}

		@Test
		void temporaryDataDirectoryExistsTest() {
			assertTrue(tempDataDirectory.exists(), "the temporary data directory does not exist.");
		}

		@Test
		void soundEffectsConfigMockedTest() {
			assertTrue(configuration.getBoolean("sound-effects"), "the config setting 'sound-effects' returned false.");
		}
	}


	@Test
	void soundsResourceIsInstalledTest() {
		assertTrue(new File(tempDataDirectory,"sounds.yml").isFile());
	}

	@Test
	void getKeysTest() {
		assertNotNull(soundConfiguration.getKeys(), "the collection returned is null.");
		assertFalse(soundConfiguration.getKeys().isEmpty(), "the collection returned is empty.");
		assertTrue(soundConfiguration.getKeys().containsAll(Arrays.asList(soundKeyProvider())),
				"not all keys returned by soundKeyProvider are in the collection returned.");
	}

	@Test
	void getSoundConfigKeysTest() {
		Collection<String> configKeys = soundConfiguration.getKeys();
		assertFalse(configKeys.isEmpty(), "getSoundConfigKeys() returned an empty collection.");
		assertEquals(3, configKeys.size(),
				"There should have been 3 keys returned, but there were " + configKeys.size() + ".");
	}

	@Disabled
	@Test
	void isValidBukkitSoundNameTest() {
		assertTrue(soundConfiguration.isValidBukkitSoundName("BLOCK_ANVIL_BREAK"));
		assertFalse(soundConfiguration.isValidBukkitSoundName("invalid_name"));
	}

	@Disabled
	@Test
	void isRegistrySoundTest() {
		YamlSoundConfiguration yamlSoundConfiguration = new YamlSoundConfiguration(plugin);
		assertTrue(yamlSoundConfiguration.isRegistrySound("BLOCK_ANVIL_BREAK"));
		assertFalse(yamlSoundConfiguration.isRegistrySound("invalid_name"));
	}

	@Test
	void isValidSoundConfigKeyTest() {
		assertTrue(soundConfiguration.isValidSoundConfigKey("ENABLED_SOUND"));
		assertFalse(soundConfiguration.isValidSoundConfigKey("invalid_key"));
	}

	@Test
	void getBukkitSoundNameTest() {
		assertEquals("ENTITY_VILLAGER_NO", soundConfiguration.getBukkitSoundName("ENABLED_SOUND"),
				"the returned bukkit sound name does not match the expected result string");
		assertNotEquals("not_a_match", soundConfiguration.getBukkitSoundName("ENABLED_SOUND"),
				"the returned bukkit sound name matches an invalid name");
		assertNull(soundConfiguration.getBukkitSoundName("invalid_key"),
				"an invalid key returned a non-null value");
	}

	@Test
	void reloadTest() {
		soundConfiguration.reload();
		assertNotNull(soundConfiguration, "soundConfig is null after reload.");
		assertTrue(soundConfiguration.isValidSoundConfigKey("ENABLED_SOUND"),
				"expected configuration key could not be found after reloading configuration.");
	}

	@Test
	void getEntryTest() {
		YamlSoundConfiguration soundConfiguration = new YamlSoundConfiguration(plugin);
		ValidSoundEntry validSoundEntry = soundConfiguration.getEntry(SoundId.ENABLED_SOUND);
		assertEquals("ENABLED_SOUND", validSoundEntry.key());
		assertTrue(validSoundEntry.enabled());
		assertTrue(validSoundEntry.playerOnly());
		assertEquals("ENTITY_VILLAGER_NO", validSoundEntry.bukkitSoundName());
		assertEquals(1.0f, validSoundEntry.volume());
		assertEquals(2.0f, validSoundEntry.pitch());
	}


	// TESTING HELPER METHODS

	/**
	 * get an array of config enum constant names
 	 * @return String[] an array of enum constant names
	 */
	private static String[] soundKeyProvider() {
		return Arrays.stream(SoundId.values()).map(Enum::name).toArray(String[]::new);
	}

	/**
	 * Create a temporary folder
	 * @throws IOException if directory cannot be created
	 */
	public static File createTempDataDir() throws IOException {
		String tempDataDirectoryPath = Files.createTempDirectory("PluginData").toFile().getAbsolutePath();
		File tempDir = new File(tempDataDirectoryPath);
		@SuppressWarnings("unused") boolean success = tempDir.mkdirs();
		if (!tempDir.isDirectory()) {
			throw new IOException();
		} else {
			tempDir.deleteOnExit();
		}
		return tempDir;
	}

	@SuppressWarnings({"UnusedReturnValue", "SameParameterValue"})
	private boolean installResource(final String name) throws IOException {

		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);

		boolean success = false;

		if (inputStream != null) {
			long bytesCopied = Files.copy(inputStream, Paths.get(tempDataDirectory.getPath(), name));
			if (bytesCopied > 0) {
				success = true;
			}
		}
		else {
			throw new IOException("InputStream for '" + name + "' resource is null.");
		}
		return success;
	}

}
