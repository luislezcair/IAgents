/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents.ui;

import ia.agents.AgenteTurista;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Interfaz para el agente turista con un formulario para los datos del paquete
 */
public class UITourist {
    private JTextField textDestino;
    private JButton buttonConsultar;
    private JButton buttonSalir;
    private JSpinner spinnerDias;
    private JSpinner spinnerPersonas;
    private JComboBox comboFormaDePago;
    private JTextField textImporteMax;
    private JXDatePicker dateFecha;
    private JPanel mainPanel;
    private JFrame mainWindow;

    private AgenteTurista turista;

    public UITourist(AgenteTurista agente) {
        turista = agente;

        buttonConsultar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                turista.sendCfp();
            }
        });

        buttonSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainWindow.dispose();
            }
        });

        // Crear una ventana principal, agrega el contenido y ajusta al tamaño
        mainWindow = new JFrame("Agente Turista");
        mainWindow.getContentPane().add(mainPanel);
        mainWindow.pack();
    }

    /**
     * Muestra la ventana principal
     */
    public void show() {
        mainWindow.setVisible(true);
    }

    /**
     * Habilita o deshabilita el botón de consultar
     * TODO: comprobar que los datos sean válidos antes de permitir consultas
     * @param enabled
     */
    public void setConsultaEnabled(boolean enabled) {
        buttonConsultar.setEnabled(enabled);
    }
}
