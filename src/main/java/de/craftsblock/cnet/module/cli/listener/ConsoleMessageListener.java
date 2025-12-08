package de.craftsblock.cnet.module.cli.listener;

import de.craftsblock.cnet.module.cli.CraftsNetCLI;
import de.craftsblock.cnet.module.cli.event.ConsoleMessageEvent;
import de.craftsblock.craftscore.event.EventHandler;
import de.craftsblock.craftscore.event.EventPriority;
import de.craftsblock.craftscore.event.ListenerAdapter;
import de.craftsblock.craftsnet.autoregister.meta.AutoRegister;
import de.craftsblock.craftsnet.autoregister.meta.constructors.PreferConstructor;

import java.util.Arrays;

/**
 * The {@link ConsoleMessageListener} class is responsible for handling console messages
 * and executing commands. It listens for the {@link ConsoleMessageEvent} and processes
 * console input into commands.
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.1
 * @see de.craftsblock.cnet.module.cli.command.CommandRegistry CommandRegistry
 * @since 1.0.0
 */
@AutoRegister
public record ConsoleMessageListener(CraftsNetCLI addon) implements ListenerAdapter {

    /**
     * Constructs a new instance of a console listener
     *
     * @param addon The {@link CraftsNetCLI} instance which instantiates this console listener
     */
    @PreferConstructor
    public ConsoleMessageListener {
    }

    /**
     * Event handler for console messages.
     *
     * @param event The {@link ConsoleMessageEvent} which has been fired.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleConsoleMessage(ConsoleMessageEvent event) {
        if (event.isCancelled()) return;

        String message = event.getMessage();
        if (message.isBlank()) return;

        if (addon.getLogStream() != null)
            addon.getLogStream().addLine("> " + message);

        String[] commandParams = message.split(" ", 2);

        String command = commandParams[0];
        String[] args = Arrays.stream(commandParams).skip(1).toArray(String[]::new);

        addon.getCommandRegistry().perform(command, args);
    }

}