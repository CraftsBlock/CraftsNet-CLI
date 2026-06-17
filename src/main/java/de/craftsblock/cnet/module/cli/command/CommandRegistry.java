package de.craftsblock.cnet.module.cli.command;

import de.craftsblock.cnet.module.cli.CraftsNetCLI;
import de.craftsblock.craftsnet.logging.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The {@link CommandRegistry} class manages and provides access to registered commands.
 * It allows commands to be retrieved, checked for existence, and executed.
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.1.0
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
        this.logger = addon.getCraftsNet().getLogger();
    }

    /**
     * Retrieves a command by name. If the command doesn't exist, it is created.
     *
     * @param name The name of the command to retrieve.
     * @return The command with the specified name or a new command if it doesn't exist.
     */
    public @NotNull Command getCommand(String name) {
        return commands.computeIfAbsent(name, Command::new);
    }

    /**
     * Finds a specific command by the name or the alias.
     *
     * @param command The name or alias of the command to find.
     * @return The matching command or {@code null} if not found.
     * @since 1.0.1
     */
    public @Nullable Command findCommand(String command) {
        return findCommand(command, null);
    }

    /**
     * Finds a specific command by the name or the alias.
     *
     * @param command The name or alias of the command to find.
     * @param orElse  The alternativ value if no matching command could be found.
     * @return The matching command or the {@code orElse} value.
     * @since 1.0.1
     */
    public @Nullable Command findCommand(String command, Command orElse) {
        return commands.getOrDefault(
                command,
                commands.values().stream()
                        .filter(cmd -> cmd.isAlias(command))
                        .findFirst().orElse(orElse)
        );
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
    public void perform(String name, String args) {
        Command command = findCommand(name);

        if (command == null) {
            logger.warning("No command or alias named %s found!", name);
            return;
        }

        if (command.isDeprecated())
            logger.warning("The command %s is marked as deprecated and should not be used!", command.getName());

        command.getExecutor().onCommand(command, name, parseArgs(args), logger);
    }

    /**
     * Parses a raw argument string into a string array.
     *
     * <p>The parsing rules are intentionally minimal and focus on simplicity and performance.
     * Each argument is built character-by-character, respecting escaped whitespace.</p>
     *
     * @param rawArgs the raw input string containing space-separated arguments,
     *                potentially including escaped spaces using {@code \ }.
     *                May be {@code null} or blank.
     * @return an array of parsed arguments; never {@code null}, but possibly empty
     */
    private String[] parseArgs(String rawArgs) {
        if (rawArgs == null || rawArgs.isEmpty()) {
            return new String[0];
        }

        List<String> args = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        boolean escaped = false;

        for (char c : rawArgs.toCharArray()) {
            if (escaped) {
                current.append(c);
                escaped = false;
            } else if (c == '\\') {
                escaped = true;
            } else if (Character.isWhitespace(c)) {
                if (!current.isEmpty()) {
                    args.add(current.toString());
                    current.setLength(0);
                }
            } else {
                current.append(c);
            }
        }

        if (!current.isEmpty()) {
            args.add(current.toString());
        }

        return args.toArray(String[]::new);
    }

    /**
     * Get a map of all currently registered commands mapped to thier name.
     *
     * @return The command map.
     * @since 1.0.1
     */
    public @NotNull ConcurrentHashMap<String, Command> getCommands() {
        return commands;
    }

}
