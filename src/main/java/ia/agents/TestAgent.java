/**
 * Created by Luis Lezcano Airaldi
 */

package ia.agents;

import jade.core.Agent;

@SuppressWarnings("unused")
public class TestAgent extends Agent {

    @Override
    protected void setup() {
        System.out.println("Hola, soy el agente "
                + getLocalName() + " y funciono de maravillas");
        System.out.println("GUID: " + getName());
        doDelete();
    }
}
