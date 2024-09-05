package cc.davyy.slime.commands;

import cc.davyy.slime.managers.HologramManager;
import net.minestom.server.command.builder.Command;

public class HologramCommand extends Command {

    private final HologramManager hologramManager;

    public HologramCommand(HologramManager hologramManager) {
        super("hologram", "holo");
        this.hologramManager = hologramManager;


    }

}