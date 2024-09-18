package cc.davyy.slime.model;

import net.minestom.server.MinecraftServer;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.extras.velocity.VelocityProxy;

import static net.minestom.server.MinecraftServer.LOGGER;

public enum ServerMode {

    ONLINE {
        @Override
        public void initEncryption(String velocitySecret) {
            MojangAuth.init();
            LOGGER.debug("Mojang authentication initialized.");
        }
    },
    OFFLINE {
        @Override
        public void initEncryption(String velocitySecret) {
            LOGGER.info("Offline mode selected, no encryption required.");
        }
    },
    VELOCITY {
        @Override
        public void initEncryption(String velocitySecret) {
            if (velocitySecret.isBlank()) {
                LOGGER.error("Velocity is enabled but no secret is specified. Stopping server.");
                MinecraftServer.stopCleanly();
                return;
            }

            VelocityProxy.enable(velocitySecret);
            LOGGER.debug("Velocity enabled: {}", VelocityProxy.isEnabled());
        }
    };

    public abstract void initEncryption(String velocitySecret);

}