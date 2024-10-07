package cc.davyy.slime.litecommands;

import net.minestom.server.coordinate.Pos;

public enum LocationAxis {

    X, Y, Z;

    public static final int SIZE = values().length;

    public double getValue(Pos pos) {
        return switch (this) {
            case X -> pos.x();
            case Y -> pos.y();
            case Z -> pos.z();
        };

    }

    public static LocationAxis at(int index) {
        for (LocationAxis axis : values()) {
            if (axis.ordinal() == index) {
                return axis;
            }
        }

        throw new IllegalArgumentException("Unknown axis index: " + index);
    }

}
