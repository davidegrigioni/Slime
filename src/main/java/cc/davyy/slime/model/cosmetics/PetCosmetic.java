package cc.davyy.slime.model.cosmetics;

import cc.davyy.slime.model.SlimePlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ai.goal.FollowTargetGoal;
import net.minestom.server.entity.ai.target.ClosestEntityTarget;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public record PetCosmetic(int id, @NotNull Component name, @NotNull EntityType entityType) implements Cosmetic {

    // Store pets associated with players
    private static final Map<UUID, EntityCreature> playerPets = new ConcurrentHashMap<>();

    @Override
    public CosmeticType type() {
        return CosmeticType.PET;
    }

    @Override
    public void apply(@NotNull SlimePlayer player) {
        final Instance instance = player.getInstance();
        final Pos position = player.getPosition();

        // Create the entity
        EntityCreature entityCreature = new EntityCreature(entityType);
        entityCreature.setCustomName(name);
        entityCreature.setInstance(instance, position);

        // Add AI and set properties
        entityCreature.addAIGroup(
                List.of(new FollowTargetGoal(entityCreature, Duration.ofSeconds(1))),
                List.of(new ClosestEntityTarget(entityCreature, 32, entity -> entity instanceof SlimePlayer))
        );

        // Store the entity for the player
        playerPets.put(player.getUuid(), entityCreature);
    }

    @Override
    public void remove(@NotNull SlimePlayer player) {
        final EntityCreature entity = playerPets.remove(player.getUuid());
        if (entity != null) {
            entity.remove();
        }
    }

}