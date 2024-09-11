package cc.davyy.slime.managers;

import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Singleton;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.network.packet.server.play.DestroyEntitiesPacket;
import net.minestom.server.network.packet.server.play.SpawnEntityPacket;
import net.minestom.server.utils.PacketUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class DisguiseManager {

    // Map to track disguises of players (key: player's UUID, value: disguised entity)
    private final Map<UUID, EntityType> disguisedEntities = new ConcurrentHashMap<>();

}