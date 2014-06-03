/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents.ui;

import ia.agents.AgenteAgencia;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;

public class UIAgencia {
    private final AgenteAgencia agenteAgencia;
    private JPanel agencyPanel;
    private JComboBox comboAgencia;
    private JButton buttonConsultar;
    private JButton buttonSalir;
    private JList listaLugares;
    private JList listaTransportes;
    private JLabel labelAgentInfo;
    private JPanel panelAgencia;
    private JComboBox comboAgencias;
    private JList listLugares;
    private JList listTransportes;
    private JFrame mainFrame;
    private JComboBox comboTipo;
    private JList listAgencias;

    public UIAgencia(AgenteAgencia a) {
        agenteAgencia = a;

        // Crea una ventana con el nombre del agente como título
        mainFrame = new JFrame(agenteAgencia.getLocalName());
        mainFrame.getContentPane().add(agencyPanel);

        labelAgentInfo.setText("Hola, soy " + agenteAgencia.getName() +
                " y esta es mi interfaz");
    }

    public void showUi() {
        mainFrame.setVisible(true);
    }
}
