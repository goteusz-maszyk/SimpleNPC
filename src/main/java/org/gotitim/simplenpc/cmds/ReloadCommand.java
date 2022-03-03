package org.gotitim.simplenpc.cmds;

import org.bukkit.command.CommandSender;
import org.gotitim.simplenpc.NPC;
import org.gotitim.simplenpc.NPCConfig;
import org.gotitim.simplenpc.SimpleNPC;

import java.util.HashMap;

public class ReloadCommand {
    public boolean execute(CommandSender sender, String[] args) {
        NPCConfig.reset();

        for (NPC npc : SimpleNPC.npcs.values()) {
            npc.despawnForAllPlayers();
        }

        NPC.addAllToConfig();
        NPCConfig.save();

        SimpleNPC.npcs = new HashMap<>();

        NPC.loadConfig();

        sender.sendMessage("Reloaded npcs.");

        return true;
    }
}
