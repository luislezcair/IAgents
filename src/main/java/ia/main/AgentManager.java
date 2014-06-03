/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.main;

import ia.agents.ontology.Alojamiento;
import ia.agents.ontology.Transporte;
import ia.main.ui.UIAgentManager;
import jade.core.*;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.util.Date;

public class AgentManager {
    Runtime rt;
    AgentContainer mainContainer;
    AgentController rma;
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

    public void launchTestAgents() {
        if(testAgentsLaunched)
            return;

        Alojamiento lugar1 = new Alojamiento(10, "Corrientes", 0.1f,
                new Date(), 123.0f, 0);
        Alojamiento lugar2 = new Alojamiento(20, "Chaco", 0.2f, new Date(),
                200.0f, 2);
        Alojamiento lugar3 = new Alojamiento(15, "Córdoba", 0.15f,
                new Date(), 230.0f, 1);
        Alojamiento lugar4 = new Alojamiento(9, "Rosario", 0.09f, new Date(),
                100.0f, 0);
        Transporte transp1 = new Transporte(30, "Corrientes", 0.1f,
                new Date(), 120.0f, 0);
        Transporte transp2 = new Transporte(20, "Chaco", 0.5f, new Date(),
                120.0f, 1);

        Object lugar1_86[] = {"Agencia86", lugar1};
        Object lugar2_86[] = {"Agencia86", lugar2};
        Object lugar3_007[] = {"Agencia007", lugar3};
        Object lugar4_007[] = {"Agencia007", lugar4};
        Object transp_86[] = {"Agencia86", transp1};
        Object transp_007[] = {"Agencia007", transp2};

        try {
            mainContainer.createNewAgent(
                    "Turista1", "ia.agents.AgenteTurista", null).start();
            //mainContainer.createNewAgent(
            //        "Turista2", "ia.agents.AgenteTurista", null).start();
            mainContainer.createNewAgent(
                    "Agencia86", "ia.agents.AgenteAgencia", null).start();
            //mainContainer.createNewAgent(
            //        "Agencia007", "ia.agents.AgenteAgencia", null).start();
            mainContainer.createNewAgent(
                    "Lugar86", "ia.agents.AgenteLugar", lugar1_86).start();
            //mainContainer.createNewAgent(
            //        "Lugar2_86", "ia.agents.AgenteLugar", lugar2_86).start();
            //mainContainer.createNewAgent(
            //       "Lugar007", "ia.agents.AgenteLugar", lugar3_007).start();
            //mainContainer.createNewAgent(
            //        "Lugar2_007", "ia.agents.AgenteLugar",
            // lugar4_007).start();
            mainContainer.createNewAgent(
                    "Transporte86", "ia.agents.AgenteTransporte", transp_86).start();
            //mainContainer.createNewAgent(
            //        "Transporte007", "ia.agents.AgenteTransporte",
            //        transp_007).start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
        testAgentsLaunched = true;
    }

    public void shutdown() {
        ui.dispose();
        rt.setCloseVM(true);

        if(rma != null) {
            try {
                rma.kill();
            } catch (StaleProxyException e) {
                e.printStackTrace();
            }
        }
        try {
            mainContainer.kill();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
}
