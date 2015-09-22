/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.main;

import com.google.gson.*;
import ia.agents.AgenteAgencia;
import ia.agents.AgenteLugar;
import ia.agents.AgenteTransporte;
import ia.agents.AgenteTurista;
import ia.agents.util.DFAgentSubscriber;
import ia.agents.util.Util;
import ia.main.storage.*;
import ia.main.ui.UIAgentManager;
import ia.main.ui.UiCreateAgent;
import jade.core.*;
import jade.core.Runtime;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.wrapper.*;

import javax.swing.*;
import java.util.*;

public class AgentManager extends Agent {
    private final List<AID> agencias = new ArrayList<>();
    private final List<UiCreateAgent> subscriptors = new ArrayList<>();
    private final Map<String, Class<? extends JsonAgent>> agentTypes = new HashMap<>();
    private final Map<String, ContainerController> agentContainers = new HashMap<>();
    private AgentController rma;
    private AgentController sniffer;
    private UIAgentManager ui;
    private boolean testAgentsLaunched;
    private Runtime rt;

    // Separamos los agentes en containers distintos porque somos buenos
    private ContainerController mainContainer;
    private ContainerController turistasContainer;
    private ContainerController agenciasContainer;
    private ContainerController lugaresContainer;
    private ContainerController transportesContainer;

    @Override
    protected void setup() {
        rt = Runtime.instance();

        // Hace que el programa termine cuando el último container se cierre
        rt.setCloseVM(true);

        mainContainer = (ContainerController) getArguments()[0];

        turistasContainer = createContainer("Turistas");
        agenciasContainer = createContainer("Agencias");
        lugaresContainer = createContainer("Lugares");
        transportesContainer = createContainer("Transportes");

        // Se suscribe al DF para obtener la lista de agencias registradas
        DFAgentDescription dfad = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Agencia");
        dfad.addServices(sd);

        initializeAgentDB();

        ui = new UIAgentManager(this);
        ui.show();

        addBehaviour(new AgenciasSubscriber(this, dfad, agencias));
    }

    private void initializeAgentDB() {
        // Registra los tipos de agentes para leer los casos de prueba
        agentTypes.put("turistas", JsonTurista.class);
        agentTypes.put("agencias", JsonAgencia.class);
        agentTypes.put("transportes", JsonTransporte.class);
        agentTypes.put("lugares", JsonLugar.class);

        // Registra los contenedores para cada tipo de agente
        agentContainers.put(AgenteTurista.class.getName(), turistasContainer);
        agentContainers.put(AgenteAgencia.class.getName(), agenciasContainer);
        agentContainers.put(AgenteLugar.class.getName(), lugaresContainer);
        agentContainers.put(AgenteTransporte.class.getName(), transportesContainer);
    }


    /**
     * Crea y devuelve un contenedor con el nombre name
     */
    private ContainerController createContainer(String name) {
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.CONTAINER_NAME, name);
        return rt.createAgentContainer(profile);
    }

    public ContainerController getContainerTuristas() {
        return turistasContainer;
    }

    public ContainerController getContainerAgencias() {
        return agenciasContainer;
    }

    public ContainerController getContainerLugares() {
        return lugaresContainer;
    }

    public ContainerController getContainerTransportes() {
        return transportesContainer;
    }

    /**
     * Ejecuta el agente RMA
     */
    public void launchRma() {
        rma = createAgent("rma", "jade.tools.rma.rma", null, mainContainer);
    }

    /**
     * Ejecuta el agente Sniffer
     */
    public void launchSniffer() {
        sniffer = createAgent("sniffer", "jade.tools.sniffer.Sniffer", null,
                mainContainer);
    }

    public void launchSimpleTestCase() {
        if(testAgentsLaunched)
            return;
        createAgentsFromJSONList(readAgentsDB("test_case_simple.json"));
        testAgentsLaunched = true;
    }

    public void launchComplexTestCase() {
        if(testAgentsLaunched)
            return;
        createAgentsFromJSONList(readAgentsDB("test_case_complex.json"));
        testAgentsLaunched = true;
    }

    public void createAgentsFromJSONList(List<JsonAgent> agents) {
        for(JsonAgent agent : agents) {
            createAgent(agent.getName(), agent.getType(), agent.getDataArray(), agentContainers.get(agent.getType()));
        }
    }

    /**
     * Contrata sicarios para matar los agentes rma, sniffer y el main container
     */
    public void shutdown() {
        SwingUtilities.invokeLater(ui::dispose);

        killAgent(rma);
        killAgent(sniffer);

        try {
            turistasContainer.kill();
            agenciasContainer.kill();
            lugaresContainer.kill();
            transportesContainer.kill();
            mainContainer.kill();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    /**
     * Mata a un agente
     * @param ac Agente al que se va a matar
     */
    private void killAgent(AgentController ac) {
        try {
            if (ac != null) {
                ac.kill();
            }
        } catch(StaleProxyException e) {
            //e.printStackTrace();
        }
    }

    /**
     * Crea un agente de la clase clase asociado a la agencia agencia
     * @param nombre Nombre cualquiera para el agente
     * @param clase Clase del agente que se va a crear
     * @param params Agencia a la que se va a asociar
     * @param cc Contenedor en el que se va a crear el agente
     */
    public AgentController createAgent(String nombre, String clase,
                                     Object[] params, ContainerController cc) {
        AgentController ac = null;
        try {
            ac = cc.createNewAgent(nombre, clase, params);
            ac.start();
        } catch (StaleProxyException e) {
            SwingUtilities.invokeLater( () ->
                   JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
                           JOptionPane.WARNING_MESSAGE));
        }
        return ac;
    }

    /**
     * Lee el archivo en formato JSON con los datos de los agentes
     * @param jsonFile Ruta al archivo JSON con los agentes de prueba
     * @return Devuelve una lista de JsonAgents que contiene los datos de los agentes.
     */
    private List<JsonAgent> readAgentsDB(String jsonFile) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser parser = new JsonParser();
        List<JsonAgent> agents = new ArrayList<>();

        String agentsFile = Util.readFile(jsonFile);
        JsonObject jsonAgents = parser.parse(agentsFile).getAsJsonObject();

        for(Map.Entry<String, Class<? extends JsonAgent>> entry : agentTypes.entrySet()) {
            JsonArray agentArray = jsonAgents.getAsJsonArray(entry.getKey());

            for (JsonElement agentElement : agentArray) {
                JsonAgent agent = gson.fromJson(agentElement, entry.getValue());
                agents.add(agent);
            }
        }
        return agents;
    }

    /**
     * Implementación de un Oberserver para que se actualice la
     * interfaz de crear agente cuando aparezcan nuevas agencias en el DF.
     * @param ui Interfaz crear agente.
     */
    public void registerSubscriber(UiCreateAgent ui) {
        subscriptors.add(ui);
        ui.setAgencias(agencias);
    }

    public void unregisterSubscriber(UiCreateAgent ui) {
        subscriptors.remove(ui);
    }

    private class AgenciasSubscriber extends DFAgentSubscriber {
        public AgenciasSubscriber(Agent a, DFAgentDescription dfad,
                                   List<AID> subscribedAgents) {
            super(a, dfad, subscribedAgents);
        }

        @Override
        protected void onRegister(DFAgentDescription dfad) {
            for(UiCreateAgent ui : subscriptors) {
                ui.setAgencias(agencias);
            }
        }

        @Override
        protected void onDeregister(DFAgentDescription dfad) {
            onRegister(dfad);
        }
    }
}
