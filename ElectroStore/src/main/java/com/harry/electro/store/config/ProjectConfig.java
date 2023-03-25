package com.harry.electro.store.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
@author :-
        Harshal Bafna
 */
@Configuration
public class ProjectConfig {

    @Bean
    public ModelMapper mapper(){
        return new ModelMapper();
    }
}
