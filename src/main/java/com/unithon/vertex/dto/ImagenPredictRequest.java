package com.unithon.vertex.dto;

import java.util.List;
import java.util.Map;

public record ImagenPredictRequest(
        List<Map<String, Object>> instances,
        Map<String, Object> parameters
) {}



