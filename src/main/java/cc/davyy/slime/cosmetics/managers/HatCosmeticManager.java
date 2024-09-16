package cc.davyy.slime.cosmetics.managers;

import cc.davyy.slime.cosmetics.CosmeticFactory;
import cc.davyy.slime.cosmetics.CosmeticService;
import cc.davyy.slime.cosmetics.model.HatCosmetic;
import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
    public void createCosmetic(@NotNull String name, @NotNull Object itemStack) {
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