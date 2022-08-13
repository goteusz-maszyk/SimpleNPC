package org.gotitim.simplenpc.cmds;

import org.bukkit.entity.Player;
import org.gotitim.simplenpc.NPC;
import org.gotitim.simplenpc.SimpleNPC;

public class CreateCommand {
    public boolean execute(Player player, String[] args) {
        String npcName = args[0];

        if(npcName.length() > 16) {
            player.sendMessage("ERROR: NPC name is too long! It needs to be =< " + 16);
            return false;
        }

        if(SimpleNPC.npcs.containsKey(npcName)) {
            player.sendMessage("ERROR: NPC with this name already exists! Please choose a different name!");
            return false;
        }

        NPC npcObject = new NPC(npcName, npcName, player.getLocation());
        npcObject.fetchSkin();
        npcObject.spawnForAllPlayers();

        SimpleNPC.npcs.put(npcName, npcObject);

        return true;
    }
}
