package cc.davyy.slime.module;

import cc.davyy.slime.commands.parkour.ParkourCommand;
import cc.davyy.slime.managers.ParkourManager;
import cc.davyy.slime.services.ParkourService;
import com.google.inject.AbstractModule;

public class ParkourModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ParkourManager.class);

        bind(ParkourCommand.class);

        bind(ParkourService.class).to(ParkourManager.class);
    }

}