package org.gotitim.simplenpc.cmds;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.gotitim.simplenpc.NPC;
import org.gotitim.simplenpc.SimpleNPC;

public class RemoveCommand {
    public boolean execute(CommandSender sender, String[] args) {
        if(!SimpleNPC.npcs.containsKey(args[0])) {
            sender.sendMessage(ChatColor.RED + "This NPC doesn't exist!");

            return false;
        }
        NPC npc = SimpleNPC.npcs.get(args[0]);

        npc.despawnForAllPlayers();

        SimpleNPC.npcs.remove(args[0]);
        sender.sendMessage("Removed NPC");

        return true;
    }
}
