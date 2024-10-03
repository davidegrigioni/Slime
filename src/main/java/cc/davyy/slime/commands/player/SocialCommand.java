package cc.davyy.slime.commands.player;

import cc.davyy.slime.config.ConfigManager;
import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.cooldown.Cooldown;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;

import java.time.temporal.ChronoUnit;
import java.util.List;

import static cc.davyy.slime.utils.ColorUtils.of;

@Command(name = "social")
@Permission("slime.social")
@Cooldown(key = "social-cooldown", count = 10, unit = ChronoUnit.SECONDS, bypass = "slime.admin")
@Singleton
public class SocialCommand {

    private final ConfigManager configManager;

    @Inject
    public SocialCommand(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Execute
    void execute(@Context SlimePlayer player) {
        final List<String> socials = configManager.getUi().getStringList("socials");
        final String socialMessage = String.join("\n", socials);

        player.sendMessage(of(socialMessage)
                .build());
    }

}