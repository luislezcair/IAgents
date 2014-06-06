/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents.ui;

import ia.agents.negotiation.DiscountManager;
import ia.agents.ontology.Alojamiento;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;

public class UILugar {
    private JButton buttonCL;
    private JButton buttonSalir;
    private JTextField textCiudad;
    private JSpinner spinnerCapacidad;
    private String[] catHotel = {"Hotel 5 Estrellas(*****)","Hotel 4 Estrellas(****)","Hotel 3 Estrellas(***)","Hotel 2 Estrellas(**)","Hotel 1 Estrella(*)"};
    private String[] catHostel = {"Premium","Estándar"};
    private String[] catCasaAlq = {"Premium","Estándar"};
    private JComboBox comboTipo;
    private JXDatePicker dateFecha;
    private JTextField textDescuentoIni;
    private JTextField textPrecioPP;
    private JPanel panelLugar;
    private JTextField textDescuentoMax;
    private JTextField textIncDescuento;
    private JRadioButton hotelRadioButton;
    private JRadioButton hostelRadioButton;
    private JRadioButton casaDeAlquilerRadioButton;
    private JFrame mainWindow;

    public UILugar() {

        // Click en Crear Lugar
        buttonCL.addActionListener(event -> {
            // Crea un nuevo lugars
            Alojamiento aloj = new Alojamiento();
            DiscountManager descuento = new DiscountManager();

            if (textCiudad.getText().isEmpty()) {
                this.showMessage("Por favor ingrese la ciudad donde se ubica el alojamiento");
            } else {
                aloj.setDestino(textCiudad.getText());
            }

            if ((Integer) spinnerCapacidad.getValue() <= 0) {
                this.showMessage("Por favor ingrese una capacidad máxima válida");
            } else {
                aloj.setCapacidad((Integer) spinnerCapacidad.getValue());
            }

            aloj.setFecha(dateFecha.getDate());
            aloj.setTipo(comboTipo.getSelectedIndex());

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
                aloj.setPrecioPorPersona(importe);
            }

        });

        // Click en Salir. Elimina la interfaz, el agente sigue funcionando
        buttonSalir.addActionListener(event -> dispose());

        //dateFecha.setDate(new Date());

        // Crear una ventana principal, agrega el contenido y ajusta al tamaño
        mainWindow = new JFrame() ;
        mainWindow.getContentPane().add(panelLugar);
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
        spinnerCapacidad = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        if (hotelRadioButton.isSelected()) {
            comboTipo = new JComboBox(catHotel);
        }else if (hostelRadioButton.isSelected()){
            comboTipo = new JComboBox(catHostel);
        }else if (casaDeAlquilerRadioButton.isSelected()){
            comboTipo = new JComboBox(catCasaAlq);
        }
    }

}
