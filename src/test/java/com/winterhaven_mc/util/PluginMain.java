package com.winterhaven_mc.util;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;

/**
 * The main class for SoundConfigTest plugin
 */
@SuppressWarnings("unused")
public final class PluginMain extends JavaPlugin {

    public YamlSoundConfiguration soundConfig;


    public PluginMain() {
        super();
    }


    protected PluginMain(JavaPluginLoader loader, PluginDescriptionFile descriptionFile, File dataFolder, File file) {
        super(loader, descriptionFile, dataFolder, file);
    }


    @Override
    public void onEnable() {
        // instantiate sound config
        soundConfig = new YamlSoundConfiguration(this);
    }

}
