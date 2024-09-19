package cc.davyy.slime.module;

import cc.davyy.slime.commands.entities.HologramCommand;
import cc.davyy.slime.commands.entities.NPCCommand;
import cc.davyy.slime.managers.entities.HologramManager;
import cc.davyy.slime.managers.entities.npc.NPCManager;
import cc.davyy.slime.services.entities.HologramService;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;

public class EntitiesModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(HologramManager.class);
        bind(NPCManager.class);

        bind(HologramService.class).to(HologramManager.class);

        bind(HologramCommand.class);
        bind(NPCCommand.class);
    }

    @Provides
    @Singleton
    public EventNode<Event> provideEventNode() {
        return EventNode.all("npc-events");
    }

}