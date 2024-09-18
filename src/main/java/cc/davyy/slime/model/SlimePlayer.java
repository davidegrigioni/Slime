package cc.davyy.slime.model;

import cc.davyy.slime.constants.TagConstants;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.platform.PlayerAdapter;
import net.luckperms.api.util.Tristate;
import net.minestom.server.entity.Player;
import net.minestom.server.network.player.PlayerConnection;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import static cc.davyy.slime.utils.ColorUtils.of;
import static cc.davyy.slime.utils.FileUtils.getConfig;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class SlimePlayer extends Player {

    private final @NotNull LuckPerms luckPerms;
    private final @NonNull PlayerAdapter<Player> playerAdapter;

    public SlimePlayer(@NotNull LuckPerms luckPerms, @NotNull UUID uuid, @NotNull String username, @NotNull PlayerConnection playerConnection) {
        super(uuid, username, playerConnection);
        this.luckPerms = luckPerms;
        this.playerAdapter = this.luckPerms.getPlayerAdapter(Player.class);
    }

    private @NotNull User getLuckPermsUser() {
        return this.playerAdapter.getUser(this);
    }

    private @NotNull CachedMetaData getLuckPermsMetaData() {
        return this.getLuckPermsUser().getCachedData().getMetaData();
    }

    /**
     * Sets a permission for the player. This method uses a {@link Node} rather
     * than a permission name, this allows for permissions that rely on context.
     * You may choose not to implement this method on a production server, and
     * leave permission management to the LuckPerms web interface or in-game
     * commands.
     *
     * @param permission the permission to set
     * @param value the value of the permission
     * @return the result of the operation
     */
    public @NotNull CompletableFuture<DataMutateResult> setPermission(@NotNull Node permission, boolean value) {
        User user = this.getLuckPermsUser();
        DataMutateResult result = value
                ? user.data().add(permission)
                : user.data().remove(permission);
        return this.luckPerms.getUserManager().saveUser(user).thenApply(ignored -> result);
    }

    /**
     * Checks if the player has a permission.
     *
     * @param permissionName the name of the permission to check
     * @return true if the player has the permission
     */
    @Override
    public boolean hasPermission(@NotNull String permissionName) {
        return this.getPermissionValue(permissionName).asBoolean();
    }

    /**
     * Gets the value of a permission. This passes a {@link Tristate} value
     * straight from LuckPerms, which may be a better option than using
     * boolean values in some cases.
     *
     * @param permissionName the name of the permission to check
     * @return the value of the permission
     */
    public @NotNull Tristate getPermissionValue(@NotNull String permissionName) {
        User user = this.getLuckPermsUser();
        return user.getCachedData().getPermissionData().checkPermission(permissionName);
    }

    /**
     * Gets the prefix of the player. This method uses the MiniMessage library
     * to parse the prefix, which is a more advanced option than using legacy
     * chat formatting.
     *
     * @return the prefix of the player
     */
    public @NotNull Component getPrefix() {
        String prefix = this.getLuckPermsMetaData().getPrefix();
        if (prefix == null) return Component.empty();
        return of(prefix).parseLegacy().build();
    }

    public @NotNull Component getChatFormat(@NotNull String message) {
        final CachedMetaData metaData = this.getLuckPermsMetaData();
        final String group = metaData.getPrimaryGroup();
        final String groupFormat = getConfig().getString("group-formats." + group) != null ? "group-formats." + group : "chat-format";
        final String format = getConfig().getString(groupFormat);

        return of(format)
                .addPlainStringPlaceholder("lobbyid", String.valueOf(getLobbyID()))
                .addFormattedStringPlaceholder("prefix", metaData.getPrefix() != null ? metaData.getPrefix() : "")
                .addFormattedStringPlaceholder("message", message)
                .addComponentPlaceholder("name", this.getName())
                .addFormattedStringPlaceholder("username-color", metaData.getMetaValue("username-color") != null ? metaData.getMetaValue("username-color") : "")
                .addFormattedStringPlaceholder("message-color", metaData.getMetaValue("message-color") != null ? metaData.getMetaValue("message-color") : "")
                .build();
    }

    public @NotNull Component getDefaultChatFormat(@NotNull String message) {
        final String format = getConfig().getString("chat-format");

        return of(format)
                .addComponentPlaceholder("prefix", this.getPrefix())
                .addPlainStringPlaceholder("lobbyid", String.valueOf(getLobbyID()))
                .addPlainStringPlaceholder("message", message)
                .addComponentPlaceholder("name", this.getName())
                .build();
    }

    public void setLobbyID(int lobbyID) { this.setTag(TagConstants.PLAYER_LOBBY_ID_TAG, lobbyID); }

    public void removeLobbyID() { this.removeTag(TagConstants.PLAYER_LOBBY_ID_TAG); }

    public boolean hasLobbyID() { return this.hasTag(TagConstants.PLAYER_LOBBY_ID_TAG); }

    public int getLobbyID() { return this.getTag(TagConstants.PLAYER_LOBBY_ID_TAG); }

    public void setParkourCourse(int courseID) { this.setTag(TagConstants.PARKOUR_COURSE_ID_TAG, courseID); }

    public int getParkourCourse() { return this.getTag(TagConstants.PARKOUR_COURSE_ID_TAG); }

    public void setCheckpoint(int checkpointID) { this.setTag(TagConstants.PARKOUR_CHECKPOINT_TAG, checkpointID); }

    public int getCheckpoint() { return this.getTag(TagConstants.PARKOUR_CHECKPOINT_TAG); }

    public void setParkourCompletionStatus(boolean status) { this.setTag(TagConstants.PARKOUR_COMPLETION_STATUS_TAG, status); }

    public boolean hasParkourCompleted() { return this.hasTag(TagConstants.PARKOUR_COMPLETION_STATUS_TAG); }

    public boolean getParkourCompletionStatus() { return this.getTag(TagConstants.PARKOUR_COMPLETION_STATUS_TAG); }

    public void setParkourStartTime(long startTime) { this.setTag(TagConstants.PARKOUR_START_TIME_TAG, startTime); }

    public long getParkourStartTime() { return this.getTag(TagConstants.PARKOUR_START_TIME_TAG); }

    public void removeFromParkourCourse() {
        this.removeTag(TagConstants.PARKOUR_COURSE_ID_TAG);
    }

    public boolean isInParkourCourse() {
        return this.hasTag(TagConstants.PARKOUR_COURSE_ID_TAG);
    }

}