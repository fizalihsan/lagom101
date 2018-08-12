package com.fizal.domain.api;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.broker.Topic;
import com.lightbend.lagom.javadsl.api.broker.kafka.KafkaProperties;

import static com.lightbend.lagom.javadsl.api.Service.*;

/**
 * The Profile service interface.
 * <p>
 * This describes everything that Lagom needs to know about how to serve and
 * consume the Profile.
 */
public interface ProfileService extends Service {

    /**
     * Example: curl http://localhost:9000/api/getProfile/123
     */
    ServiceCall<NotUsed, String> getProfile(String id);


    /**
     * Example: curl -H "Content-Type: application/json" -X POST -d '{"id":
     * "123-234-345"}' http://localhost:9000/api/createProfile
     */
    ServiceCall<Profile, Done> createProfile();

    /**
     * Example: curl -H "Content-Type: application/json" -X POST -d '{"id":
     * "123-234-345", "location": "NYC"}' http://localhost:9000/api/updateProfile
     */
    ServiceCall<Profile, Done> updateProfile();

    /**
     * This gets published to Kafka.
     */
    Topic<ProfileEvent> profileEvents();

    @Override
    default Descriptor descriptor() {
        // @formatter:off
        return named("profile").withCalls(
                pathCall("/api/getProfile/:id", this::getProfile),
                pathCall("/api/createProfile", this::createProfile),
                pathCall("/api/updateProfile", this::updateProfile)
        ).withTopics(
                topic("profile-events", this::profileEvents)
                        // Kafka partitions messages, messages within the same partition will
                        // be delivered in order, to ensure that all messages for the same user
                        // go to the same partition (and hence are delivered in order with respect
                        // to that user), we configure a partition key strategy that extracts the
                        // name as the partition key.
                        .withProperty(KafkaProperties.partitionKeyStrategy(), ProfileEvent::getId)
        ).withAutoAcl(true);
        // @formatter:on
    }
}
