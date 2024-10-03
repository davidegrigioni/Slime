package cc.davyy.slime.commands.entities;

import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.services.entities.SidebarService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;

@Command(name = "sidebar", aliases = "sb")
@Permission("slime.sidebar")
@Singleton
public class SidebarCommand {

    private final SidebarService sidebarService;

    @Inject
    public SidebarCommand(SidebarService sidebarService) {
        this.sidebarService = sidebarService;
    }

    @Execute(name = "toggle")
    void toggleSidebar(@Context SlimePlayer player) {
        sidebarService.toggleSidebar(player);
    }

}