/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents;

import jade.core.Agent;

@SuppressWarnings("unused")
public class AgenteTurista extends Agent {
    @Override
    protected void setup() {
        System.out.println("Hola, soy el agente turista "
                + getLocalName() + " y funciono de maravillas");
        System.out.println("GUID: " + getName());
    }
}
