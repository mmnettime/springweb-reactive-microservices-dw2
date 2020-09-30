package umg.dw.proyecto.model;


import java.util.Date;

public class PersonaEvent {

    private Persona persona;
    private Date date;

    public PersonaEvent(Persona persona, Date date) {
        this.persona = persona;
        this.date = date;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
