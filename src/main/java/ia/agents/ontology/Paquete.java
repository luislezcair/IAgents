/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents.ontology;

import jade.content.Concept;

import java.util.Date;

/**
 * Clase que epresenta a un paquete de viaje
 */
public class Paquete implements Concept {
    private String destino;
    private int dias;
    private Date fecha;
    private int formaDePago;
    private double importeMaxPorPersona;
    private int personas;

    private static final String[] formasDePago = {"Efectivo", "Tarjeta"};
    public static final int PAGO_EFECTIVO = 0;
    public static final int PAGO_TARJETA = 1;

    public Paquete() {
        fecha = new Date();
        dias = personas = 1;
        importeMaxPorPersona = 100.0;
    }

    public Paquete(String destino, int dias, Date fecha, int formaDePago,
                   double importeMaxPorPersona, int personas) {
        this.destino = destino;
        this.dias = dias;
        this.fecha = fecha;
        this.formaDePago = formaDePago;
        this.importeMaxPorPersona = importeMaxPorPersona;
        this.personas = personas;
    }

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
        int last = formasDePago.length - 1;
        if(formaDePago > last)
            this.formaDePago = last;
        else
            this.formaDePago = formaDePago;
    }

    public static String[] getFormasDePago() {
        return formasDePago;
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
        return "Paquete: " +
               destino + "@" +
               dias + "@" +
               fecha + "@" +
               formaDePago + "@" +
               importeMaxPorPersona + "@" +
               personas;
    }
}