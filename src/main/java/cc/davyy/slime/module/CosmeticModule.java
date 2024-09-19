package cc.davyy.slime.module;

import cc.davyy.slime.commands.cosmetic.CosmeticCommand;
import cc.davyy.slime.commands.cosmetic.subcommands.ArmorSubCommand;
import cc.davyy.slime.commands.cosmetic.subcommands.HatSubCommand;
import cc.davyy.slime.commands.cosmetic.subcommands.ParticleSubCommand;
import cc.davyy.slime.commands.cosmetic.subcommands.PetSubCommand;
import cc.davyy.slime.factories.CosmeticFactory;
import cc.davyy.slime.managers.cosmetics.ArmorCosmeticManager;
import cc.davyy.slime.managers.cosmetics.HatCosmeticManager;
import cc.davyy.slime.managers.cosmetics.ParticleCosmeticManager;
import cc.davyy.slime.managers.cosmetics.PetCosmeticManager;
import cc.davyy.slime.services.cosmetics.ArmorCosmeticService;
import cc.davyy.slime.services.cosmetics.ParticleCosmeticService;
import com.google.inject.AbstractModule;

public class CosmeticModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ArmorCosmeticManager.class);
        bind(HatCosmeticManager.class);
        bind(ParticleCosmeticManager.class);
        bind(PetCosmeticManager.class);

        bind(ArmorCosmeticService.class).to(ArmorCosmeticManager.class);
        bind(ParticleCosmeticService.class).to(ParticleCosmeticManager.class);

        bind(CosmeticCommand.class);
        bind(ArmorSubCommand.class);
        bind(HatSubCommand.class);
        bind(ParticleSubCommand.class);
        bind(PetSubCommand.class);

        bind(CosmeticFactory.class);
    }

}