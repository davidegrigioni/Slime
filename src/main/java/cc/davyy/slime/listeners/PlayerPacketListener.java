package cc.davyy.slime.listeners;

import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Singleton;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.network.packet.client.ClientPacket;
import net.minestom.server.network.packet.client.play.ClientSteerVehiclePacket;
import org.jetbrains.annotations.NotNull;

@Singleton
public class PlayerPacketListener implements EventListener<PlayerPacketEvent> {

    @Override
    public @NotNull Class<PlayerPacketEvent> eventType() {
        return PlayerPacketEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PlayerPacketEvent event) {
        final SlimePlayer player = (SlimePlayer) event.getPlayer();
        final ClientPacket clientPacket = event.getPacket();

        if (clientPacket instanceof ClientSteerVehiclePacket clientSteerVehiclePacket) {
            final Entity vehicle = player.getVehicle();
            final float fwSpeed = clientSteerVehiclePacket.forward();
            final float swSpeed = clientSteerVehiclePacket.sideways();
            final byte flags = clientSteerVehiclePacket.flags();

            if (vehicle != null) {
                final Pos pos = player.getPosition();

                vehicle.setView(pos.yaw(), pos.pitch());

                final Vec forwardDir = pos.direction();
                final Vec sideways = forwardDir.cross(new Vec(0, -1, 0));
                final Vec total = forwardDir.mul(fwSpeed).add(sideways.mul(swSpeed));

                vehicle.setVelocity(vehicle.getVelocity().add(total));

                switch (flags) {
                    case 0x1 -> {}
                    case 0x2 -> vehicle.removePassenger(player);
                }
            }
        }

        return Result.SUCCESS;
    }

}