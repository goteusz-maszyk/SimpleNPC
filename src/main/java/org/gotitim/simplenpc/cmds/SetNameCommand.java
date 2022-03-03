package org.gotitim.simplenpc.cmds;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.gotitim.simplenpc.NPC;
import org.gotitim.simplenpc.SimpleNPC;

public class SetNameCommand {
    public boolean execute(Player player, String[] args) {

        if(!SimpleNPC.npcs.containsKey(args[0])) {
            player.sendMessage(ChatColor.RED + "This NPC doesn't exist!");
            return false;
        }

        if(args[1] == null) {
            player.sendMessage(ChatColor.RED + "Please provide new NPC name!");
            return false;
        }

        NPC npc = SimpleNPC.npcs.get(args[0]);

        npc.name = args[1];

        player.sendMessage("Set the NPC's nick to " + npc.name + ".\nNOTE: You may need to reload plugin and rejoin server to see the effect.");

        return true;
    }
}
