package com.fizal.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventShards;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTagger;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.Value;

/**
 * This interface defines all the events that the Profile entity supports.
 * <p>
 * By convention, the events should be inner classes of the interface, which
 * makes it simple to get a complete picture of what events an entity has.
 */
public interface ProfileEvent extends Jsonable, AggregateEvent<ProfileEvent> {

    /**
     * Tags are used for getting and publishing streams of events. Each event
     * will have this tag, and in this case, we are partitioning the tags into
     * 4 shards, which means we can have 4 concurrent processors/publishers of
     * events.
     */
    AggregateEventShards<ProfileEvent> TAG = AggregateEventTag.sharded(ProfileEvent.class, 4);

    @SuppressWarnings("serial")
    @Value
    @JsonDeserialize
    final class ProfileCreatedEvent implements ProfileEvent {

        public final String id;

        @JsonCreator
        public ProfileCreatedEvent(String id) {
            this.id = Preconditions.checkNotNull(id, "id");
        }
    }

    @SuppressWarnings("serial")
    @Value
    @JsonDeserialize
    final class ProfileUpdatedEvent implements ProfileEvent {

        public final String id;

        @JsonCreator
        public ProfileUpdatedEvent(String id) {
            this.id = Preconditions.checkNotNull(id, "id");
        }
    }


    @Override
    default AggregateEventTagger<ProfileEvent> aggregateTag() {
        return TAG;
    }

}
