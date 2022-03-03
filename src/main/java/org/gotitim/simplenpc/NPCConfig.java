package org.gotitim.simplenpc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class NPCConfig {
    private static File file;
    public static FileConfiguration config;

    public static void setup() {
        file = new File(SimpleNPC.plugin.getDataFolder(), "npcs.yml");

        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Bukkit.getLogger().info(ChatColor.RED + "Help! An IO error ocurred while creating config file!");
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public static void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            Bukkit.getLogger().info(ChatColor.RED + "Help! An IO error ocurred while saving config file!");
        }
    }

    public static void reset() {
        for ( String key : config.getRoot().getKeys(false)) {
            config.set(key, null);
        }

        save();
    }

    public static void reload() { config = YamlConfiguration.loadConfiguration(file); }
}
