package de.craftsblock.cnet.module.cli.builtin;

import de.craftsblock.cnet.module.cli.CraftsNetCLI;
import de.craftsblock.cnet.module.cli.annotation.CommandMeta;
import de.craftsblock.cnet.module.cli.command.Command;
import de.craftsblock.cnet.module.cli.command.CommandExecutor;
import de.craftsblock.cnet.module.cli.command.CommandRegistry;
import de.craftsblock.craftsnet.CraftsNet;
import de.craftsblock.craftsnet.autoregister.meta.AutoRegister;
import de.craftsblock.craftsnet.autoregister.meta.constructors.FallbackConstructor;
import de.craftsblock.craftsnet.autoregister.meta.constructors.PreferConstructor;
import de.craftsblock.craftsnet.logging.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * A command executor that provides an overview of all registered commands.
 * <p>
 * This command lists every available CLI command along with optional
 * descriptions. When a single argument is supplied, the command displays
 * detailed information about the target command, including usage patterns
 * and aliases.
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.2
 * @since 1.0.0
 */
@AutoRegister
@CommandMeta(
        name = "help",
        description = "Shows all available commands",
        aliases = "?",
        usage = {"", "<command>"}
)
public record HelpCommand(CraftsNetCLI craftsNetCLI) implements CommandExecutor {

    /**
     * Constructs a new instance of the {@link HelpCommand} command.
     *
     * @param craftsNetCLI The {@link CraftsNetCLI} instance.
     */
    @PreferConstructor
    public HelpCommand {
    }

    /**
     * Constructs a new instance of the {@link HelpCommand} command using a
     * fallback mechanism that resolves the {@link CraftsNetCLI} addon from
     * the {@link CraftsNet} addon manager.
     *
     * @param craftsNet The {@link CraftsNet} instance.
     */
    @FallbackConstructor
    public HelpCommand(CraftsNet craftsNet) {
        this(craftsNet.getAddonManager().getAddon(CraftsNetCLI.class));
    }

    /**
     * Executes the {@link HelpCommand help command}.
     * <p>
     * If no arguments are provided, all registered commands are listed with
     * optional descriptions. When a specific command name or alias is supplied,
     * information about that command is displayed, including its primary name,
     * description, usage, and known aliases.
     *
     * @param command The {@link Command} instance representing the invoked command.
     * @param alias   The alias used to trigger this command.
     * @param args    The command arguments provided by the user.
     * @param logger  The logger used to print help information back to the console.
     */
    @Override
    public void onCommand(@NotNull Command command, String alias, @NotNull String[] args, @NotNull Logger logger) {
        CommandRegistry registry = craftsNetCLI.getCommandRegistry();

        if (args.length != 1) {
            registry.getCommands().entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> {
                        String name = entry.getKey();
                        Command value = entry.getValue();

                        String description = value.getDescription();
                        if (description == null || description.isBlank()) {
                            logger.info("- %s", name);
                            return;
                        }

                        logger.info("- %s: %s", name, description);
                    });

            return;
        }

        String commandName = args[0];
        Command target = registry.findCommand(commandName, null);

        if (target == null) {
            logger.info("No command or alias named %s found!", commandName);
            return;
        }

        String targetName = target.getName();
        if (target.isDeprecated())
            logger.warning("The %s is marked as deprecated and should not be used!", targetName);

        if (!targetName.equalsIgnoreCase(commandName))
            logger.info("Alias for: " + targetName);
        logger.info("Description: %s", target.getDescription());

        logger.info("Usages:");
        for (String usage : target.getUsage())
            logger.info(" - %s %s", targetName, usage);

        if (!target.getAliases().isEmpty())
            logger.info("Aliases: %s", String.join(", ", target.getAliases()));
    }

}
