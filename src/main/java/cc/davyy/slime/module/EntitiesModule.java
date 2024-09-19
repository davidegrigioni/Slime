package cc.davyy.slime.module;

import cc.davyy.slime.commands.entities.HologramCommand;
import cc.davyy.slime.commands.entities.NPCCommand;
import cc.davyy.slime.managers.entities.HologramManager;
import cc.davyy.slime.managers.entities.NPCManager;
import cc.davyy.slime.services.entities.HologramService;
import cc.davyy.slime.services.entities.NPCService;
import com.google.inject.AbstractModule;

public class EntitiesModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(HologramManager.class);
        bind(NPCManager.class);

        bind(NPCService.class).to(NPCManager.class);
        bind(HologramService.class).to(HologramManager.class);

        bind(HologramCommand.class);
        bind(NPCCommand.class);
    }

}