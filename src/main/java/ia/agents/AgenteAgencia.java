/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents;

import ia.agents.negotiation.BestOfferManager;
import ia.agents.negotiation.OfertaServicio;
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
    private final Codec slCodec = new SLCodec();
    private final Ontology ontology = TurismoOntology.getInstance();
    private final List<AID> servicios = new ArrayList<>();
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
        public ServicesSubscriber(Agent a, DFAgentDescription dfad, List<AID>
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
            // Si no tenemos ningún servicio asociado no podemos hacer nada
            if(servicios.isEmpty()) {
                sendRefuseMessage();
                return null;
            }

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

        /**
         * Acá se negocia con los servicios (lugares y transportes). La
         * agencia va a buscar el paquete más conveniente para el turista y
         * para ella misma. Esto es, va a devolver el primer paquete cuyo precio
         * por persona esté por debajo del precio máximo establecido por el
         * turista.
         */
        @Override
        @SuppressWarnings("unchecked")
        protected void handleAllResponses(Vector responses,
                                          Vector acceptances) {
            // Vector de mensajes CFP para otra iteración
            Vector<ACLMessage> nextCfps = new Vector<>();
            BestOfferManager ofertaActual = new BestOfferManager(paquete);
            boolean iteration = false;

            List<OfertaServicio> lugares = new ArrayList<>();
            List<OfertaServicio> transportes = new ArrayList<>();

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

                // Preguntar si recibimos una oferta de alojamiento o de
                // transporte y agregar cada una a la lista correspondiente
                Concept concept = action.getAction();
                if(concept instanceof OfertarLugarAction) {
                    OfertarLugarAction of = (OfertarLugarAction) concept;
                    Alojamiento aloj = of.getAlojamiento();
                    lugares.add(
                            new OfertaServicio(aloj, action.getActor()));
                    System.out.println("[AGENCIA] Propuesta recibida " +
                            "del LUGAR " + action.getActor());
                }
                else if(concept instanceof OfertarTransporteAction) {
                    OfertarTransporteAction of =
                            (OfertarTransporteAction) concept;
                    Transporte transp = of.getTransporte();
                    transportes.add(
                            new OfertaServicio(transp, action.getActor()));
                    System.out.println("[AGENCIA] Propuesta recibida " +
                            "del TRANSPORTE " + action.getActor());
                }
            }

            // Si no tenemos al menos un lugar y un transporte, no podemos
            // atender al turista
            if((!mejorOferta.isValidAlojamiento() && lugares.isEmpty()) ||
               (!mejorOferta.isValidTransporte() && transportes.isEmpty())) {
                sendRefuseMessage();
                return;
            }

            // Si recibimos una única oferta de lugar y no puede mejorar más,
            // la guardamos y seguimos negociando con los transportes.
            if(lugares.size() == 1 &&
               lugares.get(0).getServicio().isFinalOffer()) {
                mejorOferta.setMejorAlojamiento(lugares.get(0));
                lugares.remove(0);
            }

            // Si recibimos una única oferta de transporte y no puede mejorar
            // más, la guardamos y seguimos negociando con los lugares
            if(transportes.size() == 1 &&
               transportes.get(0).getServicio().isFinalOffer()) {
                mejorOferta.setMejorTransporte(transportes.get(0));
                transportes.remove(0);
            }

            // Si tenemos una oferta guardada, la ponemos como la oferta actual.
            // Si no, buscamos la mejor oferta de esta iteración.
            if(mejorOferta.isValidAlojamiento())
                ofertaActual.setMejorAlojamiento(mejorOferta);
            else
                ofertaActual.setMejorAlojamiento(getBest(lugares));

            if(mejorOferta.isValidTransporte())
                ofertaActual.setMejorTransporte(mejorOferta);
            else
                ofertaActual.setMejorTransporte(getBest(transportes));

            // Creamos una lista con las ofertas de todos los servicios
            List<OfertaServicio> ofertas = new ArrayList<>();
            ofertas.addAll(lugares);
            ofertas.addAll(transportes);

            if(ofertaActual.satisfacePaquete()) {
                // Si satisface las condiciones del paquete del turista,
                // aceptamos esta oferta.
                mejorOferta.setMejorAlojamiento(ofertaActual);
                mejorOferta.setMejorTransporte(ofertaActual);

                // Aceptamos este lugar y transporte
                ACLMessage accept = constructCfp();
                accept.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                accept.addReceiver(
                        mejorOferta.getOfertaAlojamiento().getAgente());
                accept.addReceiver(
                        mejorOferta.getOfertaTransporte().getAgente());
                acceptances.add(accept);

                // Los sacamos de la lista de ofertas
                ofertas.remove(mejorOferta.getOfertaAlojamiento());
                ofertas.remove(mejorOferta.getOfertaTransporte());

                // Y rechazamos el resto de las ofertas
                ofertas.forEach(x -> {
                    ACLMessage reject =
                            new ACLMessage(ACLMessage.REJECT_PROPOSAL);
                    reject.addReceiver(x.getAgente());
                    acceptances.add(reject);
                });
            } else {
                // Si las útlimas ofertas no sirvieron, las agregamos a la
                // lista de nuevo para que se rechacen.
                if(mejorOferta.isValidOffer()) {
                    ofertas.add(mejorOferta.getOfertaAlojamiento());
                    ofertas.add(mejorOferta.getOfertaTransporte());
                }

                // Analizamos las ofertas recibidas. Si es una oferta final la
                // rechazamos. Si todavía puede mejorar, le enviamos un nuevo
                // CFP.
                int performative;
                for(OfertaServicio s : ofertas) {
                    if(s.getServicio().isFinalOffer()) {
                        performative = ACLMessage.REJECT_PROPOSAL;
                    } else {
                        performative = ACLMessage.CFP;
                        if(!iteration) iteration = true;
                    }
                    ACLMessage msg = constructCfp();
                    msg.setPerformative(performative);
                    msg.addReceiver(s.getAgente());
                    nextCfps.add(msg);
                }
            }

            // Si aceptamos una oferta, terminamos
            if(!acceptances.isEmpty())
                return;

            // No aceptamos ninguna oferta, hacemos una nueva iteración.
            if(!nextCfps.isEmpty())
                newIteration(nextCfps);

            // Si rechazamos todas las ofertas (no hay ningún CFP para enviar)
            // enviamos Refuse al turista.
            if(!iteration)
                sendRefuseMessage();
        }

        /**
         * Busca la mejor oferta de una lista de ofertas de servicios
         */
        private OfertaServicio getBest(List<OfertaServicio> ofertas) {
            OfertaServicio mejor = null;
            for(OfertaServicio os : ofertas) {
                if(mejor == null ||
                   os.getServicio().isBetter(mejor.getServicio(), paquete)) {
                   mejor = os;
                }
            }
            return mejor;
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

            PaqueteAgencia pa = mejorOferta.getPaqueteAgencia();

            // Estos agentes dicen ser los mejores
            pa.setAgencia(getAID());
            pa.setAgenteLugar(mejorOferta.getOfertaAlojamiento().getAgente());
            pa.setAgenteTransporte(mejorOferta.getOfertaTransporte().getAgente());

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
