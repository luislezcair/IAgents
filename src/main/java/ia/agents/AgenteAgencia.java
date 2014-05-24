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
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;
import jade.proto.ContractNetResponder;
import jade.proto.SubscriptionInitiator;
import jade.util.leap.Iterator;

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

        lugares = new ArrayList<>();
        transportes = new ArrayList<>();
        subscribeToDf();

        TouristNegotiator touristNegotiator = new TouristNegotiator(this);
        touristNegotiator.registerHandleCfp(new TravelNegotiator(this));
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

    private void subscribeToDf() {
        DFAgentDescription ad = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.addProperties(new Property("AgenciaAsociada", getLocalName()));
        ad.addServices(sd);

        ACLMessage s = DFService.createSubscriptionMessage(this, getDefaultDF(),
                ad, null);

        addBehaviour(new DFSubscription(this, s));
    }

    /**
     * Suscripción al servicio DF para ser notificado cuando aparece algún
     * agente Lugar o Transporte que esté asociado con esta agencia y
     * agregarlo a la lista correspondiente.
     */
    private class DFSubscription extends SubscriptionInitiator {
        public DFSubscription(Agent a, ACLMessage msg) {
            super(a, msg);
        }

        @Override
        protected void handleInform(ACLMessage inform) {
            try {
                DFAgentDescription[] dfds =
                        DFService.decodeNotification(inform.getContent());
                
                // Por cada agente, examinamos el tipo de servicio y lo
                // agregamos a la lista correspondiente.
                for(DFAgentDescription dfa : dfds) {
                    AID aid = dfa.getName();
                    Iterator it = dfa.getAllServices();
                    while(it.hasNext()) {
                        ServiceDescription sd = (ServiceDescription) it.next();
                        if(sd.getType().equals("Lugar")) {
                            lugares.add(aid);
                        } else if(sd.getType().equals("Transporte")) {
                            transportes.add(aid);
                        }
                    }
                }
            } catch (FIPAException fe) {
                fe.printStackTrace();
            }
        }
    }

    /**
     * Implementación de un contract-net para manejar la comunicación con
     * turistas.
     */
    private class TouristNegotiator extends ContractNetResponder {
        public TouristNegotiator(Agent agent) {
            super(agent, createMessageTemplate(
                    FIPANames.InteractionProtocol.FIPA_CONTRACT_NET));
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
        public TravelNegotiator(Agent a) {
            super(a, null);
        }

        @Override
        protected Vector prepareCfps(ACLMessage cfp) {
            // Obtenemos el CFP del turista, que contiene la información del
            // paquete.
            String key = ((ContractNetResponder) parent).CFP_KEY;
            cfp = (ACLMessage) getDataStore().get(key);

            System.out.println("Paquete: " + cfp.getContent());

            ACLMessage cfpLugares = new ACLMessage(ACLMessage.CFP);
            ACLMessage cfpTransportes = new ACLMessage(ACLMessage.CFP);

            lugares.forEach(cfpLugares::addReceiver);
            transportes.forEach(cfpTransportes::addReceiver);

            cfpLugares.setContent("Hola lugares");
            cfpTransportes.setContent("Hola transportes");

            cfpLugares.setProtocol(
                    FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
            cfpTransportes.setProtocol(
                    FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);

            Vector<ACLMessage> msgs = new Vector<>();
            msgs.add(cfpLugares);
            msgs.add(cfpTransportes);
            return msgs;
        }

        @Override
        @SuppressWarnings("unchecked")
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
            String key = ((ContractNetResponder) parent).PROPOSE_KEY;
            getDataStore().put(key, propose);
        }
    }
}
