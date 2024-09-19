package cc.davyy.slime.services;

import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.model.parkour.Parkour;
import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ParkourService {

    void createParkour(@NotNull Component name);

    void deleteParkour(int id);

    Parkour getParkour(int id);

    List<Parkour> getAllParkours();

    void startParkour(@NotNull SlimePlayer player, int parkourId);

    void endParkour(@NotNull SlimePlayer player);

    void updateCheckpoint(@NotNull SlimePlayer player);

}