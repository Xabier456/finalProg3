import java.util.Random;

public class Personaje {
    private String raza;
    private String nombre;
    private String apodo;
    private int edad;
    private double salud;
    private int velocidad;
    private int destreza;
    private int fuerza;
    private int nivel;
    private int armadura;

    // Constructor para crear personajes manualmente
    public Personaje(String raza, String nombre, String apodo, int edad,
                     int velocidad, int destreza, int fuerza, int nivel, int armadura) {
        this.raza = raza;
        this.nombre = nombre;
        this.apodo = apodo;
        this.edad = edad;
        this.salud = 100;
        this.velocidad = velocidad;
        this.destreza = destreza;
        this.fuerza = fuerza;
        this.nivel = nivel;
        this.armadura = armadura;
    }

    // Constructor para generar personajes aleatorios
    public Personaje() {
        Random rand = new Random();
        String[] razas = {"Humano", "Orco", "Elfo"};
        this.raza = razas[rand.nextInt(3)];
        this.nombre = "Personaje-" + rand.nextInt(1000);
        this.apodo = "Apodo-" + rand.nextInt(1000);
        this.edad = rand.nextInt(301);
        this.salud = 100;
        this.velocidad = rand.nextInt(10) + 1;
        this.destreza = rand.nextInt(5) + 1;
        this.fuerza = rand.nextInt(10) + 1;
        this.nivel = rand.nextInt(10) + 1;
        this.armadura = rand.nextInt(10) + 1;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public String getRaza() { return raza; }
    public double getSalud() { return salud; }
    public int getVelocidad() { return velocidad; }
    public int getDestreza() { return destreza; }
    public int getFuerza() { return fuerza; }
    public int getNivel() { return nivel; }
    public int getArmadura() { return armadura; }
    public String getApodo() { return apodo; }

    public void recibirDano(double dano) {
        this.salud = Math.max(0, this.salud - dano);
    }

    public void aplicarMejora() {
        Random rand = new Random();
        int opcion = rand.nextInt(2);
        if (opcion == 0) {
            this.salud += 10;
        } else {
            this.nivel = Math.min(10, this.nivel + 1);
        }
    }

    public boolean estaVivo() {
        return salud > 0;
    }
}