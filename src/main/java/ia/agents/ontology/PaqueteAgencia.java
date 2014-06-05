/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents.ontology;

import jade.content.Concept;

/**
 * Un paquete turístico compuesto por un Lugar de alojamiento y un Transporte
 */
public class PaqueteAgencia implements Concept {
    private Alojamiento alojamiento;
    private Transporte transporte;
    private Paquete paquete;

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

    public double getPrecioTotal() {
        return alojamiento.getPrecio(paquete) + transporte.getPrecio(paquete);
    }

    public double getPrecioPorPersona() {
        return getPrecioTotal() / (paquete.getPersonas() * paquete.getDias());
    }

    public boolean isBetter(PaqueteAgencia otro) {
        return getPrecioTotal() < otro.getPrecioTotal();
    }

    @Override
    public String toString() {
        return alojamiento.toString() + "\n" +
               transporte.toString() + "\n" +
               paquete.toString();
    }
}
