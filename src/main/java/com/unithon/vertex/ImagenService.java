package com.unithon.vertex;

import com.unithon.vertex.dto.ImagenPredictRequest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
public class ImagenService {

    private final WebClient vertexWebClient;
    private final VertexProperties props;

    public ImagenService(WebClient vertexWebClient, VertexProperties props) {
        this.vertexWebClient = vertexWebClient;
        this.props = props;
    }

    /**
     * 프롬프트로 이미지 한 장 생성하여 PNG 바이트 반환
     */
    public byte[] generatePng(String prompt, Integer width, Integer height) {
        String model = props.model(); // "imagen-3.0-generate-002"

        // instances
        Map<String, Object> instance = new HashMap<>();
        instance.put("prompt", prompt);

        // parameters (필요 옵션만)
        Map<String, Object> params = new HashMap<>();
        params.put("sampleCount", 1);
        if (width != null)  params.put("width",  width);
        if (height != null) params.put("height", height);
        params.put("outputMimeType", "image/png"); // 또는 image/jpeg

        ImagenPredictRequest body = new ImagenPredictRequest(
                List.of(instance),
                params
        );

        // POST .../{model}:predict
        Map<String, Object> res = vertexWebClient.post()
                .uri("/{model}:predict", model)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String,Object>>() {})
                .block();

        if (res == null || !res.containsKey("predictions"))
            throw new IllegalStateException("No response from Vertex AI.");

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> predictions = (List<Map<String, Object>>) res.get("predictions");
        if (predictions == null || predictions.isEmpty())
            throw new IllegalStateException("No predictions returned.");

        String b64 = (String) predictions.get(0).get("bytesBase64Encoded");
        if (b64 == null)
            throw new IllegalStateException("bytesBase64Encoded not found in response.");

        return Base64.getDecoder().decode(b64);
    }
}
