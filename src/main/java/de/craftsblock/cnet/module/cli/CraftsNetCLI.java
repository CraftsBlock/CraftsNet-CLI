package de.craftsblock.cnet.module.cli;

import de.craftsblock.cnet.module.cli.autoregister.CommandAutoRegisterHandler;
import de.craftsblock.cnet.module.cli.command.CommandRegistry;
import de.craftsblock.cnet.module.cli.event.ConsoleMessageEvent;
import de.craftsblock.craftsnet.addon.Addon;
import de.craftsblock.craftsnet.addon.meta.annotations.Meta;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The main addon class responsible for providing CLI integration to CraftsNet.
 * <p>
 * This addon initializes the console listener, command registry, and automatic
 * command registration system. Its primary purpose is to capture console input
 * and route it through CraftsNet’s event system, enabling command execution
 * through the standard input stream.
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @since 1.0.0
 */
@Meta(name = "CraftsNetCLI")
public final class CraftsNetCLI extends Addon {

    private CommandRegistry commandRegistry;

    private Thread consoleListener;
    private BufferedReader consoleReader;

    /**
     * Called when the addon is loaded but not yet active.
     * <p>
     * This initializes the {@link CommandRegistry} and registers the automatic
     * command discovery handler, enabling annotation-based command registration.
     */
    @Override
    public void onLoad() {
        this.commandRegistry = new CommandRegistry(this);
        this.getAutoRegisterRegistry().register(new CommandAutoRegisterHandler(this));
    }

    /**
     * Called when the addon becomes active.
     * <p>
     * This sets up the console reader and starts a background thread that listens
     * for console input. When input is received, it is dispatched as a
     * {@link ConsoleMessageEvent}.
     */
    @Override
    public void onEnable() {
        this.consoleReader = createConsoleReader();
        this.consoleListener = startConsoleListener();

        if (this.consoleListener != null) {
            getLogger().info("Started the console reader");
        }
    }

    /**
     * Called when the addon is disabled.
     * <p>
     * Closes the console reader and interrupts the console listener thread.
     * Ensures a graceful shutdown of all console-related resources.
     */
    @Override
    public void onDisable() {
        if (this.consoleReader != null) {
            getLogger().info("Closing the console reader");
            try {
                consoleReader.close();
            } catch (IOException ignored) {
            }
        }

        if (consoleListener == null) {
            return;
        }

        getLogger().info("Closing the console input listener");
        this.consoleListener.interrupt();
        this.consoleListener = null;
    }

    /**
     * Creates a new {@link BufferedReader} that reads from the {@link System#in} input steam.
     * The instance is modified in a way that it does not close the underlying input stream
     * when it is closed.
     *
     * @return The new {@link BufferedReader} that reads from {@link System#in}.
     */
    private BufferedReader createConsoleReader() {
        if (this.consoleReader != null) {
            return this.consoleReader;
        }

        return new BufferedReader(new InputStreamReader(System.in)) {
            @Override
            public void close() {
                consoleReader = null;
            }
        };
    }

    /**
     * Creates a new thread that listens to the console and performs the according events.
     *
     * @return The console reader thread.
     */
    @Nullable
    private Thread startConsoleListener() {
        if (System.in == null) {
            getLogger().error("Console input stream not available!");
            return null;
        }

        Thread console = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    if (consoleReader == null) {
                        break;
                    }

                    String line = consoleReader.readLine();
                    if (line == null) {
                        getLogger().error("Unexpected console input: null");
                        getLogger().error("Console reader will be closed after this!");
                        break;
                    }

                    getListenerRegistry().call(new ConsoleMessageEvent(line));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, "CraftsNet cli");
        console.setDaemon(true);
        console.start();
        return console;
    }

    /**
     * Get the {@link CommandRegistry} instance.
     *
     * @return The {@link CommandRegistry} instance.
     */
    public CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }

}
