package com.fizal.domain.model;

import akka.Done;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.Value;

/**
 * This interface defines all the commands that the Profile entity supports.
 * <p>
 * By convention, the commands should be inner classes of the interface, which
 * makes it simple to get a complete picture of what commands an entity
 * supports.
 */
public interface ProfileCommand extends Jsonable {

    @SuppressWarnings("serial")
    @Value
    @JsonDeserialize
    final class CreateProfileCommand implements ProfileCommand, CompressedJsonable, PersistentEntity.ReplyType<Done> {
        public final String id;

        @JsonCreator
        public CreateProfileCommand(String id) {
            this.id = Preconditions.checkNotNull(id, "id");
        }
    }

    @SuppressWarnings("serial")
    @Value
    @JsonDeserialize
    final class UpdateProfileCommand implements ProfileCommand, PersistentEntity.ReplyType<Done> {

        public final String id;

        @JsonCreator
        public UpdateProfileCommand(String id) {
            this.id = Preconditions.checkNotNull(id, "id");
        }
    }

    @SuppressWarnings("serial")
    @Value
    @JsonDeserialize
    final class ReadProfileCommand implements ProfileCommand, PersistentEntity.ReplyType<String> {

        public final String id;

        @JsonCreator
        public ReadProfileCommand(String id) {
            this.id = Preconditions.checkNotNull(id, "id");
        }
    }
}
