package cc.davyy.slime.managers;

import cc.davyy.slime.services.BroadcastService;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.utils.Messages;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.minestom.server.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Collection;

import static cc.davyy.slime.utils.ColorUtils.of;
import static cc.davyy.slime.utils.FileUtils.getConfig;
import static cc.davyy.slime.utils.GeneralUtils.broadcastAllInstances;
import static cc.davyy.slime.utils.GeneralUtils.sendComponent;

@Singleton
public class BroadcastManager implements BroadcastService {

    @Override
    public void broadcastMessage(@NotNull CommandSender sender, @NotNull String message) {
        final String configMessage = getConfig().getString("broadcast");

        if (message.isEmpty()) {
            sendComponent(sender, Messages.MESSAGE_EMPTY.asComponent());
            return;
        }

        broadcastAllInstances(of(configMessage)
                .parseMMP("message", message)
                .parseLegacy()
                .build());
    }

    @Override
    public void broadcastTitle(@NotNull CommandSender sender, @NotNull String titleText, @NotNull Collection<SlimePlayer> players) {
        if (titleText.isEmpty()) {
            sendComponent(sender, Messages.MESSAGE_EMPTY.asComponent());
            return;
        }

        final Title title = Title.title(parseComponent(titleText), Component.empty());
        players.forEach(player -> player.showTitle(title));
    }

    @Override
    public void broadcastTitle(@NotNull CommandSender sender, @NotNull String titleText, @NotNull String subTitle, @NotNull Collection<SlimePlayer> players) {
        if (titleText.isEmpty()) {
            sendComponent(sender, Messages.MESSAGE_EMPTY.asComponent());
            return;
        }

        final Title title = Title.title(parseComponent(titleText), parseComponent(subTitle));
        players.forEach(player -> player.showTitle(title));
    }

    @Override
    public void broadcastTitleWithTimes(@NotNull CommandSender sender, @NotNull String titleText, @NotNull String subTitle, @NotNull Collection<SlimePlayer> players) {
        if (titleText.isEmpty()) {
            sendComponent(sender, Messages.MESSAGE_EMPTY.asComponent());
            return;
        }

        final Duration fadeInDuration = getDuration("title-settings.fadeIn");
        final Duration stayDuration = getDuration("title-settings.stay");
        final Duration fadeOutDuration = getDuration("title-settings.fadeOut");

        final Title.Times times = Title.Times.times(fadeInDuration, stayDuration, fadeOutDuration);
        final Title title = Title.title(parseComponent(titleText), parseComponent(subTitle), times);
        players.forEach(player -> player.showTitle(title));
    }

    private Duration getDuration(@NotNull String path) {
        final long value = getConfig().getLong(path + ".value");
        final String unit = getConfig().getString(path + ".unit");

        return switch (unit.toLowerCase()) {
            case "minutes" -> Duration.ofMinutes(value);
            case "seconds" -> Duration.ofSeconds(value);
            case "milliseconds" -> Duration.ofMillis(value);
            case "hours" -> Duration.ofHours(value);
            default -> throw new IllegalArgumentException("Unknown time unit: " + unit);
        };
    }

    private Component parseComponent(@NotNull String text) {
        return of(text)
                .parseLegacy()
                .build();
    }

}