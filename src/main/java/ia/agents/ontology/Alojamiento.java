/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents.ontology;

import jade.content.Concept;

import java.util.Date;

/**
 * Clase que representa un lugar de alojamiento para los turistas
 */
public class Alojamiento implements Concept {
    private int capacidad;
    private String ciudad;
    private double descuento;
    private Date fecha;
    private double precioPorPersona;
    private int tipo;

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getPrecioPorPersona() {
        return precioPorPersona;
    }

    public void setPrecioPorPersona(double precioPorPersona) {
        this.precioPorPersona = precioPorPersona;
    }
/*
    public double getPrecioTotal(int cantPersonas, int cantDias) {
        return precioPorPersona*cantPersonas*cantDias*(1 - descuento);
    }
*/
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
