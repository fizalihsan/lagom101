package com.fizal.domain.model;

import akka.Done;
import com.fizal.domain.model.ProfileCommand.CreateProfileCommand;
import com.fizal.domain.model.ProfileCommand.ReadProfileCommand;
import com.fizal.domain.model.ProfileCommand.UpdateProfileCommand;
import com.fizal.domain.model.ProfileEvent.ProfileCreatedEvent;
import com.fizal.domain.model.ProfileEvent.ProfileUpdatedEvent;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import joptsimple.internal.Strings;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * This is an event sourced entity. It has a state, {@link ProfileState}, which
 * stores what the greeting should be (eg, "Hello").
 * <p>
 * Event sourced entities are interacted with by sending them commands. This
 * entity supports two commands, a {@link CreateProfileCommand} command, which is
 * used to change the greeting, and a {@link UpdateProfileCommand} command, which is a read
 * only command which returns a greeting to the name specified by the command.
 * <p>
 * Commands get translated to events, and it's the events that get persisted by
 * the entity. Each event will have an event handler registered for it, and an
 * event handler simply applies an event to the current state. This will be done
 * when the event is first created, and it will also be done when the entity is
 * loaded from the database - each event will be replayed to recreate the state
 * of the entity.
 * <p>
 * This entity defines one event, the {@link HelloEvent.GreetingMessageChanged} event,
 * which is emitted when a {@link HelloCommand.UseGreetingMessage} command is received.
 */
@SuppressWarnings("unchecked")
public class ProfileEntity extends PersistentEntity<ProfileCommand, ProfileEvent, ProfileState> {

    /**
     * An entity can define different behaviours for different states, but it will
     * always start with an initial behaviour. This entity only has one behaviour.
     */
    @Override
    public Behavior initialBehavior(Optional<ProfileState> snapshotState) {

        /*
         * Behaviour is defined using a behaviour builder. The behaviour builder
         * starts with a state, if this entity supports snapshotting (an
         * optimisation that allows the state itself to be persisted to combine many
         * events into one), then the passed in snapshotState may have a value that
         * can be used.
         *
         * Otherwise, the default state is to use the Hello greeting.
         */
        BehaviorBuilder b = newBehaviorBuilder(snapshotState.orElse(null));

        // -----------------Command Handlers-----------------
        b.setCommandHandler(
                CreateProfileCommand.class,
                (cmd, ctx) -> {
                    if (Strings.isNullOrEmpty(cmd.id)) {
                        ctx.invalidCommand("Profile id is required");
                        return ctx.done();
                    }
                    return ctx.thenPersist(
                            new ProfileCreatedEvent(cmd.id),
                            evt -> ctx.reply(Done.getInstance())
                    );
                }
        );

        b.setCommandHandler(
                UpdateProfileCommand.class,
                (cmd, ctx) -> {
                    if (Strings.isNullOrEmpty(cmd.id)) {
                        ctx.invalidCommand("Profile id is required");
                        return ctx.done();
                    }
                    return ctx.thenPersist(
                            new ProfileUpdatedEvent(cmd.id),
                            evt -> ctx.reply(Done.getInstance())
                    );
                }
        );


        b.setReadOnlyCommandHandler(
                ReadProfileCommand.class,
                (cmd, ctx) -> ctx.reply(state() == null ? "No profile found!!! \n" : state().id + "!")
        );

        // -----------------Event Handlers-----------------
        b.setEventHandler(
                ProfileCreatedEvent.class,
                evt -> new ProfileState(evt.id, LocalDateTime.now().toString())
        );

        b.setEventHandler(
                ProfileUpdatedEvent.class,
                evt -> new ProfileState(evt.id, LocalDateTime.now().toString())
        );


        return b.build();
    }


}
