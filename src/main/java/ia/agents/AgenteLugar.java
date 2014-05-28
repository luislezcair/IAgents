/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents;

import ia.agents.ontology.*;
import ia.agents.util.DFRegisterer;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetResponder;

@SuppressWarnings("unused")
public class AgenteLugar extends Agent {
    private Codec slCodec = new SLCodec();
    private Ontology ontology = TurismoOntology.getInstance();

    @Override
    protected void setup() {
        // TODO: Crear y mostrar la interfaz

        getContentManager().registerLanguage(slCodec);
        getContentManager().registerOntology(ontology);

        DFRegisterer.register(this, "Lugar",
                              new Property("AgenciaAsociada", getAgencia()));

        addBehaviour(new AgencyNegotiator(this));
    }

    @Override
    protected void takeDown() {
        DFRegisterer.deregister(this);
    }

    /**
     * Obtiene el nombre de la agencia de la lista de argumentos
     * @return Nombre de la agencia
     */
    private String getAgencia() {
        Object[] args = getArguments();
        if(args == null || args.length < 1) {
            return "";
        }
        return args[0].toString();
    }

    /** Implementación de un contract-net para manejar la interacción con las
     * agencias.
     */
    private class AgencyNegotiator extends ContractNetResponder {
        public AgencyNegotiator(Agent a) {
            super(a, createMessageTemplate(
                    FIPANames.InteractionProtocol.FIPA_CONTRACT_NET));
        }

        @Override
        protected ACLMessage handleCfp(ACLMessage cfp) {
            // Recibimos un CFP de una agencia.
            Paquete p = new Paquete();
            try {
                Action a = (Action) getContentManager().extractContent(cfp);
                ConsultarAction ca = (ConsultarAction) a.getAction();
                p = ca.getPaquete();
            } catch(Exception e) {
                System.out.println(e.getMessage());
                return null;
            }

            // TODO: Analizar paquete

            // Creamos la respuesta
            ACLMessage reply = cfp.createReply();
            reply.setPerformative(ACLMessage.PROPOSE);

            Alojamiento a = new Alojamiento();
            OfertarLugarAction of = new OfertarLugarAction();
            of.setAlojamiento(a);

            try {
                getContentManager().fillContent(reply,
                        new Action(myAgent.getAID(), of));
            } catch(Exception e) {
                System.out.println(e.getMessage());
                return null;
            }

            return reply;
        }

        @Override
        protected ACLMessage handleAcceptProposal(ACLMessage cfp,
                                                  ACLMessage propose,
                                                  ACLMessage accept)
                                        throws FailureException {
            System.out.println("[LUGAR] La agencia aceptó la propuesta");

            // FIPA: un accept_proposal se responde con Inform o Failure.
            ACLMessage reply = accept.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            return reply;
        }

        @Override
        protected void handleRejectProposal(ACLMessage cfp,
                                            ACLMessage propose,
                                            ACLMessage reject) {

            // FIPA: ante un reject termina la negociación.
            System.out.println("[LUGAR] La agencia rechazó la propuesta");
        }
    }
}
