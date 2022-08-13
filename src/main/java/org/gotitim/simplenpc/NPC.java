package org.gotitim.simplenpc;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.logging.Level;

import static com.google.gson.JsonParser.parseReader;

public class NPC {
    public final String id;
    public String name;
    public final ServerPlayer player;
    public String skinPlayerName;

    public NPC(String id, String name, ServerPlayer player) {
        this.id = id;
        this.name = name;
        this.player = player;
        this.skinPlayerName = name;
    }

    public NPC(String id, String name, Location location) {
        GameProfile npcGameProfile = new GameProfile(UUID.randomUUID(), name);

        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        CraftWorld world = (CraftWorld) Bukkit.getWorld(location.getWorld().getName());

        ServerPlayer player = new ServerPlayer(server, world.getHandle(), npcGameProfile, null  );

        player.setPos(location.getX(), location.getY(), location.getZ());
        SynchedEntityData data = player.getEntityData();
        data.set(new EntityDataAccessor<>(17, EntityDataSerializers.BYTE), (byte) 126);

        this.id = id;
        this.name = name;
        this.player = player;
        this.skinPlayerName = name;
    }

    public void spawnForAllPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {

            ServerGamePacketListenerImpl conn = ((CraftPlayer) player).getHandle().connection;
            conn.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, this.player));
            conn.send(new ClientboundAddPlayerPacket(this.player));
            conn.send(new ClientboundSetEntityDataPacket(this.player.getId(), this.player.getEntityData(), true));
        }
    }

    public void despawnForAllPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            ServerPlayer sp = ((CraftPlayer) player).getHandle();

            ServerGamePacketListenerImpl conn = sp.connection;
            conn.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, this.player));
            conn.send(new ClientboundRemoveEntitiesPacket(this.player.getId()));
        }
    }

    public void setLocation(Location location) {
        this.player.setPos(location.getX(), location.getY(), location.getZ());
        for (Player player : Bukkit.getOnlinePlayers()) {
            ServerPlayer sp = ((CraftPlayer) player).getHandle();

            ServerGamePacketListenerImpl conn = sp.connection;
            conn.send(new ClientboundTeleportEntityPacket(this.player));
        }
    }

    public boolean fetchSkin() {
        try {
            URL uuidURL = new URL("https://api.mojang.com/users/profiles/minecraft/" + this.skinPlayerName);
            HttpURLConnection connection = (HttpURLConnection) uuidURL.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            if(connection.getResponseCode() == 204) return false;
            InputStreamReader readerUUID = new InputStreamReader(connection.getInputStream());

            String uuid = parseReader(readerUUID).getAsJsonObject().get("id").getAsString();

            URL mojang = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" +
                    uuid + "?unsigned=false");
            InputStreamReader reader = new InputStreamReader(mojang.openStream());
            JsonObject textureProperty = parseReader(reader).getAsJsonObject().
                    get("properties").getAsJsonArray().get(0).getAsJsonObject();

            this.player.getGameProfile().getProperties().put("textures", new Property(
                    "textures",
                    textureProperty.get("value").getAsString(),
                    textureProperty.get("signature").getAsString()
            ));


        } catch (IOException e) {
            Bukkit.getLogger().log(Level.ALL, "ERROR: Can't fetch npc " + this.id + " skin!");
            e.printStackTrace();
        }
        return true;
    }

    public static void loadConfig() {
        ConfigurationSection configNPCs = NPCConfig.config.getRoot();
        for(String npcID : configNPCs.getKeys(false) ) {
            ConfigurationSection locationConfig = NPCConfig.config.getConfigurationSection(npcID + ".location");
            Location location = new Location(
                    Bukkit.getWorld(locationConfig.getString("world")),
                    locationConfig.getDouble("x"),
                    locationConfig.getDouble("y"),
                    locationConfig.getDouble("z")
            );
            NPC npcObject = new NPC(npcID, NPCConfig.config.getString(npcID + ".name"), location);
            npcObject.skinPlayerName = NPCConfig.config.getString(npcID + ".skinPlayer");
            npcObject.fetchSkin();

            SimpleNPC.npcs.put(npcID, npcObject);

            npcObject.spawnForAllPlayers();
        }
    }

    public static void addAllToConfig() {
        NPCConfig.reset();
        for (NPC npc : SimpleNPC.npcs.values()) {
            npc.despawnForAllPlayers();

            if(!NPCConfig.config.contains(npc.id)) {
                NPCConfig.config.set(npc.id + ".name", npc.name);
            }

            ConfigurationSection npcCS = NPCConfig.config.getConfigurationSection(npc.id);
            Location loc = npc.player.getBukkitEntity().getLocation();
            npcCS.set("name", npc.name);
            npcCS.set("location.x", loc.getX());
            npcCS.set("location.y", loc.getY());
            npcCS.set("location.z", loc.getZ());
            npcCS.set("location.world", loc.getWorld().getName());
            npcCS.set("skinPlayer", npc.skinPlayerName);
        }
    }
}
