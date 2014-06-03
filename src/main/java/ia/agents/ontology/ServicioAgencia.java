/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents.ontology;

import ia.agents.negotiation.DiscountManager;
import jade.content.Concept;
import java.util.Date;

/**
 * Representa un lugar o un transporte. Tienen los mismos atributos,
 * así que conviene aprovechar el polimorfismo.
 */

public class ServicioAgencia implements Concept {
    private int capacidad;
    private String destino;
    private Date fecha;
    private double precioPorPersona;
    private int tipo;

    private DiscountManager ds;

    public ServicioAgencia() { }

    public ServicioAgencia(int capacidad, String destino, Date fecha,
                       double precioPorPersona, int tipo, DiscountManager ds) {
        this.capacidad = capacidad;
        this.destino = destino;
        this.fecha = fecha;
        this.precioPorPersona = precioPorPersona;
        this.tipo = tipo;
        this.ds = ds;
    }

    public ServicioAgencia(ServicioAgencia other) {
        this.capacidad = other.capacidad;
        this.destino = other.destino;
        this.fecha = other.fecha;
        this.precioPorPersona = other.precioPorPersona;
        this.tipo = other.tipo;
        this.ds = new DiscountManager(other.ds);
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public DiscountManager getDescuento() {
        return ds;
    }

    public void setDescuento(DiscountManager ds) {
        this.ds = ds;
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

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    /**
     * Versión serializada de un servicio
     * @return Cadena con los atributos separados por '@'
     */
    @Override
    public String toString() {
        return  capacidad + "@" +
                destino + "@" +
                ds + "@" +
                fecha + "@" +
                precioPorPersona + "@" +
                tipo;
    }
}
