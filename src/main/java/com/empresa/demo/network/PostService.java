package com.empresa.demo.network;

import com.empresa.demo.dto.PostDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);
    private static final String EXTERNAL_API_URL = "https://jsonplaceholder.typicode.com/posts";
    private static final int TIMEOUT_SECONDS = 10;
    WebClient webClient = WebClient.builder().baseUrl("https://jsonplaceholder.typicode.com").build();


    public List<PostDto> getPosts() {
        try {
            logger.info("Iniciando llamada a API externa: {}", EXTERNAL_API_URL);

            List<PostDto> posts = webClient.get().uri("/posts").retrieve()
                    .onStatus(status -> status.is4xxClientError(), resp -> Mono.error(new RuntimeException("Error cliente")))
                    .onStatus(status -> status.is5xxServerError(), resp -> Mono.error(new RuntimeException("Error servidor")))
                    .bodyToFlux(PostDto.class)
                    .timeout(Duration.ofSeconds(2))
                    .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                            .maxBackoff(Duration.ofSeconds(10))
                            .filter(throwable -> {

                                if (throwable instanceof RuntimeException) {
                                    String message = throwable.getMessage();
                                    return message != null && message.contains("Error servidor");
                                }
                                return throwable instanceof java.util.concurrent.TimeoutException;
                            })
                            .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) ->
                                    new RuntimeException("Máximo número de reintentos alcanzado",
                                            retrySignal.failure())))
                    .collectList().block();

        } catch (HttpClientErrorException e) {
            logger.error("Error 4xx en API externa - Petición inválida: {} - {}", e.getStatusCode(), e.getMessage());
            return Collections.emptyList();
        } catch (HttpServerErrorException e) {
            logger.warn("Error 5xx en API externa, intentando retry: {} - {}", e.getStatusCode(), e.getMessage());
            return Collections.emptyList();

        } catch (ResourceAccessException e) {
            logger.error("Timeout o error de conexión en API externa después de {} segundos: {}", TIMEOUT_SECONDS, e.getMessage());
            return Collections.emptyList();
        } catch (Exception e) {
            logger.error("Error inesperado al consumir API externa: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
        return Collections.emptyList();
    }


} 