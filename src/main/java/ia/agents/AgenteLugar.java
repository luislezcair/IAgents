/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents;

import ia.agents.negotiation.AgencyNegotiator;
import ia.agents.negotiation.DiscountManager;
import ia.agents.ontology.*;
import ia.agents.ui.UILugar;
import ia.agents.util.DFRegisterer;
import ia.agents.util.Util;
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

public class AgenteLugar extends Agent {
    private final Codec slCodec = new SLCodec();
    private final Ontology ontology = TurismoOntology.getInstance();

    // Mapa de conversation-id -> Alojamiento
    private final HashMap<String, Alojamiento> ofertasPrevias = new HashMap<>();
    private Alojamiento lugar;

    @Override
    protected void setup() {
        getContentManager().registerLanguage(slCodec);
        getContentManager().registerOntology(ontology);

        // Obtiene la lista de agencias de los argumentos y crea propiedades
        // para registrarse en el DF y asociarse con esas agencias
        List<Property> properties = new ArrayList<>();
        List<String> agencias = Util.getAgencias(getArguments());
        agencias.forEach(
                x -> properties.add(new Property("AgenciaAsociada", x)));

        DFRegisterer.register(this, "Lugar", properties);

        MessageTemplate mt = MessageTemplate.and(
            MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol
                        .FIPA_ITERATED_CONTRACT_NET),
            MessageTemplate.and(
                MessageTemplate.MatchPerformative(ACLMessage.CFP),
                MessageTemplate.and(
                        MessageTemplate.MatchOntology(ontology.getName()),
                        MessageTemplate.MatchLanguage(slCodec.getName()))));

        // El despachador crea un Responder cuando llega un CFP para
        // manejar cada conversación. Seguimos los protocolos estándares, pero
        // eso no me va a dar mejor nota.
        addBehaviour(new SSResponderDispatcher(this, mt) {
            @Override
            protected Behaviour createResponder(ACLMessage initiationMsg) {
                return new AgencyNegotiatorLugar(myAgent, initiationMsg);
            }
        });

        lugar = (Alojamiento) Util.getServicio(getArguments());

        // Si no recibimos un alojamiento como parámetro,
        // creamos y mostramos la interfaz para que se ingrese uno.
        if(lugar == null) {
            lugar = new Alojamiento();
            new UILugar(this);
        }
        System.out.println("Se creó el agente Lugar " + getName());
    }

    @Override
    protected void takeDown() {
        DFRegisterer.deregister(this);
    }


    public Alojamiento getAlojamiento() {
        return lugar;
    }

    private class AgencyNegotiatorLugar extends AgencyNegotiator {
        public AgencyNegotiatorLugar(Agent a, ACLMessage cfp) {
            super(a, cfp, ofertasPrevias);
        }

        @Override
        public AgentAction prepareResponseAction() {
            String cid = getConversationId();
            Alojamiento alojamiento;
            OfertarLugarAction of = new OfertarLugarAction();

            // Comprueba las ofertas previas para esta negociación y aumenta
            // el descuento hasta un máximo. Si llega al máximo, esta es la
            // oferta final
            if(ofertasPrevias.containsKey(cid)) {
                alojamiento = ofertasPrevias.get(cid);
                DiscountManager ds = alojamiento.getDescuento();
                ds.updateValue();
            }
            else {
                alojamiento = lugar;
                ofertasPrevias.put(cid, new Alojamiento(lugar));
            }
            of.setAlojamiento(alojamiento);
            return of;
        }

        /**
         * Verificar si este agente puede satisfacer el servicio.
         * (e.g si tenemos capacidad, si la fecha y el destino coinciden)
         * @param p Paquete turístico
         * @return true si se puede satisfacer, false en caso contrario
         */
        @Override
        public boolean canOfferService(Paquete p) {
            return lugar.getCapacidad() >= p.getPersonas() &&
                   lugar.getDestino().equalsIgnoreCase(p.getDestino()) &&
                   DateTimeComparator.getDateOnlyInstance().compare(
                           lugar.getFecha(), p.getFecha()) <= 0;
        }
    }
}