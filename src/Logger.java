import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Logger {
    private static final String LOG_DIR = "logs";
    private static final String CONTADOR_FILE = "logs/contador_partidas.txt";
    private static final DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss");

    static {
        try {
            Files.createDirectories(Paths.get(LOG_DIR)); // Crear carpeta logs si no existe
        } catch (IOException ignored) {}
    }

    // Obtener y actualizar número de partida
    private static int getAndIncrementPartidaNumber() {
        try {
            Path contadorPath = Paths.get(CONTADOR_FILE);
            int currentNumber = 0;

            if (Files.exists(contadorPath)) {
                String content = Files.readString(contadorPath);
                currentNumber = Integer.parseInt(content.trim());
            }

            Files.write(contadorPath, String.valueOf(currentNumber + 1).getBytes());
            return currentNumber + 1;
        } catch (Exception e) {
            return 1; // Fallback
        }
    }

    // Generar nombre de archivo único
    private static String generarNombreArchivo(int partidaNumber) {
        String fecha = LocalDateTime.now().format(dateFormatter);
        return String.format("partida_%d_%s.log", partidaNumber, fecha);
    }

    // Guardar log en archivo individual
    public static void guardarLog(List<String> log) {
        try {
            int partidaNumber = getAndIncrementPartidaNumber();
            String nombreArchivo = generarNombreArchivo(partidaNumber);
            Path archivoLog = Paths.get(LOG_DIR, nombreArchivo);

            String header = "=== INICIO DE PARTIDA ===\n";
            String footer = "\n=== FIN DE PARTIDA ===";

            Files.write(archivoLog, header.getBytes());
            Files.write(archivoLog, log, StandardOpenOption.APPEND);
            Files.write(archivoLog, footer.getBytes(), StandardOpenOption.APPEND);

        } catch (IOException e) {
            System.err.println("Error al guardar log: " + e.getMessage());
        }
    }

    public static void mostrarLogs() {
        try (var archivos = Files.list(Paths.get(LOG_DIR))) {
            List<Path> logs = archivos
                    .filter(p -> p.toString().endsWith(".log"))
                    .sorted((p1, p2) -> {
                        // Ordenar por número de partida (extraído del nombre)
                        int n1 = extraerNumeroPartida(p1);
                        int n2 = extraerNumeroPartida(p2);
                        return Integer.compare(n1, n2);
                    })
                    .toList();

            if (logs.isEmpty()) {
                System.out.println("\nNo hay logs disponibles.");
                return;
            }

            // Mostrar lista numerada
            System.out.println("\n=== LOGS DISPONIBLES ===");
            for (int i = 0; i < logs.size(); i++) {
                String nombre = logs.get(i).getFileName().toString();
                System.out.printf("[%d] %s\n", i + 1, nombre);
            }

            // Selección de log
            Scanner scanner = new Scanner(System.in);
            System.out.print("\nIngrese el número del log (0 para volver): ");
            int opcion = scanner.nextInt();

            if (opcion == 0) return;
            if (opcion < 1 || opcion > logs.size()) {
                System.out.println("Opción inválida.");
                return;
            }

            // Mostrar contenido del log seleccionado
            Path logSeleccionado = logs.get(opcion - 1);
            System.out.println("\n=== CONTENIDO DE " + logSeleccionado.getFileName() + " ===");
            Files.readAllLines(logSeleccionado).forEach(System.out::println);

        } catch (IOException e) {
            System.err.println("Error al leer logs: " + e.getMessage());
        }
    }

    private static int extraerNumeroPartida(Path path) {
        String nombre = path.getFileName().toString();
        // Ejemplo: partida_22_20231026_093045.log → 22
        try {
            return Integer.parseInt(nombre.split("_")[1]);
        } catch (Exception e) {
            return 0; // Fallback para ordenación
        }
    }

    // Borrar todos los logs
    public static void borrarLogs() {
        try (var archivos = Files.list(Paths.get(LOG_DIR))) {
            archivos.forEach(p -> {
                try { Files.delete(p); }
                catch (IOException ignored) {}
            });
            System.out.println();
            System.out.println("Todos los logs borrados");
        } catch (IOException e) {
            System.err.println("Error al borrar logs");
        }
    }
}