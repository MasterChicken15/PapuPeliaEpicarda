/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Monse
 */
public class Polinene extends PersonajeBase {
  private int cantidadPolimonedas;
    private int inteligencia;
    private boolean tamear;
    private int nivelAura;
    
    public Polinene(int cantidadPolimonedas, int inteligencia, boolean tamear, int nivelAura, String id, String nombre, int vida, int fuerza, int poder, int defensa, String arma, String rutaImagen) {
        super(id, nombre, vida, fuerza, poder, defensa, arma, rutaImagen);
        this.cantidadPolimonedas = cantidadPolimonedas;
        this.inteligencia = inteligencia;
        this.tamear = tamear;
        this.nivelAura = nivelAura;
    }
 public Polinene(String id, String nombre, int vida, int fuerza, int defensa,
                String arma, String rutaImagen) {
    this(50, 60, true, 10, id, nombre, vida, fuerza, 40, defensa, arma, rutaImagen);
}
    public int getCantidadPolimonedas() {
        return cantidadPolimonedas;
    }

    public void setCantidadPolimonedas(int cantidadPolimonedas) {
        this.cantidadPolimonedas = cantidadPolimonedas;
    }

    public int getInteligencia() {
        return inteligencia;
    }

    public void setInteligencia(int inteligencia) {
        this.inteligencia = inteligencia;
    }

    public boolean isTamear() {
        return tamear;
    }

    public void setTamear(boolean tamear) {
        this.tamear = tamear;
    }

    public int getNivelAura() {
        return nivelAura;
    }

    public void setNivelAura(int nivelAura) {
        this.nivelAura = nivelAura;
    }
    
    @Override
    public String ejecutarHabilidadEspecial(PersonajeBase objetivo) {
        this.nivelAura += 15;
        this.inteligencia += 10;
        int bonoBurrito = tamear ? 25 : 10;
        int danoEstrategico = this.poder + this.inteligencia + this.nivelAura + bonoBurrito
                + (this.cantidadPolimonedas / 5);
        String estadoBurrito = tamear ? "burrito domesticado" : "burrito en entrenamiento";
        String msg = nombre + " aplica estrategia IPN (" + estadoBurrito + ", Aura: "
                + nivelAura + ", INT: " + inteligencia + ").";
        if (objetivo != null) {
            msg += " " + objetivo.recibirDano(danoEstrategico);
        }
        return msg;
    }

    @Override
    public String getTipoClase() {
        return "Polinene";
    }
   
 
}
