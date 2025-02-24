package com.associate.notify.engineer.whatsapp.client;

import com.associate.notify.engineer.whatsapp.WhatsAppSender;

import reactor.core.publisher.Mono;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Albert Gomes Cabral
 */
@Service
public class WhatsAppMetaSender implements WhatsAppSender {

    @Override
    public void sendWhatsAppMessage(String messageText, String to) {
        String url = "https://graph.facebook.com/v22.0/" + to + "/messages";

        WebClient webClient = WebClient.builder()
                .baseUrl(url)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + _authToken)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        Mono<String> response = webClient.post()
                .bodyValue(messageText)
                .retrieve()
                .bodyToMono(String.class);

        response.subscribe(System.out::println);
    }

    private static final String _authToken = "";

}
