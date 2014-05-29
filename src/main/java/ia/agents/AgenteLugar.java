/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents;

import ia.agents.negotiation.AgencyNegotiator;
import ia.agents.ontology.*;
import ia.agents.util.DFRegisterer;
import jade.content.AgentAction;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SSResponderDispatcher;

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

        MessageTemplate mt = MessageTemplate.and(
            MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol
                        .FIPA_ITERATED_CONTRACT_NET),
            MessageTemplate.and(
                MessageTemplate.MatchPerformative(ACLMessage.CFP),
                MessageTemplate.and(
                        MessageTemplate.MatchOntology(ontology.getName()),
                        MessageTemplate.MatchLanguage(slCodec.getName()))));

        // El despachador crea un Respondedor cuando llega un CFP para
        // manejar cada conversación.
        addBehaviour(new SSResponderDispatcher(this, mt) {
            @Override
            protected Behaviour createResponder(ACLMessage initiationMsg) {
                return new AgencyNegotiatorLugar(myAgent, initiationMsg);
            }
        });
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
        if (args == null || args.length < 1) {
            return "";
        }
        return args[0].toString();
    }

    private class AgencyNegotiatorLugar extends AgencyNegotiator {
        private AgencyNegotiatorLugar(Agent a, ACLMessage cfp) {
            super(a, cfp);
        }

        @Override
        public AgentAction prepareResponseAction(Paquete p) {
            // TODO: crear un alojamiento con las características que sean
            // necesarias para satisfacer las necesidades del paquete.
            Alojamiento a = new Alojamiento();
            OfertarLugarAction of = new OfertarLugarAction();
            of.setAlojamiento(a);
            return of;
        }

        @Override
        public boolean canOfferService(Paquete p) {
            // TODO: Verificar si este agente puede satisfacer el servicio.
            // (e.g si tenemos capacidad, si la fecha coincide, etc.)
            return true;
        }
    }
}