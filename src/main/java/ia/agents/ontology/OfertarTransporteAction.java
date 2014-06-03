/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents.ontology;

import jade.content.AgentAction;

public class OfertarTransporteAction implements AgentAction {
    private Transporte transporte;
    private boolean finalOffer;

    public boolean isFinalOffer() {
        return finalOffer;
    }

    public void setFinalOffer(boolean finalOffer) {
        this.finalOffer = finalOffer;
    }

    public Transporte getTransporte() {
        return transporte;
    }

    public void setTransporte(Transporte transporte) {
        this.transporte = transporte;
    }
}
