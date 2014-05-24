/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents;

import ia.agents.util.DFRegisterer;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.*;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetResponder;

@SuppressWarnings("unused")
public class AgenteTransporte extends Agent {
    @Override
    protected void setup() {
        // TODO: Crear y mostrar la interfaz

        DFRegisterer.register(this, "Transporte",
                              new Property("AgenciaAsociada", getAgencia()));

        addBehaviour(new AgencyNegotiator());
    }

    protected void takeDown() {
        DFRegisterer.deregister(this);
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
            reply.setContent("Tengo un jet privado para 4 personas a 20 peco");
            reply.setPerformative(ACLMessage.PROPOSE);
            return reply;
        }

        @Override
        protected ACLMessage handleAcceptProposal(ACLMessage cfp,
                                                  ACLMessage propose,
                                                  ACLMessage accept)
                throws FailureException {
            System.out.println("[TRANSPORTE] La agencia aceptó la propuesta");

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
            System.out.println("[TRANSPORTE] La agencia rechazó la propuesta");
        }
    }
}
