/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Monse
 */
public class Barbaro extends PersonajeBase{
    private int estamina;
    private int resistencia;
    private String armaAdicional;

    public Barbaro(int estamina, int resistencia, String armaAdicional, String id, String nombre, int vida, int fuerza, int poder, int defensa, String arma, String rutaImagen) {
        super(id, nombre, vida, fuerza, poder, defensa, arma, rutaImagen);
        this.estamina = estamina;
        this.resistencia = resistencia;
        this.armaAdicional = armaAdicional;
    }

    public Barbaro(String id, String nombre, int vida, int fuerza, int defensa, String rutaImagen) {
        this(60, 50, "Hacha de mano", id, nombre, vida, fuerza, 25, defensa, "Mandoble", rutaImagen);
    }
    public int getEstamina() {
        return estamina;
    }

    public void setEstamina(int estamina) {
        this.estamina = estamina;
    }

    public int getResistencia() {
        return resistencia;
    }

    public void setResistencia(int resistencia) {
        this.resistencia = resistencia;
    }

    public String getArmaAdicional() {
        return armaAdicional;
    }

    public void setArmaAdicional(String armaAdicional) {
        this.armaAdicional = armaAdicional;
    }
    
    @Override
    public String ejecutarHabilidadEspecial(PersonajeBase objetivo) {
        int sacrificioDefensa = Math.min(this.defensa, Math.max(5, this.defensa / 3));
        this.defensa -= sacrificioDefensa;
        this.estamina = Math.max(0, this.estamina - 10);
        int danoCritico = (this.fuerza * 2) + (this.resistencia / 4) + 15;
        String msg = nombre + " entra en furia con " + arma + " y " + armaAdicional
                + " (sacrifica " + sacrificioDefensa + " de defensa).";
        if (objetivo != null) {
            msg += " " + objetivo.recibirDano(danoCritico);
        }
        return msg;
    }

    @Override
    public String getTipoClase() {
        return "Bárbaro";
    }
    
}
