/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author SUN
 */
public abstract class PersonajeBase {
    protected String id;
    protected String nombre;
    protected int vida;
    protected int vidaMaxima;
    protected int fuerza;
    protected int poder;
    protected int defensa;
    protected String arma;
    protected String rutaImagen; // Columna VARCHAR en la tabla Personajes (Unidad V)
     
    public PersonajeBase(String id, String nombre, int vida, int fuerza, int poder, int defensa, String arma, String rutaImagen){   
        this.id = id;
        this.nombre = nombre; 
        this.vida = vida;
        this.vidaMaxima = vida;
        this.fuerza = fuerza; 
        this.poder = poder;
        this.defensa = defensa;
        this.arma = arma;
        this.rutaImagen = rutaImagen; 
    }
    //GETTERS Y SETTERS

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getVida() {
        return vida;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public int getFuerza() {
        return fuerza;
    }

    public int getPoder() {
        return poder;
    }

    public int getDefensa() {
        return defensa;
    }

    public String getArma() {
        return arma;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setVida(int vida) {
        this.vida = Math.max(0, Math.min(vida, vidaMaxima));
    }

    public void setVidaMaxima(int vidaMaxima) {
        this.vidaMaxima = vidaMaxima;
    }

    public void setFuerza(int fuerza) {
        this.fuerza = fuerza;
    }

    public void setPoder(int poder) {
        this.poder = poder;
    }

    public void setDefensa(int defensa) {
        this.defensa = defensa;
    }

    public void setArma(String arma) {
        this.arma = arma;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }
    
    public String recibirDano(int danoBruto) {
    // TODO: Calcular el daño neto descontando parte de 'this.defensa' 
// asegurando daño mínimo de 1 y que 'this.vida' nunca sea menor a 0.
    int danoNeto = Math.max(1, danoBruto - (this.defensa / 4)); 
    this.vida = Math.max(0, this.vida - danoNeto); 
    return this.nombre + " recibe " + danoNeto + " pts de daño (HP: " + this.vida + "/" + this.vidaMaxima + ")";
  }
    public abstract String ejecutarHabilidadEspecial(PersonajeBase objetivo); 
    public abstract String getTipoClase();
}
