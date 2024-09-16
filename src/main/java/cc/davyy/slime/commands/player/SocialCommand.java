package cc.davyy.slime.commands.player;

import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.text;

@Singleton
public class SocialCommand extends Command {

    public SocialCommand() {
        super("social");

        setDefaultExecutor(this::execute);
    }

    private void execute(@NotNull CommandSender sender, @NotNull CommandContext context) {
        final Component twitterComponent = text("Twitter: ")
                .color(TextColor.fromHexString("#1DA1F2"))  // Twitter's blue color
                .append(text("@YourTwitterHandle")
                        .color(TextColor.fromHexString("#1DA1F2"))
                        .decorate(TextDecoration.BOLD));

        final Component tiktokComponent = text("TikTok: ")
                .color(TextColor.fromHexString("#000000"))  // TikTok's black color
                .append(text("@YourTikTokHandle")
                        .color(TextColor.fromHexString("#00F2F3"))  // TikTok's gradient color
                        .decorate(TextDecoration.BOLD));

        final Component instagramComponent = text("Instagram: ")
                .color(TextColor.fromHexString("#C13584"))  // Instagram's pink color
                .append(text("@YourInstagramHandle")
                        .color(TextColor.fromHexString("#C13584"))
                        .decorate(TextDecoration.BOLD));

        final Component youtubeComponent = text("YouTube: ")
                .color(TextColor.fromHexString("#FF0000"))  // YouTube's red color
                .append(text("YourYouTubeChannelURL")
                        .color(TextColor.fromHexString("#FF0000"))
                        .decorate(TextDecoration.BOLD));

        final Component socialMessage = text("Follow us on social media:\n")
                .append(twitterComponent)
                .append(newline())
                .append(tiktokComponent)
                .append(newline())
                .append(instagramComponent)
                .append(newline())
                .append(youtubeComponent);

        sender.sendMessage(socialMessage);
    }

}