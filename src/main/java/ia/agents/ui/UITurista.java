/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents.ui;

import ia.agents.AgenteTurista;
import ia.agents.ontology.Paquete;
import ia.agents.ui.util.ImprovedFormattedTextField;
import jade.core.AID;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.MaskFormatter;
import java.text.*;
import java.util.List;

/**
 * Interfaz para el agente turista con un formulario para los datos del paquete
 */
public class UITurista {
    private JTextField textDestino;
    private JButton buttonConsultar;
    private JButton buttonOcultar;
    private JSpinner spinnerDias;
    private JSpinner spinnerPersonas;
    private JComboBox comboFormaDePago;
    private JFormattedTextField textImporteMax;
    private JXDatePicker dateFecha;
    private JPanel panelTurista;
    private JList<String> listAgencies;
    private final JFrame mainWindow;

    private final AgenteTurista turista;

    public UITurista(AgenteTurista agente, Paquete paquete) {
        turista = agente;

        // Click en Consultar
        buttonConsultar.addActionListener(event -> {
            // Carga los datos del paquete y envía un CFP a las agencias
            Paquete p = new Paquete();
            p.setDestino(textDestino.getText());
            p.setDias((int) spinnerDias.getValue());
            p.setFecha(dateFecha.getDate());
            p.setFormaDePago(comboFormaDePago.getSelectedIndex());
            p.setImporteMaxPorPersona(Double.valueOf(textImporteMax.getValue().toString()));
            p.setPersonas((int) spinnerPersonas.getValue());
            turista.sendCfp(p);
        });

        // Click en Salir. Elimina la interfaz, el agente sigue funcionando
        buttonOcultar.addActionListener(event -> dispose());

        // Listeners para habilitar/deshabilitar el botón de consulta cuando
        // se modifican los campos de texto.
        textImporteMax.addPropertyChangeListener("editValid",
                e -> setConsultaEnabled());
        textDestino.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) {
                setConsultaEnabled();
            }

            @Override public void removeUpdate(DocumentEvent e) {
                setConsultaEnabled();
            }

            @Override public void changedUpdate(DocumentEvent e) {
                setConsultaEnabled();
            }
        });

        try {
            MaskFormatter decimals = new MaskFormatter("####.##");
            decimals.install(textImporteMax);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setPaquete(paquete);

        textDestino.requestFocus();

        // Crear una ventana principal, agrega el contenido y ajusta al tamaño
        mainWindow = new JFrame(agente.getName());
        mainWindow.getContentPane().add(panelTurista);
        mainWindow.getRootPane().setDefaultButton(buttonConsultar);

        // Carga y establece un ícono para la ventana
        ImageIcon icon = new ImageIcon(
                getClass().getResource("/icons/Turista.png"));
        mainWindow.setIconImage(icon.getImage());

        mainWindow.pack();
        mainWindow.setVisible(true);
        mainWindow.setMinimumSize(mainWindow.getSize());
    }

    public void setConsultaEnabled() {
        buttonConsultar.setEnabled(
            textImporteMax.isEditValid() && !textDestino.getText().isEmpty()
        );
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
     * Destruye la UI y libera recursos
     */
    public void dispose() {
        mainWindow.dispose();
    }

    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(mainWindow, msg, mainWindow.getTitle()
                        + " - Error", JOptionPane.WARNING_MESSAGE);
    }

    private void setPaquete(Paquete p) {
        textDestino.setText(p.getDestino());
        spinnerDias.setValue(p.getDias());
        spinnerPersonas.setValue(p.getPersonas());
        dateFecha.setDate(p.getFecha());
        try {
            comboFormaDePago.setSelectedIndex(p.getFormaDePago());
        } catch(IllegalArgumentException e) {
            comboFormaDePago.setSelectedIndex(-1);
        }
        textImporteMax.setValue(p.getImporteMaxPorPersona());
    }

    private void createUIComponents() {
        listAgencies = new JList<>(new DefaultListModel<>());
        spinnerDias = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        spinnerPersonas = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        comboFormaDePago = new JComboBox<>(Paquete.getFormasDePago());

        textImporteMax = new ImprovedFormattedTextField(
                DecimalFormat.getInstance());
    }
}
