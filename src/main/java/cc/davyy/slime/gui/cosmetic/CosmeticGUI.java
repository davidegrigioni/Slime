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
public class CosmeticGUI extends Inventory {

    private static final String COSMETIC_GUI_TITLE = FileUtils.getConfig().getString("cosmetic-gui-title");

    private static final int[] COSMETIC_SLOTS = {10, 12, 14};

    public CosmeticGUI() {
        super(InventoryType.CHEST_3_ROW, ColorUtils.of(COSMETIC_GUI_TITLE).build());
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

        this.setItemStack(COSMETIC_SLOTS[0], particleEffect);
        this.setItemStack(COSMETIC_SLOTS[1], hat);
        this.setItemStack(COSMETIC_SLOTS[2], petItem);
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
        for (int cosmeticSlot : COSMETIC_SLOTS) {
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

    private void openParticleEffectGUI(SlimePlayer player) {
        // Implement the logic to open the Particle Effect sub-GUI
        ParticleEffectGUI particleEffectGUI = new ParticleEffectGUI();
        particleEffectGUI.open(player);
    }

    private void openHatGUI(SlimePlayer player) {
        // Implement the logic to open the Hat sub-GUI
        HatGUI hatGUI = new HatGUI();
        hatGUI.open(player);
    }

    private void openPetGUI(SlimePlayer player) {
        // Implement the logic to open the Pet sub-GUI
        PetGUI petGUI = new PetGUI();
        petGUI.open(player);
    }

}