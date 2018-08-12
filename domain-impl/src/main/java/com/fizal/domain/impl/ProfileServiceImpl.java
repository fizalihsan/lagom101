package com.fizal.domain.impl;

import akka.Done;
import akka.NotUsed;
import akka.japi.Pair;
import com.fizal.domain.api.Profile;
import com.fizal.domain.api.ProfileService;
import com.fizal.domain.model.ProfileCommand;
import com.fizal.domain.model.ProfileCommand.CreateProfileCommand;
import com.fizal.domain.model.ProfileCommand.ReadProfileCommand;
import com.fizal.domain.model.ProfileCommand.UpdateProfileCommand;
import com.fizal.domain.model.ProfileEntity;
import com.fizal.domain.model.ProfileEvent;
import com.fizal.domain.model.ProfileEvent.ProfileUpdatedEvent;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.broker.Topic;
import com.lightbend.lagom.javadsl.broker.TopicProducer;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;

import javax.inject.Inject;

public class ProfileServiceImpl implements ProfileService {

    private final PersistentEntityRegistry persistentEntityRegistry;

    @Inject
    public ProfileServiceImpl(PersistentEntityRegistry persistentEntityRegistry) {
        this.persistentEntityRegistry = persistentEntityRegistry;
        persistentEntityRegistry.register(ProfileEntity.class);
    }

    @Override
    public ServiceCall<NotUsed, String> getProfile(String id) {
        return request -> {
            PersistentEntityRef<ProfileCommand> ref = persistentEntityRegistry.refFor(ProfileEntity.class, id);
            return ref.ask(new ReadProfileCommand(id));
        };
    }

    @Override
    public ServiceCall<Profile, Done> createProfile() {
        return request -> {
            PersistentEntityRef<ProfileCommand> ref = persistentEntityRegistry.refFor(ProfileEntity.class, request.id);
            return ref.ask(new CreateProfileCommand(request.id));
        };

    }

    @Override
    public ServiceCall<Profile, Done> updateProfile() {
        return request -> {
            PersistentEntityRef<ProfileCommand> ref = persistentEntityRegistry.refFor(ProfileEntity.class, request.id);
            return ref.ask(new UpdateProfileCommand(request.id));
        };

    }

    @Override
    public Topic<com.fizal.domain.api.ProfileEvent> profileEvents() {
        return TopicProducer.taggedStreamWithOffset(ProfileEvent.TAG.allTags(), (tag, offset) ->

                // Load the event stream for the passed in shard tag
                persistentEntityRegistry.eventStream(tag, offset).map(eventAndOffset -> {

                    // Now we want to convert from the persisted event to the published event.
                    // Although these two events are currently identical, in future they may
                    // change and need to evolve separately, by separating them now we save
                    // a lot of potential trouble in future.
                    com.fizal.domain.api.ProfileEvent eventToPublish;

                    if (eventAndOffset.first() instanceof ProfileUpdatedEvent) {
                        ProfileUpdatedEvent messageChanged = (ProfileUpdatedEvent) eventAndOffset.first();
                        eventToPublish = new com.fizal.domain.api.ProfileEvent.ProfileUpdated(
                                messageChanged.id
                        );
                    } else {
                        throw new IllegalArgumentException("Unknown event: " + eventAndOffset.first());
                    }

                    // We return a pair of the translated event, and its offset, so that
                    // Lagom can track which offsets have been published.
                    return Pair.create(eventToPublish, eventAndOffset.second());
                })
        );
    }
}
