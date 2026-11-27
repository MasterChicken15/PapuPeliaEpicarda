/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Monse
 */
public class Caballero extends PersonajeBase {
   private int armadura;
    private int resistencia;
    private boolean escudo;
    private String criaturaApoyo;

    public Caballero(int armadura, int resistencia, boolean escudo, String criaturaApoyo, String id, String nombre, int vida, int fuerza, int poder, int defensa, String arma, String rutaImagen) {
        super(id, nombre, vida, fuerza, poder, defensa, arma, rutaImagen);
        this.armadura = armadura;
        this.resistencia = resistencia;
        this.escudo = escudo;
        this.criaturaApoyo = criaturaApoyo;
    }

    public Caballero(String id, String nombre, int vida, int fuerza, int defensa, String rutaImagen) {
        this(50, 50, true, "Corcel blindado", id, nombre, vida, fuerza, 30, defensa,
                "Espada larga", rutaImagen);
    }
    public int getArmadura() {
        return armadura;
    }

    public void setArmadura(int armadura) {
        this.armadura = armadura;
    }

    public int getResistencia() {
        return resistencia;
    }

    public void setResistencia(int resistencia) {
        this.resistencia = resistencia;
    }

    public boolean isEscudo() {
        return escudo;
    }

    public void setEscudo(boolean escudo) {
        this.escudo = escudo;
    }

    public String getCriaturaApoyo() {
        return criaturaApoyo;
    }

    public void setCriaturaApoyo(String criaturaApoyo) {
        this.criaturaApoyo = criaturaApoyo;
    }
    @Override
    public String ejecutarHabilidadEspecial(PersonajeBase objetivo) {
        int bonoEscudo = escudo ? (this.armadura / 3) + 10 : 5;
        this.defensa += bonoEscudo;
        int danoEmbestida = this.fuerza + (this.resistencia / 3) + (escudo ? 15 : 0);
        String msg = nombre + " alza su escudo (" + (escudo ? "activo" : "sin escudo")
                + ", +" + bonoEscudo + " defensa) y embiste montado en " + criaturaApoyo + ".";
        if (objetivo != null) {
            msg += " " + objetivo.recibirDano(danoEmbestida);
        }
        return msg;
    }

    @Override
    public String getTipoClase() {
        return "Caballero";
    }
    
    
    
}
