/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

import java.util.Vector;

@SuppressWarnings("unused")
public class AgenteTurista extends Agent {
    private Vector agencias;

    @Override
    protected void setup() {
        agencias = new Vector();

        // Behaviour para obtener las agencias registradas
        // en las páginas amarillas cada 60 segundos.
        addBehaviour(new TickerBehaviour(this, 6000) {
            @Override
            protected void onTick() {
                // Obtener la lista de agencias
                DFAgentDescription ad = new DFAgentDescription();
                ServiceDescription sd = new ServiceDescription();
                sd.setType("Agencia");
                ad.addServices(sd);

                try {
                    DFAgentDescription[] result = DFService.search(myAgent, ad);
                    agencias.clear();
                    for(int i = 0; i < result.length; i++) {
                        agencias.addElement(result[i].getName());
                    }
                } catch (FIPAException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
