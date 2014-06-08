/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents;

import ia.agents.negotiation.AgencyNegotiator;
import ia.agents.negotiation.DiscountManager;
import ia.agents.ontology.*;
import ia.agents.ui.UITransporte;
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
import org.joda.time.DateTimeComparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AgenteTransporte extends Agent {
    private final Codec slCodec = new SLCodec();
    private final Ontology ontology = TurismoOntology.getInstance();

    private final HashMap<String, Transporte> ofertasPrevias = new HashMap<>();
    private Transporte transporte;

    @Override
    protected void setup() {
        getContentManager().registerLanguage(slCodec);
        getContentManager().registerOntology(ontology);

        List<Property> properties = new ArrayList<>();
        getAgencias().forEach(
                x -> properties.add(new Property("AgenciaAsociada", x)));

        DFRegisterer.register(this, "Transporte", properties);

        MessageTemplate mt = MessageTemplate.and(
            MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol
                    .FIPA_ITERATED_CONTRACT_NET),
            MessageTemplate.and(
                MessageTemplate.MatchPerformative(ACLMessage.CFP),
                MessageTemplate.and(
                    MessageTemplate.MatchOntology(ontology.getName()),
                    MessageTemplate.MatchLanguage(slCodec.getName()))));

        // El despachador crea un Responder cuando llega un CFP para
        // manejar cada conversación.
        addBehaviour(new SSResponderDispatcher(this, mt) {
            @Override
            protected Behaviour createResponder(ACLMessage initiationMsg) {
                return new AgencyNegotiatorTransporte(myAgent, initiationMsg);
            }
        });

        transporte = getTransporteArg();

        // Si no recibimos un transporte como parámetro, creamos y mostramos
        // la interfaz para que se ingrese uno.
        if(transporte == null) {
            transporte = new Transporte();
            new UITransporte(this);
        }
    }

    protected void takeDown() {
        DFRegisterer.deregister(this);
    }

    /**
     * Obtiene el nombre de la agencia de la lista de argumentos
     * @return Nombre de la agencia
     */
    private List<String> getAgencias() {
        Object[] args = getArguments();
        if(args == null || args.length < 1 || args[0] == null ||
                !(args[0] instanceof ArrayList<?>)) {
            return new ArrayList<>();
        }

        // Convierte la lista que viene como argumento a una lista de strings
        // para evitar unchecked warnings
        List<?> argList = (List<?>) args[0];
        List<String> agencias = new ArrayList<>();
        argList.forEach(x -> agencias.add((String) x));
        return agencias;
    }

    /**
     * Obtiene el alojamiento de los argumentos
     * @return Alojamiento obtenido
     */
    private Transporte getTransporteArg() {
        Object[] args = getArguments();
        if(args == null || args.length < 2) {
            return null;
        }
        return (Transporte)args[1];
    }

    public Transporte getTransporte() {
        return transporte;
    }

    /**
     * Implementamos un AgencyNegotiator para adecuarlo a Transportes.
     */
    private class AgencyNegotiatorTransporte extends AgencyNegotiator {
        public AgencyNegotiatorTransporte(Agent a, ACLMessage cfp) {
            super(a, cfp, ofertasPrevias);
        }

        @Override
        public AgentAction prepareResponseAction() {
            OfertarTransporteAction of = new OfertarTransporteAction();
            String cid = getConversationId();
            Transporte transp;

            // Comprueba las ofertas previas para esta negociación y aumenta
            // el descuento hasta un máximo. Si llega al máximo, esta es la
            // oferta final
            if(ofertasPrevias.containsKey(cid)) {
                transp = ofertasPrevias.get(cid);
                DiscountManager ds = transp.getDescuento();
                ds.updateValue();
            }
            else {
                transp = transporte;
                ofertasPrevias.put(cid, new Transporte(transp));
            }
            of.setTransporte(transp);
            return of;
        }

        @Override
        public boolean canOfferService(Paquete p) {
            return transporte.getCapacidad() > p.getPersonas() &&
                   transporte.getDestino().equalsIgnoreCase(p.getDestino()) &&
                   DateTimeComparator.getDateOnlyInstance().compare(
                            transporte.getFecha(), p.getFecha()) <= 0;
        }
    }
}
