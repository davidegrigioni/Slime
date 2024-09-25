package cc.davyy.slime.commands.admin;

import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.services.SpawnService;
import cc.davyy.slime.utils.Messages;
import cc.davyy.slime.utils.PosUtils;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.coordinate.Pos;
import org.jetbrains.annotations.NotNull;

import static cc.davyy.slime.utils.GeneralUtils.hasPlayerPermission;

@Singleton
public class SetSpawnCommand extends Command {

    private final SpawnService spawnService;

    @Inject
    public SetSpawnCommand(SpawnService spawnService) {
        super("setspawn");
        this.spawnService = spawnService;

        setCondition((sender, commandString) -> {
            if (!hasPlayerPermission(sender, "slime.setspawn")) {
                sender.sendMessage(Messages.NO_PERMS.asComponent());
                return false;
            }
            return true;
        });

        setDefaultExecutor(this::setSpawn);
    }

    private void setSpawn(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final SlimePlayer player = (SlimePlayer) sender;
        final Pos pos = player.getPosition();

        spawnService.setSpawnPosition(pos);
        player.sendMessage(Messages.SPAWN_SET
                .addPlaceholder("pos", PosUtils.toString(pos))
                .asComponent());
    }

}