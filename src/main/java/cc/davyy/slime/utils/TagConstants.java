package cc.davyy.slime.utils;

import net.minestom.server.tag.Tag;

public final class TagConstants {

    // Lobby-related tags
    public static final Tag<String> LOBBY_NAME_TAG = Tag.String("lobby-name");
    public static final Tag<Integer> LOBBY_ID_TAG = Tag.Integer("lobby-id");
    public static final Tag<Integer> DEATH_Y = Tag.Integer("deathY");
    public static final Tag<String> ACTION_TAG = Tag.String("action");

    // Server GUI tags
    public static final Tag<String> SKYWARS_SERVER_TAG = Tag.String("skywars-server");
    public static final Tag<String> SERVER_SWITCH_TAG = Tag.String("server-switch");

    // Player-specific tags for lobbies
    public static final Tag<Integer> PLAYER_LOBBY_ID_TAG = Tag.Integer("player-lobby-id");

    // Hologram Tags
    public static final Tag<Integer> HOLOGRAM_ID_TAG = Tag.Integer("hologram-id");

}