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

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SoundEntryTest
{
	SoundEntry soundEntry;


	@BeforeEach
	void setUp()
	{
		soundEntry = new SoundEntry("ENABLED_SOUND", true, true, "ENTITY_VILLAGER_NO", 1.0f, 2.0f);
	}

	@AfterEach
	void tearDown()
	{
		soundEntry = null;
	}


	@Test
	void key()
	{
		assertEquals("ENABLED_SOUND", soundEntry.key());
	}


	@Test
	void enabled()
	{
		assertTrue(soundEntry.enabled());
	}


	@Test
	void playerOnly()
	{
		assertTrue(soundEntry.playerOnly());
	}


	@Test
	void bukkitSoundName()
	{
		assertEquals("ENTITY_VILLAGER_NO", soundEntry.bukkitSoundName());
	}


	@Test
	void volume()
	{
		assertEquals(1.0f, soundEntry.volume());
	}


	@Test
	void pitch()
	{
		assertEquals(2.0f, soundEntry.pitch());
	}


	@Test
	void key_blank()
	{
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
				new SoundEntry("", true, true, "ENTITY_VILLAGER_NO", 1.0f, 2.0f));
		assertEquals("The key was blank.", e.getMessage());
	}


	@Test
	void key_null()
	{
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
				new SoundEntry(null, true, true, "ENTITY_VILLAGER_NO", 1.0f, 2.0f));
		assertEquals("The key was null.", e.getMessage());
	}


	@Test
	void soundName_blank()
	{
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
				new SoundEntry("ENABLED_SOUND", true, true, "", 1.0f, 2.0f));
		assertEquals("The sound name was blank.", e.getMessage());
	}


	@Test
	void soundName_null()
	{
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
				new SoundEntry("ENABLED_SOUND", true, true, null, 1.0f, 2.0f));
		assertEquals("The sound name was null.", e.getMessage());
	}

}
