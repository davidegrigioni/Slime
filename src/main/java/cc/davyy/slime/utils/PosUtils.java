package cc.davyy.slime.utils;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class PosUtils {

    private PosUtils() {}

    public static Pos buildPosFromPlayer(@NotNull Player player) { return new Pos(player.getPosition()); }

    public static Pos of(@NotNull Point point) { return new Pos(point); }

    public static Pos of(double x, double y, double z) { return new Pos(x, y, z); }

}