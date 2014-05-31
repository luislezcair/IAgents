/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.main;

import ia.main.ui.UIAgentManager;
import jade.core.*;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

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

        String args86[] = {"Agencia86"};
        String args007[] = {"Agencia007"};

        try {
            mainContainer.createNewAgent(
                    "Turista1", "ia.agents.AgenteTurista", null).start();
            mainContainer.createNewAgent(
                    "Turista2", "ia.agents.AgenteTurista", null).start();
            mainContainer.createNewAgent(
                    "Agencia86", "ia.agents.AgenteAgencia", null).start();
            mainContainer.createNewAgent(
                    "Agencia007", "ia.agents.AgenteAgencia", null).start();
            mainContainer.createNewAgent(
                    "Lugar86", "ia.agents.AgenteLugar", args86).start();
            mainContainer.createNewAgent(
                    "Lugar2_86", "ia.agents.AgenteLugar", args86).start();
            mainContainer.createNewAgent(
                    "Lugar007", "ia.agents.AgenteLugar", args007).start();
            mainContainer.createNewAgent(
                    "Lugar2_007", "ia.agents.AgenteLugar", args007).start();
            mainContainer.createNewAgent(
                    "Transporte86", "ia.agents.AgenteTransporte", args86).start();
            mainContainer.createNewAgent(
                    "Transporte007", "ia.agents.AgenteTransporte", args007).start();
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
