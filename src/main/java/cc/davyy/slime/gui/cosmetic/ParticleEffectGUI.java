package cc.davyy.slime.gui.cosmetic;

import cc.davyy.slime.managers.ConfigManager;
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
public class ParticleEffectGUI extends Inventory {

    private final String particleGuiTitle;
    private final ConfigManager configManager;

    @Inject
    public ParticleEffectGUI(String particleGuiTitle, ConfigManager configManager) {
        super(InventoryType.CHEST_3_ROW, ColorUtils.of(particleGuiTitle).build());
        this.particleGuiTitle = configManager.getConfig().getString("particle-gui-title");
        this.configManager = configManager;
        setupParticles();
        fillBackground();
    }

    public void open(@NotNull SlimePlayer player) {
        player.openInventory(this);
    }

    private void setupParticles() {
        // Example particle effects (you can replace these with actual particles)
        ItemStack smokeParticle = createItem(Material.SOUL_SAND,
                "<gradient:#555555:#ffffff>Smoke</gradient>",
                List.of("Create a cloud of smoke!", "Click to use."));

        ItemStack sparkParticle = createItem(Material.GLOWSTONE_DUST,
                "<gradient:#ffcc00:#ff6600>Sparks</gradient>",
                List.of("Create sparkling effects!", "Click to use."));

        this.setItemStack(10, smokeParticle);
        this.setItemStack(12, sparkParticle);
    }

    private void fillBackground() {
        ItemStack glassPane = ItemStack.builder(Material.GRAY_STAINED_GLASS_PANE)
                .customName(Component.text(" ")) // Invisible item
                .build();

        for (int i = 0; i < this.getSize(); i++) {
            if (i != 10 && i != 12) { // Only fill non-particle slots
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
