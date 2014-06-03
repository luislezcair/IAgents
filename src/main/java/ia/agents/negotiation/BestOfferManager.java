package ia.agents.negotiation;

import ia.agents.ontology.Paquete;
import ia.agents.ontology.ServicioAgencia;
import jade.core.AID;

/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */
public class BestOfferManager {
    private ServicioAgencia servicio;
    private AID mejorAgente;
    private boolean finalOffer;

    /**
     * Comprueba si el servico t es mejor que el servicio actual
     * @param otroServicio Lugar o Transporte con el que se compara
     * @return true si es mejor, false si no es mejor
     */
    public boolean isBetter(ServicioAgencia otroServicio, Paquete p) {
        if(servicio == null)
            return true;

        // TODO: analizar por mejor precio
        return getPrecio(otroServicio, p) < getPrecio(servicio, p);
    }

    public void setBetter(ServicioAgencia s) {
        servicio = s;
    }

    public boolean isFinalOffer() {
        return finalOffer;
    }

    public void setFinalOffer(boolean finalOffer) {
        this.finalOffer = finalOffer;
    }

    public ServicioAgencia getBetter() {
        return servicio;
    }

    public AID getMejorAgente() {
        return mejorAgente;
    }

    public void setMejorAgente(AID mejorAgente) {
        this.mejorAgente = mejorAgente;
    }

    public double getPrecio(ServicioAgencia s, Paquete p) {
        return s.getPrecioPorPersona()*p.getPersonas()*p.getDias()*
               (1-s.getDescuento().getValue());
    }

    public double getPrecioTotalPorPersona(ServicioAgencia otro, Paquete p) {
        return (getPrecio(otro, p) + getPrecio(servicio, p)) /
                (p.getDias() * p.getPersonas());
    }
}
