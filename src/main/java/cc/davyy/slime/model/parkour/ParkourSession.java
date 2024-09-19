package cc.davyy.slime.model.parkour;

import org.jetbrains.annotations.NotNull;

public record ParkourSession(@NotNull Parkour parkour, long startTime, int currentCheckpoint) {}
