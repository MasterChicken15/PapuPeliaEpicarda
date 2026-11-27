/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Monse
 */
public class Ninfa extends PersonajeBase{
    private String elemento;
    private int nivelSeduccion;
    private int experiencia;
    
    public Ninfa(String elemento, int nivelSeduccion, int experiencia, String id, String nombre, int vida, int fuerza, int poder, int defensa, String arma, String rutaImagen) {
        super(id, nombre, vida, fuerza, poder, defensa, arma, rutaImagen);
        this.elemento = elemento;
        this.nivelSeduccion = nivelSeduccion;
        this.experiencia = experiencia;
    }
    
    public Ninfa(String id, String nombre, int vida, int fuerza, int defensa, String rutaImagen) {
    this("Naturaleza", 25, 10, id, nombre, vida, fuerza, 45, defensa, "Báculo silvestre", rutaImagen);
}

    public String getElemento() {
        return elemento;
    }

    public int getNivelSeduccion() {
        return nivelSeduccion;
    }

    public int getExperiencia() {
        return experiencia;
    }

    public void setElemento(String elemento) {
        this.elemento = elemento;
    }

    public void setNivelSeduccion(int nivelSeduccion) {
        this.nivelSeduccion = nivelSeduccion;
    }

    public void setExperiencia(int experiencia) {
        this.experiencia = experiencia;
    }
   @Override
    public String ejecutarHabilidadEspecial(PersonajeBase objetivo) {
        this.experiencia += 15;
        int descarga = this.poder + (this.nivelSeduccion * 2) + (this.experiencia / 3);
        if (objetivo == null) {
            return nombre + " canaliza el elemento " + elemento + " con seducción nivel "
                    + nivelSeduccion + ".";
        }
        int reduccionDefensa = Math.min(objetivo.getDefensa(), Math.max(4, this.nivelSeduccion / 2));
        objetivo.setDefensa(objetivo.getDefensa() - reduccionDefensa);
        return nombre + " hechiza a " + objetivo.getNombre() + " (reduce su defensa en "
                + reduccionDefensa + ") y libera descarga de " + elemento + ". "
                + objetivo.recibirDano(descarga);
    }

    @Override
    public String getTipoClase() {
        return "Ninfa";
    }
    
    
    
}
