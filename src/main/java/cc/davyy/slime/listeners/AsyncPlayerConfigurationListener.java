package cc.davyy.slime.listeners;

import cc.davyy.slime.utils.PosUtils;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.validate.Check;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

import static cc.davyy.slime.utils.FileUtils.getConfig;

public class AsyncPlayerConfigurationListener implements EventListener<AsyncPlayerConfigurationEvent> {

    @Override
    public @NotNull Class<AsyncPlayerConfigurationEvent> eventType() { return AsyncPlayerConfigurationEvent.class; }

    @Override
    public @NotNull Result run(@NotNull AsyncPlayerConfigurationEvent event) {
        Player player = event.getPlayer();

        InstanceManager instanceManager = MinecraftServer.getInstanceManager();

        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();
        instanceContainer.setGenerator(unit -> unit.modifier().fillHeight(0, 40, Block.STONE));

        instanceContainer.setChunkSupplier(LightingChunk::new);

        var instances = MinecraftServer.getInstanceManager().getInstances();
        Instance instance = instances.stream().skip(new Random().nextInt(instances.size())).findFirst().orElse(null);
        event.setSpawningInstance(instance);

        final String posString = getConfig().getString("spawn-pos");
        final Pos pos = PosUtils.fromString(posString);
        //Check.notNull(pos, "Position cannot be null, Check your Config!");
        player.setRespawnPoint(new Pos(0, 40f, 0));

        return Result.SUCCESS;
    }

}