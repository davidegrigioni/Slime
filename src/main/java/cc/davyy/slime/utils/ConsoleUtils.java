package cc.davyy.slime.utils;

import net.minestom.server.MinecraftServer;

import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class ConsoleUtils {

    private static final ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor(Thread
            .ofVirtual()
            .name("SlimeConsoleWorker")
            .factory());
    private static final Scanner CONSOLE_IN = new Scanner(System.in);
    private static final Object consoleLock = new Object();

    private ConsoleUtils() {}

    /**
     * Sets up the console so commands can be executed from there
     */
    public static void setupConsole() {
        worker.scheduleAtFixedRate(() -> {
            if (CONSOLE_IN.hasNext()) {
                String command = CONSOLE_IN.nextLine();

                synchronized (consoleLock) {
                    MinecraftServer.getCommandManager().getDispatcher().execute(MinecraftServer.getCommandManager().getConsoleSender(), command);
                }
            }
        }, 50, 50, TimeUnit.MILLISECONDS);
    }

}