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

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;


class ValidSoundEntryTest
{
	ValidSoundEntry validSoundEntry;


	@BeforeEach
	void setUp()
	{
		validSoundEntry = new ValidSoundEntry("ENABLED_SOUND", true, true, "ENTITY_VILLAGER_NO", 1.0f, 2.0f);
	}

	@AfterEach
	void tearDown()
	{
		validSoundEntry = null;
	}


	@Test
	void key()
	{
		SoundEntry soundEntry = SoundEntry.of("ENABLED_SOUND", true, true, "ENTITY_VILLAGER_NO", 1.0f, 2.0f);
		assertInstanceOf(ValidSoundEntry.class, soundEntry);
		assertEquals("ENABLED_SOUND", soundEntry.key());
	}


	@Test
	void enabled()
	{
		assertTrue(validSoundEntry.enabled());
	}


	@Test
	void playerOnly()
	{
		assertTrue(validSoundEntry.playerOnly());
	}


	@Test
	void bukkitSoundName()
	{
		assertEquals("ENTITY_VILLAGER_NO", validSoundEntry.bukkitSoundName());
	}


	@Test
	void volume()
	{
		assertEquals(1.0f, validSoundEntry.volume());
	}


	@Test
	void pitch()
	{
		assertEquals(2.0f, validSoundEntry.pitch());
	}


	@Test
	void key_blank()
	{
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
				new ValidSoundEntry("", true, true, "ENTITY_VILLAGER_NO", 1.0f, 2.0f));
		assertEquals("The key was blank.", e.getMessage());
	}


	@Test
	void key_null()
	{
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
				new ValidSoundEntry(null, true, true, "ENTITY_VILLAGER_NO", 1.0f, 2.0f));
		assertEquals("The key was null.", e.getMessage());
	}


	@Test
	void soundName_blank()
	{
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
				new ValidSoundEntry("ENABLED_SOUND", true, true, "", 1.0f, 2.0f));
		assertEquals("The sound name was blank.", e.getMessage());
	}


	@Test
	void soundName_null()
	{
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
				new ValidSoundEntry("ENABLED_SOUND", true, true, null, 1.0f, 2.0f));
		assertEquals("The sound name was null.", e.getMessage());
	}

}
