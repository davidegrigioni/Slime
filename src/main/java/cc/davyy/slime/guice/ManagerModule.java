package cc.davyy.slime.guice;

import cc.davyy.slime.managers.general.SkinManager;
import cc.davyy.slime.managers.entities.nametag.NameTagManager;
import cc.davyy.slime.managers.entities.npc.NPCManager;
import cc.davyy.slime.managers.gameplay.ChatManager;
import cc.davyy.slime.managers.general.CommandManager;
import cc.davyy.slime.managers.general.EventsManager;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.network.packet.server.play.TeamsPacket;
import net.minestom.server.scoreboard.Team;
import net.minestom.server.scoreboard.TeamBuilder;
import net.minestom.server.scoreboard.TeamManager;

import java.util.function.Function;

public class ManagerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(NameTagManager.class).in(Singleton.class);
        bind(NPCManager.class).in(Singleton.class);

        bind(ChatManager.class).in(Singleton.class);

        bind(CommandManager.class).in(Singleton.class);
        bind(EventsManager.class).in(Singleton.class);

        bind(SkinManager.class).in(Singleton.class);
    }

    @Provides
    static EventNode<Event> provideEventNode() {
        return EventNode.all("npc-events");
    }

    @Provides
    static Function<Entity, Team> provideEntityToTeamFunction(TeamManager teamManager) {
        final Team nameTagTeam = new TeamBuilder("name-tags", teamManager)
                .collisionRule(TeamsPacket.CollisionRule.NEVER)
                .build();
        return entity -> nameTagTeam;
    }

    @Provides
    static TeamManager provideTeamManager() {
        return MinecraftServer.getTeamManager();
    }

}