/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author SUN
 */
public class Orco extends PersonajeBase{
    private int colmillos;
    private String tamano;
    private String color;
    private String aditamento;
   
public Orco (String id, String nombre, int vida, int fuerza, int poder, int defensa, String arma, String rutaImagen,int colmillos, String tamano, String color, String aditamento){
    super(id,nombre,vida,fuerza,poder,defensa,arma,rutaImagen);
    this.colmillos=colmillos;
    this.tamano=tamano;
    this.color=color;
    this.aditamento=aditamento; 
}
public Orco(String id, String nombre, int vida, int fuerza, int defensa, String rutaImagen) {
        this(id, nombre, vida, fuerza, 30, defensa, "Hacha de guerra", rutaImagen,
                2, "Corpulento", "Verde", "Cicatrices de batalla");
    }

    public int getColmillos() {
        return colmillos;
    }

    public String getTamano() {
        return tamano;
    }

    

    public String getColor() {
        return color;
    }

    public String getAditamento() {
        return aditamento;
    }

    public void setColmillos(int colmillos) {
        this.colmillos = colmillos;
    }

    public void setTamano(String tamano) {
        this.tamano = tamano;
    }

    
    public void setColor(String color) {
        this.color = color;
    }

    public void setAditamento(String aditamento) {
        this.aditamento = aditamento;
    }

@Override
    public String ejecutarHabilidadEspecial(PersonajeBase objetivo) {
        int bonoFuerza = 15 + (colmillos * 2);
        this.fuerza += bonoFuerza;
        int golpe = this.fuerza + (this.poder / 2);
        String resultado = nombre + " (" + tamano + ", " + color + ", " + aditamento
                + ") lanza ¡WAAAGH! y gana +" + bonoFuerza + " de fuerza.";
        if (objetivo != null) {
            resultado += " " + objetivo.recibirDano(golpe);
        }
        return resultado;
    }
@Override
    public String getTipoClase() {
        return "Orco";
    }
}
