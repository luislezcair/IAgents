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
        
    }

    public void shutdown() {
        if(rma != null) {
            try {
                rma.kill();
                mainContainer.kill();
            } catch (StaleProxyException e) {
                e.printStackTrace();
            }
        }
        ui.dispose();
        rt.setCloseVM(true);
    }
}
