/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents.ui;

import ia.agents.AgenteAgencia;

import javax.swing.*;

public class UIAgency {
    private final AgenteAgencia agenteAgencia;
    private JPanel mainPanel;
    private JLabel labelAgentInfo;
    private JFrame mainFrame;

    public UIAgency(AgenteAgencia a) {
        agenteAgencia = a;

        // Crea una ventana con el nombre del agente como título
        mainFrame = new JFrame(agenteAgencia.getLocalName());
        mainFrame.getContentPane().add(mainPanel);

        labelAgentInfo.setText("Hola, soy " + agenteAgencia.getName() +
                " y esta es mi interfaz");
    }

    public void showUi() {
        mainFrame.setVisible(true);
    }
}
