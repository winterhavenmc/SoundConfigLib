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

package com.winterhavenmc.util.soundconfig.Server;

import com.google.common.base.Preconditions;
import org.bukkit.*;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

public final class MockServer {

	private final static Server instance = mock(withSettings().stubOnly());

	static {

		Thread creatingThread = Thread.currentThread();
		when(instance.isPrimaryThread()).then(mock -> Thread.currentThread().equals(creatingThread));

		PluginManager pluginManager = new SimplePluginManager(instance, new SimpleCommandMap(instance));
		when(instance.getPluginManager()).thenReturn(pluginManager);

		Logger logger = Logger.getLogger(MockServer.class.getCanonicalName());
		when(instance.getLogger()).thenReturn(logger);

		when(instance.getName()).thenReturn(MockServer.class.getSimpleName());

		// all good to here.

		// these aren't going to get pulled from the jar manifesto during testing phase. they should be fetched from the pom.xml, but just setting manually for now
//      when(instance.getVersion()).thenReturn("Version_" + MockServer.class.getPackage().getImplementationVersion());
        when(instance.getVersion()).thenReturn("Version_1.24.4");

		// same as above
//		when(instance.getBukkitVersion()).thenReturn("BukkitVersion_" + Bukkit.class.getPackage().getImplementationVersion());
		when(instance.getBukkitVersion()).thenReturn("BukkitVersion_1.24.4");


		//TODO: OK, this is what we're really after. the Registry.
		Map<Class<? extends Keyed>, Registry<?>> registers = new ConcurrentHashMap<>();
		when(instance.getRegistry(any())).then(invocationOnMock -> registers.computeIfAbsent(invocationOnMock.getArgument(0), aClass -> new Registry<>() {
			private final Map<NamespacedKey, Keyed> cache = new ConcurrentHashMap<>();

			@Override
			public Keyed get(NamespacedKey key) {
				Class<? extends Keyed> theClass;
				// Some registries have extra Typed classes such as BlockType and ItemType.
				// To avoid class cast exceptions during init mock the Typed class.
				// To get the correct class, we just use the field type.
				try {
					theClass = (Class<? extends Keyed>) aClass.getField(key.getKey().toUpperCase(Locale.ROOT).replace('.', '_')).getType();
				} catch (ClassCastException | NoSuchFieldException e) {
					throw new RuntimeException(e);
				}

				return cache.computeIfAbsent(key, key2 -> mock(theClass, withSettings().stubOnly()));
			}

			@NotNull
			@Override
			public Keyed getOrThrow(@NotNull NamespacedKey key) {
				Keyed keyed = get(key);

				Preconditions.checkArgument(keyed != null, "No %s registry entry found for key %s.", aClass, key);

				return keyed;
			}

			@NotNull
			@Override
			public Stream<Keyed> stream() {
				throw new UnsupportedOperationException("Not supported");
			}

			@Override
			public Iterator<Keyed> iterator() {
				throw new UnsupportedOperationException("Not supported");
			}
		}));

		UnsafeValues unsafeValues = mock(withSettings().stubOnly());
		when(instance.getUnsafe()).thenReturn(unsafeValues);

		Bukkit.setServer(instance);
	}

	MockServer() {
	}

	public static void setup() {
	}

	public static Server getInstance() {
		return instance;
	}
}
