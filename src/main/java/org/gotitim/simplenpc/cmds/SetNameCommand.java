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

        if(args[1].length() > 16) {
            player.sendMessage("ERROR: NPC name is too long! It needs to be =< " + 16);
            return false;
        }

        NPC npc = SimpleNPC.npcs.get(args[0]);

        npc.name = args[1];

        player.sendMessage("Set the NPC's nick to " + npc.name);

        npc.despawnForAllPlayers();
        npc.spawnForAllPlayers();

        return true;
    }
}
