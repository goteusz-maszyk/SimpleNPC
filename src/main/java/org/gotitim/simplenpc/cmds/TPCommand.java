package org.gotitim.simplenpc.cmds;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.gotitim.simplenpc.NPC;
import org.gotitim.simplenpc.SimpleNPC;

public class TPCommand {
    public boolean execute(Player player, String[] args) {

        if(!SimpleNPC.npcs.containsKey(args[0])) {
            player.sendMessage(ChatColor.RED + "This NPC doesn't exist!");

            return false;
        }
        NPC npc = SimpleNPC.npcs.get(args[0]);

        npc.setLocation(player.getLocation());

        return true;
    }
}
