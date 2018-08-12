package com.fizal.domain.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.base.Preconditions;
import lombok.Value;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ProfileEvent.ProfileCreated.class, name = "new-profile-created")
})
public interface ProfileEvent {

    String getId();

    @Value
    final class ProfileCreated implements ProfileEvent {
        public final String id;
        public String name = "";
        public String location = "";

        @JsonCreator
        public ProfileCreated(String id) {
            this.id = Preconditions.checkNotNull(id, "id");
        }

        @Override
        public String getId() {
            return id;
        }
    }

    @Value
    final class ProfileUpdated implements ProfileEvent {
        public final String id;
        public String name = "";
        public String location = "";

        @JsonCreator
        public ProfileUpdated(String id) {
            this.id = Preconditions.checkNotNull(id, "id");
        }

        @Override
        public String getId() {
            return id;
        }
    }
}
