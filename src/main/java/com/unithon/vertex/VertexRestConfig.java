package com.unithon.vertex;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

// VertexRestConfig.java
@Configuration
@RequiredArgsConstructor
public class VertexRestConfig {

    private final VertexProperties props;

    @Bean
    public ExchangeStrategies largeBodyStrategies() {
        return ExchangeStrategies.builder()
                .codecs(c -> c.defaultCodecs().maxInMemorySize(100 * 1024 * 1024)) // 100MB
                .build();
    }

    @Bean
    public GoogleCredentials googleCredentials() throws IOException {
        return GoogleCredentials
                .fromStream(new FileInputStream(props.serviceAccountKey()))
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
    }

    @Bean
    public WebClient vertexWebClient(GoogleCredentials credentials,
                                     ExchangeStrategies largeBodyStrategies) {
        String baseUrl = String.format(
                "https://%s-aiplatform.googleapis.com/v1/projects/%s/locations/%s/publishers/google/models",
                props.location(), props.projectId(), props.location()
        );

        ExchangeFilterFunction auth = (req, next) -> {
            try { credentials.refreshIfExpired(); } catch (IOException ignore) {}
            return next.exchange(
                    ClientRequest.from(req)
                            .headers(h -> h.setBearerAuth(credentials.getAccessToken().getTokenValue()))
                            .build()
            );
        };

        return WebClient.builder()
                .baseUrl(baseUrl)
                .exchangeStrategies(largeBodyStrategies) // ★ 반드시 설정
                .filter(auth)
                .build();
    }
}
