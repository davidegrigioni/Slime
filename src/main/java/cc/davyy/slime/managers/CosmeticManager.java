package cc.davyy.slime.managers;

import cc.davyy.slime.constants.TagConstants;
import cc.davyy.slime.interfaces.ICosmetics;
import cc.davyy.slime.model.Cosmetic;
import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static cc.davyy.slime.utils.ColorUtils.stringListToComponentList;
import static net.kyori.adventure.text.Component.text;
import static cc.davyy.slime.utils.ColorUtils.of;

@Singleton
public class CosmeticManager implements ICosmetics {

    private static final AtomicInteger cosmeticIDCounter = new AtomicInteger(1);

    private final Map<Integer, Cosmetic> cosmeticMap = new ConcurrentHashMap<>();

    @Override
    public void createCosmetic(@NotNull String cosmeticName, @NotNull String cosmeticDescription, @NotNull ItemStack cosmeticItem, @NotNull Cosmetic.CosmeticType cosmeticType) {
        int id = cosmeticIDCounter.getAndIncrement();
        Cosmetic cosmetic = new Cosmetic(id, cosmeticName, cosmeticDescription, cosmeticItem, cosmeticType);
        cosmeticMap.put(id, cosmetic);
    }

    @Override
    public void removeCosmetic(int id) {
        cosmeticMap.remove(id);
    }

    @Override
    public Cosmetic getCosmetic(int id) {
        return cosmeticMap.get(id);
    }

    @Override
    public void applyCosmetic(@NotNull SlimePlayer player, int id) {
        Cosmetic cosmetic = cosmeticMap.get(id);
        if (cosmetic != null) {
            switch (cosmetic.type()) {
                case ITEM -> applyItemCosmetic(player, cosmetic);
                /*case PARTICLE -> applyParticleCosmetic(player, cosmetic);
                case MOB -> applyMobCosmetic(player, cosmetic);*/
            }
        } else {
            player.sendMessage(text("Cosmetic with ID " + id + " does not exist.")
                    .color(NamedTextColor.RED));
        }
    }

    @Override
    public void removeCosmetic(@NotNull SlimePlayer player, int id) {
        Cosmetic cosmetic = cosmeticMap.get(id);
        if (cosmetic != null) {
            /*switch (cosmetic.type()) {
                case ITEM -> removeItemCosmetic(player, cosmetic);
                case PARTICLE -> removeParticleEffect(player);
                case MOB -> removeMobCosmetic(player);
            }*/
        } else {
            player.sendMessage(text("Cosmetic with ID " + id + " does not exist.")
                    .color(NamedTextColor.RED));
        }
    }

    @Override
    public List<Cosmetic> getAvailableCosmetics() {
        return new ArrayList<>(cosmeticMap.values());
    }

    private void applyItemCosmetic(@NotNull SlimePlayer player, @NotNull Cosmetic cosmetic) {
        player.getInventory().addItemStack(cosmetic.cosmeticItem());
        player.sendMessage(text("You have received the cosmetic item: " + cosmetic.name())
                .color(NamedTextColor.GREEN));
    }

}