package com.example.incident.config;

import com.example.incident.kafka.IncidentEvent;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
public class KafkaConfig {
    @Bean
    NewTopic rawIncidentsTopic() {
        return TopicBuilder.name("raw-incidents").partitions(6).replicas(1).build();
    }

    @Bean
    ConsumerFactory<String, IncidentEvent> incidentConsumerFactory(org.springframework.boot.autoconfigure.kafka.KafkaProperties properties) {
        Map<String, Object> props = new HashMap<>(properties.buildConsumerProperties(null));
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        JsonDeserializer<IncidentEvent> deserializer = new JsonDeserializer<>(IncidentEvent.class);
        deserializer.addTrustedPackages("com.example.incident.kafka");
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }
}
