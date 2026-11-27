/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Monse
 */
public class Stand extends PersonajeBase {
    private int rango;
    private String tipo;
    private String frase;
    private String usuario;

    public Stand(int rango, String tipo, String frase, String usuario, String id, String nombre, int vida, int fuerza, int poder, int defensa, String arma, String rutaImagen) {
        super(id, nombre, vida, fuerza, poder, defensa, arma, rutaImagen);
        this.rango = rango;
        this.tipo = tipo;
        this.frase = frase;
        this.usuario = usuario;
    }
    
    public Stand(String id, String nombre, int vida, int fuerza, int defensa, String rutaImagen) {
        this(10, "Corto alcance", "¡ORA ORA ORA!", "Usuario", id, nombre, vida, fuerza,
                55, defensa, "Golpes de precisión", rutaImagen);
    }

    public int getRango() {
        return rango;
    }

    public void setRango(int rango) {
        this.rango = rango;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFrase() {
        return frase;
    }

    public void setFrase(String frase) {
        this.frase = frase;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    @Override
    public String ejecutarHabilidadEspecial(PersonajeBase objetivo) {
        int danoRafaga = this.poder + this.fuerza + (this.rango * 2);
        String msg = usuario + " invoca a " + nombre + " (" + tipo + ", rango " + rango
                + ") y grita: \"" + frase + "\"";
        if (objetivo != null) {
            msg += " " + objetivo.recibirDano(danoRafaga);
        }
        return msg;
    }

    @Override
    public String getTipoClase() {
        return "Stand";
    }
    
    
    
}
