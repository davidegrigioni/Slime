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
public class PetGUI extends Inventory {

    private final String petGuiTitle;
    private final ConfigManager configManager;

    @Inject
    public PetGUI(String petGuiTitle, ConfigManager configManager) {
        super(InventoryType.CHEST_3_ROW, ColorUtils.of(petGuiTitle).build());
        this.petGuiTitle = configManager.getConfig().getString("pet-gui-title");
        this.configManager = configManager;
        setupPets();
        fillBackground();
    }

    public void open(@NotNull SlimePlayer player) {
        player.openInventory(this);
    }

    private void setupPets() {
        // Example pets (you can replace these with actual pets)
        ItemStack dogPet = createItem(Material.BONE,
                "<gradient:#ffcc00:#996600>Dog</gradient>",
                List.of("A loyal canine companion!", "Click to summon."));

        ItemStack catPet = createItem(Material.STRING,
                "<gradient:#ff9999:#660066>Cat</gradient>",
                List.of("A cute feline friend!", "Click to summon."));

        this.setItemStack(10, dogPet);
        this.setItemStack(12, catPet);
    }

    private void fillBackground() {
        ItemStack glassPane = ItemStack.builder(Material.GRAY_STAINED_GLASS_PANE)
                .customName(Component.text(" ")) // Invisible item
                .build();

        for (int i = 0; i < this.getSize(); i++) {
            if (i != 10 && i != 12) { // Only fill non-pet slots
                this.setItemStack(i, glassPane);
            }
        }
    }

    private ItemStack createItem(Material material, String name, List<String> lore) {
        return ItemStack.builder(material)
                .customName(ColorUtils.of(name).build())
                .lore(ColorUtils.stringListToComponentList(lore))
                .build();
    }

}