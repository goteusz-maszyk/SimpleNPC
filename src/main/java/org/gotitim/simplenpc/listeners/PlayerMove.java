package org.gotitim.simplenpc.listeners;

import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.gotitim.simplenpc.NPC;
import org.gotitim.simplenpc.SimpleNPC;

public class PlayerMove implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();

        for (NPC npc : SimpleNPC.npcs.values()) {
            ServerPlayer sp = ((CraftPlayer) p).getHandle();

            Location loc = npc.player.getBukkitEntity().getLocation();
            loc.setDirection(p.getLocation().subtract(loc).toVector());
            float yaw = loc.getYaw();
            float pitch = loc.getPitch();

            sp.connection.send(new ClientboundRotateHeadPacket(npc.player, (byte) ((yaw%360)*256/360)));
            sp.connection.send(new ClientboundMoveEntityPacket.Rot(npc.player.getId(), (byte) ((yaw%360)*256/360), (byte) ((pitch%360)*256/360), false));
        }

    }
}
