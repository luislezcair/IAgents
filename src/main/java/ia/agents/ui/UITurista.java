/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents.ui;

import ia.agents.AgenteTurista;
import ia.agents.ontology.Paquete;
import jade.core.AID;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.util.Date;
import java.util.List;

/**
 * Interfaz para el agente turista con un formulario para los datos del paquete
 */
public class UITurista {
    private JTextField textDestino;
    private JButton buttonConsultar;
    private JButton buttonSalir;
    private JSpinner spinnerDias;
    private JSpinner spinnerPersonas;
    private JComboBox comboFormaDePago;
    private JTextField textImporteMax;
    private JXDatePicker dateFecha;
    private JPanel mainPanel;
    private JList<String> listAgencies;
    private JFrame mainWindow;

    private final AgenteTurista turista;

    public UITurista(AgenteTurista agente) {
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

        dateFecha.setDate(new Date());

        // Crear una ventana principal, agrega el contenido y ajusta al tamaño
        mainWindow = new JFrame(agente.getName());
        mainWindow.getContentPane().add(mainPanel);
        mainWindow.pack();
    }

    public void setAgenciesList(List<AID> agencies) {
        DefaultListModel<String> model =
                (DefaultListModel<String>)listAgencies.getModel();
        model.clear();
        for(AID aid : agencies) {
            model.addElement(aid.getName());
        }
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

    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(mainWindow, msg, mainWindow.getTitle()
                        + " - Error", JOptionPane.WARNING_MESSAGE);
    }

    public void setPaquete(Paquete p) {
        textDestino.setText(p.getDestino());
        spinnerDias.setValue(p.getDias());
        spinnerPersonas.setValue(p.getPersonas());
        dateFecha.setDate(p.getFecha());
        comboFormaDePago.setSelectedIndex(p.getFormaDePago());
        textImporteMax.setText(String.valueOf(p.getImporteMaxPorPersona()));
    }

    private void createUIComponents() {
        listAgencies = new JList<>(new DefaultListModel<>());
    }
}
