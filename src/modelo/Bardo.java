/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Monse
 */
public class Bardo extends PersonajeBase {
    private int inspiracion;
    private int velocidadAtaque;

    public Bardo(int inspiracion, int velocidadAtaque, String id, String nombre, int vida, int fuerza, int poder, int defensa, String arma, String rutaImagen) {
        super(id, nombre, vida, fuerza, poder, defensa, arma, rutaImagen);
        this.inspiracion = inspiracion;
        this.velocidadAtaque = velocidadAtaque;
    }
public Bardo(String id, String nombre, int vida, int fuerza, int defensa, String rutaImagen) {
        this(30, 10, id, nombre, vida, fuerza, 35, defensa, "Laúd encantado", rutaImagen);
    }
    public int getInspiracion() {
        return inspiracion;
    }

    public void setInspiracion(int inspiracion) {
        this.inspiracion = inspiracion;
    }

    public int getVelocidadAtaque() {
        return velocidadAtaque;
    }

    public void setVelocidadAtaque(int velocidadAtaque) {
        this.velocidadAtaque = velocidadAtaque;
    }
    
    
   @Override
    public String ejecutarHabilidadEspecial(PersonajeBase objetivo) {
        int curacion = 15 + (this.inspiracion / 2);
        setVida(this.vida + curacion);
        this.velocidadAtaque += 10;
        int danoAcorde = this.poder + this.velocidadAtaque;
        String texto = nombre + " entona su melodía, recupera " + curacion + " HP (HP: "
                + this.vida + "/" + this.vidaMaxima + ") y sube velocidad de ataque a "
                + this.velocidadAtaque + ".";
        if (objetivo != null) {
            texto += " " + objetivo.recibirDano(danoAcorde);
        }
        return texto;
    }

    @Override
    public String getTipoClase() {
        return "Bardo";
    }
  
}
