/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents.ui;

import ia.agents.AgenteAgencia;

import javax.swing.*;
import java.awt.*;

public class UIAgency {
    private AgenteAgencia agenteAgencia;
    private JPanel mainPanel;
    private JLabel labelAgentInfo;
    private JFrame mainFrame;

    public UIAgency(AgenteAgencia a) {
        agenteAgencia = a;

        // Crea una ventana con el nombre del agente como título
        mainFrame = new JFrame(agenteAgencia.getLocalName());
    }

    public void setupUi() {
        labelAgentInfo.setText("Hola, soy " + agenteAgencia.getName() +
                " y esta es mi interfaz");

        // Agrega el contenido de la ventana
        mainFrame.getContentPane().add(mainPanel, BorderLayout.CENTER);
        // y la muestra
        mainFrame.setVisible(true);
    }
}
