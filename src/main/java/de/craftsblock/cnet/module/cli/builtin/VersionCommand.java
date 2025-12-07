package de.craftsblock.cnet.module.cli.builtin;

import de.craftsblock.cnet.module.cli.annotation.CommandMeta;
import de.craftsblock.cnet.module.cli.command.Command;
import de.craftsblock.cnet.module.cli.command.CommandExecutor;
import de.craftsblock.craftsnet.CraftsNet;
import de.craftsblock.craftsnet.autoregister.meta.AutoRegister;
import de.craftsblock.craftsnet.logging.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * Command executor for the version command.
 * This class implements the {@link CommandExecutor} interface and provides functionality to handle the "version" command.
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @see CommandExecutor
 * @since 1.0.0
 */
@AutoRegister
@CommandMeta(name = "version", aliases = {"ver", "v"})
public class VersionCommand implements CommandExecutor {

    /**
     * Executes the version command.
     * This method logs the current version of CraftsNet to the provided logger.
     *
     * @param command The command that was executed.
     * @param alias   The alias name of the command used to access it.
     * @param args    The arguments passed with the command (not used in this implementation).
     * @param logger  The logger used to log messages.
     */
    @Override
    public void onCommand(@NotNull Command command, @NotNull String alias, @NotNull String[] args, @NotNull Logger logger) {
        logger.info("You are using CraftsNet v%s", CraftsNet.version);
    }

}
