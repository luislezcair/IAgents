/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents;

//import ia.agents.ui.UIAgency;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetResponder;

import java.util.ArrayList;
import java.util.List;

public class AgenteAgencia extends Agent {
    //private UIAgency ui;

    @Override
    protected void setup() {
        // Crear y mostrar la interfaz
        /*
        ui = new UIAgency(this);
        ui.setupUi();*/

        // Registrarse en el servicio de páginas amarillas
        DFAgentDescription ad = new DFAgentDescription();
        ad.setName(getAID());

        ServiceDescription sd = new ServiceDescription();
        sd.setType("Agencia");
        sd.setName("Agencia-" + getLocalName());

        ad.addServices(sd);

        try {
            DFService.register(this, ad);
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        List<AID> lugares = getAgentesAsociados("Lugar");
        List<AID> transportes = getAgentesAsociados(("Transporte"));

        addBehaviour(new TouristNegotiator());
    }

    protected void takeDown() {
        // Deregistrarse del servicio de páginas amarillas
        try {
            DFService.deregister(this);
        } catch(FIPAException e) {
            //e.printStackTrace();
        }
    }

    /**
     * Consulta el DF para obtener los agentes asociados con esta agencia
     * @param agentType Tipo de agente a buscar
     * @return Lista con los agentes encontrados
     */
    private List<AID> getAgentesAsociados(String agentType) {
        // TODO: este método debería servir para obtener los lugares y
        // transportes asociados cuando se recibe una petición del turista
        // y hay que buscar los mejores paquetes. Hay que moverlo a un
        // Behaviour cuando esté implementada la comunicación entre agentes.
        List<AID> resultList = new ArrayList<AID>();

        // Buscamos agentes en el DF
        DFAgentDescription ad = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();

        sd.setType(agentType);
        sd.addProperties(new Property("AgenciaAsociada", getLocalName()));
        ad.addServices(sd);

        try {
            DFAgentDescription[] result = DFService.search(this, ad);
            for(DFAgentDescription dfagent : result) {
                resultList.add(dfagent.getName());
            }
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        return resultList;
    }

    /**
     * Implementación de un contract-net para manejar la comunicación con
     * turistas.
     */
    private class TouristNegotiator extends ContractNetResponder {
        public TouristNegotiator() {
            super(null, null);
        }

        @Override
        protected ACLMessage handleCfp(ACLMessage cfp) {
            // Recibimos un CFP de un agente turista, respondemos con las
            // ofertas
            ACLMessage reply = cfp.createReply();

            // TODO: usar ontología
            String s = cfp.getContent();

            // Chequeo cualquiera
            if(!s.isEmpty()) {
                reply.setPerformative(ACLMessage.PROPOSE);
            } else {
                reply.setPerformative(ACLMessage.REFUSE);
            }

            return reply;
        }

        @Override
        protected ACLMessage handleAcceptProposal(ACLMessage cfp,
                                                  ACLMessage propose,
                                                  ACLMessage accept)
                                        throws FailureException {
            System.out.println("[AGENCIA] El turista " +
                               accept.getSender().getName() +
                               " aceptó la oferta");

            // FIPA: ante un accept proposal, se realiza una acción y se
            // responde Inform si se realizó con éxito o failure si falló
            ACLMessage reply = accept.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            return reply;
        }

        @Override
        protected void handleRejectProposal(ACLMessage cfp,
                                            ACLMessage propose,
                                            ACLMessage reject) {

            // El turista rechazó la oferta de esta agencia.
            System.out.println("[AGENCIA] El turista rechazó la oferta");
        }
    }
}
