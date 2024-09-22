package cc.davyy.slime.model;

import net.minestom.server.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record Vehicle(int id, @NotNull EntityType entityType, double vehicleSpeed, @NotNull UUID playerUUID) {}