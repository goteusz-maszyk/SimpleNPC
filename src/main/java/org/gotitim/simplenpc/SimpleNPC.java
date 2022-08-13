package org.gotitim.simplenpc;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.gotitim.simplenpc.cmds.MainCommand;
import org.gotitim.simplenpc.cmds.TabCompleter;
import org.gotitim.simplenpc.listeners.MenuClick;
import org.gotitim.simplenpc.listeners.PlayerJoin;
import org.gotitim.simplenpc.listeners.PlayerMove;

import java.util.HashMap;
import java.util.Map;

public final class SimpleNPC extends JavaPlugin {
    public static Map<String, NPC> npcs = new HashMap<>();
    public static SimpleNPC plugin;

    @Override
    public void onEnable() {
        plugin = this;

        getCommand("npc").setExecutor(new MainCommand());
        getCommand("npc").setTabCompleter(new TabCompleter());
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new PlayerMove(), this);
        getServer().getPluginManager().registerEvents(new MenuClick(), this);

        NPCConfig.setup();
        NPC.loadConfig();
    }

    @Override
    public void onDisable() {
        NPC.addAllToConfig();

        NPCConfig.save();
    }

    public static Class getNMSClass(String className, Boolean isBukkitClass) {
        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];

        try {
            if(isBukkitClass) {
                return Class.forName("org.bukkit.craftbukkit." + version + "." + className);
            } else {
                return Class.forName("net.minecraft.server." + className);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
