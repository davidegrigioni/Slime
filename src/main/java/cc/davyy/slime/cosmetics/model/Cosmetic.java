package cc.davyy.slime.cosmetics.model;

import cc.davyy.slime.model.SlimePlayer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.utils.PacketUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public sealed interface Cosmetic<T> permits ItemCosmetic, MobCosmetic, ParticleCosmetic {

    int id();
    @NotNull String name();
    @NotNull CosmeticType type();
    @NotNull T data();

    enum CosmeticType {
        HAT((player, cosmetic, args) -> player.setHelmet(((ItemCosmetic) cosmetic).item())),
        ARMOR((player, cosmetic, args) -> player.setBodyEquipment(((ItemCosmetic) cosmetic).item())),
        MOB((player, cosmetic, args) -> {
            MobCosmetic mobCosmetic = (MobCosmetic) cosmetic;
            final Instance instance = player.getInstance();
            final EntityCreature entityCreature = new EntityCreature(((MobCosmetic) cosmetic).entityType());
            final Pos pos = player.getPosition();
            entityCreature.setInstance(instance, pos);
            player.sendMessage("You now have a " + mobCosmetic.name() + " mob!");
        }),
        PARTICLE((player, cosmetic, args) -> {
            ParticleCosmetic particleCosmetic = (ParticleCosmetic) cosmetic;

            // Extract custom arguments (e.g., position and offset) from the args array
            Pos position = (Pos) args[0]; // Assuming args[0] is position
            Pos offset = (Pos) args[1];   // Assuming args[1] is offset
            float maxSpeed = (float) args[2]; // Assuming args[2] is maxSpeed
            int count = (int) args[3];     // Assuming args[3] is particle count

            // Construct ParticlePacket with dynamic arguments
            final ParticlePacket particlePacket = new ParticlePacket(
                    particleCosmetic.particle(),
                    position,
                    offset,
                    maxSpeed,
                    count
            );
            PacketUtils.broadcastPlayPacket(particlePacket);
            player.sendMessage("You now have a " + particleCosmetic.name() + " particle effect!");
        });

        private final CosmeticAction<Cosmetic<?>> action;

        CosmeticType(CosmeticAction<Cosmetic<?>> action) {
            this.action = action;
        }

        public void apply(@NotNull SlimePlayer player, @NotNull Cosmetic<?> cosmetic, @Nullable Object... args) {
            action.apply(player, cosmetic, args);
        }
    }

}

@FunctionalInterface
interface CosmeticAction<T extends Cosmetic<?>> {

    void apply(@NotNull SlimePlayer player, @NotNull T cosmetic, Object... args);

}