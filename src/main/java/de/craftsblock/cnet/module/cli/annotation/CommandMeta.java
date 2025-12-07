package de.craftsblock.cnet.module.cli.annotation;

import java.lang.annotation.*;

/**
 * Annotation used to define metadata for a CLI command.
 * <p>
 * Classes annotated with {@link CommandMeta} represent executable commands
 * within the CraftsNet CLI module. The annotation provides the command name
 * and optional aliases, which are used during command registration and lookup.
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @since 1.0.0
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandMeta {

    /**
     * Specifies the primary name of the command.
     *
     * @return The main command name.
     */
    String name();

    /**
     * Specifies alternative names under which the command can be invoked.
     *
     * @return An array of command aliases. Defaults to an empty array.
     */
    String[] aliases() default {};

}
