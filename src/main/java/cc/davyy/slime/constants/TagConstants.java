package cc.davyy.slime.constants;

import net.minestom.server.tag.Tag;

public final class TagConstants {

    // Lobby-related tags
    public static final Tag<String> LOBBY_NAME_TAG = Tag.String("lobby-name");
    public static final Tag<Integer> LOBBY_ID_TAG = Tag.Integer("lobby-id");
    public static final Tag<Integer> DEATH_Y = Tag.Integer("deathY");
    public static final Tag<String> ACTION_TAG = Tag.String("action");

    // Parkour Tags
    public static final Tag<Integer> PARKOUR_COURSE_ID_TAG = Tag.Integer("parkour_course_id");
    public static final Tag<Integer> PARKOUR_CHECKPOINT_TAG = Tag.Integer("parkour_checkpoint_id");
    public static final Tag<String> PLAYER_PARKOUR_PROGRESS_TAG = Tag.String("player_parkour_progress");
    public static final Tag<Boolean> PARKOUR_COMPLETION_STATUS_TAG = Tag.Boolean("parkour_completion_status");
    public static final Tag<Long> PARKOUR_START_TIME_TAG = Tag.Long("parkour_start_time");
    public static final Tag<Long> PARKOUR_BEST_TIME_TAG = Tag.Long("parkour_best_time");
    public static final Tag<Integer> PARKOUR_ATTEMPTS_TAG = Tag.Integer("parkour_attempts");
    public static final Tag<Integer> PARKOUR_FALL_Y_TAG = Tag.Integer("parkour-y-fall");

    // NPC Related tags
    public static final Tag<Integer> NPC_ID_TAG = Tag.Integer("npc-id");

    // Cosmetic Related tags
    public static final Tag<String> COSMETIC_NAME_TAG = Tag.String("cosmetic-name");

    // Server GUI tags
    public static final Tag<String> SERVER_SWITCH_TAG = Tag.String("server-switch");

    // Player-specific tags for lobbies
    public static final Tag<Integer> PLAYER_LOBBY_ID_TAG = Tag.Integer("player-lobby-id");

    // Hologram Tags
    public static final Tag<Integer> HOLOGRAM_ID_TAG = Tag.Integer("hologram-id");

}