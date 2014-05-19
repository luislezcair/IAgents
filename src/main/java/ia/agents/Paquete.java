/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents;

import java.util.Date;

/**
 * Clase que epresenta a un paquete de viaje
 */
public class Paquete {
    private String destino;
    private int dias;
    private Date fecha;
    private int formaDePago;
    private double importeMaxPorPersona;
    private int personas;

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public int getDias() {
        return dias;
    }

    public void setDias(int dias) {
        this.dias = dias;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getFormaDePago() {
        return formaDePago;
    }

    public void setFormaDePago(int formaDePago) {
        this.formaDePago = formaDePago;
    }

    public double getImporteMaxPorPersona() {
        return importeMaxPorPersona;
    }

    public void setImporteMaxPorPersona(double importeMaxPorPersona) {
        this.importeMaxPorPersona = importeMaxPorPersona;
    }

    public int getPersonas() {
        return personas;
    }

    public void setPersonas(int personas) {
        this.personas = personas;
    }

    /**
     * Versión serializada de un paquete turístico
     * @return Cadena con los atributos separados por '@'
     */
    @Override
    public String toString() {
        return destino + "@" +
               dias + "@" +
               fecha + "@" +
               formaDePago + "@" +
               importeMaxPorPersona + "@" +
               personas;
    }
}