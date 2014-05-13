/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents;

import ia.agents.ui.UIAgency;
import jade.core.Agent;

public class Agencia extends Agent {
    private UIAgency ui;

    @Override
    protected void setup() {
        ui = new UIAgency(this);
        ui.setupUi();
    }
}
