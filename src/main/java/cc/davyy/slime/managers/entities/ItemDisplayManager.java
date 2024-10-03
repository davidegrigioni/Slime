package cc.davyy.slime.managers.entities;

import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.services.entities.ItemDisplayService;
import com.google.inject.Singleton;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.display.ItemDisplayMeta;
import net.minestom.server.item.ItemStack;
import net.minestom.server.utils.location.RelativeVec;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static net.kyori.adventure.text.Component.text;

@Singleton
public class ItemDisplayManager implements ItemDisplayService {

    private final Map<UUID, Entity> itemDisplays = new ConcurrentHashMap<>(); // Store item displays per player UUID

    @Override
    public void summonItemDisplay(@NotNull SlimePlayer player) {
        final Entity entity = new Entity(EntityType.ITEM_DISPLAY);
        entity.setInstance(player.getInstance(), player.getPosition());
        itemDisplays.put(player.getUuid(), entity);

        player.sendMessage(text("Item display summoned!"));
    }

    @Override
    public void modifyItemDisplay(@NotNull SlimePlayer player,
                                  @NotNull ItemStack itemStack,
                                  @NotNull RelativeVec scale,
                                  @NotNull ItemDisplayMeta.DisplayContext displayContext) {
        Entity entity = itemDisplays.get(player.getUuid());

        if (entity == null) {
            player.sendMessage(text("No item display found to modify!"));
            return;
        }

        final ItemDisplayMeta displayMeta = (ItemDisplayMeta) entity.getEntityMeta();
        displayMeta.setScale(scale.from(player));
        displayMeta.setItemStack(itemStack);
        displayMeta.setDisplayContext(displayContext);

        player.sendMessage(text("Item display modified!"));
    }

    @Override
    public void removeItemDisplay(@NotNull SlimePlayer player) {
        final Entity entity = itemDisplays.remove(player.getUuid());

        if (entity != null) {
            entity.remove();
            player.sendMessage(text("Item display removed!"));
            return;
        }

        player.sendMessage(text("No item display found to remove."));
    }

    @Override
    public void clearAllDisplays() {
        itemDisplays.values().forEach(Entity::remove);
        itemDisplays.clear();
    }

}