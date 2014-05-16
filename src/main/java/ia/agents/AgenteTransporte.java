/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

@SuppressWarnings("unused")
public class AgenteTransporte extends Agent {
    // TODO: recibir agencia como argumento y registrarse con esa

    @Override
    protected void setup() {
        // TODO: Crear y mostrar la interfaz

        // Registrarse en el servicio de páginas amarillas
        DFAgentDescription ad = new DFAgentDescription();
        ad.setName(getAID());

        ServiceDescription sd = new ServiceDescription();
        sd.setType("Transporte");
        sd.setName("Transporte-" + getLocalName());

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
}
