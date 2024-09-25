package cc.davyy.slime.module;

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
import org.reflections.Reflections;

import java.util.Set;
import java.util.function.Function;

public class ManagerModule extends AbstractModule {

    @Override
    protected void configure() {
        registerManagersBindings();
    }

    @Provides
    @Singleton
    public EventNode<Event> provideEventNode() {
        return EventNode.all("npc-events");
    }

    @Provides
    public Function<Entity, Team> provideEntityToTeamFunction(TeamManager teamManager) {
        final Team nameTagTeam = new TeamBuilder("name-tags", teamManager)
                .collisionRule(TeamsPacket.CollisionRule.NEVER)
                .build();
        return entity -> nameTagTeam;
    }

    @Provides
    public TeamManager provideTeamManager() {
        return MinecraftServer.getTeamManager();
    }

    private void registerManagersBindings() {
        Reflections reflections = new Reflections("cc.davyy.slime.managers");
        Set<Class<?>> singletonClasses = reflections.getTypesAnnotatedWith(Singleton.class);

        for (Class<?> clazz : singletonClasses) {
            bind(clazz).in(Singleton.class);
        }
    }

}