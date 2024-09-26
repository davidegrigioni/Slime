package cc.davyy.slime.managers;

import cc.davyy.slime.services.BrandService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minestom.server.MinecraftServer;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static net.kyori.adventure.text.Component.text;

@Singleton
public class BrandManager implements BrandService {

    private static final ComponentLogger LOGGER = ComponentLogger.logger(BrandManager.class);

    private final List<String> brandNameStyles;
    private final long animationInterval;
    private final String defaultBrandName;

    private int currentIndex = 0;

    @Inject
    public BrandManager(ConfigManager configManager) {
        this.brandNameStyles = configManager.getConfig().getStringList("branding.animation-styles");
        this.animationInterval = configManager.getConfig().getLong("branding.animation-interval");
        this.defaultBrandName = configManager.getConfig().getString("branding.brand-name");
        final boolean animateEnabled = configManager.getConfig().getBoolean("branding.animate");

        if (!animateEnabled) {
            setDefaultBrandName();
            return;
        }

        if (animationInterval <= 0) {
            LOGGER.warn("Animation interval is non-positive, animation will not start.");
            setDefaultBrandName();
            return;
        }

        startAnimation();
    }

    @Override
    public void startAnimation() {
        MinecraftServer.getSchedulerManager().buildTask(this::updateBrandName)
                .repeat(Duration.of(animationInterval, TimeUnit.SECONDS.toChronoUnit()))
                .schedule();
    }

    private void updateBrandName() {
        if (brandNameStyles == null || brandNameStyles.isEmpty()) {
            LOGGER.warn("No brand name styles available for animation.");
            return;
        }

        try {
            final String brandName = brandNameStyles.get(currentIndex);

            final Component brandComponent = text(brandName);
            final String serializedBrandComponent = LegacyComponentSerializer.legacySection().serialize(brandComponent);

            MinecraftServer.setBrandName(serializedBrandComponent);

            currentIndex = (currentIndex + 1) % brandNameStyles.size();
        } catch (Exception ex) {
            LOGGER.error("Error updating brand name", ex);
        }
    }

    private void setDefaultBrandName() {
        try {

            final Component brandComponent = text(defaultBrandName);
            final String serializedBrandComponent = LegacyComponentSerializer
                    .legacySection()
                    .serialize(brandComponent);

            MinecraftServer.setBrandName(serializedBrandComponent);

            LOGGER.info("Animation disabled, setting default brand name: {}", defaultBrandName);
        } catch (Exception ex) {
            LOGGER.error("Error setting default brand name", ex);
        }
    }

}