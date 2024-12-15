/*
 * Copyright (c) 2024 Tim Savage.
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

package com.winterhavenmc.util.soundconfig;

import com.winterhavenmc.util.soundconfig.sounds.SoundId;
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
	private final static InputStream SOUNDS_RESOURCE = Thread.currentThread().getContextClassLoader().getResourceAsStream("sounds.yml");

	// temporary plugin data directory for testing
	File tempDataDirectory;

	// real plugin configuration
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


	@BeforeEach
	public void setUp() throws IOException, InvalidConfigurationException {

		// load real configuration from string
		configuration.loadFromString("sound-effects: true");

		// create temporary data directory for mock plugin
		tempDataDirectory = createTempDataDir();

		// if sounds.yml resource exists and isn't already installed, copy to temporary data directory
		// this is only necessary until mocking of saveResource() is implemented
		installResource(SOUNDS_RESOURCE, "sounds.yml");

		// return real loggers for plugin and server
		when(plugin.getLogger()).thenReturn(Logger.getLogger("Mock Plugin"));

		// responses for mock plugin
		when(plugin.getConfig()).thenReturn(configuration);
		when(plugin.getDataFolder()).thenReturn(tempDataDirectory);
		when(plugin.getResource("sounds.yml")).thenReturn(SOUNDS_RESOURCE);
		// TODO: mock void method plugin.saveResource("sounds.yml", false) to install resource in temp dir
//		doReturn(installResource(SOUNDS_RESOURCE, "sounds.yml")).when(plugin).saveResource("sounds.yml", false);

		// responses for mock configuration
//		when(configuration.getBoolean("sound-effects")).thenReturn(true);

		// responses for mock player
		when(player.getName()).thenReturn("player_one");
		when(player.getUniqueId()).thenReturn(playerUid);
		when(player.getLocation()).thenReturn(location);

		// responses for mock world
		when(world.getName()).thenReturn("world");
		when(world.getUID()).thenReturn(worldUid);
	}


	@AfterEach
	public void tearDown() {
		tempDataDirectory = null;
	}

	@Nested
	class MockingSetupTests {

		@Test
		@DisplayName("Mock plugin is not null.")
		void MockPluginNotNull() {
			assertNotNull(plugin);
		}

		@Test
		@DisplayName("SoundConfig is not null.")
		void soundConfigNotNull() {
			YamlSoundConfiguration yamlSoundConfiguration = new YamlSoundConfiguration(plugin);
			assertNotNull(yamlSoundConfiguration);
		}

		@Test
		void canAccessSoundResourceTest() {
			assertNotNull(SOUNDS_RESOURCE, "the real 'sounds.yml' resource is null.");
		}

		@Test
		@DisplayName("Plugin method getResource(\"sounds.yml\") is mocked.")
		void getResourceIsMockedTest() {
			assertNotNull(plugin.getResource("sounds.yml"), "the mock 'sounds.yml' resource is null.");
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
			assertTrue(tempDataDirectory.exists(), "The temporary data directory does not exist.");
		}

		@Test
		void soundEffectsConfigMockedTest() {
			assertTrue(configuration.getBoolean("sound-effects"));
		}
	}


	@Test
	void soundsResourceIsInstalledTest() {
		assertTrue(new File(tempDataDirectory,"sounds.yml").isFile());
	}

	@Test
	void getKeysTest() {
		YamlSoundConfiguration yamlSoundConfiguration = new YamlSoundConfiguration(plugin);
		assertNotNull(yamlSoundConfiguration.getKeys(), "the collection returned is null.");
		assertFalse(yamlSoundConfiguration.getKeys().isEmpty(), "the collection returned is empty.");
		assertTrue(yamlSoundConfiguration.getKeys().containsAll(Arrays.asList(soundKeyProvider())),
				"not all keys returned by soundKeyProvider are in the collection returned.");
	}

	@Test
	void getSoundConfigKeysTest() {
		YamlSoundConfiguration yamlSoundConfiguration = new YamlSoundConfiguration(plugin);
		Collection<String> configKeys = yamlSoundConfiguration.getKeys();
		System.out.println("got " + configKeys.size() + " config keys.");

		assertFalse(configKeys.isEmpty(), "getSoundConfigKeys() returned an empty collection.");
		assertEquals(3, configKeys.size(),
				"There should have been 3 keys returned, but there were " + configKeys.size() + ".");
	}

	@Disabled
	@Test
	void isRegistrySoundTest() {
		YamlSoundConfiguration yamlSoundConfiguration = new YamlSoundConfiguration(plugin);
		assertTrue(yamlSoundConfiguration.isRegistrySound("ENTITY_VILLAGER_NO"));
		assertFalse(yamlSoundConfiguration.isRegistrySound("invalid_name"));
	}

	@Test
	void isValidSoundConfigKeyTest() {
		YamlSoundConfiguration yamlSoundConfiguration = new YamlSoundConfiguration(plugin);
		assertTrue(yamlSoundConfiguration.isValidSoundConfigKey("ENABLED_SOUND"));
		assertFalse(yamlSoundConfiguration.isValidSoundConfigKey("invalid_key"));
	}

	@Test
	void getBukkitSoundNameTest() {
		YamlSoundConfiguration yamlSoundConfiguration = new YamlSoundConfiguration(plugin);
		assertEquals("ENTITY_VILLAGER_NO", yamlSoundConfiguration.getBukkitSoundName("ENABLED_SOUND"),
				"the returned bukkit sound name does not match the expected result string");
		assertNotEquals("not_a_match", yamlSoundConfiguration.getBukkitSoundName("ENABLED_SOUND"),
				"the returned bukkit sound name matches an invalid name");
		assertNull(yamlSoundConfiguration.getBukkitSoundName("invalid_key"),
				"an invalid key returned a non-null value");
	}

	@Test
	@DisplayName("Test SoundConfig reload method.")
	void reloadTest() {
		YamlSoundConfiguration yamlSoundConfiguration = new YamlSoundConfiguration(plugin);
		yamlSoundConfiguration.reload();
		assertNotNull(yamlSoundConfiguration, "soundConfig is null after reload.");
		assertTrue(yamlSoundConfiguration.isValidSoundConfigKey("ENABLED_SOUND"),
				"expected config key is not valid after reload.");
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
		boolean success = tempDir.mkdirs();
		if (!tempDir.isDirectory()) {
			throw new IOException();
		} else {
			tempDir.deleteOnExit();
		}
		return tempDir;
	}

	@SuppressWarnings({"UnusedReturnValue", "SameParameterValue"})
	private boolean installResource(final InputStream inputStream, final String destination) throws IOException {

		boolean success = false;

		if (inputStream != null) {
			long bytesCopied = Files.copy(inputStream, Paths.get(tempDataDirectory.getPath(), destination));
			if (bytesCopied > 0) {
				success = true;
				System.out.println(destination + " resource installed to temporary data directory by testing setup method. Bytes copied: " + bytesCopied);
			}
		}
		else {
			System.out.println("inputStream is null.");
		}
		return success;
	}

}
