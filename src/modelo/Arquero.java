/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Monse
 */
public class Arquero extends PersonajeBase {
   private int rango;
    private String municion;

    public Arquero(int rango, String municion, String id, String nombre, int vida, int fuerza, int poder, int defensa, String arma, String rutaImagen) {
        super(id, nombre, vida, fuerza, poder, defensa, arma, rutaImagen);
        this.rango = rango;
        this.municion = municion;
    }

    public Arquero(String id, String nombre, int vida, int fuerza, int defensa, String rutaImagen) {
        this(80, "Flechas de acero", id, nombre, vida, fuerza, 35, defensa, "Arco compuesto", rutaImagen);
    }
    public int getRango() {
        return rango;
    }

    public void setRango(int rango) {
        this.rango = rango;
    }

    public String getMunicion() {
        return municion;
    }

    public void setMunicion(String municion) {
        this.municion = municion;
    }
    @Override
    public String ejecutarHabilidadEspecial(PersonajeBase objetivo) {
        if (objetivo == null) {
            return nombre + " apunta a distancia (rango " + rango + ") con " + municion + ".";
        }
        int defensaOriginal = objetivo.getDefensa();
        int defensaReducida = Math.max(0, defensaOriginal / 2);
        objetivo.setDefensa(defensaReducida);
        int danoDisparo = this.fuerza + (this.poder / 2) + (this.rango / 4);
        String resultado = nombre + " dispara " + municion + " perforantes (rango " + rango
                + ", ignora mitad de defensa). " + objetivo.recibirDano(danoDisparo);
        objetivo.setDefensa(defensaOriginal);
        return resultado;
    }

    @Override
    public String getTipoClase() {
        return "Arquero";
    }
 
}
