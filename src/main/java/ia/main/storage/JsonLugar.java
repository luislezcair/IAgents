package ia.main.storage;

import ia.agents.AgenteLugar;
import ia.agents.ontology.Alojamiento;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JsonLugar extends JsonAgent {
    private Alojamiento alojamiento;
    private String nombre = "UNNAMED PLACE";
    private String[] agencias;
    private List<String> agenciasList;

    @Override
    public String getName() {
        return nombre;
    }

    @Override
    public String getType() {
        return AgenteLugar.class.getName();
    }

    @Override
    public Object[] getDataArray() {
        createAgencyList();
        return new Object[] { agenciasList, alojamiento };
    }

    @Override
    public String toString() {
        createAgencyList();
        return "nombre: " + nombre +
                " tipo: " + getType() +
                " agencias: " + agenciasList.stream().collect(Collectors.joining(" ")) +
                " alojamiento: " + alojamiento.toString();
    }


    private void createAgencyList() {
        if(agenciasList == null)
            agenciasList = new ArrayList<>(Arrays.asList(agencias));
    }

}
