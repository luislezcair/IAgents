/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetResponder;

@SuppressWarnings("unused")
public class AgenteLugar extends Agent {
    @Override
    protected void setup() {
        // TODO: Crear y mostrar la interfaz

        // Registrarse en el servicio de páginas amarillas
        DFAgentDescription ad = new DFAgentDescription();
        ad.setName(getAID());

        ServiceDescription sd = new ServiceDescription();
        sd.setType("Lugar");
        sd.setName("Lugar-" + getLocalName());

        // Esta propiedad se usa para saber qué lugar se corresponde con qué
        // agencia. Se la pasa como argumento al crear el agente.
        sd.addProperties(new Property("AgenciaAsociada", getAgencia()));

        ad.addServices(sd);

        try {
            DFService.register(this, ad);
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        addBehaviour(new AgencyNegotiator());
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
     * Obtiene el nombre de la agencia de la lista de argumentos
     * @return Nombre de la agencia
     */
    private String getAgencia() {
        Object[] args = getArguments();
        if(args == null || args.length < 1) {
            return "";
        }
        return args[0].toString();
    }

    /** Implementación de un contract-net para manejar la interacción con las
     * agencias.
     */
    private class AgencyNegotiator extends ContractNetResponder {
        public AgencyNegotiator() {
            super(null, null);
        }

        @Override
        protected ACLMessage handleCfp(ACLMessage cfp) {
            // Recibimos un CFP de una agencia, respondemos con los datos del
            // alojamiento disponible.
            ACLMessage reply = cfp.createReply();
            reply.setContent("Tengo lugar para 2 personas a 20 peco");
            reply.setPerformative(ACLMessage.PROPOSE);
            return reply;
        }

        @Override
        protected ACLMessage handleAcceptProposal(ACLMessage cfp,
                                                  ACLMessage propose,
                                                  ACLMessage accept)
                                        throws FailureException {
            System.out.println("[LUGAR] La agencia aceptó la propuesta");

            // FIPA: un accept_proposal se responde con Inform o Failure.
            ACLMessage reply = accept.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            return reply;
        }

        @Override
        protected void handleRejectProposal(ACLMessage cfp,
                                            ACLMessage propose,
                                            ACLMessage reject) {

            // FIPA: ante un reject termina la negociación.
            System.out.println("[LUGAR] La agencia rechazó la propuesta");
        }
    }
}
