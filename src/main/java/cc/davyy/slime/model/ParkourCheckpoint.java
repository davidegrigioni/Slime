package cc.davyy.slime.model;

import net.minestom.server.coordinate.Pos;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record ParkourCheckpoint(int id, @NotNull List<Pos> checkpointList) {}