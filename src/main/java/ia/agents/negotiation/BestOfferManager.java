/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents.negotiation;

import ia.agents.ontology.*;
import jade.core.AID;

public class BestOfferManager {
    private PaqueteAgencia pa = new PaqueteAgencia();
    private AID agenteLugar;
    private AID agenteTransporte;

    private boolean validTransporte = false;
    private boolean validAlojamiento = false;

    public BestOfferManager(Paquete p) {
        pa.setPaquete(p);
    }

    public void setMejor(Alojamiento a, AID mejorAgente) {
        pa.setAlojamiento(a);
        agenteLugar = mejorAgente;
        validAlojamiento = true;
    }

    public void setMejor(Transporte t, AID mejorAgente) {
        pa.setTransporte(t);
        agenteTransporte = mejorAgente;
        validTransporte = true;
    }

    public void setMejorAlojamiento(BestOfferManager oferta) {
        pa.setAlojamiento(oferta.getAlojamiento());
        agenteLugar = oferta.getAgenteLugar();
        validAlojamiento = true;
    }

    public void setMejorTransporte(BestOfferManager oferta) {
        pa.setTransporte(oferta.getTransporte());
        agenteTransporte = oferta.getAgenteTransporte();
        validTransporte = true;
    }

    public boolean esMejor(Alojamiento a) {
        return validAlojamiento &&
                pa.getAlojamiento().isBetter(a, pa.getPaquete());
    }

    public boolean esMejor(Transporte t) {
        return validTransporte &&
                pa.getTransporte().isBetter(t, pa.getPaquete());
    }

    public AID getAgenteLugar() {
        return agenteLugar;
    }

    public AID getAgenteTransporte() {
        return agenteTransporte;
    }

    public boolean isValidTransporte() {
        return validTransporte;
    }

    public boolean isValidAlojamiento() {
        return validAlojamiento;
    }

    /**
     * Hace que esta oferta sea inválida a partir de ahora.
     */
    public void setInvalid() {
        validAlojamiento = validTransporte = false;
    }

    /**
     * @return Devuelve true si esta oferta es válida. Una oferta es válida si
     * tiene un lugar y un transporte. Si faltan uno o los dos, ya no es válida.
     */
    public boolean isValidOffer() {
        return validTransporte && validAlojamiento;
    }

    /**
     * @return Devuelve true si la oferta satisface las condiciones del paquete
     */
    public boolean satisfacePaquete() {
        return pa.getPrecioPorPersona() <
                pa.getPaquete().getImporteMaxPorPersona();
    }

    /**
     * @return Devuelve el Paquete de esta oferta (Lugar y Transporte).
     */
    public PaqueteAgencia getPaqueteAgencia() {
        return pa;
    }

    /**
     * Método de conveniencia para acortar las llamadas
     * @return Alojamiento de esta oferta
     */
    public Alojamiento getAlojamiento() {
        return pa.getAlojamiento();
    }

    /**
     * Método de conveniencia para acortar las llamadas
     * @return Transporte de esta oferta
     */
    public Transporte getTransporte() {
        return pa.getTransporte();
    }
}
