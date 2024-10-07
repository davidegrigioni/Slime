package cc.davyy.slime.commands.entities;

import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.services.entities.DisguiseService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.RootCommand;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import net.minestom.server.entity.EntityType;

@RootCommand
@Permission("slime.disguise")
@Singleton
public class DisguiseCommand {

//    private final DisguiseService disguiseService;
//
//    @Inject
//    public DisguiseCommand(DisguiseService disguiseService) {
//        this.disguiseService = disguiseService;
//    }
//
//    @Execute(name = "undisguise")
//    void undisguise(@Context SlimePlayer player) {
//        disguiseService.undisguise(player);
//    }
//
//    @Execute(name = "disguise")
//    void disguise(@Context SlimePlayer player, @Arg String nickName) {
//        disguiseService.disguise(player, EntityType.PLAYER, nickName);
//    }
//
//    @Execute(name = "disguise")
//    void disguise(@Context SlimePlayer player, @Arg EntityType entityType) {
//        disguiseService.disguise(player, entityType, "");
//    }

}