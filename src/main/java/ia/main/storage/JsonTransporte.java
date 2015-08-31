package ia.main.storage;

import ia.agents.AgenteTransporte;
import ia.agents.ontology.Transporte;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JsonTransporte extends JsonAgent {
    private Transporte transporte;
    private String nombre = "UNNAMED TRANSPORT";
    private String[] agencias;
    private List<String> agenciasList;

    @Override
    public String getName() {
        return nombre;
    }

    @Override
    public String getType() {
        return AgenteTransporte.class.getName();
    }

    @Override
    public Object[] getDataArray() {
        createAgencyList();
        return new Object[] { agenciasList, transporte };
    }

    @Override
    public String toString() {
        createAgencyList();
        return "nombre: " + nombre +
                " tipo: " + getType() +
                " agencias: " + agenciasList.stream().collect(Collectors.joining(" ")) +
                " transporte: " + transporte.toString();
    }

    private void createAgencyList() {
        if(agenciasList == null)
            agenciasList = new ArrayList<>(Arrays.asList(agencias));
    }
}
