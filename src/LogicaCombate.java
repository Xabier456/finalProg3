import java.util.Random;


public class LogicaCombate {
    private static final Random rand = new Random();

    public static double calcularDanio(Personaje atacante, Personaje defensor) {
        int PD = atacante.getDestreza() * atacante.getFuerza() * atacante.getNivel();
        double ED = (rand.nextInt(100) + 1) / 100.0; // ED como decimal (0.01 a 1.00)
        double VA = PD * ED; // Ahora ED actúa como porcentaje correcto
        int PDEF = (defensor.getArmadura() * defensor.getVelocidad()) / 2; // dividí por 2 porque muchas veces no alcanzaba a hacer daño

        // Cálculo del daño según raza (ejemplo para humano)
        double danioBase = (((VA * ED) - PDEF) / 500.0) * 100;

        // Aplicar modificadores de raza
        switch (atacante.getRaza()) {
            case "Elfo": danioBase *= 1.05; break;
            case "Orco": danioBase *= 1.10; break;
        }

        return Math.max(0, danioBase);
    }
}