/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Monse
 */
public class Saiyan extends PersonajeBase{
    private boolean cola;
    private int celulasZ;
    private int zenkai;
    private int ki;

    public Saiyan(boolean cola, int celulasZ, int zenkai, int ki, String id, String nombre, int vida, int fuerza, int poder, int defensa, String arma, String rutaImagen) {
        super(id, nombre, vida, fuerza, poder, defensa, arma, rutaImagen);
        this.cola = cola;
        this.celulasZ = celulasZ;
        this.zenkai = zenkai;
        this.ki = ki;
    }
    
    public Saiyan(String id, String nombre, int vida, int fuerza, int defensa, String rutaImagen) {
        this(true, 50, 10, 80, id, nombre, vida, fuerza, 60, defensa, "Energía Ki", rutaImagen);
    }

    public boolean isCola() {
        return cola;
    }

    public void setCola(boolean cola) {
        this.cola = cola;
    }

    public int getCelulasZ() {
        return celulasZ;
    }

    public void setCelulasZ(int celulasZ) {
        this.celulasZ = celulasZ;
    }

    public int getZenkai() {
        return zenkai;
    }

    public void setZenkai(int zenkai) {
        this.zenkai = zenkai;
    }

    public int getKi() {
        return ki;
    }

    public void setKi(int ki) {
        this.ki = ki;
    }
    
    @Override
    public String ejecutarHabilidadEspecial(PersonajeBase objetivo) {
        this.ki += 40;
        boolean zenkaiActivo = (this.vida * 100) < (this.vidaMaxima * 30);
        int multiplicador = zenkaiActivo ? 2 : 1;
        if (zenkaiActivo) {
            this.zenkai += 15;
        }
        this.fuerza += (15 + (this.celulasZ / 10)) * multiplicador;
        this.poder += (20 + (this.ki / 5)) * multiplicador;
        int danoRafaga = this.poder + this.fuerza + (zenkaiActivo ? this.zenkai * 2 : 0);
        String estado = zenkaiActivo ? " [BONO ZENKAI ACTIVADO]" : "";
        String msg = nombre + " eleva su Ki a " + ki + " y se transforma" + estado
                + " (Fuerza: " + fuerza + ", Poder: " + poder + ").";
        if (objetivo != null) {
            msg += " " + objetivo.recibirDano(danoRafaga);
        }
        return msg;
    }

    @Override
    public String getTipoClase() {
        return "Saiyan";
    }
    
    
}
