package com.ujjwalbhardwaj.intuit.profile.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ujjwalbhardwaj.intuit.commons.enums.EventType;
import com.ujjwalbhardwaj.intuit.profile.event.ProfileEventFactory;
import com.ujjwalbhardwaj.intuit.profile.strategy.ProfileCreatedStrategy;
import com.ujjwalbhardwaj.intuit.profile.strategy.ProfileEventStrategy;
import com.ujjwalbhardwaj.intuit.profile.strategy.ProfileUpdatedStrategy;
import com.ujjwalbhardwaj.intuit.profile.strategy.ProfileValidatedStrategy;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class SpringConfig {

    @Bean
    public FactoryBean<?> profileEventProcessorFactory() {
        final ServiceLocatorFactoryBean serviceLocatorFactoryBean = new ServiceLocatorFactoryBean();
        serviceLocatorFactoryBean.setServiceLocatorInterface(ProfileEventFactory.class);
        return serviceLocatorFactoryBean;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public Map<EventType, ProfileEventStrategy> eventTypeProfileEventStrategyMap(
            final ProfileCreatedStrategy profileCreatedStrategy, final ProfileUpdatedStrategy profileUpdatedStrategy,
            final ProfileValidatedStrategy profileValidatedStrategy) {
        return Map.of(
                EventType.CREATE_PROFILE, profileCreatedStrategy,
                EventType.UPDATE_PROFILE, profileUpdatedStrategy,
                EventType.PROFILE_VALIDATED, profileValidatedStrategy);
    }

}

