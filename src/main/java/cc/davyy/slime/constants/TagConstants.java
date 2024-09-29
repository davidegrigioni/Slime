package cc.davyy.slime.constants;

import net.kyori.adventure.bossbar.BossBar;
import net.minestom.server.entity.EntityType;
import net.minestom.server.scoreboard.Sidebar;
import net.minestom.server.tag.Tag;

public final class TagConstants {

    // Lobby-related tags
    public static final Tag<String> LOBBY_NAME_TAG = Tag.String("lobby-name");
    public static final Tag<Integer> LOBBY_ID_TAG = Tag.Integer("lobby-id");
    public static final Tag<Integer> DEATH_Y = Tag.Integer("deathY");
    public static final Tag<String> ACTION_TAG = Tag.String("action");

    public static final Tag<String> NICKNAME_TAG = Tag.Transient("nickname-disguises");
    public static final Tag<EntityType> ENTITY_TYPE_TAG = Tag.Transient("entities-disguises");

    public static final Tag<BossBar> BOSS_BAR_TAG = Tag.Transient("bossbars");

    public static final Tag<Sidebar> SIDEBAR_TAG = Tag.Transient("player_sidebars");

    // Server GUI tags
    public static final Tag<String> SERVER_SWITCH_TAG = Tag.String("server-switch");

    // Player-specific tags for lobbies
    public static final Tag<Integer> PLAYER_LOBBY_ID_TAG = Tag.Integer("player-lobby-id");

    // Hologram Tags
    public static final Tag<Integer> HOLOGRAM_ID_TAG = Tag.Integer("hologram-id");

}