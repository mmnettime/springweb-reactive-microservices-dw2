package umg.dw.proyecto.repository;

import umg.dw.proyecto.model.Persona;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface PersonaRepository extends ReactiveMongoRepository<Persona, String> {
}
