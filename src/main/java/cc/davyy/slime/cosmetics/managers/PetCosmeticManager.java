package cc.davyy.slime.cosmetics.managers;

import cc.davyy.slime.factories.CosmeticFactory;
import cc.davyy.slime.cosmetics.CosmeticService;
import cc.davyy.slime.cosmetics.model.PetCosmetic;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.utils.Messages;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
public class PetCosmeticManager implements CosmeticService<PetCosmetic> {

    private static final AtomicInteger petIDCounter = new AtomicInteger(1);

    private final CosmeticFactory cosmeticFactory;
    private final Map<Integer, PetCosmetic> petCosmeticMap = new ConcurrentHashMap<>();

    @Inject
    public PetCosmeticManager(CosmeticFactory cosmeticFactory) {
        this.cosmeticFactory = cosmeticFactory;
    }

    @Override
    public void createCosmetic(@NotNull Component name, @NotNull Object entity) {
        if (!(entity instanceof EntityType)) {
            throw new IllegalArgumentException("Invalid data type for item cosmetic. Expected EntityType.");
        }

        final int id = petIDCounter.getAndIncrement();
        final PetCosmetic cosmetic = cosmeticFactory.createPetCosmetic(id, name, (EntityType) entity);

        petCosmeticMap.put(id, cosmetic);
    }

    @Override
    public void applyCosmetic(@NotNull SlimePlayer player, int id) {
        final PetCosmetic cosmetic = petCosmeticMap.get(id);

        if (cosmetic == null) {
            player.sendMessage(Messages.COSMETIC_ID_NOT_FOUND
                    .addPlaceholder("id", String.valueOf(id))
                    .asComponent());
            return;
        }

        cosmetic.apply(player);
        player.sendMessage(Messages.COSMETIC_APPLIED
                .addPlaceholder("cosmeticname", cosmetic.name())
                .asComponent());
    }

    @Override
    public void removeCosmetic(@NotNull SlimePlayer player, int id) {
        final PetCosmetic cosmetic = petCosmeticMap.get(id);

        if (cosmetic == null) {
            player.sendMessage(Messages.COSMETIC_ID_NOT_FOUND
                    .addPlaceholder("id", String.valueOf(id))
                    .asComponent());
            return;
        }

        cosmetic.remove(player);
        player.sendMessage(Messages.COSMETIC_REMOVED
                .addPlaceholder("cosmeticname", cosmetic.name())
                .asComponent());
    }

    @Override
    public @NotNull Optional<PetCosmetic> getCosmeticByID(int id) {
        return Optional.ofNullable(petCosmeticMap.get(id));
    }

    @Override
    public @NotNull List<PetCosmetic> getAvailableCosmetics() {
        return List.copyOf(petCosmeticMap.values());
    }

}