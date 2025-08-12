package com.unithon;

import com.unithon.vertex.VertexProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties(VertexProperties.class)
public class UnithonApplication {

    public static void main(String[] args) {
        SpringApplication.run(UnithonApplication.class, args);
    }

}
