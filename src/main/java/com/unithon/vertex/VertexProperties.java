package com.unithon.vertex;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "vertex")
public record VertexProperties(
        String projectId,
        String location,
        String serviceAccountKey,
        String model
) {}