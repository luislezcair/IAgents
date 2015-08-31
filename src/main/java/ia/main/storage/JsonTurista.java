package ia.main.storage;

import ia.agents.AgenteTurista;
import ia.agents.ontology.Paquete;

public class JsonTurista extends JsonAgent {
    private Paquete paquete;
    private String nombre = "UNNAMED TOURIST";

    @Override
    public String getName() {
        return nombre;
    }

    @Override
    public String getType() {
        return AgenteTurista.class.getName();
    }

    @Override
    public Object[] getDataArray() {
        return new Object[] { paquete };
    }

    @Override
    public String toString() {
        return "nombre: " + nombre +
               " tipo: " + getType() +
               " paquete: " + paquete.toString();
    }
}
