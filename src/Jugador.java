import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Jugador {
    private List<Personaje> personajes;

    public Jugador(List<Personaje> personajes) {
        this.personajes = personajes;
    }

    public List<Personaje> getPersonajes() {
        return new ArrayList<>(personajes); // Copia defensiva
    }

    public void eliminarPersonaje(Personaje personaje) {
        personajes.remove(personaje);
    }

    public boolean tienePersonajesVivos() {
        return !personajes.isEmpty();
    }

    public Personaje seleccionarPersonajeAleatorio() {
        if (personajes.isEmpty()) return null;
        Random rand = new Random();
        return personajes.get(rand.nextInt(personajes.size()));
    }

    public void setPersonajes(List<Personaje> personajes) {
        this.personajes = personajes;
    }

}