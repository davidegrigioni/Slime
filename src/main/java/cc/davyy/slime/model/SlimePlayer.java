package cc.davyy.slime.model;

import cc.davyy.slime.constants.TagConstants;
import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.platform.PlayerAdapter;
import net.luckperms.api.util.Tristate;
import net.minestom.server.entity.Player;
import net.minestom.server.network.player.PlayerConnection;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import static cc.davyy.slime.utils.ColorUtils.of;

import java.util.UUID;

public class SlimePlayer extends Player {

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
     * Checks if the player has a permission.
     *
     * @param permissionName the name of the permission to check
     * @return true if the player has the permission
     */
    @Override
    public boolean hasPermission(@NotNull String permissionName) {
        return this.getPermissionValue(permissionName).asBoolean();
    }

    public String getPrimaryGroup() {
        return this.getLuckPermsMetaData().getPrimaryGroup();
    }

    public String getMetaValue(@NotNull String key) {
        return this.getLuckPermsMetaData().getMetaValue(key);
    }

    public String getPlainPrefix() {
        return this.getLuckPermsMetaData().getPrefix();
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

    public void setVanish() { this.setTag(TagConstants.VANISH_TAG, "vanished"); }

    public void unsetVanish() { this.removeTag(TagConstants.VANISH_TAG); }

    public boolean hasVanish() { return this.hasTag(TagConstants.VANISH_TAG); }

    public String getVanish() { return this.getTag(TagConstants.VANISH_TAG); }

    public void setLobbyID(int lobbyID) { this.setTag(TagConstants.PLAYER_LOBBY_ID_TAG, lobbyID); }

    public boolean hasLobbyID() { return this.hasTag(TagConstants.PLAYER_LOBBY_ID_TAG); }

    public int getLobbyID() { return this.getTag(TagConstants.PLAYER_LOBBY_ID_TAG); }

}