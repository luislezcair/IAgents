/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents;

import ia.agents.ui.UIAgency;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;

public class AgenteAgencia extends Agent {
    private UIAgency ui;

    @Override
    protected void setup() {
        // Crear y mostrar la interfaz
        ui = new UIAgency(this);
        ui.setupUi();

        // Registrarse en el servicio de páginas amarillas
        DFAgentDescription ad = new DFAgentDescription();
        ad.setName(getAID());

        ServiceDescription sd = new ServiceDescription();
        sd.setType("Agencia");
        sd.setName("Agencia-" + getLocalName());

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
