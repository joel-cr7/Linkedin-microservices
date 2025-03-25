package com.linkedin.postservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic postCreatedTopic(){
        return new NewTopic("post_created_topic", 3, (short) 1);
    }

    @Bean
    public NewTopic postLikedTopic(){
        return new NewTopic("post_liked_topic", 3, (short) 1);
    }

}
