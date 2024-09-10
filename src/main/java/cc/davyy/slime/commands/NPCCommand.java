package cc.davyy.slime.commands;

import com.google.inject.Singleton;
import net.minestom.server.command.builder.Command;

@Singleton
public class NPCCommand extends Command {

    public NPCCommand() {
        super("npc");
    }

}