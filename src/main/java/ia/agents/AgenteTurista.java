/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents;

import ia.agents.ui.UITourist;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class AgenteTurista extends Agent {
    private List<AID> agencias;
    private Paquete paquete;
    private UITourist ui;

    @Override
    protected void setup() {
        agencias = new ArrayList<AID>();
        paquete = new Paquete();
        ui = new UITourist(this);

        // Behaviour para obtener las agencias registradas
        // en las páginas amarillas cada 60 segundos.
        addBehaviour(new TickerBehaviour(this, 6000) {
            @Override
            protected void onTick() {
                // Descripción del agente y servicio que se quiere consultar
                DFAgentDescription ad = new DFAgentDescription();
                ServiceDescription sd = new ServiceDescription();
                sd.setType("Agencia");
                ad.addServices(sd);

                try {
                    DFAgentDescription[] result = DFService.search(myAgent, ad);
                    agencias.clear();
                    for(DFAgentDescription dfagent : result) {
                        agencias.add(dfagent.getName());
                    }
                    // Habilita las consultas en el formulario si hay agencias
                    ui.setConsultaEnabled(!agencias.isEmpty());
                } catch (FIPAException e) {
                    e.printStackTrace();
                }
            }
        });

        ui.show();
    }

    /**
     * Envía un CFP a las agencias registradas para empezar la negociación.
     * TODO: ampliar para manejar las respuestas
     */
    public void sendCfp() {
        addBehaviour(new OneShotBehaviour(this) {
            @Override
            public void action() {
                if(agencias.isEmpty())
                    return;

                ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
                for(AID aid : agencias) {
                    cfp.addReceiver(aid);
                }
                cfp.setContent(paquete.toString());
                myAgent.send(cfp);
            }
        });
    }
}
