/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.main;

import ia.agents.negotiation.DiscountManager;
import ia.agents.ontology.Alojamiento;
import ia.agents.ontology.Paquete;
import ia.agents.ontology.Transporte;
import ia.agents.util.DFAgentSubscriber;
import ia.main.ui.UIAgentManager;
import ia.main.ui.UiCreateAgent;
import jade.core.*;
import jade.core.Runtime;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.wrapper.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AgentManager extends Agent {
    private final List<AID> agencias = new ArrayList<>();
    private final List<UiCreateAgent> subscriptors = new ArrayList<>();
    private AgentController rma;
    private AgentController sniffer;
    private UIAgentManager ui;
    private boolean testAgentsLaunched;
    private Runtime rt;

    // Containers:
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

        ui = new UIAgentManager(this);
        ui.show();

        addBehaviour(new AgenciasSubscriber(this, dfad, agencias));
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

    public void launchTestAgents() {
        if(testAgentsLaunched)
            return;

        Paquete paquete = new Paquete("Corrientes", 5, new Date(),
                Paquete.PAGO_EFECTIVO, 1000.0, 5);
        Paquete paquete2 = new Paquete("Chaco", 6, new Date(),
                Paquete.PAGO_TARJETA, 200.0, 7);
        Alojamiento lugar1 = new Alojamiento(10, "Corrientes", new Date(),
                123.0f, Alojamiento.TIPO_CASA_ALQ,
                new DiscountManager(0.1, 0.5, 0.1));
        Alojamiento lugar2 = new Alojamiento(20, "Corrientes", new Date(),
                200.0f, Alojamiento.TIPO_HOTEL,
                new DiscountManager(0.05, 0.3, 0.1));
        Alojamiento lugar3 = new Alojamiento(15, "Corrientes", new Date(),
                230.0f, Alojamiento.TIPO_HOSTEL,
                new DiscountManager(0.07, 0.4, 0.05));
        Alojamiento lugar4 = new Alojamiento(9, "Corrientes", new Date(),
                100.0f, Alojamiento.TIPO_HOTEL,
                new DiscountManager(0.05, 0.5, 0.08));
        Transporte transp1 = new Transporte(30, "Corrientes", new Date(),
                120.0f, Transporte.TIPO_AVION,
                new DiscountManager(0.08, 0.45, 0.05));
        Transporte transp2 = new Transporte(20, "Corrientes", new Date(),
                120.0f, Transporte.TIPO_COLECTIVO,
                new DiscountManager(0.15, 0.6, 0.06));

        List<String> lugar1Agencias = new ArrayList<>();
        List<String> lugar2Agencias = new ArrayList<>();
        List<String> lugar3Agencias = new ArrayList<>();

        lugar1Agencias.add("Agencia86@IAMainPlatform");
        lugar1Agencias.add("Agencia007@IAMainPlatform");
        lugar2Agencias.add("Agencia86@IAMainPlatform");
        lugar3Agencias.add("Agencia007@IAMainPlatform");

        Object turista[] = {paquete};
        Object turista2[] = {paquete2};
        Object lugar1_86[] = {lugar1Agencias, lugar1};
        Object lugar2_86[] = {lugar2Agencias, lugar2};
        Object lugar3_007[] = {lugar3Agencias, lugar3};
        Object lugar4_007[] = {lugar3Agencias, lugar4};
        Object transp_86[] = {lugar2Agencias, transp1};
        Object transp_007[] = {lugar3Agencias, transp2};

        createAgent("Turista1", "ia.agents.AgenteTurista", turista, turistasContainer);
        createAgent("Turista2", "ia.agents.AgenteTurista", turista2, turistasContainer);
        createAgent("Agencia86", "ia.agents.AgenteAgencia", null, agenciasContainer);
        createAgent("Agencia007", "ia.agents.AgenteAgencia", null, agenciasContainer);
        createAgent("Lugar86", "ia.agents.AgenteLugar", lugar1_86, lugaresContainer);
        createAgent("Lugar2_86", "ia.agents.AgenteLugar", lugar2_86, lugaresContainer);
        createAgent("Lugar007", "ia.agents.AgenteLugar", lugar3_007, lugaresContainer);
        createAgent("Lugar2_007", "ia.agents.AgenteLugar", lugar4_007, lugaresContainer);
        createAgent("Transporte86", "ia.agents.AgenteTransporte", transp_86, transportesContainer);
        createAgent("Transporte007", "ia.agents.AgenteTransporte", transp_007, transportesContainer);

        testAgentsLaunched = true;
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
        } catch( StaleProxyException e) {
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
            e.printStackTrace();
        }
        return ac;
    }

    /**
     * Implementación de una especie de Oberserver para que se actualice la
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
