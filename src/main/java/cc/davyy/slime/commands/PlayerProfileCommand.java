package cc.davyy.slime.commands;

import cc.davyy.slime.database.DatabaseManager;
import cc.davyy.slime.database.entities.PlayerProfile;
import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import net.kyori.adventure.text.Component;

@Command(name = "playerprofile", aliases = "profile")
@Singleton
public class PlayerProfileCommand {

    private final DatabaseManager databaseManager;

    @Inject
    public PlayerProfileCommand(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Execute(name = "view")
    void executePlayerProfile(@Context SlimePlayer player, @Arg SlimePlayer target) {
        databaseManager.getPlayerProfile(target.getUuid()).thenAcceptAsync(optionalProfile -> {
            if (optionalProfile.isPresent()) {
                PlayerProfile profile = optionalProfile.get();
                player.sendMessage(Component.text("Player Profile for: " + profile.getPlayerID()));
                player.sendMessage(Component.text("Username: " + player.getUsername()));
                player.sendMessage(Component.text("Staff: " + profile.isStaff()));
                player.sendMessage(Component.text("Vanish: " + profile.isVanished()));
                player.sendMessage(Component.text("Kills: " + profile.getKills()));
            } else {
                player.sendMessage(Component.text("No profile found for this player."));
            }
        });
    }

    @Execute(name = "register")
    void register(@Context SlimePlayer sender, @Arg SlimePlayer target) {
        databaseManager.getPlayerProfile(target.getUuid()).thenAcceptAsync(optionalProfile -> {
            if (optionalProfile.isPresent()) {
                sender.sendMessage(Component.text("Profile already exists for " + target.getUsername()));
            } else {
                // Create a new PlayerProfile
                PlayerProfile newProfile = new PlayerProfile();

                databaseManager.saveOrUpdatePlayerProfile(newProfile).thenRun(() -> {
                    sender.sendMessage(Component.text("New profile created for " + target.getUsername()));
                }).exceptionally(ex -> {
                    sender.sendMessage(Component.text("Failed to create profile!"));
                    return null;
                });
            }
        });
    }

}
