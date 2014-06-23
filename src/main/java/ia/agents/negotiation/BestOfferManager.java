/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents.negotiation;

import ia.agents.ontology.*;

public class BestOfferManager {
    private final PaqueteAgencia pa = new PaqueteAgencia();
    private OfertaServicio transporte;
    private OfertaServicio alojamiento;
    private boolean validTransporte = false;
    private boolean validAlojamiento = false;

    public BestOfferManager(Paquete p) {
        pa.setPaquete(p);
    }

    public void setMejorAlojamiento(BestOfferManager oferta) {
        setMejorAlojamiento(oferta.getOfertaAlojamiento());
    }

    public void setMejorTransporte(BestOfferManager oferta) {
        setMejorTransporte(oferta.getOfertaTransporte());
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

    public void setMejorAlojamiento(OfertaServicio aloj) {
        alojamiento = aloj;
        pa.setAlojamiento((Alojamiento)aloj.getServicio());
        validAlojamiento = true;
    }

    public void setMejorTransporte(OfertaServicio transp) {
        transporte = transp;
        pa.setTransporte((Transporte)transp.getServicio());
        validTransporte = true;
    }

    public OfertaServicio getOfertaAlojamiento() {
        return alojamiento;
    }

    public OfertaServicio getOfertaTransporte() {
        return transporte;
    }
}
