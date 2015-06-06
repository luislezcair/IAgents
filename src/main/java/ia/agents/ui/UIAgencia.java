/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents.ui;

import ia.agents.AgenteAgencia;
import jade.core.AID;
import javax.swing.*;
import java.util.List;

public class UIAgencia {
    private JPanel panelAgencia;
    private JButton buttonOcultar;
    private JList<String> listServicios;
    private JLabel labelAgencia;
    private JFrame mainWindow;

    public UIAgencia(AgenteAgencia agente) {
        labelAgencia.setText(agente.getName());

        buttonOcultar.addActionListener(e -> mainWindow.dispose());

        // Crea una ventana con el nombre del agente como título y la muestra
        mainWindow = new JFrame("Agencia " + agente.getName());
        mainWindow.getContentPane().add(panelAgencia);

        // Carga y establece un ícono para la ventana
        ImageIcon icon = new ImageIcon(
                getClass().getResource("/icons/Agencia.png"));
        mainWindow.setIconImage(icon.getImage());

        mainWindow.pack();
    }

    public void show() {
        mainWindow.setVisible(true);
    }

    public void setServicios(List<AID> servicios) {
        DefaultListModel<String> model =
                (DefaultListModel<String>) listServicios.getModel();
        model.clear();
        for(AID aid : servicios) {
            model.addElement(aid.getName());
        }
    }

    private void createUIComponents() {
        listServicios = new JList<>(new DefaultListModel<>());
    }
}
