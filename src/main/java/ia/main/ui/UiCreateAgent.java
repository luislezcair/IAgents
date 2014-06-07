package ia.main.ui;

import ia.main.AgentManager;
import jade.core.AID;

import javax.swing.*;
import java.util.List;

/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */
public class UiCreateAgent {
    private JPanel mainPanel;
    private JButton buttonCancelar;
    private JButton buttonCrearAgente;
    private JTextField textNombreAgente;
    private JComboBox<String> comboAgencias;
    private JLabel labelAgencia;
    private JPanel panelCreateAgent;
    private JFrame window;

    public UiCreateAgent(AgentManager am, String tipoAgente, String claseAgente,
                         boolean hasAgency) {
        if(hasAgency) {
            setAgencias(am.getAgencias());
        }

        labelAgencia.setVisible(hasAgency);
        comboAgencias.setVisible(hasAgency);

        window = new JFrame("Crear agente " + tipoAgente);
        window.getContentPane().add(mainPanel);
        window.getRootPane().setDefaultButton(buttonCrearAgente);
        window.pack();
        window.setVisible(true);

        buttonCancelar.addActionListener(e -> window.dispose());
        buttonCrearAgente.addActionListener(e -> {
            Object[] params = {comboAgencias.getModel().getSelectedItem()};
            am.createAgent(textNombreAgente.getText(), claseAgente, params);
            window.dispose();});

        panelCreateAgent.setBorder(
               BorderFactory.createTitledBorder("Crear agente " + tipoAgente));
    }

    /**
     * Carga el comboBox con la lista de agencias.
     * @param agencias Lista de agencias registradas en el DF.
     */
    private void setAgencias(List<AID> agencias) {
        DefaultComboBoxModel<String> model =
                (DefaultComboBoxModel<String>) comboAgencias.getModel();
        model.removeAllElements();
        for(AID aid : agencias) {
            model.addElement(aid.getName());
        }
    }

    private void createUIComponents() {
        comboAgencias = new JComboBox<>(new DefaultComboBoxModel<>());
    }
}
