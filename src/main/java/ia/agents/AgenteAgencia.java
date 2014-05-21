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
import jade.proto.ContractNetInitiator;
import jade.proto.ContractNetResponder;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class AgenteAgencia extends Agent {
    //private UIAgency ui;
    private List<AID> lugares;
    private List<AID> transportes;

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

        lugares = getAgentesAsociados("Lugar");
        transportes = getAgentesAsociados(("Transporte"));

        TouristNegotiator touristNegotiator = new TouristNegotiator();
        touristNegotiator.registerHandleCfp(new TravelNegotiator());
        addBehaviour(touristNegotiator);
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

    /**
     * Implementación de un contract-net para manejar la comunicación con
     * lugares y transportes
     */
    private class TravelNegotiator extends ContractNetInitiator {
        public TravelNegotiator() {
            super(null, null);
        }

        @Override
        protected Vector prepareCfps(ACLMessage cfp) {
            ACLMessage cfpLugares = new ACLMessage(ACLMessage.CFP);
            ACLMessage cfpTransportes = new ACLMessage(ACLMessage.CFP);

            for(AID lugar : lugares) {
                cfpLugares.addReceiver(lugar);
            }

            for(AID transp : transportes) {
                cfpTransportes.addReceiver(transp);
            }

            cfpLugares.setContent("Hola lugares");
            cfpTransportes.setContent("Hola transportes");

            Vector<ACLMessage> msgs = new Vector<ACLMessage>();
            msgs.add(cfpLugares);
            msgs.add(cfpTransportes);
            return msgs;
        }

        @Override
        protected void handleAllResponses(Vector responses,
                                          Vector acceptances) {
            // La agencia recibe las ofertas de los lugares y transportes y
            // decide cuál es la mejor.
            for(Object obj : responses) {
                ACLMessage resp = (ACLMessage) obj;
                if(resp.getPerformative() == ACLMessage.PROPOSE) {
                    // Preguntar si recibimos una oferta de alojamiento o de
                    // transporte y procesar cada una.
                    System.out.println(
                            "[AGENCIA] Propuesta recibida del Lugar o " +
                            "Transporte " + resp.getSender().getName());

                    // Si nos convence, aceptamos
                    ACLMessage accept = resp.createReply();
                    accept.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                    acceptances.add(accept);
                } else {
                    System.out.println(
                            "[AGENCIA] Rechazo recibido del Lugar o " +
                            "Transporte " + resp.getSender().getName());
                }
            }
        }

        @Override
        protected void handleAllResultNotifications(Vector resultNotif) {
            // Recibimos inform o failure de algún Lugar o Transporte
            System.out.println("[AGENCIA] INFORM de algún Lugar o Transporte");

            // Acá se construye la oferta que se envía al turista
            ACLMessage propose = new ACLMessage(ACLMessage.PROPOSE);
            propose.setContent("Hotel en Hawai y viaje en avión");

            // Informa al comportamiento padre, a través del DataStore, que
            // terminó la negociación con los lugares y transportes para que
            // pueda continuar la negociación con el turista.
            // THIS CODE WORKS, I HAVE NO IDEA WHY
            String key = ((ContractNetResponder) parent).PROPOSE_KEY;
            getDataStore().put(key, propose);
        }
    }
}
