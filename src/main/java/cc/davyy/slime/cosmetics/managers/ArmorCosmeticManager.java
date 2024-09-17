package cc.davyy.slime.cosmetics.managers;

import cc.davyy.slime.cosmetics.ArmorCosmeticService;
import cc.davyy.slime.factories.CosmeticFactory;
import cc.davyy.slime.cosmetics.model.ArmorCosmetic;
import cc.davyy.slime.cosmetics.model.ArmorData;
import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
public class ArmorCosmeticManager implements ArmorCosmeticService {

    private static final AtomicInteger armorIDCounter = new AtomicInteger(1);

    private final CosmeticFactory cosmeticFactory;
    private final Map<Integer, ArmorCosmetic> armorCosmeticMap = new ConcurrentHashMap<>();

    @Inject
    public ArmorCosmeticManager(CosmeticFactory cosmeticFactory) {
        this.cosmeticFactory = cosmeticFactory;
    }

    @Override
    public void createCosmetic(@NotNull Component name, @NotNull ArmorData armorData) {
        final int armorID = armorIDCounter.getAndIncrement();
        final ArmorCosmetic armorCosmetic = cosmeticFactory.createArmorCosmetic(armorID, name, armorData);

        armorCosmeticMap.put(armorID, armorCosmetic);
    }

    @Override
    public void applyCosmetic(@NotNull SlimePlayer player, int id) {
        final ArmorCosmetic armorCosmetic = armorCosmeticMap.get(id);

        armorCosmetic.apply(player);
    }

    @Override
    public void removeCosmetic(@NotNull SlimePlayer player, int id) {
        final ArmorCosmetic armorCosmetic = armorCosmeticMap.get(id);

        armorCosmetic.remove(player);
    }

    @Override
    public @NotNull Optional<ArmorCosmetic> getCosmeticByID(int id) {
        return Optional.ofNullable(armorCosmeticMap.get(id));
    }

    @Override
    public @NotNull List<ArmorCosmetic> getAvailableCosmetics() {
        return List.copyOf(armorCosmeticMap.values());
    }

}