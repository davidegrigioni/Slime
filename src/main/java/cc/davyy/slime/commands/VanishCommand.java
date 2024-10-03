package cc.davyy.slime.commands;

import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.services.gameplay.VanishService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.rollczi.litecommands.annotations.command.RootCommand;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;

@RootCommand
@Permission("slime.vanish")
@Singleton
public class VanishCommand {

    private final VanishService vanishService;

    @Inject
    public VanishCommand(VanishService vanishService) {
        this.vanishService = vanishService;
    }

    @Execute(name = "vanish")
    void executeVanish(@Context SlimePlayer player) {
        vanishService.vanish(player);
    }

    @Execute(name = "unvanish")
    void executeUnvanish(@Context SlimePlayer player) {
        vanishService.unvanish(player);
    }

}