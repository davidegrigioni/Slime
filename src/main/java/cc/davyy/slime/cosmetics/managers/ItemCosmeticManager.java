package cc.davyy.slime.cosmetics.managers;

import cc.davyy.slime.cosmetics.model.Cosmetic;
import cc.davyy.slime.cosmetics.CosmeticFactory;
import cc.davyy.slime.cosmetics.CosmeticService;
import cc.davyy.slime.cosmetics.model.ItemCosmetic;
import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static net.kyori.adventure.text.Component.text;

@Singleton
public class ItemCosmeticManager implements CosmeticService<ItemStack> {

    private static final AtomicInteger itemCosmeticIDCounter = new AtomicInteger(1);

    private final CosmeticFactory cosmeticFactory;
    private final Map<Integer, ItemCosmetic> itemCosmeticMap = new ConcurrentHashMap<>();

    @Inject
    public ItemCosmeticManager(CosmeticFactory cosmeticFactory) {
        this.cosmeticFactory = cosmeticFactory;
    }

    @Override
    public void createCosmetic(@NotNull String name, @NotNull ItemStack data, @NotNull Cosmetic.CosmeticType type) {
        final int itemCosmeticID = itemCosmeticIDCounter.getAndIncrement();
        final ItemCosmetic itemCosmetic = cosmeticFactory.createItemCosmetic(itemCosmeticID, name, data, type);

        itemCosmeticMap.put(itemCosmeticID, itemCosmetic);
    }

    @Override
    public void removeCosmetic(int id) {
        itemCosmeticMap.remove(id);
    }

    @Override
    public Optional<Cosmetic<ItemStack>> getCosmeticByID(int id) {
        return Optional.ofNullable(itemCosmeticMap.get(id));
    }

    @Override
    public void applyCosmetic(@NotNull SlimePlayer player, int id) {
        ItemCosmetic itemCosmetic = itemCosmeticMap.get(id);

        if (itemCosmetic == null) {
            player.sendMessage(text("No cosmetic found with ID: " + id).color(NamedTextColor.RED));
            return;
        }

        itemCosmetic.type().apply(player, itemCosmetic);
        player.sendMessage(text("You now have a " + itemCosmetic.name() + " cosmetic!").color(NamedTextColor.GREEN));
    }

    @Override
    public void removeCosmetic(@NotNull SlimePlayer player, int id) {
        ItemCosmetic itemCosmetic = itemCosmeticMap.get(id);

        if (itemCosmetic == null) {
            player.sendMessage(text("No cosmetic found with ID: " + id).color(NamedTextColor.RED));
            return;
        }

        // Check if the player has the cosmetic applied and remove it
        if (player.getHelmet().isSimilar(itemCosmetic.item())) {
            player.setHelmet(ItemStack.AIR);  // Set to AIR instead of null
            player.sendMessage("You have removed the " + itemCosmetic.name() + " hat.");
        } else if (player.getBodyEquipment().isSimilar(itemCosmetic.item())) {
            player.setBodyEquipment(ItemStack.AIR);  // Set to AIR instead of null
            player.sendMessage("You have removed the " + itemCosmetic.name() + " armor.");
        } else {
            player.sendMessage("No cosmetic is currently applied.");
        }
    }

    @Override
    public @NotNull List<Cosmetic<ItemStack>> getAvailableCosmetics() {
        return List.copyOf(itemCosmeticMap.values());
    }

}