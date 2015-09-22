/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents.ui;

import ia.agents.AgenteTransporte;
import ia.agents.negotiation.DiscountManager;
import ia.agents.ontology.Transporte;
import ia.agents.ui.util.ImprovedFormattedTextField;
import ia.agents.util.Util;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UITransporte {
    private JPanel panelTransporte;
    private JButton buttonCT;
    private JButton buttonOcultar;
    private JTextField textCiudad;
    private JXDatePicker dateFecha;
    private JComboBox<String> comboCategoria;
    private JSpinner spinnerCapacidad;
    private JFormattedTextField textDescuentoIni;
    private JFormattedTextField textPrecioPP;
    private JFormattedTextField textDescuentoMax;
    private JFormattedTextField textIncDescuento;
    private JRadioButton avionRadioButton;
    private JRadioButton colectivoRadioButton;
    private JRadioButton otrosRadioButton;
    private JButton buttonGenerar;
    private JFrame mainWindow;
    private List<DefaultComboBoxModel<String>> modelos;
    private final Util util = new Util();

    public UITransporte(AgenteTransporte agente) {
        // Click en Crear Lugar
        buttonCT.addActionListener(event -> {
            // Obtiene el transporte del agente para establecer los valores
            // de los atributos.

            int tipo;
            if(avionRadioButton.isSelected()) {
                tipo = 0;
            } else if(colectivoRadioButton.isSelected()) {
                tipo = 1;
            } else {
                tipo = 2;
            }

            Transporte t = agente.getTransporte();
            DiscountManager d = t.getDescuento();

            t.setDestino(textCiudad.getText());
            t.setCapacidad((int)spinnerCapacidad.getValue());
            t.setFecha(dateFecha.getDate());
            t.setTipo(tipo);
            t.setCategoria(comboCategoria.getSelectedIndex());
            t.setPrecioPorPersona(Double.valueOf(textPrecioPP.getValue().toString()));

            d.setValue(Double.valueOf(textDescuentoIni.getValue().toString()));
            d.setMax(Double.valueOf(textDescuentoMax.getValue().toString()));
            d.setStep(Double.valueOf(textIncDescuento.getValue().toString()));

            mainWindow.dispose();
        });

        // Click en Salir. Elimina la interfaz, el agente sigue funcionando
        buttonOcultar.addActionListener(event -> mainWindow.dispose());
        buttonGenerar.addActionListener(event -> {
            generateValues();
            textCiudad.requestFocus();
        });

        // Click en los radio-buttons
        avionRadioButton.addActionListener(
                e -> comboCategoria.setModel(modelos.get(0)));
        colectivoRadioButton.addActionListener(
                e -> comboCategoria.setModel(modelos.get(1)));
        otrosRadioButton.addActionListener(
                e -> comboCategoria.setModel(modelos.get(2)));

        textCiudad.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                setEstablecerEnabled();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                setEstablecerEnabled();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                setEstablecerEnabled();
            }
        });
        dateFecha.addPropertyChangeListener("date", e -> setEstablecerEnabled());
        dateFecha.getEditor().setEditable(false);

        // Crear una ventana principal, agrega el contenido y ajusta al tamaño
        mainWindow = new JFrame("Agente Transporte " + agente.getName());
        mainWindow.getContentPane().add(panelTransporte);

        // Carga y establece un ícono para la ventana
        ImageIcon icon = new ImageIcon(
                getClass().getResource("/icons/Transporte.png"));
        mainWindow.setIconImage(icon.getImage());

        mainWindow.pack();
        mainWindow.setVisible(true);
        mainWindow.getRootPane().setDefaultButton(buttonCT);

        mainWindow.setMinimumSize(mainWindow.getSize());

        setTransporte(agente.getTransporte());
    }

    private void setTransporte(Transporte t) {
        DiscountManager d = t.getDescuento();

        textCiudad.setText(t.getDestino());
        spinnerCapacidad.setValue(t.getCapacidad());
        dateFecha.setDate(t.getFecha());

        textDescuentoIni.setValue(d.getValue());
        textDescuentoMax.setValue(d.getMax());
        textIncDescuento.setValue(d.getStep());
        textPrecioPP.setValue(t.getPrecioPorPersona());

        selectTipo(t.getTipo());

        try {
            comboCategoria.setSelectedIndex(t.getCategoria());
        } catch(Exception e) {
            comboCategoria.setSelectedIndex(-1);
        }
    }

    private void setEstablecerEnabled() {
        buttonCT.setEnabled(
                !textCiudad.getText().isEmpty() && dateFecha.getDate() != null
        );
    }

    private void showMessage(String msg) {
        JOptionPane.showMessageDialog(mainWindow, msg, mainWindow.getTitle()
                + " - Error", JOptionPane.WARNING_MESSAGE);
    }

    private void createUIComponents() {
        spinnerCapacidad = new JSpinner(new SpinnerNumberModel(0, 0, 500, 1));
        modelos = new ArrayList<>();
        for(String[] s : Transporte.getCategorias()) {
            modelos.add(new DefaultComboBoxModel<>(s));
        }
        NumberFormat decimals = DecimalFormat.getInstance();
        textPrecioPP = new ImprovedFormattedTextField(decimals);
        textDescuentoIni = new ImprovedFormattedTextField(decimals);
        textDescuentoMax = new ImprovedFormattedTextField(decimals);
        textIncDescuento = new ImprovedFormattedTextField(decimals);
    }

    /**
     * Genera valores aleatorios para los campos para que sea más fácil
     * completar los datos.
     */
    private void generateValues() {
        spinnerCapacidad.setValue(util.getRandomNumber(10, 50));
        dateFecha.setDate(new Date());
        textPrecioPP.setValue(util.getRandomNumber("300.0", "1000.0"));
        textDescuentoIni.setValue(util.getRandomNumber("0.0", "0.2"));
        textDescuentoMax.setValue(util.getRandomNumber("0.2", "0.5"));
        textIncDescuento.setValue(util.getRandomNumber("0.01", "0.1"));

        selectTipo(util.getRandomNumber(1, 3));
    }

    private void selectTipo(int tipo) {
        switch(tipo) {
            case 1:
                colectivoRadioButton.doClick();
                break;
            case 2:
                avionRadioButton.doClick();
                break;
            case 3:
                otrosRadioButton.doClick();
                break;
        }
    }
}
