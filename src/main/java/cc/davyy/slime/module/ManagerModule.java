package cc.davyy.slime.module;

import cc.davyy.slime.managers.*;
import cc.davyy.slime.managers.cosmetics.ArmorCosmeticManager;
import cc.davyy.slime.managers.cosmetics.HatCosmeticManager;
import cc.davyy.slime.managers.cosmetics.ParticleCosmeticManager;
import cc.davyy.slime.managers.cosmetics.PetCosmeticManager;
import cc.davyy.slime.managers.entities.HologramManager;
import cc.davyy.slime.managers.entities.nametag.NameTagManager;
import cc.davyy.slime.managers.entities.npc.NPCManager;
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
        bind(ArmorCosmeticManager.class).in(Singleton.class);
        bind(HatCosmeticManager.class).in(Singleton.class);
        bind(ParticleCosmeticManager.class).in(Singleton.class);
        bind(PetCosmeticManager.class).in(Singleton.class);

        bind(NameTagManager.class).in(Singleton.class);
        bind(NPCManager.class).in(Singleton.class);
        bind(HologramManager.class).in(Singleton.class);

        bind(BossBarManager.class).in(Singleton.class);
        bind(BrandManager.class).in(Singleton.class);
        bind(BroadcastManager.class).in(Singleton.class);
        bind(CommandManager.class).in(Singleton.class);
        bind(GameModeManager.class).in(Singleton.class);
        bind(LobbyManager.class).in(Singleton.class);
        bind(SidebarManager.class).in(Singleton.class);
        bind(SkinManager.class).in(Singleton.class);
        bind(SpawnManager.class).in(Singleton.class);
        bind(TeleportManager.class).in(Singleton.class);
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

}