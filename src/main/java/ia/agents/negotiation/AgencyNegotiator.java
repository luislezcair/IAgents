/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents.negotiation;

import ia.agents.ontology.ConsultarAction;
import ia.agents.ontology.Paquete;
import ia.agents.ontology.ServicioAgencia;
import jade.content.AgentAction;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.lang.acl.ACLMessage;
import jade.proto.SSIteratedContractNetResponder;

import java.util.Map;

/**
 * Tanto Lugar como Transporte negocian con las Agencias de manera similar,
 * entonces separamos en una clase el comportamiento común.
 * Se va a crear un Responder que maneje la conversación con una Agencia por
 * cada CFP recibido.
 */
public abstract class AgencyNegotiator extends SSIteratedContractNetResponder {
    private String cid;
    private Map<String, ? extends ServicioAgencia> ofertasPrevias;

    public AgencyNegotiator(Agent a, ACLMessage cfp,
                       Map<String, ? extends ServicioAgencia> ofertasPrevias) {
        super(a, cfp);
        this.ofertasPrevias = ofertasPrevias;
    }

    /**
     * Prepara la respuesta que se va a enviar a la Agencia con el servicio
     * correspondiente
     * @param p Paquete turístico para preparar el servicio
     * @return La acción que se va a enviar a la Agencia
     */
    protected abstract AgentAction prepareResponseAction(Paquete p);

    /**
     * Verifica si podemos responder a este paquete. Si devuelve true se
     * llama al método prepareResponseAction. En caso contrario se envía un
     * REFUSE.
     * @param p Paquete turístico
     * @return true si podemos satisfacer el transporte. false en caso contrario
     */
    protected abstract boolean canOfferService(Paquete p);

    protected String getConversationId() {
        return cid;
    }

    @Override
    protected ACLMessage handleCfp(ACLMessage cfp) {
        // Recibimos un CFP de una agencia
        Paquete p;
        try {
            Action a = (Action) myAgent.getContentManager().extractContent(cfp);
            ConsultarAction ca = (ConsultarAction) a.getAction();
            p = ca.getPaquete();
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

        ACLMessage reply = cfp.createReply();
        cid = cfp.getConversationId();

        // Si no podemos ofrecer el servicio enviamos Refuse y terminamos
        if(!canOfferService(p)) {
            reply.setPerformative(ACLMessage.REFUSE);
            return reply;
        }

        // Si podemos ofrecer el servicio creamos un PROPOSE
        reply.setPerformative(ACLMessage.PROPOSE);
        AgentAction agentAction = prepareResponseAction(p);

        try {
            myAgent.getContentManager().fillContent(reply,
                    new Action(myAgent.getAID(), agentAction));
        } catch(Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }

        return reply;
    }

    @Override
    protected ACLMessage handleAcceptProposal(ACLMessage cfp,
                                              ACLMessage propose,
                                              ACLMessage accept)
            throws FailureException {
        System.out.println(myAgent.getLocalName() + ": La agencia aceptó la " +
                "propuesta");

        if(ofertasPrevias.containsKey(cid))
            ofertasPrevias.remove(cid);

        // FIPA: un accept_proposal se responde con Inform o Failure.
        ACLMessage reply = accept.createReply();
        reply.setPerformative(ACLMessage.INFORM);
        return reply;
    }

    @Override
    protected void handleRejectProposal(ACLMessage cfp,
                                        ACLMessage propose,
                                        ACLMessage reject) {

        if(ofertasPrevias.containsKey(cid))
            ofertasPrevias.remove(cid);

        // FIPA: ante un reject termina la negociación.
        System.out.println(myAgent.getLocalName() + ": La agencia rechazó " +
                "la propuesta");
    }
}
