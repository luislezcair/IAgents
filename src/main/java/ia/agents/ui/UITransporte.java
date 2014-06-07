/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents.ui;

import ia.agents.AgenteTransporte;
import ia.agents.negotiation.DiscountManager;
import ia.agents.ontology.Transporte;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;

public class UITransporte {
    private JPanel panelTransporte;
    private JButton buttonCT;
    private JButton buttonOcultar;
    private JTextField textCiudad;
    private JXDatePicker dateFecha;
    private String[] catColectivos = {"Suite Premium", "Cama", "Semi-Cama"};
    private String[] catAviones = {"Primera Clase", "Clase Turista"};
    private String[] otros = {"Combi", "Ferrocarril"};
    private DefaultComboBoxModel<String> colectivosModel;
    private DefaultComboBoxModel<String> avionesModel;
    private DefaultComboBoxModel<String> otrosModel;
    private JComboBox<String> comboCategoria;
    private JSpinner spinnerCapacidad;
    private JTextField textDescuentoIni;
    private JTextField textPrecioPP;
    private JTextField textDescuentoMax;
    private JTextField textIncDescuento;
    private JRadioButton avionRadioButton;
    private JRadioButton colectivoRadioButton;
    private JRadioButton otrosRadioButton;
    private JFrame mainWindow;

    public UITransporte(AgenteTransporte agente) {
        // Click en Crear Lugar
        buttonCT.addActionListener(event -> {
            // Obtiene el transporte del agente para establecer los valores
            // de los atributos.
            Transporte transp = agente.getTransporte();
            DiscountManager descuento = transp.getDescuento();

            String ciudad = textCiudad.getText();
            if (ciudad.isEmpty()) {
                this.showMessage(
                        "Por favor ingrese la ciudad destino donde se dirije.");
                return;
            }

            int capacidad = (int) spinnerCapacidad.getValue();
            if (capacidad <= 0) {
                this.showMessage(
                        "Por favor ingrese una capacidad máxima válida.");
                return;
            }

            double descIni;
            String descuentoInicial = textDescuentoIni.getText();
            if (descuentoInicial.isEmpty()) {
                this.showMessage("Por favor ingrese un descuento inicial.");
                return;
            } else {
                try {
                    descIni = Double.valueOf(descuentoInicial);
                } catch (NumberFormatException excepcionInutil) {
                    descIni = 0.0d;
                }
            }

            double descMax;
            String descMaximo = textDescuentoMax.getText();
            if (descMaximo.isEmpty()) {
                this.showMessage("Por favor ingrese un descuento máximo.");
                return;
            } else {
                try {
                    descMax = Double.valueOf(descMaximo);
                } catch (NumberFormatException excepcionInutil) {
                    descMax = 0.0d;
                }
            }

            double incDesc;
            String strIncDesc = textIncDescuento.getText();
            if (strIncDesc.isEmpty()) {
                this.showMessage(
                        "Por favor ingrese la variación del descuento.");
                return;
            } else {
                try {
                    incDesc = Double.valueOf(strIncDesc);
                } catch (NumberFormatException excepcionInutil) {
                    incDesc = 0.0d;
                }
            }

            double importe;
            String strImporte = textPrecioPP.getText();
            if (strImporte.isEmpty()) {
                this.showMessage("Por favor ingrese el precio por persona.");
                return;
            } else {
                try {
                    importe = Double.valueOf(strImporte);
                } catch (NumberFormatException excepcionInutil) {
                    importe = 0.0d;
                }
            }

            transp.setDestino(ciudad);
            transp.setCapacidad(capacidad);
            transp.setFecha(dateFecha.getDate());
            transp.setTipo(comboCategoria.getSelectedIndex());
            descuento.setValue(descIni);
            descuento.setMax(descMax);
            descuento.setStep(incDesc);
            transp.setPrecioPorPersona(importe);

            mainWindow.dispose();
        });

        // Click en Salir. Elimina la interfaz, el agente sigue funcionando
        buttonOcultar.addActionListener(event -> mainWindow.dispose());

        // Click en los radio-buttons
        avionRadioButton.addActionListener(
                e -> comboCategoria.setModel(avionesModel));
        colectivoRadioButton.addActionListener(
                e -> comboCategoria.setModel(colectivosModel));
        otrosRadioButton.addActionListener(
                e -> comboCategoria.setModel(otrosModel));

        // Crear una ventana principal, agrega el contenido y ajusta al tamaño
        mainWindow = new JFrame("Crear agente Transporte");
        mainWindow.getContentPane().add(panelTransporte);
        mainWindow.pack();
        mainWindow.setVisible(true);

        setTransporte(agente.getTransporte());
    }

    private void setTransporte(Transporte t) {
        DiscountManager d = t.getDescuento();

        textCiudad.setText(t.getDestino());
        spinnerCapacidad.setValue(t.getCapacidad());
        dateFecha.setDate(t.getFecha());

        textDescuentoIni.setText(String.valueOf(d.getValue()));
        textDescuentoMax.setText(String.valueOf(d.getMax()));
        textIncDescuento.setText(String.valueOf(d.getStep()));
    }

    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(mainWindow, msg, mainWindow.getTitle()
                + " - Error", JOptionPane.WARNING_MESSAGE);
    }

    private void createUIComponents() {
        spinnerCapacidad = new JSpinner(new SpinnerNumberModel(0, 0, 500, 1));

        avionesModel = new DefaultComboBoxModel<>(catAviones);
        colectivosModel = new DefaultComboBoxModel<>(catColectivos);
        otrosModel = new DefaultComboBoxModel<>(otros);
    }
}
