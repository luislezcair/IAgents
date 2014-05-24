/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents.util;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;

/**
 * Clase con métodos estáticos para facilitar el registro y deregistro de
 * agentes en el DF.
 */
public class DFRegisterer {
    public static void register(Agent agent, String serviceType,
                            Property property) {
        agent.addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                DFAgentDescription ad = new DFAgentDescription();
                ad.setName(agent.getAID());

                ServiceDescription sd = new ServiceDescription();
                sd.setType(serviceType);
                sd.setName(serviceType + "-" + agent.getLocalName());

                if(property != null)
                    sd.addProperties(property);

                ad.addServices(sd);

                try {
                    DFService.register(agent, ad);
                } catch (FIPAException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void deregister(Agent agent) {
        try {
            DFService.deregister(agent);
        } catch (FIPAException e) {
            System.out.println(agent.getLocalName() +
                    " no pudo deregistrarse del DF");
        }
    }
}
