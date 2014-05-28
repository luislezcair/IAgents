/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents;

import ia.agents.ontology.ConsultarAction;
import ia.agents.ontology.Paquete;
import ia.agents.ontology.TurismoOntology;
import ia.agents.ui.UITourist;
import ia.agents.util.DFAgentSubscriber;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.basic.Action;
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
    private Codec slCodec = new SLCodec();
    private Ontology ontology = TurismoOntology.getInstance();

    private List<AID> agencias = new ArrayList<>();
    private UITourist ui;

    @Override
    protected void setup() {
        getContentManager().registerLanguage(slCodec);
        getContentManager().registerOntology(ontology);

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

    @Override
    protected void takeDown() {
        ui.dispose();
    }

    /**
     * Inicia la negociación con las agencias
     */
    public void sendCfp(Paquete p) {
        addBehaviour(new PackageNegotiator(this, p));
    }

    /**
     * Implementación de un contract-net para manejar la interacción con las
     * agencias.
     */
    private class PackageNegotiator extends ContractNetInitiator {
        private Paquete paquete;

        public PackageNegotiator(Agent a, Paquete p) {
            super(a, null);
            paquete = p;
        }

        @Override
        protected Vector prepareCfps(ACLMessage cfp) {
            cfp = new ACLMessage(ACLMessage.CFP);

            for(AID aid : agencias) {
                cfp.addReceiver(aid);
            }

            cfp.setLanguage(slCodec.getName());
            cfp.setOntology(ontology.getName());
            cfp.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);

            ConsultarAction ca = new ConsultarAction();
            ca.setPaquete(paquete);

            // Hay que "envolver" el AgentAction Consultar en un Action
            Action action = new Action(myAgent.getAID(), ca);

            try {
                getContentManager().fillContent(cfp, action);
            } catch(Exception oe) {
                System.out.println("[TURISTA]: " + oe.getMessage());
            }

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
