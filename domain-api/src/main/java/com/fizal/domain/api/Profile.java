package com.fizal.domain.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import lombok.Value;

@Value
@JsonDeserialize
public final class Profile {

    public final String id;
    public String name = "";
    public String location = "";

    @JsonCreator
    public Profile(String id) {
        this.id = Preconditions.checkNotNull(id, "id");
    }

}
