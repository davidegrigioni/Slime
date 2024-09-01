package cc.davyy.slime.misc;

import cc.davyy.slime.utils.ColorUtils;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minestom.server.MinecraftServer;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static cc.davyy.slime.utils.FileUtils.getConfig;

@Singleton
public class BrandAnimator {

    private static final ComponentLogger LOGGER = ComponentLogger.logger(BrandAnimator.class);

    private final List<String> brandNameStyles;
    private final long animationInterval;
    private int currentIndex = 0;

    public BrandAnimator() {
        this.brandNameStyles = getConfig().getStringList("animation-styles");
        this.animationInterval = getConfig().getLong("animation-interval");
        final boolean animateEnabled = getConfig().getBoolean("animate");

        if (animateEnabled) {
            startAnimation();
        }
    }

    public void startAnimation() {
        MinecraftServer.getSchedulerManager().buildTask(this::updateBrandName)
                .repeat(animationInterval, TimeUnit.MILLISECONDS.toChronoUnit())
                .schedule();
    }

    private void updateBrandName() {
        if (brandNameStyles == null || brandNameStyles.isEmpty()) {
            LOGGER.info("No brand name styles available for animation.");
            return;
        }

        final String brandName = brandNameStyles.get(currentIndex);

        final Component component = ColorUtils
                .of(brandName)
                .build();
        final String legacyBrandName = LegacyComponentSerializer
                .legacyAmpersand()
                .serialize(component);

        MinecraftServer.setBrandName(legacyBrandName);

        currentIndex = (currentIndex + 1) % brandNameStyles.size();
    }

}