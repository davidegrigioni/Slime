package cc.davyy.slime.utils;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PosUtils {

    private PosUtils() {}

    /**
     * Creates a {@link Pos} from a string in the format "x,y,z".
     *
     * @param posString The string representing the position.
     * @return A {@link Pos} object, or {@code null} if the string is invalid.
     */
    public static @Nullable Pos fromString(@NotNull String posString) {
        try {
            String[] parts = posString.split(",");
            if (parts.length != 3) {
                return null;
            }

            double x = Double.parseDouble(parts[0].trim());
            double y = Double.parseDouble(parts[1].trim());
            double z = Double.parseDouble(parts[2].trim());

            return new Pos(x, y, z);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Converts a {@link Pos} to a string in the format "x,y,z".
     *
     * @param pos The {@link Pos} object.
     * @return A string representing the position.
     */
    public static @NotNull String toString(@NotNull Pos pos) {
        return String.format("%d,%d,%d", (int) pos.x(), (int) pos.y(), (int) pos.z());
    }

    /**
     * Creates a {@link Pos} from individual coordinates.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param z The z-coordinate.
     * @return A {@link Pos} object.
     */
    public static @NotNull Pos of(double x, double y, double z) {
        return new Pos(x, y, z);
    }

    public static @NotNull Pos of(double x, double y, double z, float yaw, float pitch) {
        return new Pos(x, y, z, yaw, pitch);
    }

    /**
     * Creates a {@link Pos} from a {@link Point} object.
     *
     * @param point The {@link Point} object.
     * @return A {@link Pos} object.
     */
    public static @NotNull Pos of(@NotNull Point point) {
        return new Pos(point.x(), point.y(), point.z());
    }

}