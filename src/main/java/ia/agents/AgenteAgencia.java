/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents;

//import ia.agents.ui.UIAgency;
import ia.agents.ontology.*;
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
    //private UIAgency ui;
    private Codec slCodec = new SLCodec();
    private Ontology ontology = TurismoOntology.getInstance();
    private List<AID> servicios = new ArrayList<>();

    @Override
    protected void setup() {
        // Crear y mostrar la interfaz
        /*
        ui = new UIAgency(this);
        ui.setupUi();*/
        getContentManager().registerLanguage(slCodec);
        getContentManager().registerOntology(ontology);

        DFRegisterer.register(this, "Agencia", null);

        subscribeToDf();

        TouristNegotiator touristNegotiator = new TouristNegotiator(this);
        touristNegotiator.registerHandleCfp(new TravelNegotiator(this));
        addBehaviour(touristNegotiator);
    }

    @Override
    protected void takeDown() {
        DFRegisterer.deregister(this);
    }

    private void subscribeToDf() {
        DFAgentDescription dfad = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.addProperties(new Property("AgenciaAsociada", getLocalName()));
        dfad.addServices(sd);

        addBehaviour(new DFAgentSubscriber(this, dfad, servicios));
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

            Paquete p = new Paquete();
            try {
                // Extraemos del mensaje la descripción del paquete
                Action a = (Action) getContentManager().extractContent(cfp);
                ConsultarAction ca = (ConsultarAction) a.getAction();
                p = ca.getPaquete();
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }

            ACLMessage cfpServicios = new ACLMessage(ACLMessage.CFP);
            servicios.forEach(cfpServicios::addReceiver);
            cfpServicios.setOntology(ontology.getName());
            cfpServicios.setLanguage(slCodec.getName());
            cfpServicios.setProtocol(
                    FIPANames.InteractionProtocol.FIPA_ITERATED_CONTRACT_NET);

            ConsultarAction consulta = new ConsultarAction();
            consulta.setPaquete(p);

            try {
                getContentManager().fillContent(cfpServicios,
                        new Action(myAgent.getAID(), consulta));
            } catch(Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }

            Vector<ACLMessage> msgs = new Vector<>();
            msgs.add(cfpServicios);
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
                    Action action;
                    try {
                        action = (Action) getContentManager()
                                    .extractContent(resp);
                    } catch(Exception e) {
                        System.out.println("ERROR: " + e.getMessage());
                        return;
                    }

                    // Preguntar si recibimos una oferta de alojamiento o de
                    // transporte y procesar cada una.
                    Concept concept = action.getAction();
                    if(concept instanceof OfertarLugarAction) {
                        OfertarLugarAction of = (OfertarLugarAction) concept;
                        Alojamiento aloj = of.getAlojamiento();
                        System.out.println("[AGENCIA] Propuesta recibida " +
                                "del LUGAR " + action.getActor());
                        System.out.println("ALOJAMIENTO " + aloj);
                    }
                    else if(concept instanceof OfertarTransporteAction) {
                        OfertarTransporteAction of =
                                (OfertarTransporteAction) concept;
                        Transporte transp = of.getTransporte();
                        System.out.println("[AGENCIA] Propuesta recibida " +
                                "del TRANSPORTE " + action.getActor());
                        System.out.println("TRANSPORTE " + transp);
                    }

                    // Si nos convence, aceptamos
                    //TODO: enviar reject-proposals
                    ACLMessage accept = resp.createReply();
                    accept.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                    acceptances.add(accept);
                } else {
                    System.out.println(
                            "[AGENCIA] Rechazo recibido del Lugar o " +
                            "Transporte " + resp.getSender().getName());
                }
            }

            // Si no aceptamos ninguna propuesta de los servicios,
            // no podemos antender al turista y rechazamos su CFP.
            if(acceptances.isEmpty()) {
                ACLMessage refuse = new ACLMessage(ACLMessage.REFUSE);
                String key = ((ContractNetResponder) parent).REPLY_KEY;
                getDataStore().put(key, refuse);
            }
        }

        @Override
        protected void handleAllResultNotifications(Vector resultNotif) {
            // Recibimos inform o failure de algún Lugar o Transporte
            System.out.println("[AGENCIA] INFORM de algún Lugar o Transporte");

            // Creamos el mensaje PROPOSE
            ACLMessage propose = new ACLMessage(ACLMessage.PROPOSE);
            propose.setLanguage(slCodec.getName());
            propose.setOntology(ontology.getName());

            // Construimos la lista de ofertas que se envían al turista
            // TODO: reemplazar por la mejor combinación Lugar-Transporte...
            OfertarPaqueteAction of = new OfertarPaqueteAction();
            List<PaqueteAgencia> paquetes = new ArrayList<>();
            PaqueteAgencia pa = new PaqueteAgencia();
            Alojamiento aloj = new Alojamiento();
            Transporte transp = new Transporte();
            pa.setAlojamiento(aloj);
            pa.setTransporte(transp);
            paquetes.add(pa);

            of.setPaqueteAgencias(paquetes);

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
        }
    }
}
