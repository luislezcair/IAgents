/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents;

/**
 * Clase que representa a una unidad de transporte
 */
@SuppressWarnings("unused")
public class Transporte {
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
     * Versión serializada de un medio de transporte
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
