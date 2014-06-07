/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.main;

import ia.agents.negotiation.DiscountManager;
import ia.agents.ontology.Alojamiento;
import ia.agents.ontology.Paquete;
import ia.agents.ontology.Transporte;
import ia.agents.util.DFAgentSubscriber;
import ia.main.ui.UIAgentManager;
import jade.core.*;
import jade.core.Runtime;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.wrapper.*;
import jade.wrapper.AgentContainer;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AgentManager extends Agent {
    List<AID> agencias = new ArrayList<>();
    AgentContainer mainContainer;
    AgentController rma;
    AgentController sniffer;
    UIAgentManager ui;
    boolean testAgentsLaunched;

    @Override
    protected void setup() {
        ui = new UIAgentManager(this);
        ui.show();

        Runtime rt = Runtime.instance();

        // Hace que el programa termine cuando el último container se cierre
        rt.setCloseVM(true);

        mainContainer = (AgentContainer) getArguments()[0];

        // Se suscribe al DF para obtener la lista de agencias registradas
        DFAgentDescription dfad = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Agencia");
        dfad.addServices(sd);

        addBehaviour(new DFAgentSubscriber(this, dfad, agencias));
    }

    /**
     * Ejecuta el agente RMA
     */
    public void launchRma() {
        try {
            rma = mainContainer.createNewAgent(
                    "rma", "jade.tools.rma.rma", null);
            rma.start();
        } catch(StaleProxyException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ejecuta el agente Sniffer
     */
    public void launchSniffer() {
        try {
            sniffer = mainContainer.createNewAgent("sniffer",
                    "jade.tools.sniffer.Sniffer", null);
            sniffer.start();
        } catch(StaleProxyException e) {
            e.printStackTrace();
        }
    }

    public void launchTestAgents() {
        if(testAgentsLaunched)
            return;

        Paquete paquete = new Paquete("Corrientes", 5, new Date(), -1, 1000.0,
                5);
        Paquete paquete2 = new Paquete("Chaco", 6, new Date(), -1, 200.0, 7);
        Alojamiento lugar1 = new Alojamiento(10, "Corrientes", new Date(),
                123.0f, 0, new DiscountManager(0.1, 0.5, 0.1));
        Alojamiento lugar2 = new Alojamiento(20, "Corrientes", new Date(),
                200.0f, 2, new DiscountManager(0.05, 0.3, 0.1));
        Alojamiento lugar3 = new Alojamiento(15, "Corrientes", new Date(),
                230.0f, 1, new DiscountManager(0.07, 0.4, 0.05));
        Alojamiento lugar4 = new Alojamiento(9, "Corrientes", new Date(),
                100.0f, 0, new DiscountManager(0.05, 0.5, 0.08));
        Transporte transp1 = new Transporte(30, "Corrientes", new Date(),
                120.0f, 0, new DiscountManager(0.08, 0.45, 0.05));
        Transporte transp2 = new Transporte(20, "Corrientes", new Date(),
                120.0f, 1, new DiscountManager(0.15, 0.6, 0.06));

        Object turista[] = {paquete};
        Object turista2[] = {paquete2};
        Object lugar1_86[] = {"Agencia86@IAMainPlatform", lugar1};
        Object lugar2_86[] = {"Agencia86@IAMainPlatform", lugar2};
        Object lugar3_007[] = {"Agencia007@IAMainPlatform", lugar3};
        Object lugar4_007[] = {"Agencia007@IAMainPlatform", lugar4};
        Object transp_86[] = {"Agencia86@IAMainPlatform", transp1};
        Object transp_007[] = {"Agencia007@IAMainPlatform", transp2};

        createAgent("Turista1", "ia.agents.AgenteTurista", turista);
        createAgent("Turista2", "ia.agents.AgenteTurista", turista2);
        createAgent("Agencia86", "ia.agents.AgenteAgencia", null);
        createAgent("Agencia007", "ia.agents.AgenteAgencia", null);
        createAgent("Lugar86", "ia.agents.AgenteLugar", lugar1_86);
        createAgent("Lugar2_86", "ia.agents.AgenteLugar", lugar2_86);
        createAgent("Lugar007", "ia.agents.AgenteLugar", lugar3_007);
        createAgent("Lugar2_007", "ia.agents.AgenteLugar", lugar4_007);
        createAgent("Transporte86", "ia.agents.AgenteTransporte", transp_86);
        createAgent("Transporte007", "ia.agents.AgenteTransporte", transp_007);

        testAgentsLaunched = true;
    }

    /**
     * Contrata sicarios para matar los agentes rma, sniffer y el main container
     */
    public void shutdown() {
        SwingUtilities.invokeLater(ui::dispose);

        killAgent(rma);
        killAgent(sniffer);

        try {
            mainContainer.kill();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    /**
     * Mata a un agente
     * @param ac Agente al que se va a matar
     */
    private void killAgent(AgentController ac) {
        try {
            if (ac != null) {
                ac.kill();
            }
        } catch( StaleProxyException e) {
            //e.printStackTrace();
        }
    }

    public List<AID> getAgencias() {
        return agencias;
    }

    /**
     * Crea un agente de la clase clase asociado a la agencia agencia
     * @param clase Clase del agente que se va a crear
     * @param params Agencia a la que se va a asociar
     */
    public void createAgent(String nombre, String clase, Object[] params) {
        try {
            mainContainer.createNewAgent(nombre, clase, params).start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
}
