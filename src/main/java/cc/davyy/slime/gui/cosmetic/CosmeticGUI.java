package cc.davyy.slime.gui.cosmetic;

import cc.davyy.slime.managers.general.ConfigManager;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.utils.ColorUtils;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Singleton
public class CosmeticGUI extends Inventory {

    private final ConfigManager configManager;
    private final String cosmeticGuiTitle;
    private final int[] cosmeticSlots = { 10, 12, 14 };

    @Inject
    public CosmeticGUI(ConfigManager configManager, String cosmeticGuiTitle) {
        super(InventoryType.CHEST_3_ROW, ColorUtils.of(cosmeticGuiTitle).build());
        this.configManager = configManager;
        this.cosmeticGuiTitle = configManager.getConfig().getString("cosmetic-gui-title");
        setupCosmetics();
        fillBackground();
    }

    public void open(@NotNull SlimePlayer player) {
        player.openInventory(this);
    }

    private void setupCosmetics() {
        // Particle Effect Item
        ItemStack particleEffect = createItem(Material.POPPED_CHORUS_FRUIT,
                "<gradient:#ff007f:#7f00ff>Particle Effect</gradient>",
                List.of("Select a particle effect to use!", "Click to choose."));

        // Fancy Hat Item
        ItemStack hat = createItem(Material.LEATHER_HELMET,
                "<gradient:#00bfff:#1e90ff>Fancy Hat</gradient>",
                List.of("Wear a stylish hat!", "Click to equip."));

        // Pet Item
        ItemStack petItem = createItem(Material.BONE,
                "<gradient:#ffd700:#ff8c00>Pet Companion</gradient>",
                List.of("Select a pet to follow you!", "Click to choose."));

        this.setItemStack(cosmeticSlots[0], particleEffect);
        this.setItemStack(cosmeticSlots[1], hat);
        this.setItemStack(cosmeticSlots[2], petItem);
    }

    private void fillBackground() {
        ItemStack glassPane = ItemStack.builder(Material.GRAY_STAINED_GLASS_PANE)
                .customName(Component.text(" ")) // Invisible item
                .build();

        for (int i = 0; i < this.getSize(); i++) {
            if (!isCosmeticSlot(i)) {
                this.setItemStack(i, glassPane);
            }
        }
    }

    private boolean isCosmeticSlot(int slot) {
        for (int cosmeticSlot : cosmeticSlots) {
            if (slot == cosmeticSlot) {
                return true;
            }
        }
        return false;
    }

    private ItemStack createItem(Material material, String name, List<String> lore) {
        return ItemStack.builder(material)
                .customName(ColorUtils.of(name).build())
                .lore(ColorUtils.stringListToComponentList(lore))
                .build();
    }

}