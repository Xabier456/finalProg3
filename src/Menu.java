import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Menu {
    private Scanner scanner = new Scanner(System.in);

    public void mostrarMenu() {
        while (true) {
            System.out.println("\n=== MENÚ ===");
            System.out.println("1. Nueva partida (personajes aleatorios)");
            System.out.println("2. Nueva partida (ingresar personajes)");
            System.out.println("3. Ver logs");
            System.out.println("4. Borrar logs");
            System.out.println("5. Salir");
            System.out.print("Seleccione opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine();  // Limpiar buffer

            switch (opcion) {
                case 1:
                    iniciarPartida(true);
                    break;
                case 2:
                    iniciarPartida(false);
                    break;
                case 3:
                    Logger.mostrarLogs();
                    break;
                case 4:
                    Logger.borrarLogs();
                    break;
                case 5:
                    System.exit(0);
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    private void iniciarPartida(boolean esAleatoriaGlobal) {
        List<Personaje> personajes = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            int jugador = (i < 3) ? 1 : 2; // Determinar jugador
            System.out.println("\n=== PERSONAJE " + (i + 1) + "/6 (Jugador " + jugador + ") ===");

            // Preguntar por tipo de creación
            boolean esAleatorio = esAleatoriaGlobal ? true : preguntarTipoCreacion();

            if (esAleatorio) {
                Personaje p = new Personaje();
                personajes.add(p);
                //mostrarPersonajeGenerado(p);
            } else {
                personajes.add(crearPersonajeManual(i + 1, jugador));
            }
        }

        // Mostrar datos de los personajes
        mostrarPersonajesEnConsola(personajes);

        // Crear jugadores con copias independientes
        Jugador j1 = new Jugador(new ArrayList<>(personajes.subList(0, 3)));
        Jugador j2 = new Jugador(new ArrayList<>(personajes.subList(3, 6)));

        // Iniciar combate
        Combate combate = new Combate(j1, j2);
        agregarPersonajesAlLog(combate.getLog(), j1, j2); // Añadir datos al log
        combate.iniciarCombate();

        // Pausa final
        System.out.println("\nPartida finalizada. Presione ENTER para volver al menú.");
        scanner.nextLine();
    }

    private boolean preguntarTipoCreacion() {
        while (true) {
            System.out.print("¿Generar personaje aleatorio? (1. Sí / 2. No): ");
            String opcion = scanner.nextLine();
            if (opcion.equals("1")) return true;
            if (opcion.equals("2")) return false;
            System.out.println("Opción inválida. Use 1 o 2");
        }
    }

    private void mostrarPersonajesEnConsola(List<Personaje> personajes) {
        System.out.println("\n=== PERSONAJES GENERADOS ===");
        for (int i = 0; i < personajes.size(); i++) {
            Personaje p = personajes.get(i);
            String jugador = (i < 3) ? "Jugador 1" : "Jugador 2";

            System.out.println("[" + jugador + "] " + formatearDatosPersonaje(p));
        }
        esperarEnter();
    }

    private void agregarPersonajesAlLog(List<String> log, Jugador j1, Jugador j2) {
        log.add("\n=== PERSONAJES JUGADOR 1 ===");
        j1.getPersonajes().forEach(p -> log.add(formatearDatosPersonaje(p)));

        log.add("\n=== PERSONAJES JUGADOR 2 ===");
        j2.getPersonajes().forEach(p -> log.add(formatearDatosPersonaje(p)));
    }

    private String formatearDatosPersonaje(Personaje p) {
        return String.format(
                "%s (%s) | Salud: %.1f | Fuerza: %d | Nivel: %d | Raza: %s | Velocidad: %d | Destreza: %d | Armadura: %d",
                p.getNombre(),
                p.getApodo(),
                p.getSalud(),
                p.getFuerza(),
                p.getNivel(),
                p.getRaza(),
                p.getVelocidad(),
                p.getDestreza(),
                p.getArmadura()
        );
    }

    private void esperarEnter() {
        System.out.print("\nPresione ENTER para continuar...");
        scanner.nextLine();
    }

    private Personaje crearPersonajeManual(int numeroPersonaje, int jugador) {
        System.out.println("\n--- CREACIÓN MANUAL (Jugador " + jugador + ") ---");
        //indicar jugador del personaje

        // Solicitar raza
        String raza = solicitarRaza();

        // Solicitar datos básicos
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Apodo: ");
        String apodo = scanner.nextLine();

        // Validar edad
        int edad = solicitarNumero("Edad (0-300): ", 0, 300);

        // Solicitar características con validación
        int velocidad = solicitarNumero("Velocidad (1-10): ", 1, 10);
        int destreza = solicitarNumero("Destreza (1-5): ", 1, 5);
        int fuerza = solicitarNumero("Fuerza (1-10): ", 1, 10);
        int nivel = solicitarNumero("Nivel (1-10): ", 1, 10);
        int armadura = solicitarNumero("Armadura (1-10): ", 1, 10);

        return new Personaje(raza, nombre, apodo, edad,
                velocidad, destreza, fuerza, nivel, armadura);
    }

    private void mostrarPersonajeGenerado(Personaje p) {
        System.out.println("\nPersonaje generado:");
        System.out.println("• Raza: " + p.getRaza());
        System.out.println("• Nombre: " + p.getNombre());
        System.out.println("• Apodo: " + p.getApodo());
        System.out.println("• Fuerza: " + p.getFuerza());
        System.out.println("• Nivel: " + p.getNivel());
        System.out.println("• Velocidad: " + p.getVelocidad());
        System.out.println("• Destreza: " + p.getDestreza());
        System.out.println("• Armadura: " + p.getArmadura());
        esperarEnter();
    }

    private String solicitarRaza() {
        while(true) {
            System.out.print("Raza (Humano/Elfo/Orco): ");
            String input = scanner.nextLine().trim();
            if(input.equalsIgnoreCase("Humano")) return "Humano";
            if(input.equalsIgnoreCase("Elfo")) return "Elfo";
            if(input.equalsIgnoreCase("Orco")) return "Orco";
            System.out.println("Raza inválida. Intente nuevamente.");
        }
    }

    private int solicitarNumero(String mensaje, int min, int max) {
        while(true) {
            try {
                System.out.print(mensaje);
                int valor = Integer.parseInt(scanner.nextLine());
                if(valor >= min && valor <= max) return valor;
                System.out.println("Valor fuera de rango (" + min + "-" + max + ")");
            } catch(NumberFormatException e) {
                System.out.println("Ingrese un número válido");
            }
        }
    }
}
