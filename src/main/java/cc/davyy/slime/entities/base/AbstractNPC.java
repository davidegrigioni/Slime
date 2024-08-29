package cc.davyy.slime.entities.base;

import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.*;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractNPC extends EntityCreature {

    protected final Component name;

    public AbstractNPC(@NotNull Component name, @NotNull Instance instance, @NotNull Pos spawn) {
        super(EntityType.PLAYER);
        this.name = name;
        setupNPC(name, instance, spawn);
    }

    private void setupNPC(@NotNull Component name, @NotNull Instance instance, @NotNull Pos spawn) {
        setInstance(instance, spawn);
        this.setCustomName(name);
        this.setCustomNameVisible(true);
        this.setNoGravity(true);
        this.setInvulnerable(true);
    }

    public abstract void onInteract(@NotNull Player player);

}