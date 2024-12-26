package com.example.demo;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;

import com.example.demo.Prompt;

@Controller
public class LlamaController {

    private final WebClient webClient;

    public LlamaController() {
        this.webClient = WebClient.builder()
            .baseUrl("http://localhost:11434")
            .defaultHeader("Content-Type", "application/json")
            .build();
    }

    @GetMapping("/")
    public String showForm(Model model) {
        model.addAttribute("prompt", new Prompt());
        return "index";
    }

    @PostMapping("/sendPrompt")
    public String sendPrompt(@ModelAttribute Prompt prompt, Model model) {
        model.addAttribute("prompt", prompt);
        return "streaming";
    }

    @GetMapping(value = "/streamResponse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    public Flux<String> streamResponse(@ModelAttribute Prompt prompt) {
        return callOllamaApi(prompt.getText());
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
