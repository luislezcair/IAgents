/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents.negotiation;

import ia.agents.ontology.ServicioAgencia;
import jade.core.AID;

public class OfertaServicio {
    private ServicioAgencia servicio;
    private AID agente;

    public OfertaServicio(ServicioAgencia s, AID agent) {
        servicio = s;
        agente = agent;
    }

    public ServicioAgencia getServicio() {
        return servicio;
    }

    public void setServicio(ServicioAgencia servicio) {
        this.servicio = servicio;
    }

    public AID getAgente() {
        return agente;
    }

    public void setAgente(AID agente) {
        this.agente = agente;
    }
}
