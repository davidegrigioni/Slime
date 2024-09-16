package cc.davyy.slime.cosmetics.managers;

import cc.davyy.slime.cosmetics.CosmeticFactory;
import cc.davyy.slime.cosmetics.ParticleCosmeticService;
import cc.davyy.slime.cosmetics.model.ParticleCosmetic;
import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.utils.PacketUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
public class ParticleCosmeticManager implements ParticleCosmeticService {

    private static final AtomicInteger particleIDCounter = new AtomicInteger(1);

    private final CosmeticFactory cosmeticFactory;
    private final Map<Integer, ParticleCosmetic> particleCosmeticMap = new ConcurrentHashMap<>();

    @Inject
    public ParticleCosmeticManager(CosmeticFactory cosmeticFactory) {
        this.cosmeticFactory = cosmeticFactory;
    }

    @Override
    public void createCosmetic(@NotNull Component name, @NotNull Particle particle, @NotNull Pos pos, @NotNull Pos posOffset, int maxSpeed, int particleCount) {
        final int particleID = particleIDCounter.getAndIncrement();
        final ParticleCosmetic particleCosmetic = cosmeticFactory.createParticleCosmetic(particleID, name, particle, pos, posOffset, maxSpeed, particleCount);

        particleCosmeticMap.put(particleID, particleCosmetic);
    }

    @Override
    public void applyCosmetic(@NotNull SlimePlayer player, int id) {
        final ParticleCosmetic particleCosmetic = particleCosmeticMap.get(id);

        ParticlePacket particlePacket = new ParticlePacket(particleCosmetic.particle(), particleCosmetic.pos(), particleCosmetic.posOffset(), particleCosmetic.maxSpeed(), particleCosmetic.particleCount());
        PacketUtils.broadcastPlayPacket(particlePacket);

        player.sendMessage("sended particles");
    }

    @Override
    public void removeCosmetic(@NotNull SlimePlayer player, int id) {
        final ParticleCosmetic particleCosmetic = particleCosmeticMap.remove(id);

    }

    @Override
    public @NotNull Optional<ParticleCosmetic> getCosmeticByID(int id) {
        return Optional.ofNullable(particleCosmeticMap.get(id));
    }

    @Override
    public @NotNull List<ParticleCosmetic> getAvailableCosmetics() {
        return List.copyOf(particleCosmeticMap.values());
    }

}