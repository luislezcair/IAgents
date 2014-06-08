/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.main;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

class Main {
    public static void main(String[] args) {
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.PLATFORM_ID, "IAMainPlatform");
        profile.setParameter(Profile.CONTAINER_NAME, "IAMainContainer");

        Runtime rt = Runtime.instance();

        AgentContainer mainContainer = rt.createMainContainer(profile);

        try {
            // Crear el agente principal y le pasa el mainContainer como
            // parámetro, para que pueda manejar el runtime.
            Object[] params = {mainContainer};
            AgentController ac = mainContainer.createNewAgent("manager",
                    "ia.main.AgentManager", params);
            ac.start();
        } catch(StaleProxyException e) {
            e.printStackTrace();
        }
    }
}
