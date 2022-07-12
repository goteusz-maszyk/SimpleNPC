package org.gotitim.simplenpc.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.gotitim.simplenpc.SimpleNPC;

import java.util.ArrayList;
import java.util.List;

public class TabCompleter implements org.bukkit.command.TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(args == null || args.length == 0) return null;

        List<String> returned = new ArrayList<>();
        if(args.length == 1) {
            returned.add("create");
            returned.add("remove");
            returned.add("tphere");
            returned.add("tpto");
            returned.add("setskin");
            returned.add("setname");
            returned.add("reload");
        } else if(args.length == 2) {
            for (String npcName : SimpleNPC.npcs.keySet()) {
                returned.add(npcName);
            }
        }

        return returned;
    }
}
