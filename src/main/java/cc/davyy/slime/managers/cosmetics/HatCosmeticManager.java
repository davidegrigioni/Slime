package cc.davyy.slime.managers.cosmetics;

import cc.davyy.slime.factories.CosmeticFactory;
import cc.davyy.slime.services.cosmetics.CosmeticService;
import cc.davyy.slime.model.cosmetics.HatCosmetic;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.utils.Messages;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
public class HatCosmeticManager implements CosmeticService<HatCosmetic> {

    private static final AtomicInteger hatIDCounter = new AtomicInteger(1);

    private final CosmeticFactory cosmeticFactory;
    private final Map<Integer, HatCosmetic> hatCosmeticMap = new ConcurrentHashMap<>();

    @Inject
    public HatCosmeticManager(CosmeticFactory cosmeticFactory) {
        this.cosmeticFactory = cosmeticFactory;
    }

    /**
     * Creates an item cosmetic (e.g., hat) and registers it.
     *
     * @param name      The name of the cosmetic.
     * @param itemStack The ItemStack associated with this cosmetic.
     */
    @Override
    public void createCosmetic(@NotNull Component name, @NotNull Object itemStack) {
        if (!(itemStack instanceof ItemStack)) {
            throw new IllegalArgumentException("Invalid data type for item cosmetic. Expected ItemStack.");
        }

        final int id = hatIDCounter.getAndIncrement();
        final HatCosmetic cosmetic = cosmeticFactory.createHatCosmetic(id, name, (ItemStack) itemStack);

        hatCosmeticMap.put(id, cosmetic);
    }

    /**
     * Applies the cosmetic to the player (e.g., equips a hat).
     *
     * @param player The player to apply the cosmetic to.
     * @param id     The ID of the cosmetic to apply.
     */
    @Override
    public void applyCosmetic(@NotNull SlimePlayer player, int id) {
        final HatCosmetic cosmetic = hatCosmeticMap.get(id);

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

    /**
     * Removes the cosmetic from the player (e.g., unequips a hat).
     *
     * @param player The player to remove the cosmetic from.
     * @param id     The ID of the cosmetic to remove.
     */
    @Override
    public void removeCosmetic(@NotNull SlimePlayer player, int id) {
        final HatCosmetic cosmetic = hatCosmeticMap.get(id);

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

    /**
     * Retrieves an item cosmetic by its ID.
     *
     * @param id The ID of the cosmetic.
     * @return The optional containing the cosmetic if found.
     */
    @Override
    public @NotNull Optional<HatCosmetic> getCosmeticByID(int id) {
        return Optional.ofNullable(hatCosmeticMap.get(id));
    }

    /**
     * Returns a list of all available item cosmetics.
     *
     * @return A list of all available item cosmetics.
     */
    @Override
    public @NotNull List<HatCosmetic> getAvailableCosmetics() {
        return List.copyOf(hatCosmeticMap.values());
    }

}