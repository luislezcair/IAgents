/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents.ui;

import ia.agents.AgenteLugar;
import ia.agents.negotiation.DiscountManager;
import ia.agents.ontology.Alojamiento;
import ia.agents.ui.util.ImprovedFormattedTextField;
import ia.agents.util.Util;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UILugar {
    private JButton buttonCL;
    private JButton buttonOcultar;
    private JTextField textCiudad;
    private JSpinner spinnerCapacidad;
    private JComboBox<String> comboTipo;
    private JXDatePicker dateFecha;
    private JFormattedTextField textDescuentoIni;
    private JFormattedTextField textPrecioPP;
    private JPanel panelLugar;
    private JFormattedTextField textDescuentoMax;
    private JFormattedTextField textIncDescuento;
    private JRadioButton hotelRadioButton;
    private JRadioButton hostelRadioButton;
    private JRadioButton casaDeAlquilerRadioButton;
    private JButton buttonGenerar;
    private JFrame mainWindow;
    private List<DefaultComboBoxModel<String>> modelos;
    private final Util util = new Util();

    public UILugar(AgenteLugar agente) {
        // Click en Crear Lugar
        buttonCL.addActionListener(event -> {
            // Obtiene el alojamiento del agente para establecer los valores
            // de los atributos.

            int tipo;
            if(hotelRadioButton.isSelected()) {
                tipo = 0;
            } else if(hostelRadioButton.isSelected()) {
                tipo = 1;
            } else {
                tipo = 2;
            }

            Alojamiento aloj = agente.getAlojamiento();
            DiscountManager desc = aloj.getDescuento();

            aloj.setDestino(textCiudad.getText());
            aloj.setCapacidad((int)spinnerCapacidad.getValue());
            aloj.setFecha(dateFecha.getDate());
            aloj.setTipo(tipo);
            aloj.setCategoria(comboTipo.getSelectedIndex());
            aloj.setPrecioPorPersona(Double.valueOf(textPrecioPP.getValue().toString()));

            desc.setValue(Double.valueOf(textDescuentoIni.getValue().toString()));
            desc.setMax(Double.valueOf(textDescuentoMax.getValue().toString()));
            desc.setStep(Double.valueOf(textIncDescuento.getValue().toString()));

            mainWindow.dispose();
        });

        // Click en Salir. Elimina la interfaz, el agente sigue funcionando
        buttonOcultar.addActionListener(event -> mainWindow.dispose());
        buttonGenerar.addActionListener(event -> {
            generateValues();
            textCiudad.requestFocus();
        });

        // Click en los radio-buttons
        hotelRadioButton.addActionListener(
                e -> comboTipo.setModel(modelos.get(0)));
        hostelRadioButton.addActionListener(
                e -> comboTipo.setModel(modelos.get(1)));
        casaDeAlquilerRadioButton.addActionListener(
                e -> comboTipo.setModel(modelos.get(2)));

        EventQueue.invokeLater(() -> spinnerCapacidad.setValue(1));

        textCiudad.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) {
                setEstablecerEnabled();
            }

            @Override public void removeUpdate(DocumentEvent e) {
                setEstablecerEnabled();
            }

            @Override public void changedUpdate(DocumentEvent e) {
                setEstablecerEnabled();
            }
        });
        dateFecha.addPropertyChangeListener("date", e -> setEstablecerEnabled());
        dateFecha.getEditor().setEditable(false);

        // Crear una ventana principal, agrega el contenido y ajusta al tamaño
        mainWindow = new JFrame("Agente Lugar " + agente.getName()) ;
        mainWindow.getContentPane().add(panelLugar);

        // Carga y establece un ícono para la ventana
        ImageIcon icon = new ImageIcon(
                getClass().getResource("/icons/Lugar.png"));
        mainWindow.setIconImage(icon.getImage());

        mainWindow.pack();
        mainWindow.setVisible(true);
        mainWindow.getRootPane().setDefaultButton(buttonCL);

        mainWindow.setMinimumSize(mainWindow.getSize());

        setAlojamiento(agente.getAlojamiento());
    }

    private void setAlojamiento(Alojamiento a) {
        DiscountManager d = a.getDescuento();

        textCiudad.setText(a.getDestino());
        spinnerCapacidad.setValue(a.getCapacidad());
        dateFecha.setDate(a.getFecha());

        textDescuentoIni.setValue(d.getValue());
        textDescuentoMax.setValue(d.getMax());
        textIncDescuento.setValue(d.getStep());
        textPrecioPP.setValue(a.getPrecioPorPersona());

        selectTipo(a.getTipo());

        try {
            comboTipo.setSelectedIndex(a.getCategoria());
        } catch(Exception e) {
            comboTipo.setSelectedIndex(-1);
        }
    }

    private void setEstablecerEnabled() {
        buttonCL.setEnabled(
            !textCiudad.getText().isEmpty() && dateFecha.getDate() != null
        );
    }

    private void createUIComponents() {
        spinnerCapacidad = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        modelos = new ArrayList<>();
        for(String[] cat : Alojamiento.getCategorias()) {
            modelos.add(new DefaultComboBoxModel<>(cat));
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
                hotelRadioButton.doClick();
                break;
            case 2:
                hostelRadioButton.doClick();
                break;
            case 3:
                casaDeAlquilerRadioButton.doClick();
                break;
        }
    }
}
