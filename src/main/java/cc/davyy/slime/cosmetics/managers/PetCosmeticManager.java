package cc.davyy.slime.cosmetics.managers;

import cc.davyy.slime.cosmetics.CosmeticFactory;
import cc.davyy.slime.cosmetics.CosmeticService;
import cc.davyy.slime.cosmetics.model.PetCosmetic;
import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.EntityCreature;
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
    public void createCosmetic(@NotNull String name, @NotNull Object entity) {
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
            player.sendMessage(Component.text("Cosmetic not found with ID: " + id)
                    .color(NamedTextColor.RED)
                    .append(Component.text(" Please check the ID and try again.")
                            .color(NamedTextColor.GRAY)));
            return;
        }

        cosmetic.apply(player);
        player.sendMessage(Component.text("Successfully applied cosmetic: ")
                .color(NamedTextColor.GREEN)
                .append(Component.text(cosmetic.name())
                        .color(NamedTextColor.GOLD)));
    }

    @Override
    public void removeCosmetic(@NotNull SlimePlayer player, int id) {
        final PetCosmetic cosmetic = petCosmeticMap.get(id);

        if (cosmetic == null) {
            player.sendMessage(Component.text("Cosmetic not found with ID: " + id)
                    .color(NamedTextColor.RED)
                    .append(Component.text(" Please check the ID and try again.")
                            .color(NamedTextColor.GRAY)));
            return;
        }

        cosmetic.remove(player);
        player.sendMessage(Component.text("Successfully removed cosmetic: ")
                .color(NamedTextColor.RED)
                .append(Component.text(cosmetic.name())
                        .color(NamedTextColor.GRAY)));
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