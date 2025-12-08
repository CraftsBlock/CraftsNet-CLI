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
 * The {@link ShutdownCommand} class implements the {@link CommandExecutor} interface
 * and defines the behavior of the "shutdown" command.
 * This command is used to gracefully shut down the CraftsNet program.
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.1
 * @since 1.0.0
 */
@AutoRegister
@CommandMeta(
        name = "shutdown",
        description = "Shuts down craftsnet",
        aliases = {"quit", "exit", "stop"}
)
public record ShutdownCommand(CraftsNet craftsNet) implements CommandExecutor {

    /**
     * Constructs a new instance of {@link ShutdownCommand}.
     *
     * @param craftsNet The CraftsNet instance which instantiates this shutdown command
     */
    @PreferConstructor
    public ShutdownCommand {
    }

    /**
     * Executes the "shutdown" command, initiating the shutdown process for CraftsNet.
     *
     * @param command The {@code Command} object representing the "shutdown" command.
     * @param alias   The alias name of the command used to access it.
     * @param args    The arguments passed with the command (not used in this command).
     * @param logger  The logger for outputting messages or logging.
     */
    @Override
    public void onCommand(@NotNull Command command, @NotNull String alias, @NotNull String[] args, @NotNull Logger logger) {
        logger.info("Shutdown request was sent!");
        craftsNet.stop();

        logger.info("Programm has been exited successful.");
        System.exit(0);
    }

}
