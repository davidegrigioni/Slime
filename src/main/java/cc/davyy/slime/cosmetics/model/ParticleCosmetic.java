package cc.davyy.slime.cosmetics.model;

import cc.davyy.slime.model.SlimePlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.utils.PacketUtils;
import org.jetbrains.annotations.NotNull;

public record ParticleCosmetic(int id, @NotNull Component name, @NotNull Particle particle,
                               @NotNull Pos pos, @NotNull Pos posOffset, int maxSpeed,
                               int particleCount) implements Cosmetic {

    @Override
    public CosmeticType type() {
        return CosmeticType.PARTICLE;
    }

    @Override
    public void apply(@NotNull SlimePlayer player) {
        ParticlePacket particlePacket = new ParticlePacket(
                particle,
                pos,
                posOffset,
                maxSpeed,
                particleCount
        );
        PacketUtils.broadcastPlayPacket(particlePacket);
    }

    @Override
    public void remove(@NotNull SlimePlayer player) {}

}