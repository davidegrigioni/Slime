package cc.davyy.slime.commands.admin;

import cc.davyy.slime.managers.gameplay.ChatManager;
import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.join.Join;
import dev.rollczi.litecommands.annotations.permission.Permission;

@Command(name = "say")
@Permission("slime.say")
@Singleton
public class SayCommand {

    private final ChatManager chatManager;

    @Inject
    public SayCommand(ChatManager chatManager) {
        this.chatManager = chatManager;
    }

    @Execute
    void execute(@Context SlimePlayer player, @Arg SlimePlayer target, @Join String finalMessage) {
        target.sendMessage(chatManager.setDefaultChatFormat(player, finalMessage));
    }

}