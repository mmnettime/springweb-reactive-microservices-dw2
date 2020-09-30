package umg.dw.proyecto.resource;

import umg.dw.proyecto.model.Persona;
import umg.dw.proyecto.model.PersonaEvent;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;


import java.time.Duration;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.EmitterProcessor;
import umg.dw.proyecto.repository.PersonaRepository;


@CrossOrigin(origins={"http://localhost:8082", "http://localhost:4200","http://localhost:8100"})
@RestController
@RequestMapping("/rest/persona")
public class PersonaResource {

    private final PersonaRepository personaRepository;

    public PersonaResource(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }
    
    private EmitterProcessor<Persona> notificationProcessor;


    @PostConstruct
    private void createProcessor() {
        notificationProcessor = EmitterProcessor.<Persona>create();
    }
    
    @GetMapping("/all")
    public Flux<Persona> getAll() {
        return personaRepository
                .findAll();
    }

    @GetMapping("/{id}")
    public Mono<Persona> getId(@PathVariable("id") final String empId) {
        return personaRepository.findById(empId);
    }
    
    @PostMapping("/employee")
    public Mono<Persona> addPersona(@RequestBody Persona persona){
        return personaRepository.save(persona);
    }
    
    @PutMapping("/actualiza/{id}")
    public Mono<ResponseEntity<Persona>> updatePersona(@PathVariable(value = "id") String id,
                                                   @Valid @RequestBody Persona persona) {
        notificationProcessor.onNext(persona);
        return personaRepository.findById(id)
                .flatMap(personaExiste -> {
                    personaExiste.setNombre(persona.getNombre());
                    personaExiste.setEdad(persona.getEdad());
                    return personaRepository.save(persona);
                })
                .map(updatedPersona -> new ResponseEntity<>(updatedPersona, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    
    @DeleteMapping("delete/{id}")
    public Mono<ResponseEntity<Void>> deletePersona(@PathVariable(value = "id" ) String id, Persona persona) {
        notificationProcessor.onNext(persona);

        return personaRepository.findById(id)
                .flatMap(existingPersona ->
                        
                        personaRepository.delete(existingPersona)
                            .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))) 
                        
                )
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        
        
        
    }
    
   

    
    @RequestMapping(
            path = "/update",
            method = RequestMethod.PUT)
    public ResponseEntity<?> update(@RequestBody Persona employee) {
        personaRepository.save(employee);
                        notificationProcessor.onNext(employee);

        return new ResponseEntity<>(employee, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/actualizar/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public Mono<Persona> update(@PathVariable(value = "id" ) String id, @Valid @RequestBody Persona persona) {
                notificationProcessor.onNext(persona);
        return personaRepository.save(persona);
    }
        
  
    @RequestMapping(
            path = "/create",
            method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody Persona persona) {
        personaRepository.save(persona);
                        notificationProcessor.onNext(persona);
        return new ResponseEntity<>(persona, HttpStatus.OK);
        
    }
    
     @RequestMapping(
            path = "/borrar",
            method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@RequestBody Persona employee) {
        personaRepository.delete(employee);
        notificationProcessor.onNext(employee);
        return new ResponseEntity<>(employee, HttpStatus.OK);
        
    }
    
    
    @GetMapping(value = "/{id}/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<PersonaEvent> getEvents(@PathVariable("id")
                                    final String empId) {
        return personaRepository.findById(empId)
                .flatMapMany(persona -> {

                    Flux<Long> interval = Flux.interval(Duration.ofSeconds(2));

                    Flux<PersonaEvent> personaEventFlux =
                            Flux.fromStream(Stream.generate(() -> new PersonaEvent(persona,
                                            new Date()))
                            );


                    return Flux.zip(interval, personaEventFlux)
                            .map(Tuple2::getT2);

                });

    }
    

    private Flux<ServerSentEvent<Persona>> getPersonaSSE() {

        // notification processor retorna un FLUX en el cual podemos estar "suscritos" cuando este tenga otro valor ...
        return notificationProcessor
                .log().map((persona) -> {
                            System.out.println("Sending Persona:" + persona.getId());
                            return ServerSentEvent.<Persona>builder()
                                    .id(UUID.randomUUID().toString())
                                    .event("persona-result")
                                    .data(persona)
                                    .build();
                        }).concatWith(Flux.never());
    }

    private Flux<ServerSentEvent<Persona>> getNotificationHeartbeat() {
        return Flux.interval(Duration.ofSeconds(10))
                .map(i -> {
                    System.out.println(String.format("sending heartbeat [%s] ...", i.toString()));
                    return ServerSentEvent.<Persona>builder()
                            .id(String.valueOf(i))
                            .event("heartbeat-result")
                            .data(null)
                            .build();
                });
    }

    @GetMapping(
            value = "/notification/sse"
    )
    public Flux<ServerSentEvent<Persona>> getJobResultNotification() {

        return Flux.merge(getNotificationHeartbeat(), getPersonaSSE());
    }

}
