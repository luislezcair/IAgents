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
    private final AgentManager manager;
    private final JFrame mainWindow;
    private JPanel mainPanel;
    private JButton buttonSalir;
    private JButton buttonComplexTestCase;
    private JButton buttonRma;
    private JButton buttonSniffer;
    private JButton buttonTransporte;
    private JButton buttonLugar;
    private JButton buttonAgencia;
    private JButton buttonTurista;
    private JButton buttonSimpleTestCase;

    public UIAgentManager(AgentManager am) {
        manager = am;

        mainWindow = new JFrame("Sistema Multiagente de Turismo");
        mainWindow.getContentPane().add(mainPanel);

        // Carga y establece un ícono para la ventana
        ImageIcon icon = new ImageIcon(
                getClass().getResource("/icons/MainWindow.png"));
        mainWindow.setIconImage(icon.getImage());

        mainWindow.pack();

        // Coloca la ventana en el centro de la pantalla
        mainWindow.setLocationRelativeTo(null);

        // Al cerrar la ventana terminamos la aplicación
        mainWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                am.shutdown();
            }
        });

        mainWindow.setMinimumSize(mainWindow.getSize());

        buttonSalir.addActionListener(event -> manager.shutdown());
        buttonComplexTestCase.addActionListener(event -> manager.launchComplexTestCase());
        buttonSimpleTestCase.addActionListener(event -> manager.launchSimpleTestCase());
        buttonRma.addActionListener(event -> manager.launchRma());
        buttonSniffer.addActionListener(event -> manager.launchSniffer());

        buttonTransporte.addActionListener(event ->
                new UiCreateAgent(manager, "Transporte",
                        "ia.agents.AgenteTransporte", true,
                        am.getContainerTransportes()));

        buttonLugar.addActionListener(event ->
                new UiCreateAgent(manager, "Lugar", "ia.agents.AgenteLugar",
                        true, am.getContainerLugares()));

        buttonAgencia.addActionListener(event ->
                new UiCreateAgent(manager, "Agencia", "ia.agents.AgenteAgencia",
                        false, am.getContainerAgencias()));

        buttonTurista.addActionListener(event ->
                new UiCreateAgent(manager, "Turista", "ia.agents.AgenteTurista",
                        false, am.getContainerTuristas()));
    }

    public void show() {
        mainWindow.setVisible(true);
    }

    public void dispose() {
        mainWindow.dispose();
    }
}
