/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

@SuppressWarnings("unused")
public class AgenteTransporte extends Agent {
    @Override
    protected void setup() {
        // TODO: Crear y mostrar la interfaz

        // Registrarse en el servicio de páginas amarillas
        DFAgentDescription ad = new DFAgentDescription();
        ad.setName(getAID());

        ServiceDescription sd = new ServiceDescription();
        sd.setType("Transporte");
        sd.setName("Transporte-" + getLocalName());

        // Esta propiedad se usa para saber qué lugar se corresponde con qué
        // agencia. Se la pasa como argumento al crear el agente.
        sd.addProperties(new Property("AgenciaAsociada", getAgencia()));

        ad.addServices(sd);

        try {
            DFService.register(this, ad);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
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
}
