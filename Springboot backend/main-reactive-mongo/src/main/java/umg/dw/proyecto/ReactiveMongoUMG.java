package umg.dw.proyecto;

import umg.dw.proyecto.model.Persona;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.util.UUID;
import java.util.stream.Stream;
import umg.dw.proyecto.repository.PersonaRepository;

@SpringBootApplication
@EnableDiscoveryClient
public class ReactiveMongoUMG {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveMongoUMG.class, args);
	}
	
	@Bean
	@LoadBalanced
	public WebClient.Builder loadBalancedWebClientBuilder() {
		return WebClient.builder();
	}
}
