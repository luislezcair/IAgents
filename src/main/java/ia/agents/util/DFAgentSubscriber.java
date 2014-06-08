/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents.util;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;
import jade.util.leap.Iterator;

import java.util.List;

/**
 * Clase para suscribirse al servicio DF para ser notificado cuando aparece
 * algún agente de interés para el suscriptor y mantener una lista
 * actualizada de estos agentes.
 */
public class DFAgentSubscriber extends SubscriptionInitiator {
    private final List<AID> agents;

    public DFAgentSubscriber(Agent a, DFAgentDescription dfad,
                             List<AID> subscribedAgents) {
        super(a, DFService.createSubscriptionMessage(a, a.getDefaultDF(),
                                                     dfad, null));
        agents = subscribedAgents;
    }

    protected void onRegister(DFAgentDescription dfad) { }

    protected void onDeregister(DFAgentDescription dfad) { }

    @Override
    protected void handleInform(ACLMessage inform) {
        try {
            DFAgentDescription[] dfds =
                    DFService.decodeNotification(inform.getContent());

            for(DFAgentDescription dfad : dfds) {
                Iterator services = dfad.getAllServices();
                if (services.hasNext()) {
                    agents.add(dfad.getName());
                    onRegister(dfad);
                } else {
                    agents.remove(dfad.getName());
                    onDeregister(dfad);
                }
            }
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }
}
