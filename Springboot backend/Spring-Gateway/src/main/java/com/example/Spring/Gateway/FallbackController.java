package com.example.Spring.Gateway;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

    @RequestMapping("/serviceFallBack")
    public Mono<String> ServicioFallBackService(){
        return Mono.just("El servicio no esta disponible");
    }
}
