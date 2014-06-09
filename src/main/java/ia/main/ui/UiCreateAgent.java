/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.main.ui;

import ia.main.AgentManager;
import jade.core.AID;
import jade.wrapper.ContainerController;

import javax.swing.*;
import java.util.List;

public class UiCreateAgent {
    private JPanel mainPanel;
    private JButton buttonCancelar;
    private JButton buttonCrearAgente;
    private JTextField textNombreAgente;
    private JLabel labelAgencia;
    private JPanel panelCreateAgent;
    private JList<String> listAgencias;
    private JScrollPane paneAgencia;
    private JLabel labelHelp;
    private final JFrame window;

    public UiCreateAgent(AgentManager am, String tipoAgente, String claseAgente,
                         boolean hasAgency, ContainerController cc) {
        labelAgencia.setVisible(hasAgency);
        paneAgencia.setVisible(hasAgency);
        labelHelp.setVisible(hasAgency);

        window = new JFrame("Crear agente " + tipoAgente);
        window.getContentPane().add(mainPanel);
        window.getRootPane().setDefaultButton(buttonCrearAgente);
        window.pack();

        // Coloca la ventana en el centro de la pantalla
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        buttonCancelar.addActionListener(
                e -> {window.dispose(); am.unregisterSubscriber(this);});
        buttonCrearAgente.addActionListener(e -> {
            Object[] params = { listAgencias.getSelectedValuesList() };
            am.createAgent(textNombreAgente.getText(), claseAgente, params, cc);
            window.dispose();});

        panelCreateAgent.setBorder(
               BorderFactory.createTitledBorder("Crear agente " + tipoAgente));

        if(hasAgency) {
            am.registerSubscriber(this);
        }
    }

    /**
     * Carga el comboBox con la lista de agencias.
     * @param agencias Lista de agencias registradas en el DF.
     */
    public void setAgencias(List<AID> agencias) {
        DefaultListModel<String> model =
                (DefaultListModel<String>) listAgencias.getModel();
        model.removeAllElements();
        for(AID aid : agencias) {
            model.addElement(aid.getName());
        }

        // Actualiza la ventana para reajustarse al contenido
        SwingUtilities.invokeLater(window::pack);
    }

    private void createUIComponents() {
        listAgencias = new JList<>(new DefaultListModel<>());
    }
}
