package umg.dw.proyecto.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Persona {

    @Id
    private String id = new ObjectId().toString();
    private String nombre;
    private Integer edad;
    

    public Persona() {
        
    }

    public Persona(String nombre, Integer edad) {
        this.nombre = nombre;
        this.edad = edad;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

   

   
    @Override
    public String toString() {
        return "Employee{" +
                "id:'" + id + '\'' +
                ", nombre:'" + nombre + '\'' +
                ", edad:" + edad + 
                
                '}';
    }
}
