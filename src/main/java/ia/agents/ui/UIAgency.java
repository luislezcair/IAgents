/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents.ui;

import ia.agents.Agencia;

import javax.swing.*;
import java.awt.*;

public class UIAgency {
    private Agencia agencia;
    private JPanel mainPanel;
    private JLabel labelAgentInfo;
    private JFrame mainFrame;

    public UIAgency(Agencia a) {
        agencia = a;

        // Crea una ventana con el nombre del agente como título
        mainFrame = new JFrame(agencia.getLocalName());
    }

    public void setupUi() {
        labelAgentInfo.setText("Hola, soy " + agencia.getName() +
                " y esta es mi interfaz");

        // Agrega el contenido de la ventana
        mainFrame.getContentPane().add(mainPanel, BorderLayout.CENTER);
        // y la muestra
        mainFrame.setVisible(true);
    }
}
