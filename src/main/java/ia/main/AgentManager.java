package ia.main;

import jade.core.*;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */
public class AgentManager {
    AgentContainer mainContainer;

    public void launchRuntime() {
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.PLATFORM_ID, "IAMainPlatform");
        profile.setParameter(Profile.CONTAINER_NAME, "IAMainContainer");

        Runtime rt = Runtime.instance();
        mainContainer = rt.createMainContainer(profile);
    }

    public void launchRma() {
        try {
            AgentController rma =
                    mainContainer.createNewAgent(
                            "rma", "jade.tools.rma.rma", null);
            rma.start();
        } catch(StaleProxyException e) {
            e.printStackTrace();
        }
    }
}
