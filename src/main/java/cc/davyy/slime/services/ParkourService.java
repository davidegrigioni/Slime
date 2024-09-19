package cc.davyy.slime.services;

import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.model.parkour.Parkour;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public interface ParkourService {

    void createParkour(@NotNull Component name);

    void deleteParkour(int id);

    Optional<Parkour> getParkour(int id);

    List<Parkour> getAllParkours();

    void startParkour(@NotNull SlimePlayer player, int parkourId);

    void endParkour(@NotNull SlimePlayer player);

    void setCheckpoint(@NotNull SlimePlayer player);

    void editCheckpoint(@NotNull SlimePlayer player, int checkpointId);

    void finishParkour(@NotNull SlimePlayer player);

}