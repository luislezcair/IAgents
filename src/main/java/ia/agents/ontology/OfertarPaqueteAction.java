/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents.ontology;

import jade.content.AgentAction;

import java.util.List;

public class OfertarPaqueteAction implements AgentAction {
    private Paquete paquete;
    private List<PaqueteAgencia> paqueteAgencias;

    public Paquete getPaquete() {
        return paquete;
    }

    public void setPaquete(Paquete paquete) {
        this.paquete = paquete;
    }

    public List<PaqueteAgencia> getPaqueteAgencias() {
        return paqueteAgencias;
    }

    public void setPaqueteAgencias(List<PaqueteAgencia> paqueteAgencias) {
        this.paqueteAgencias = paqueteAgencias;
    }
}
