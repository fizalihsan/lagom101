package com.fizal.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.serialization.CompressedJsonable;
import lombok.Value;

/**
 * The state for the {@link ProfileEntity} entity.
 */
@SuppressWarnings("serial")
@Value
@JsonDeserialize
public final class ProfileState implements CompressedJsonable {

    public final String id;
    public final String timestamp;

    @JsonCreator
    public ProfileState(String id, String timestamp) {
        this.id = Preconditions.checkNotNull(id, "id");
        this.timestamp = Preconditions.checkNotNull(timestamp, "timestamp");
    }
}
