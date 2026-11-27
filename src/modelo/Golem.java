/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Monse
 */
public class Golem extends PersonajeBase{
    private int resistencia;
    private String elemento;

    public Golem(int resistencia, String elemento, String id, String nombre, int vida, int fuerza, int poder, int defensa, String arma, String rutaImagen) {
        super(id, nombre, vida, fuerza, poder, defensa, arma, rutaImagen);
        this.resistencia = resistencia;
        this.elemento = elemento;
    }
    
    public Golem(String id, String nombre, int vida, int fuerza, int defensa, String rutaImagen) {
        this(60, "Tierra", id, nombre, vida, fuerza, 25, defensa, "Puños de roca", rutaImagen);
    }

    public int getResistencia() {
        return resistencia;
    }

    public void setResistencia(int resistencia) {
        this.resistencia = resistencia;
    }

    public String getElemento() {
        return elemento;
    }

    public void setElemento(String elemento) {
        this.elemento = elemento;
    }
    
    @Override
    public String ejecutarHabilidadEspecial(PersonajeBase objetivo) {
        this.resistencia *= 2;
        this.defensa *= 2;
        int regeneracion = 20 + (this.resistencia / 10);
        setVida(this.vida + regeneracion);
        String msg = nombre + " (" + elemento + ") activa Mitosis y Hivemind: duplica resistencia ("
                + resistencia + ") y defensa (" + defensa + "), y regenera " + regeneracion
                + " HP (HP: " + vida + "/" + vidaMaxima + ").";
        if (objetivo != null) {
            msg += " " + objetivo.recibirDano(this.fuerza + (this.resistencia / 4));
        }
        return msg;
    }

    @Override
    public String getTipoClase() {
        return "Golem";
    }
}
