package cc.davyy.slime.commands;

import com.google.inject.Singleton;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.permission.Permission;

@Command(name = "vanish")
@Permission("slime.vanish")
@Singleton
public class VanishCommand {}