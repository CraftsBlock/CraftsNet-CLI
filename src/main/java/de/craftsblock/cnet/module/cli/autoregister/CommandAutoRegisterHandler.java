package de.craftsblock.cnet.module.cli.autoregister;

import de.craftsblock.cnet.module.cli.CraftsNetCLI;
import de.craftsblock.cnet.module.cli.annotation.CommandMeta;
import de.craftsblock.cnet.module.cli.command.CommandExecutor;
import de.craftsblock.cnet.module.cli.command.CommandRegistry;
import de.craftsblock.craftsnet.autoregister.AutoRegisterHandler;
import de.craftsblock.craftsnet.autoregister.meta.AutoRegisterInfo;

/**
 * A handler for automatically registering {@link CommandExecutor} implementations. This class extends
 * {@link AutoRegisterHandler} and provides a concrete implementation for handling the registration of
 * {@link CommandExecutor} instances into the requirement registry of {@link CraftsNetCLI}.
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.1
 * @since 1.0.0
 */
public class CommandAutoRegisterHandler extends AutoRegisterHandler<CommandExecutor> {

    private final CraftsNetCLI craftsNetCLI;
    private final CommandRegistry registry;

    /**
     * Constructs an {@link CommandAutoRegisterHandler} with the specified {@link CraftsNetCLI} instance.
     *
     * @param craftsNetCLI The main {@link CraftsNetCLI} instance, which provides access to the application's context.
     */
    public CommandAutoRegisterHandler(CraftsNetCLI craftsNetCLI) {
        super(craftsNetCLI.getCraftsNet());
        this.craftsNetCLI = craftsNetCLI;
        registry = craftsNetCLI.getCommandRegistry();
    }

    /**
     * Handles the registration of the provided {@link CommandExecutor}.
     *
     * <p>This method attempts to register the given {@link CommandExecutor} with the {@link CraftsNetCLI#getCommandRegistry()}
     * of the associated {@link CraftsNetCLI} instance. If registration is successful, the method
     * returns {@code true}.</p>
     *
     * @param commandExecutor The {@link CommandExecutor} to be registered.
     * @param args            Additional arguments (not used in this implementation but provided for extensibility).
     * @return {@code true} if the registration was successful, {@code false} otherwise.
     */
    @Override
    protected boolean handle(CommandExecutor commandExecutor, AutoRegisterInfo info, Object... args) {
        CommandMeta meta = commandExecutor.getClass().getDeclaredAnnotation(CommandMeta.class);
        if (meta == null) {
            craftsNetCLI.getLogger().error("Command %s is not annotated with @%s",
                    commandExecutor.getClass().getName(), CommandMeta.class.getSimpleName());
            return true;
        }

        String name = meta.name();
        if (registry.hasCommand(name))
            return false;

        registry.getCommand(name).setDescription(meta.description());
        registry.getCommand(name).setUsage(meta.usage());
        registry.getCommand(name).setExecutor(commandExecutor);
        registry.getCommand(name).addAlias(meta.aliases());
        return true;
    }

}
