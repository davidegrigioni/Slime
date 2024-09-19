package cc.davyy.slime.factories;

import cc.davyy.slime.model.parkour.Parkour;
import cc.davyy.slime.model.parkour.ParkourCheckpoint;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

@Singleton
public final class ParkourFactory {

    public Parkour createParkour(int id, @NotNull Component parkourName, @NotNull ParkourCheckpoint parkourCheckpoint) {
        return new Parkour(id, parkourName, parkourCheckpoint);
    }

}