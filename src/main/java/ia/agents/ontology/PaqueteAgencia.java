/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents.ontology;

import jade.content.Concept;
import jade.core.AID;

/**
 * Un paquete turístico compuesto por un Lugar de alojamiento y un Transporte
 */
public class PaqueteAgencia implements Concept {
    private Alojamiento alojamiento;
    private Transporte transporte;
    private Paquete paquete;
    private AID agencia;

    public Alojamiento getAlojamiento() {
        return alojamiento;
    }

    public void setAlojamiento(Alojamiento alojamiento) {
        this.alojamiento = alojamiento;
    }

    public Transporte getTransporte() {
        return transporte;
    }

    public void setTransporte(Transporte transporte) {
        this.transporte = transporte;
    }

    public Paquete getPaquete() {
        return paquete;
    }

    public void setPaquete(Paquete paquete) {
        this.paquete = paquete;
    }

    public AID getAgencia() {
        return agencia;
    }

    public void setAgencia(AID agencia) {
        this.agencia = agencia;
    }

    public double getPrecioTotal() {
        return alojamiento.getPrecio(paquete) + transporte.getPrecio(paquete);
    }

    public double getPrecioPorPersona() {
        return getPrecioTotal() / (paquete.getPersonas() * paquete.getDias());
    }

    public boolean isBetter(PaqueteAgencia otro) {
        return getPrecioTotal() < otro.getPrecioTotal();
    }

    public int compareTo(PaqueteAgencia otro) {
        return isBetter(otro) ? -1 : 1;
    }

    @Override
    public String toString() {
        return alojamiento.toString() + "\n" +
               transporte.toString() + "\n" +
               paquete.toString();
    }
}
