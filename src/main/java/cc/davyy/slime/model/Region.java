package cc.davyy.slime.model;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;

public class Region {

    private final Vec minPoint;
    private final Vec maxPoint;

    /**
     * Creates a region defined by two opposite corners.
     *
     * @param point1 The first corner of the region.
     * @param point2 The second corner of the region.
     */
    public Region(Vec point1, Vec point2) {
        this.minPoint = new Vec(
                Math.min(point1.x(), point2.x()),
                Math.min(point1.y(), point2.y()),
                Math.min(point1.z(), point2.z())
        );

        this.maxPoint = new Vec(
                Math.max(point1.x(), point2.x()),
                Math.max(point1.y(), point2.y()),
                Math.max(point1.z(), point2.z())
        );
    }

    /**
     * Checks if a given position is within the region.
     *
     * @param pos The position to check.
     * @return True if the position is inside the region, false otherwise.
     */
    public boolean contains(Pos pos) {
        return pos.x() >= minPoint.x() && pos.x() <= maxPoint.x() &&
                pos.y() >= minPoint.y() && pos.y() <= maxPoint.y() &&
                pos.z() >= minPoint.z() && pos.z() <= maxPoint.z();
    }

    public Vec getMinPoint() { return minPoint; }

    public Vec getMaxPoint() { return maxPoint; }

}