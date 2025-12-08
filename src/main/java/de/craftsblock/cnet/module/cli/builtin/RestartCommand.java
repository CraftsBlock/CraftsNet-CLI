package de.craftsblock.cnet.module.cli.builtin;

import de.craftsblock.cnet.module.cli.annotation.CommandMeta;
import de.craftsblock.cnet.module.cli.command.Command;
import de.craftsblock.cnet.module.cli.command.CommandExecutor;
import de.craftsblock.craftsnet.CraftsNet;
import de.craftsblock.craftsnet.autoregister.meta.AutoRegister;
import de.craftsblock.craftsnet.autoregister.meta.constructors.PreferConstructor;
import de.craftsblock.craftsnet.logging.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * Command executor for the reload command.
 * This class implements the {@link CommandExecutor} interface and provides functionality to handle the "restart" command.
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.1
 * @since 1.0.0
 * @deprecated This command is using a feature of craftsnet which will be removed completely in the future.
 * Therefor this command will be removed when this feature does no longer exist in craftsnet.
 */
@AutoRegister
@CommandMeta(
        name = "restart",
        description = "Restarts craftsnet",
        aliases = {"reload", "rl"},
        usage = {"", "confirm"}
)
@Deprecated(since = "1.0.0", forRemoval = true)
public record RestartCommand(CraftsNet craftsNet) implements CommandExecutor {

    /**
     * Constructs a new instance of the reload command.
     *
     * @param craftsNet The CraftsNet instance for which the reload command was registered
     */
    @PreferConstructor
    public RestartCommand {
    }

    /**
     * Executes the reload command.
     *
     * @param command The command to be executed.
     * @param alias   The alias name of the command used to access it.
     * @param args    The arguments passed with the command.
     * @param logger  The logger for outputting messages or logging.
     */
    @Override
    @SuppressWarnings("removal")
    public void onCommand(@NotNull Command command, @NotNull String alias, @NotNull String[] args, @NotNull Logger logger) {
        if (args.length != 1 || !args[0].equalsIgnoreCase("confirm")) {
            logger.warning("Please use '%s confirm' if you really want to reload CraftsNet as it may break some things.", alias);
            return;
        }

        try {
            logger.info("Restarting CraftsNet...");
            craftsNet.restart(() -> {
                System.out.println();
                System.out.println();
            });
        } catch (Exception e) {
            logger.error("Could not restart CraftsNet", e);
        }
    }

}
