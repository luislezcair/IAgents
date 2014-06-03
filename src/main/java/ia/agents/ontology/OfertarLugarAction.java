/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents.ontology;

import jade.content.AgentAction;

public class OfertarLugarAction implements AgentAction {
    private Alojamiento alojamiento;
    private boolean finalOffer;

    public boolean isFinalOffer() {
        return finalOffer;
    }

    public void setFinalOffer(boolean finalOffer) {
        this.finalOffer = finalOffer;
    }

    public Alojamiento getAlojamiento() {
        return alojamiento;
    }

    public void setAlojamiento(Alojamiento alojamiento) {
        this.alojamiento = alojamiento;
    }
}
