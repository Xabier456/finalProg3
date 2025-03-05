import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Combate {
    private Jugador jugador1;
    private Jugador jugador2;
    private boolean atacaJugador1;
    private List<String> log = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);

    public Combate(Jugador j1, Jugador j2) {
        this.jugador1 = j1;
        this.jugador2 = j2;
        this.atacaJugador1 = new Random().nextBoolean();
        System.out.println("Jugador " + (atacaJugador1 ? "1" : "2") + " salió sorteado para atacar primero!.");
    }

    public void iniciarCombate() {
        log.add("Inicio del combate!");
        int ronda = 1;
        while (jugador1.tienePersonajesVivos() && jugador2.tienePersonajesVivos()) {
            System.out.println("\n--- Ronda " + ronda + " ---");
            log.add("\n--- Ronda " + ronda + " ---");
            Personaje p1 = jugador1.seleccionarPersonajeAleatorio();
            Personaje p2 = jugador2.seleccionarPersonajeAleatorio();
            log.add(p1.getApodo() + " (J1) vs " + p2.getApodo() + " (J2)");


            boolean p1Inicia = atacaJugador1;
            ejecutarRonda(p1, p2, p1Inicia);

            ronda++;
        }
        determinarGanador();
        Logger.guardarLog(log);
    }

    private void ejecutarRonda(Personaje p1, Personaje p2, boolean p1Inicia) {
        int ataques = 0;
        while (ataques < 14 && p1.estaVivo() && p2.estaVivo()) {
            if (p1Inicia) {
                atacar(p1, p2);
            } else {
                atacar(p2, p1);
            }
            ataques++;
            p1Inicia = !p1Inicia;
        }

        determinarGanadorRonda(p1, p2);
    }

    private void atacar(Personaje atacante, Personaje defensor) {
        double danio = LogicaCombate.calcularDanio(atacante, defensor);
        defensor.recibirDano(danio);

        // Determinar jugadores
        boolean atacanteEsJ1 = jugador1.getPersonajes().contains(atacante);
        boolean defensorEsJ1 = jugador1.getPersonajes().contains(defensor);

        String mensaje = String.format(
                "%s (J%d) ataca a %s (J%d) (-%.1f salud). Salud restante: %.1f",
                atacante.getApodo(),
                atacanteEsJ1 ? 1 : 2,
                defensor.getApodo(),
                defensorEsJ1 ? 1 : 2,
                danio,
                defensor.getSalud()
        );

        log.add(mensaje);
        System.out.println(mensaje);
        esperarEnter();
    }

    private void determinarGanadorRonda(Personaje p1, Personaje p2) {
        String mensaje;
        if (!p1.estaVivo()) {
            jugador1.eliminarPersonaje(p1);
            p2.aplicarMejora();
            atacaJugador1 = false;
            mensaje = String.format(
                    "%s (Jugador 1) ha muerto. ¡Jugador 2 gana la ronda!",
                    p1.getApodo()
            );
        } else if (!p2.estaVivo()) {
            jugador2.eliminarPersonaje(p2);
            p1.aplicarMejora();
            atacaJugador1 = true;
            mensaje = String.format(
                    "%s (Jugador 2) ha muerto. ¡Jugador 1 gana la ronda!",
                    p2.getApodo()
            );
        } else {
            mensaje = "Empate! Ambos personajes siguen vivos.";
        }
        log.add(mensaje);
        System.out.println(mensaje);
        esperarEnter();
    }

    private void determinarGanador() {
        String mensaje = "\n¡Jugador " +
                (jugador1.tienePersonajesVivos() ? "1" : "2") +
                " gana el Trono de Hierro!";
        System.out.println(mensaje);
        log.add(mensaje);
        agregarResumenFinal();
    }

    private void agregarResumenFinal() {
        log.add("\n=== RESUMEN FINAL ===");
        agregarResumenJugador(jugador1, 1);
        agregarResumenJugador(jugador2, 2);
    }

    private void agregarResumenJugador(Jugador jugador, int numeroJugador) {
        log.add("\n=== JUGADOR " + numeroJugador + " ===");
        jugador.getPersonajes().forEach(p -> {
            String estado = p.estaVivo() ?
                    String.format("Vivo (Salud: %.1f)", p.getSalud()) :
                    "Muerto";
            log.add("- " + p.getApodo() + ": " + estado);
        });
    }



    private void esperarEnter() {
        System.out.print("Presione ENTER para continuar...");
        scanner.nextLine();
    }

    public List<String> getLog() {
        return log;
    }
}