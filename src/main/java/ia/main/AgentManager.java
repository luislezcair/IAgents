/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.main;

import ia.main.ui.UIAgentManager;

public class AgentManager {
    private UIAgentManager ui;

    public AgentManager() {
        ui = new UIAgentManager();
    }

    public void showUi() {
        ui.setupUi();
    }
}
