package com.rox.docguru;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Controller
public class LlamaController {

    private final WebClient webClient;

    public LlamaController() {
        this.webClient = WebClient.builder()
            .baseUrl("http://localhost:11434")
            .defaultHeader("Content-Type", "application/json")
            .build();
    }

   

    @PostMapping(value = "/streamResponse", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    public Flux<String> streamResponse(@RequestBody Payload payload) {
        System.out.println("Received payload: " + payload.getPrompt());
        return callOllamaApi(payload.getPrompt());
    }

    private Flux<String> callOllamaApi(String promptText) {
        String apiUrl = "/api/generate";
        String requestBody = String.format("{\"model\": \"llama3\", \"prompt\": \"%s\", \"stream\": true}", promptText);

        return this.webClient.post()
            .uri(apiUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .retrieve()
            .bodyToFlux(String.class);
    }
}
