package org.gotitim.simplenpc.cmds;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class MainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args == null || args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Please provide valid action from one of following: |create|remove|setname|setskin|tphere|reload|");
            return false;
        }

        String[] newArgs = Arrays.copyOfRange(args, 1, args.length);

        switch (args[0]) {
            case "reload":
                return new ReloadCommand().execute(sender, newArgs);
        }

        if(!(sender instanceof Player)) {
            sender.sendMessage("Only players can manage NPCs!");
            return false;
        }

        Player player = (Player) sender;

        if(args == null || args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Please provide valid action from one of following: |create|remove|setname|setskin|tphere|reload| and npc name!");
            return false;
        }

        switch (args[0]) {
            case "create":
                return new CreateCommand().execute(player, newArgs);
            case "remove":
                return new RemoveCommand().execute(player, newArgs);
            case "tphere":
                return new TPCommand().execute(player, newArgs);
            case "tpto":
                return new TPToCommand().execute(player, newArgs);
            case "setskin":
                return new SetSkinCommand().execute(player, newArgs);
            case "setname":
                return new SetNameCommand().execute(player, newArgs);
        }
        return true;
    }
}
