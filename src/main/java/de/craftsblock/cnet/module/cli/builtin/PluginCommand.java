package de.craftsblock.cnet.module.cli.builtin;

import de.craftsblock.cnet.module.cli.annotation.CommandMeta;
import de.craftsblock.cnet.module.cli.command.Command;
import de.craftsblock.cnet.module.cli.command.CommandExecutor;
import de.craftsblock.craftsnet.CraftsNet;
import de.craftsblock.craftsnet.addon.Addon;
import de.craftsblock.craftsnet.addon.AddonManager;
import de.craftsblock.craftsnet.addon.meta.AddonMeta;
import de.craftsblock.craftsnet.autoregister.meta.AutoRegister;
import de.craftsblock.craftsnet.autoregister.meta.constructors.PreferConstructor;
import de.craftsblock.craftsnet.logging.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * Command executor for the plugin command.
 * This class implements the {@link CommandExecutor} interface and provides functionality to handle the "plugin" command.
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @since 1.0.0
 */
@AutoRegister
@CommandMeta(name = "plugin", aliases = {"pl", "plugins", "addon", "addons"})
public record PluginCommand(CraftsNet craftsNet) implements CommandExecutor {

    /**
     * Constructs a new instance of the plugin command
     *
     * @param craftsNet The CraftsNet instance which instantiates this plugin command.
     */
    @PreferConstructor
    public PluginCommand {
    }

    /**
     * Executes the plugin command.
     * This method retrieves the {@link AddonManager} from CraftsNet and logs information about installed addons to the provided logger.
     *
     * @param command The command that was executed.
     * @param alias   The alias name of the command used to access it.
     * @param args    The arguments passed with the command (not used in this implementation).
     * @param logger  The logger used to log messages.
     */
    @Override
    public void onCommand(@NotNull Command command, @NotNull String alias, @NotNull String[] args, @NotNull Logger logger) {
        AddonManager addonManager = craftsNet.getAddonManager();

        if (args.length != 1) {
            if (addonManager.getAddons().isEmpty()) {
                logger.info("Currently there are no addons installed!");
                return;
            }

            logger.info(
                    "Plugins (%s): %s",
                    addonManager.getAddons().size(),
                    String.join(", ", addonManager.getAddons().values().stream().map(Addon::getName).toList())
            );
            return;
        }

        String addonName = args[0];
        Addon addon = addonManager.getAddon(addonName);
        if (addon == null) {
            logger.info("No plugin named %s found!", addonName);
            return;
        }

        AddonMeta meta = addon.getMeta();
        logger.info("%s version %s", meta.name(), meta.version());

        if (!meta.description().isBlank())
            logger.info(meta.description());

        if (!meta.authors().isEmpty()) {
            boolean multipleAuthors = meta.authors().size() != 1;

            if (multipleAuthors)
                logger.info("Authors: %s", String.join(", ", meta.authors()).replaceFirst(", ([^,]+)$", " and $1"));
            else logger.info("Author: %s", meta.authors().get(0));
        }

        if (!meta.website().isBlank())
            logger.info("Website: " + meta.website());
    }

}
