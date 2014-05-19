/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents;

//import ia.agents.ui.UIAgency;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;

import java.util.ArrayList;
import java.util.List;

public class AgenteAgencia extends Agent {
    //private UIAgency ui;

    @Override
    protected void setup() {
        // Crear y mostrar la interfaz
        /*
        ui = new UIAgency(this);
        ui.setupUi();*/

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

        List<AID> lugares = getAgentesAsociados("Lugar");
        List<AID> transportes = getAgentesAsociados(("Transporte"));

        for(AID aid : lugares) {
            System.out.println(aid.getName());
        }
        for(AID aid : transportes) {
            System.out.println(aid.getName());
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
     * Consulta el DF para obtener los agentes asociados con esta agencia
     * @param agentType Tipo de agente a buscar
     * @return Lista con los agentes encontrados
     */
    private List<AID> getAgentesAsociados(String agentType) {
        List<AID> resultList = new ArrayList<AID>();

        // Buscamos agentes en el DF
        DFAgentDescription ad = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();

        sd.setType(agentType);
        sd.addProperties(new Property("AgenciaAsociada", getLocalName()));
        ad.addServices(sd);

        try {
            DFAgentDescription[] result = DFService.search(this, ad);
            for(DFAgentDescription dfagent : result) {
                resultList.add(dfagent.getName());
            }
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        return resultList;
    }
}
