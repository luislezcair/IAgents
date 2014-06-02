/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents.ui;

import ia.agents.AgenteTurista;
import ia.agents.ontology.Paquete;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;

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
    private JPanel touristPanel;
    private JFrame mainWindow;

    private final AgenteTurista turista;

    public UITourist(AgenteTurista agente) {
        turista = agente;

        // Click en Consultar
        buttonConsultar.addActionListener(event -> {
            // Carga los datos del paquete y envía un CFP a las agencias
            Paquete p = new Paquete();

            p.setDestino(textDestino.getText());
            p.setDias((Integer)spinnerDias.getValue());
            p.setFecha(dateFecha.getDate());
            p.setFormaDePago(comboFormaDePago.getSelectedIndex());

            // En vez de devolver cero, Javita se quiere pasar de listo
            double importe;
            try {
                importe = Double.valueOf(textImporteMax.getText());
            } catch (NumberFormatException excepcionInutil) {
                importe = 0.0d;
            }
            p.setImporteMaxPorPersona(importe);

            p.setPersonas((Integer)spinnerPersonas.getValue());

            turista.sendCfp(p);
        });

        // Click en Salir. Elimina la interfaz, el agente sigue funcionando
        buttonSalir.addActionListener(event -> dispose());

        // Crear una ventana principal, agrega el contenido y ajusta al tamaño
        mainWindow = new JFrame("Agente Turista");
        mainWindow.getContentPane().add(touristPanel);
        mainWindow.pack();
    }

    /**
     * Muestra la ventana principal
     */
    public void show() {
        mainWindow.setVisible(true);
    }

    /**
     * Destruye la UI y libera recursos
     */
    public void dispose() {
        mainWindow.dispose();
    }
}
