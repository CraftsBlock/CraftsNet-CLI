package de.craftsblock.cnet.module.cli.command;

import de.craftsblock.cnet.module.cli.CraftsNetCLI;
import de.craftsblock.craftsnet.logging.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentHashMap;

/**
 * The {@link CommandRegistry} class manages and provides access to registered commands.
 * It allows commands to be retrieved, checked for existence, and executed.

 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @see Command
 * @since 1.0.0
 */
public class CommandRegistry {

    private final ConcurrentHashMap<String, Command> commands = new ConcurrentHashMap<>();
    private final Logger logger;

    /**
     * Constructs a new instance of the command registry.
     *
     * @param addon The CraftsNet instance which instantiates this command registry.
     */
    public CommandRegistry(CraftsNetCLI addon) {
        this.logger = addon.getLogger();
    }

    /**
     * Retrieves a command by name. If the command doesn't exist, it is created.
     *
     * @param name The name of the command to retrieve.
     * @return The command with the specified name or a new command if it doesn't exist.
     */
    @NotNull
    public Command getCommand(String name) {
        return commands.computeIfAbsent(name, Command::new);
    }

    /**
     * Checks if a command with the given name exists.
     *
     * @param name The name of the command to check for existence.
     * @return {@code true} if a command with the specified name exists, otherwise {@code false}.
     */
    public boolean hasCommand(String name) {
        return commands.containsKey(name);
    }

    /**
     * Performs the execution of a command with the provided name and arguments.
     *
     * @param name The name of the command to execute.
     * @param args The arguments to pass to the command executor.
     */
    public void perform(String name, String[] args) {
        Command command;

        if (commands.containsKey(name)) command = commands.get(name);
        else command = commands.values().stream()
                .filter(cmd -> cmd.isAlias(name))
                .findFirst()
                .orElse(null);

        if (command == null) {
            logger.warning("This command was not found!");
            return;
        }

        command.getExecutor().onCommand(command, name, args, logger);
    }

}
