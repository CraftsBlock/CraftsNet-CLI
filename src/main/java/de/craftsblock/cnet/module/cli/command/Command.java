package de.craftsblock.cnet.module.cli.command;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The {@link Command} class represents a command that can be executed.
 * It contains information about the command's name and its associated executor.
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.1.0
 * @see CommandExecutor
 * @see CommandRegistry
 * @since 1.0.0
 */
public class Command {

    private final String name;
    private final ConcurrentLinkedQueue<String> aliases = new ConcurrentLinkedQueue<>();

    private String description = "";
    private String[] usage = new String[]{};
    private CommandExecutor executor;

    /**
     * Constructs a new {@link Command} with the specified name.
     *
     * @param name The name of the command. It uniquely identifies the command.
     */
    public Command(String name) {
        this.name = name;
    }

    /**
     * Retrieves the name of the command.
     *
     * @return The name of the command, which is a unique identifier.
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if the command has an associated executor.
     *
     * @return {@code true} if an executor is set for this command, otherwise {@code false}.
     */
    public boolean hasExecutor() {
        return executor != null;
    }

    /**
     * Sets the executor for this command. The executor is responsible for handling the command's logic.
     *
     * @param executor The executor that will handle the execution of this command.
     */
    public void setExecutor(@NotNull CommandExecutor executor) {
        this.executor = executor;
    }

    /**
     * Retrieves the executor associated with this command.
     *
     * @return The executor for this command, or {@code null} if no executor is set.
     */
    public CommandExecutor getExecutor() {
        return executor;
    }

    /**
     * Sets the description for this command.
     *
     * @param description The description.
     * @since 1.0.1
     */
    public void setDescription(@NotNull String description) {
        this.description = description;
    }

    /**
     * Retrieves the description of this command.
     *
     * @return The description.
     * @since 1.0.1
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the usage for this command.
     *
     * @param usage The array of usages. This overrides the current set usages.
     * @since 1.0.1
     */
    public void setUsage(@NotNull String[] usage) {
        this.usage = usage;
    }

    /**
     * Retrieves the usages of this command.
     *
     * @return An array of usages.
     * @since 1.0.1
     */
    public String[] getUsage() {
        return usage;
    }

    /**
     * Checks whether this command is deprecated or not.
     * A command is deprecated if the {@link Deprecated} annotation is present
     * on the {@link CommandExecutor}.
     *
     * @return {@code true} if the command is deprecated, otherwise {@code false}.
     * @since 1.0.1
     */
    public boolean isDeprecated() {
        return executor != null && executor.getClass().getAnnotation(Deprecated.class) != null;
    }

    /**
     * Adds aliases to this command
     *
     * @param aliases The aliases which should be added
     */
    public void addAlias(@NotNull String... aliases) {
        this.aliases.addAll(Arrays.asList(aliases));
    }

    /**
     * Adds aliases to this command
     *
     * @param aliases The aliases which should be added
     * @since 1.0.1
     */
    public void removeAlias(@NotNull String... aliases) {
        this.aliases.removeAll(Arrays.asList(aliases));
    }

    /**
     * Checks if an alias is already present.
     *
     * @param alias The name of the alias which should be checked.
     * @return true if the alias is already present, false otherwise.
     */
    public boolean isAlias(String alias) {
        if (alias == null) return false;
        return aliases.contains(alias);
    }

    /**
     * Returns a list of all aliases for the command.
     *
     * @return The list of aliases.
     * @since 1.0.1
     */
    public Collection<String> getAliases() {
        return aliases;
    }

}
