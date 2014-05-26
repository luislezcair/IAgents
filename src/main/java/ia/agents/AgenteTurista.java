/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents;

import ia.agents.ui.UITourist;
import ia.agents.util.DFAgentSubscriber;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class AgenteTurista extends Agent {
    private List<AID> agencias;
    private Paquete paquete;
    private UITourist ui;

    @Override
    protected void setup() {
        agencias = new ArrayList<>();
        paquete = new Paquete();

        subscribeToDf();

        ui = new UITourist(this);
        ui.show();
    }

    private void subscribeToDf() {
        DFAgentDescription dfad = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Agencia");
        dfad.addServices(sd);

        addBehaviour(new DFAgentSubscriber(this, dfad, agencias));
    }

    /**
     * Inicia la negociación con las agencias
     */
    public void sendCfp() {
        addBehaviour(new PackageNegotiator(this));
    }

    /**
     * Método utilizado en la UI para rellenar los datos del paquete
     * @return El paquete turístico de este turista
     */
    public Paquete getPaquete() {
        return paquete;
    }

    /**
     * Implementación de un contract-net para manejar la interacción con las
     * agencias.
     */
    private class PackageNegotiator extends ContractNetInitiator {
        public PackageNegotiator(Agent a) {
            super(a, null);
        }

        @Override
        protected Vector prepareCfps(ACLMessage cfp) {
            cfp = new ACLMessage(ACLMessage.CFP);

            for(AID aid : agencias) {
                cfp.addReceiver(aid);
            }

            // TODO: Usar ontología
            cfp.setContent(paquete.toString());
            cfp.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);

            Vector<ACLMessage> v = new Vector<>();
            v.add(cfp);
            return v;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void handleAllResponses(Vector responses,
                                          Vector acceptances) {
            // El turista decide el mejor entre todos los paquetes
            // recibidos de las agencias.
            for(Object obj : responses) {
                ACLMessage resp = (ACLMessage) obj;
                if(resp.getPerformative() == ACLMessage.PROPOSE) {
                    // Procesar la respuesta recibida de una agencia,
                    // decidir cuál es la mejor.
                    System.out.println(
                            "[TURISTA] Propuesta recibida de la agencia " +
                                    resp.getSender().getName());

                    // Si nos conviene este paquete, respondemos accept
                    ACLMessage accept = resp.createReply();
                    accept.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                    acceptances.add(accept);
                } else {
                    System.out.println(
                            "[TURISTA] Rechazo recibido de la agencia " +
                                        resp.getSender().getName());
                }
            }
        }
    }
}
