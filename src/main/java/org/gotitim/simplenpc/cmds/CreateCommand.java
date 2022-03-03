package org.gotitim.simplenpc.cmds;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.gotitim.simplenpc.NPC;
import org.gotitim.simplenpc.SimpleNPC;

import java.util.UUID;

import static net.minecraft.world.entity.player.Player.MAX_NAME_LENGTH;

public class CreateCommand {
    public boolean execute(Player player, String[] args) {
        String npcName = args[0];

        if(npcName.length() > MAX_NAME_LENGTH) {
            player.sendMessage("ERROR: NPC name is too long! It needs to be =< " + MAX_NAME_LENGTH);
            return false;
        }

        if(SimpleNPC.npcs.containsKey(npcName)) {
            player.sendMessage("ERROR: NPC with this name already exists! Please choose a different name!");
            return false;
        }

        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayer sp = craftPlayer.getHandle();

        GameProfile npcGameProfile = new GameProfile(UUID.randomUUID(), npcName);

        MinecraftServer server = sp.getServer();
        ServerLevel level = sp.getLevel();

        ServerPlayer npc = new ServerPlayer(server, level, npcGameProfile);

        Location loc = player.getLocation();

        npc.setPos(loc.getX(), loc.getY(), loc.getZ());

        NPC npcObject = new NPC(npcName, npcName, npc);
        npcObject.fetchSkin();
        npcObject.spawnForAllPlayers();

        SimpleNPC.npcs.put(npcName, npcObject);

        return true;
    }
}
