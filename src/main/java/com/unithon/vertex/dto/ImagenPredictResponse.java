package com.unithon.vertex.dto;

import java.util.List;
import java.util.Map;

public record ImagenPredictResponse(
        List<Map<String, Object>> predictions
) {}
