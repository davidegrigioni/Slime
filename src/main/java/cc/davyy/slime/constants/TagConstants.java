package cc.davyy.slime.constants;

import net.kyori.adventure.bossbar.BossBar;
import net.minestom.server.scoreboard.Sidebar;
import net.minestom.server.tag.Tag;

public final class TagConstants {

    public static final Tag<String> LOBBY_NAME_TAG = Tag.String("lobby-name");
    public static final Tag<Integer> LOBBY_ID_TAG = Tag.Integer("lobby-id");
    public static final Tag<Integer> DEATH_Y = Tag.Integer("deathY");
    public static final Tag<String> ACTION_TAG = Tag.String("action");

    public static final Tag<String> VANISH_TAG = Tag.String("vanish");

    public static final Tag<String> DISGUISE_TAG = Tag.Transient("disguise");

    public static final Tag<BossBar> BOSS_BAR_TAG = Tag.Transient("bossbars");

    public static final Tag<Sidebar> SIDEBAR_TAG = Tag.Transient("player_sidebars");

    public static final Tag<String> SERVER_SWITCH_TAG = Tag.String("server-switch");

    public static final Tag<Integer> PLAYER_LOBBY_ID_TAG = Tag.Integer("player-lobby-id");

    public static final Tag<Integer> HOLOGRAM_ID_TAG = Tag.Integer("hologram-id");

}