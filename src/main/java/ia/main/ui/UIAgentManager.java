/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.main.ui;

import ia.main.AgentManager;

import javax.swing.*;

public class UIAgentManager {
    private AgentManager manager;
    private JFrame mainWindow;
    private JPanel mainPanel;
    private JButton buttonSalir;
    private JButton buttonTestAgents;
    private JButton buttonRma;

    public UIAgentManager(AgentManager am) {
        manager = am;

        mainWindow = new JFrame("Sistema Multiagente de Turismo");
        mainWindow.getContentPane().add(mainPanel);
        mainWindow.pack();

        buttonSalir.addActionListener(event -> manager.shutdown());
        buttonTestAgents.addActionListener(event -> manager.launchTestAgents());
        buttonRma.addActionListener(event -> manager.launchRma());
    }

    public void show() {
        mainWindow.setVisible(true);
    }

    public void dispose() {
        mainWindow.dispose();
    }
}
