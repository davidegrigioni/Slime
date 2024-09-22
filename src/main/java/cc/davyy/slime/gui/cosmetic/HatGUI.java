package cc.davyy.slime.gui.cosmetic;

import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.utils.ColorUtils;
import cc.davyy.slime.utils.FileUtils;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Singleton
public class HatGUI extends Inventory {

    private static final String HAT_GUI_TITLE = FileUtils.getConfig().getString("hat-gui-title");

    public HatGUI() {
        super(InventoryType.CHEST_3_ROW, ColorUtils.of(HAT_GUI_TITLE).build());
        setupHats();
        fillBackground();
    }

    public void open(@NotNull SlimePlayer player) {
        player.openInventory(this);
    }

    private void setupHats() {
        // Example hats (you can replace these with actual hat items)
        ItemStack leatherHat = createItem(Material.LEATHER_HELMET,
                "<gradient:#ff7f50:#ff4500>Leather Hat</gradient>",
                List.of("A stylish leather hat!", "Click to equip."));

        ItemStack wizardHat = createItem(Material.DIAMOND_HELMET,
                "<gradient:#00bfff:#1e90ff>Wizard Hat</gradient>",
                List.of("A magical wizard hat!", "Click to equip."));

        this.setItemStack(10, leatherHat);
        this.setItemStack(12, wizardHat);
    }

    private void fillBackground() {
        ItemStack glassPane = ItemStack.builder(Material.GRAY_STAINED_GLASS_PANE)
                .customName(Component.text(" ")) // Invisible item
                .build();

        for (int i = 0; i < this.getSize(); i++) {
            if (i != 10 && i != 12) { // Only fill non-hat slots
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