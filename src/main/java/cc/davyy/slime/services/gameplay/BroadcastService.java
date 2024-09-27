package cc.davyy.slime.services.gameplay;

import net.minestom.server.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Service interface for broadcasting messages and titles to players.
 */
public interface BroadcastService {

    /**
     * Broadcasts a message to all players.
     * <p>
     * This method sends a message to all players on the server. The message is broadcasted from the given sender.
     * </p>
     *
     * @param sender the sender of the message, typically used for context or logging
     * @param message the message to be broadcasted
     */
    void broadcastMessage(@NotNull CommandSender sender, @NotNull String message);

    /**
     * Broadcasts a title to a collection of players.
     * <p>
     * This method sends a title to all specified players. The title is displayed to the players with a fade-in, stay, and fade-out effect.
     * </p>
     *
     * @param sender the sender of the broadcast, typically used for context or logging
     * @param titleText the text of the title to be broadcasted
     */
    void broadcastTitle(@NotNull CommandSender sender, @NotNull String titleText);

    /**
     * Broadcasts a title and subtitle to a collection of players.
     * <p>
     * This method sends both a title and a subtitle to all specified players. The title and subtitle are displayed with a fade-in, stay, and fade-out effect.
     * </p>
     *
     * @param sender the sender of the broadcast, typically used for context or logging
     * @param titleText the text of the title to be broadcasted
     * @param subTitle the text of the subtitle to be broadcasted
     */
    void broadcastTitle(@NotNull CommandSender sender, @NotNull String titleText, @NotNull String subTitle);

    /**
     * Broadcasts a title and subtitle with specific timing to a collection of players.
     * <p>
     * This method sends a title and a subtitle to all specified players, with customized timings for the fade-in, stay, and fade-out durations.
     * </p>
     *
     * @param sender the sender of the broadcast, typically used for context or logging
     * @param titleText the text of the title to be broadcasted
     * @param subTitle the text of the subtitle to be broadcasted
     */
    void broadcastTitleWithTimes(@NotNull CommandSender sender, @NotNull String titleText, @NotNull String subTitle);

}
