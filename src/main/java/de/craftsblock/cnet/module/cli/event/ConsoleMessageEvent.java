package de.craftsblock.cnet.module.cli.event;

import de.craftsblock.craftscore.event.CancellableEvent;

/**
 * The {@link ConsoleMessageEvent} class represents an event related to a console message.
 * It extends the base {@link CancellableEvent} class, allowing it to be used within an
 * event-driven system.
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @see CancellableEvent
 * @since 1.0.0
 */
public class ConsoleMessageEvent extends CancellableEvent {

    private final String message;

    /**
     * Constructs a new {@link ConsoleMessageEvent} with the specified console message.
     *
     * @param message The console message for this event.
     */
    public ConsoleMessageEvent(String message) {
        this.message = message;
    }

    /**
     * Gets the console message associated with the event.
     *
     * @return The console message.
     */
    public String getMessage() {
        return message;
    }

}
