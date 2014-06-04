/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents.ontology;

import jade.content.AgentAction;

/**
 * Acción que realiza la Agencia al ofertar un paquete al Turista,
 * compuesto por el Lugar y Transporte elegidos.
 */
public class OfertarPaqueteAction implements AgentAction {
    private Paquete paquete;
    private PaqueteAgencia paqueteAgencia;

    public Paquete getPaquete() {
        return paquete;
    }

    public void setPaquete(Paquete paquete) {
        this.paquete = paquete;
    }

    public PaqueteAgencia getPaqueteAgencia() {
        return paqueteAgencia;
    }

    public void setPaqueteAgencia(PaqueteAgencia paqueteAgencia) {
        this.paqueteAgencia = paqueteAgencia;
    }
}
