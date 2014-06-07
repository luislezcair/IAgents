/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents;

import ia.agents.negotiation.BestOfferManager;
import ia.agents.ontology.*;
import ia.agents.ui.UIAgencia;
import ia.agents.util.*;

import jade.content.Concept;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;
import jade.proto.ContractNetResponder;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class AgenteAgencia extends Agent {
    private Codec slCodec = new SLCodec();
    private Ontology ontology = TurismoOntology.getInstance();
    private List<AID> servicios = new ArrayList<>();
    private UIAgencia ui;

    @Override
    protected void setup() {
        getContentManager().registerLanguage(slCodec);
        getContentManager().registerOntology(ontology);

        DFRegisterer.register(this, "Agencia", null);

        subscribeToDf();

        TouristNegotiator touristNegotiator = new TouristNegotiator(this);
        touristNegotiator.registerHandleCfp(new TravelNegotiator(this));
        addBehaviour(touristNegotiator);

        // Crear y mostrar la interfaz
        ui = new UIAgencia(this);
    }

    @Override
    protected void takeDown() {
        DFRegisterer.deregister(this);
    }

    private void subscribeToDf() {
        DFAgentDescription dfad = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.addProperties(new Property("AgenciaAsociada", getName()));
        dfad.addServices(sd);

        addBehaviour(new ServicesSubscriber(this, dfad, servicios));
    }

    private class ServicesSubscriber extends DFAgentSubscriber {
        private ServicesSubscriber(Agent a, DFAgentDescription dfad, List<AID>
                subscribedAgents) {
            super(a, dfad, subscribedAgents);
        }

        @Override
        protected void onRegister(DFAgentDescription dfad) {
            ui.setServicios(servicios);
        }

        @Override
        protected void onDeregister(DFAgentDescription dfad) {
            ui.setServicios(servicios);
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
        private BestOfferManager mejorOferta;
        private Paquete paquete;
        private String cid;

        public TravelNegotiator(Agent a) {
            super(a, null);
        }

        /**
         * Contruye un mensaje Cfp con el paquete como contenido
         * @return Mensaje Cfp
         */
        private ACLMessage constructCfp() {
            ACLMessage cfp = new ACLMessage(ACLMessage.CFP);

            cfp.setOntology(ontology.getName());
            cfp.setLanguage(slCodec.getName());
            cfp.setProtocol(
                    FIPANames.InteractionProtocol.FIPA_ITERATED_CONTRACT_NET);
            cfp.setConversationId(cid);

            ConsultarAction consulta = new ConsultarAction();
            consulta.setPaquete(paquete);

            try {
                getContentManager().fillContent(cfp,
                        new Action(myAgent.getAID(), consulta));
            } catch(Exception e) {
                System.out.println("constructCfp() ERROR: " + e.getMessage());
            }
            return cfp;
        }

        @Override
        protected Vector prepareCfps(ACLMessage cfp) {
            // Obtenemos el CFP del turista, que contiene la información del
            // paquete.
            String key = ((ContractNetResponder) parent).CFP_KEY;
            cfp = (ACLMessage) getDataStore().get(key);

            Vector<ACLMessage> msgs = new Vector<>();

            // Extraemos del mensaje la descripción del paquete
            try {
                Action a = (Action) getContentManager().extractContent(cfp);
                ConsultarAction ca = (ConsultarAction) a.getAction();
                paquete = ca.getPaquete();
                mejorOferta = new BestOfferManager(paquete);
            } catch(Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                return msgs;
            }

            // Generamos un nuevo conversation-id para esta negociación
            cid = myAgent.getName() + System.currentTimeMillis() + "_" +
                    String.valueOf((int)(Math.random()*100.0));

            ACLMessage cfpServicios = constructCfp();

            // Agregamos todos los receptores del mesaje
            servicios.forEach(cfpServicios::addReceiver);

            msgs.add(cfpServicios);
            return msgs;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void handleAllResponses(Vector responses,
                                          Vector acceptances) {
            // Vector de mensajes CFP para otra iteración
            Vector<ACLMessage> nextCfps = new Vector<>();
            BestOfferManager ofertaActual = new BestOfferManager(paquete);
            boolean refuse = false;

            // La agencia recibe las ofertas de los lugares y transportes y
            // decide cuál es la mejor.
            for(Object obj : responses) {
                ACLMessage resp = (ACLMessage) obj;

                if(resp.getPerformative() != ACLMessage.PROPOSE) {
                    System.out.println(
                       "[AGENCIA] Rechazo recibido del Lugar o Transporte " +
                                    resp.getSender().getName());
                    continue;
                }

                Action action;
                try {
                    action = (Action) getContentManager().extractContent(resp);
                } catch(Exception e) {
                    e.printStackTrace();
                    continue;
                }

                int performative = ACLMessage.FAILURE;
                AID aid = action.getActor();

                // Tomamos la mejor oferta de la iteración anterior para
                // compararla con las que llegan en esta nueva iteración.
                if(mejorOferta.isValidAlojamiento())
                    ofertaActual.setMejorAlojamiento(mejorOferta);

                if(mejorOferta.isValidTransporte())
                    ofertaActual.setMejorTransporte(mejorOferta);

                // Preguntar si recibimos una oferta de alojamiento o de
                // transporte y procesar cada una.
                Concept concept = action.getAction();
                if(concept instanceof OfertarLugarAction) {
                    OfertarLugarAction of = (OfertarLugarAction) concept;
                    Alojamiento aloj = of.getAlojamiento();
                    System.out.println("[AGENCIA] Propuesta recibida " +
                            "del LUGAR " + action.getActor());

                    if(!ofertaActual.esMejor(aloj)) {
                        if(ofertaActual.isValidAlojamiento()) {
                            // Llegó un lugar mejor. El que era mejor deja de
                            // serlo y se le manda un reject si era una
                            // oferta final o un nuevo CFP para que la mejore
                            if(ofertaActual.getAlojamiento().isFinalOffer()) {
                                performative = ACLMessage.REJECT_PROPOSAL;
                            } else {
                                performative = ACLMessage.CFP;
                            }
                            aid = ofertaActual.getAgenteLugar();
                        }
                        ofertaActual.setMejor(aloj, action.getActor());
                    } else {
                        performative = ACLMessage.CFP;
                    }
                }
                else if(concept instanceof OfertarTransporteAction) {
                    OfertarTransporteAction of =
                            (OfertarTransporteAction) concept;
                    Transporte transp = of.getTransporte();
                    System.out.println("[AGENCIA] Propuesta recibida " +
                            "del TRANSPORTE " + action.getActor());

                    if(!ofertaActual.esMejor(transp)) {
                        if (ofertaActual.isValidTransporte()) {
                            // Llegó un transporte mejor. El que era mejor
                            // deja de serlo y se le manda un reject si era una
                            // oferta final o un nuevo CFP para que la mejore
                            if (ofertaActual.getTransporte().isFinalOffer()) {
                                performative = ACLMessage.REJECT_PROPOSAL;
                            } else {
                                performative = ACLMessage.CFP;
                            }
                            aid = ofertaActual.getAgenteTransporte();
                        }
                        ofertaActual.setMejor(transp, action.getActor());
                    } else {
                        performative = ACLMessage.CFP;
                    }
                }

                // Construye la repropuesta para volver a enviar a los servicios
                // Acá podría modificarse el paquete del turista
                if(performative != ACLMessage.FAILURE) {
                    ACLMessage cfp = constructCfp();
                    cfp.setPerformative(performative);
                    cfp.addReceiver(aid);
                    nextCfps.add(cfp);
                }
            }

            System.out.println("Mejor lugar: " + ofertaActual.getAlojamiento());
            System.out.println("Mejor transp: " + ofertaActual.getTransporte());

            if(!ofertaActual.isValidOffer()) {
                // No encontramos ningún lugar o ningún transporte que pueda
                // satisfacer el pedido del turista. Se lo rechaza.
                refuse = true;
            }
            else if(ofertaActual.satisfacePaquete()) {
                // El mejor lugar y el mejor transporte de esta tanda están
                // por debajo del precio máximo. Se acepta esta oferta.
                mejorOferta.setMejorAlojamiento(ofertaActual);
                mejorOferta.setMejorTransporte(ofertaActual);

                ACLMessage accept = constructCfp();
                accept.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                accept.addReceiver(mejorOferta.getAgenteLugar());
                accept.addReceiver(mejorOferta.getAgenteTransporte());
                acceptances.add(accept);

                System.out.println("Precio total: " + mejorOferta
                        .getPaqueteAgencia().getPrecioPorPersona());
            } else {
                // El mejor lugar y el mejor transporte superan el precio
                // máximo, así que volvemos a enviarles CFPs para que mejoren
                // su oferta.
                ACLMessage cfp = constructCfp();

                // Si es la oferta final del lugar, me quedo con este lugar y
                // no le enviamos nuevos CFPs.
                if(!ofertaActual.getAlojamiento().isFinalOffer())
                    cfp.addReceiver(ofertaActual.getAgenteLugar());
                else
                    mejorOferta.setMejorAlojamiento(ofertaActual);

                // Si es la oferta final del transporte, me quedo con este
                // transporte y no le enviamos nuevos CFPs
                if(!ofertaActual.getTransporte().isFinalOffer())
                    cfp.addReceiver(ofertaActual.getAgenteTransporte());
                else
                    mejorOferta.setMejorTransporte(ofertaActual);

                nextCfps.add(cfp);

                // Si fue la oferta final de ambos no puedo satisfacer el
                // pedido del turista. Rechazo los dos servicios y rechazo la
                // propuesta del turista.
                if(ofertaActual.getAlojamiento().isFinalOffer() &&
                        ofertaActual.getTransporte().isFinalOffer()) {
                    cfp.setPerformative(ACLMessage.REJECT_PROPOSAL);
                    cfp.addReceiver(ofertaActual.getAgenteTransporte());
                    cfp.addReceiver(ofertaActual.getAgenteLugar());
                    acceptances.add(cfp);

                    ofertaActual.setInvalid();
                    refuse = true;
                }
            }

            // Se rechazaron todas las propuestas o ninguna servía. Envia el
            // rechazo al turista
            if(refuse)
                sendRefuseMessage();

            // Enviar los accept-proposals o reject-proposals
            if(!acceptances.isEmpty())
                return;

            // Si hay candidatos se comienza una nueva iteración para que
            // mejoren sus ofertas.
            if(!nextCfps.isEmpty())
                newIteration(nextCfps);
        }

        /**
         * Coloca el mensaje REFUSE en el DataStore del ContractNetResponder
         * para que se lo envíe al turista.
         */
        private void sendRefuseMessage() {
            ACLMessage refuse = new ACLMessage(ACLMessage.REFUSE);
            String key = ((ContractNetResponder) parent).REPLY_KEY;
            getDataStore().put(key, refuse);
        }

        @Override
        protected void handleAllResultNotifications(Vector resultNotif) {
            // Recibimos inform o failure de algún Lugar o Transporte
            System.out.println("[AGENCIA] INFORM de algún Lugar o Transporte");

            // Creamos el mensaje PROPOSE
            ACLMessage propose = new ACLMessage(ACLMessage.PROPOSE);
            propose.setLanguage(slCodec.getName());
            propose.setOntology(ontology.getName());

            // Construimos la oferta que se envía al turista
            OfertarPaqueteAction of = new OfertarPaqueteAction();
            of.setPaqueteAgencia(mejorOferta.getPaqueteAgencia());

            try {
                getContentManager().fillContent(propose,
                        new Action(myAgent.getAID(), of));
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Informa al comportamiento padre, a través del DataStore, que
            // terminó la negociación con los lugares y transportes para que
            // pueda continuar la negociación con el turista.
            String key = ((ContractNetResponder) parent).PROPOSE_KEY;
            getDataStore().put(key, propose);

            mejorOferta.setInvalid();
        }
    }
}
