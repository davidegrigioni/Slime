package cc.davyy.slime.commands.parkour;

import cc.davyy.slime.managers.ParkourManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minestom.server.command.builder.Command;

@Singleton
public class ParkourCommand extends Command {

    private final ParkourManager parkourManager;

    @Inject
    public ParkourCommand(ParkourManager parkourManager) {
        super("parkour");
        this.parkourManager = parkourManager;
    }

}