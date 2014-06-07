/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.main.ui;

import ia.main.AgentManager;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class UIAgentManager {
    private AgentManager manager;
    private JFrame mainWindow;
    private JPanel mainPanel;
    private JButton buttonSalir;
    private JButton buttonTestAgents;
    private JButton buttonRma;
    private JButton buttonSniffer;
    private JButton buttonTransporte;

    public UIAgentManager(AgentManager am) {
        manager = am;

        mainWindow = new JFrame("Sistema Multiagente de Turismo");
        mainWindow.getContentPane().add(mainPanel);
        mainWindow.pack();

        // Al cerrar la ventana terminamos la aplicación
        mainWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                am.shutdown();
            }
        });

        buttonSalir.addActionListener(event -> manager.shutdown());
        buttonTestAgents.addActionListener(event -> manager.launchTestAgents());
        buttonRma.addActionListener(event -> manager.launchRma());
        buttonSniffer.addActionListener(event -> manager.launchSniffer());

        buttonTransporte.addActionListener(event -> manager.createAgent());
    }

    public void show() {
        mainWindow.setVisible(true);
    }

    public void dispose() {
        mainWindow.dispose();
    }
}
