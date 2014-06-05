/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.main;

import ia.agents.negotiation.DiscountManager;
import ia.agents.ontology.Alojamiento;
import ia.agents.ontology.Paquete;
import ia.agents.ontology.Transporte;
import ia.main.ui.UIAgentManager;
import jade.core.*;
import jade.core.Runtime;
import jade.wrapper.*;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentState;

import java.util.Date;

public class AgentManager {
    Runtime rt;
    AgentContainer mainContainer;
    AgentController rma;
    AgentController sniffer;
    UIAgentManager ui;
    boolean testAgentsLaunched;

    public AgentManager() {
        ui = new UIAgentManager(this);
        ui.show();

        rt = Runtime.instance();
    }

    public void launchRuntime() {
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.PLATFORM_ID, "IAMainPlatform");
        profile.setParameter(Profile.CONTAINER_NAME, "IAMainContainer");

        mainContainer = rt.createMainContainer(profile);
    }

    public void launchRma() {
        try {
            rma = mainContainer.createNewAgent(
                    "rma", "jade.tools.rma.rma", null);
            rma.start();
        } catch(StaleProxyException e) {
            e.printStackTrace();
        }
    }

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
        Alojamiento lugar1 = new Alojamiento(10, "Corrientes", new Date(),
                123.0f, 0, new DiscountManager(0.1, 0.5, 0.1));
        Alojamiento lugar2 = new Alojamiento(20, "Corrientes", new Date(),
                200.0f, 2, new DiscountManager(0.05, 0.3, 0.1));
        Alojamiento lugar3 = new Alojamiento(15, "Chaco", new Date(),
                230.0f, 1, new DiscountManager(0.07, 0.4, 0.05));
        Alojamiento lugar4 = new Alojamiento(9, "Chaco", new Date(), 100.0f,
                0, new DiscountManager(0.05, 0.5, 0.08));
        Transporte transp1 = new Transporte(30, "Corrientes", new Date(),
                120.0f, 0, new DiscountManager(0.08, 0.45, 0.05));
        Transporte transp2 = new Transporte(20, "Rosario", new Date(), 120.0f,
                1, new DiscountManager(0.15, 0.6, 0.06));

        Object turista[] = {paquete};
        Object lugar1_86[] = {"Agencia86", lugar1};
        Object lugar2_86[] = {"Agencia86", lugar2};
        Object lugar3_007[] = {"Agencia007", lugar3};
        Object lugar4_007[] = {"Agencia007", lugar4};
        Object transp_86[] = {"Agencia86", transp1};
        Object transp_007[] = {"Agencia007", transp2};

        try {
            mainContainer.createNewAgent(
                    "Turista1", "ia.agents.AgenteTurista", turista).start();
            //mainContainer.createNewAgent(
            //        "Turista2", "ia.agents.AgenteTurista", null).start();
            mainContainer.createNewAgent(
                    "Agencia86", "ia.agents.AgenteAgencia", null).start();
            mainContainer.createNewAgent(
                    "Agencia007", "ia.agents.AgenteAgencia", null).start();
            mainContainer.createNewAgent(
                    "Lugar86", "ia.agents.AgenteLugar", lugar1_86).start();
            mainContainer.createNewAgent(
                    "Lugar2_86", "ia.agents.AgenteLugar", lugar2_86).start();
            mainContainer.createNewAgent(
                   "Lugar007", "ia.agents.AgenteLugar", lugar3_007).start();
            mainContainer.createNewAgent(
                    "Lugar2_007", "ia.agents.AgenteLugar", lugar4_007).start();
            mainContainer.createNewAgent(
                    "Transporte86", "ia.agents.AgenteTransporte", transp_86).start();
            mainContainer.createNewAgent(
                    "Transporte007", "ia.agents.AgenteTransporte",
                    transp_007).start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
        testAgentsLaunched = true;
    }

    public void shutdown() {
        ui.dispose();
        rt.setCloseVM(true);

        killAgent(rma);
        killAgent(sniffer);

        try {
            mainContainer.kill();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    private void killAgent(AgentController ac) {
        try {
            if (ac != null) {
                ac.kill();
            }
        } catch( StaleProxyException e) {
            //e.printStackTrace();
        }
    }
}
