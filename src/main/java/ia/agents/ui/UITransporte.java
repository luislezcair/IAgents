/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents.ui;

import ia.agents.negotiation.DiscountManager;
import ia.agents.ontology.Transporte;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;

public class UITransporte {
    private JPanel panelTransporte;
    private JButton buttonCT;
    private JButton buttonSalir;
    private JTextField textCiudad;
    private JXDatePicker dateFecha;
    private String[] catColectivos = {"Suite Premium","Cama","Semi-Cama",};
    private String[] catAviones = {"Primera Clase","Clase Turista"};
    private String[] otros = {"Combi","Ferrocarril"};
    private JComboBox comboCategoria;
    private JSpinner spinnerCapacidad;
    private JTextField textDescuentoIni;
    private JTextField textPrecioPP;
    private JTextField textDescuentoMax;
    private JTextField textIncDescuento;
    private JRadioButton avionRadioButton;
    private JRadioButton colectivoRadioButton;
    private JRadioButton otrosRadioButton;
    private JFrame mainWindow;


    public UITransporte() {

        // Click en Crear Lugar
        buttonCT.addActionListener(event -> {
            // Crea un nuevo lugars
            Transporte transp = new Transporte();
            DiscountManager descuento = new DiscountManager();

            if (textCiudad.getText().isEmpty()) {
                this.showMessage("Por favor ingrese la ciudad destino donde se dirije");
            } else {
                transp.setDestino(textCiudad.getText());
            }

            if ((Integer) spinnerCapacidad.getValue() <= 0) {
                this.showMessage("Por favor ingrese una capacidad máxima válida");
            } else {
                transp.setCapacidad((Integer) spinnerCapacidad.getValue());
            }

            transp.setFecha(dateFecha.getDate());
            transp.setTipo(comboCategoria.getSelectedIndex());

            double descIni;
            if (textDescuentoIni.getText().isEmpty()) {
                this.showMessage("Por favor ingrese un descuento inicial");
            } else {
                try {
                    descIni = Double.valueOf(textDescuentoIni.getText());
                } catch (NumberFormatException excepcionInutil) {
                    descIni = 0.0d;
                }
                descuento.setValue(descIni);
            }

            double descMax;
            if (textDescuentoMax.getText().isEmpty()) {
                this.showMessage("Por favor ingrese un descuento máximo");
            } else {
                try {
                    descMax = Double.valueOf(textDescuentoMax.getText());
                } catch (NumberFormatException excepcionInutil) {
                    descMax = 0.0d;
                }
                descuento.setMax(descMax);
            }

            double incDesc;
            if (textIncDescuento.getText().isEmpty()) {
                this.showMessage("Por favor ingrese la variación del descuento");
            } else {
                try {
                    incDesc = Double.valueOf(textIncDescuento.getText());
                } catch (NumberFormatException excepcionInutil) {
                    incDesc = 0.0d;
                }
                descuento.setStep(incDesc);
            }

            double importe;
            if (textPrecioPP.getText().isEmpty()) {
                this.showMessage("Por favor ingrese el precio por persona");
            } else {
                try {
                    importe = Double.valueOf(textPrecioPP.getText());
                } catch (NumberFormatException excepcionInutil) {
                    importe = 0.0d;
                }
                transp.setPrecioPorPersona(importe);
            }

        });

        // Click en Salir. Elimina la interfaz, el agente sigue funcionando
        buttonSalir.addActionListener(event -> dispose());

        //dateFecha.setDate(new Date());

        // Crear una ventana principal, agrega el contenido y ajusta al tamaño
        mainWindow = new JFrame();
        mainWindow.getContentPane().add(panelTransporte);
        mainWindow.pack();
        //spinnerPersonas.addComponentListener(new ComponentAdapter() {
        //});
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

    private void createUIComponents() {
        spinnerCapacidad = new JSpinner(new SpinnerNumberModel(0, 0, 500, 1));

        if (avionRadioButton.isSelected()) {
            comboCategoria = new JComboBox(catAviones);
        }
        else if (colectivoRadioButton.isSelected()){
            comboCategoria = new JComboBox(catColectivos);
        }
        else if (otrosRadioButton.isSelected()){
            comboCategoria = new JComboBox(otros);
        }
    }
}
