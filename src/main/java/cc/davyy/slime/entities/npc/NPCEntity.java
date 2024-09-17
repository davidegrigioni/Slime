package cc.davyy.slime.entities.npc;

import cc.davyy.slime.constants.TagConstants;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.*;
import net.minestom.server.entity.metadata.PlayerMeta;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.PlayerInfoUpdatePacket;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class NPCEntity extends EntityCreature {

    private final String name;
    private final PlayerSkin skin;

    public NPCEntity(@NotNull String name, @NotNull PlayerSkin skin, @NotNull Instance instance,
                     @NotNull Pos spawn) {
        super(EntityType.PLAYER);
        this.name = name;
        this.skin = skin;

        final PlayerMeta meta = (PlayerMeta) getEntityMeta();
        meta.setNotifyAboutChanges(false);
        meta.setCapeEnabled(false);
        meta.setJacketEnabled(true);
        meta.setLeftSleeveEnabled(true);
        meta.setRightSleeveEnabled(true);
        meta.setLeftLegEnabled(true);
        meta.setRightLegEnabled(true);
        meta.setHatEnabled(true);
        meta.setNotifyAboutChanges(true);

        setInstance(instance, spawn);
    }

    @Override
    public void updateNewViewer(@NotNull Player player) {
        // Required to spawn player
        final List<PlayerInfoUpdatePacket.Property> properties = List.of(
                new PlayerInfoUpdatePacket.Property("textures", skin.textures(), skin.signature())
        );
        player.sendPacket(new PlayerInfoUpdatePacket(PlayerInfoUpdatePacket.Action.ADD_PLAYER,
                        new PlayerInfoUpdatePacket.Entry(
                                getUuid(), name, properties, false, 0, GameMode.SURVIVAL, null,
                                null)
                )
        );

        super.updateNewViewer(player);
    }

    public void setNpcID(int npcID) {
        setTag(TagConstants.NPC_ID_TAG, npcID);
    }

}