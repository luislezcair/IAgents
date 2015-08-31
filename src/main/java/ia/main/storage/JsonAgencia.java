package ia.main.storage;

import ia.agents.AgenteAgencia;

public class JsonAgencia extends JsonAgent {
    private String nombre = "UNNAMED AGENCY";

    @Override
    public String getName() {
        return nombre;
    }

    @Override
    public String getType() {
        return AgenteAgencia.class.getName();
    }

    @Override
    public Object[] getDataArray() {
        return new Object[0];
    }

    @Override
    public String toString() {
        return "nombre: " + nombre +
                " tipo: " + getType();
    }
}
