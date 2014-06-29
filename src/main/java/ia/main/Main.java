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

import javax.swing.*;

class Main {
    public static void main(String[] args) {
        Runtime rt = Runtime.instance();

        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.PLATFORM_ID, "IAMainPlatform");
        profile.setParameter(Profile.CONTAINER_NAME, "IAMainContainer");

        AgentContainer mainContainer = rt.createMainContainer(profile);

        // Usa el look&feel nativo. En Windows funciona.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

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
