/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents;

/**
 * Clase que representa un lugar de alojamiento para los turistas
 */
@SuppressWarnings("unused")
public class Alojamiento {
    private int capacidad;
    private double descuento;
    private double precioPorPersona;
    private int tipo;

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public double getPrecioPorPersona() {
        return precioPorPersona;
    }

    public void setPrecioPorPersona(double precioPorPersona) {
        this.precioPorPersona = precioPorPersona;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    /**
     * Versión serializada de un lugar de alojamiento
     * @return Cadena con los atributos separados por '@'
     */
    @Override
    public String toString() {
        return capacidad + "@" +
               descuento + "@" +
               precioPorPersona + "@" +
               tipo;
    }
}
