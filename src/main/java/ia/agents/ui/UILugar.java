/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents.ui;

import ia.agents.AgenteLugar;
import ia.agents.negotiation.DiscountManager;
import ia.agents.ontology.Alojamiento;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;

public class UILugar {
    private JButton buttonCL;
    private JButton buttonOcultar;
    private JTextField textCiudad;
    private JSpinner spinnerCapacidad;
    private String[] catHotel = {"Hotel 5 Estrellas (*****)",
                                 "Hotel 4 Estrellas (****)",
                                 "Hotel 3 Estrellas (***)",
                                 "Hotel 2 Estrellas (**)",
                                 "Hotel 1 Estrella (*)"};
    private String[] catHostel = {"Premium", "Estándar"};
    private String[] catCasaAlq = {"Premium", "Estándar"};
    private DefaultComboBoxModel<String> catHotelModel;
    private DefaultComboBoxModel<String> catHostelModel;
    private DefaultComboBoxModel<String> catCasaAlqModel;
    private JComboBox<String> comboTipo;
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

    public UILugar(AgenteLugar agente) {
        // Click en Crear Lugar
        buttonCL.addActionListener(event -> {
            // Obtiene el alojamiento del agente para establecer los valores
            // de los atributos.
            Alojamiento aloj = agente.getAlojamiento();
            DiscountManager descuento = aloj.getDescuento();

            String ciudad = textCiudad.getText();
            if (ciudad.isEmpty()) {
                this.showMessage(
                   "Por favor ingrese la ciudad donde se ubica el alojamiento");
                return;
            }

            int capacidad = (int)spinnerCapacidad.getValue();
            if(capacidad <= 0) {
                this.showMessage(
                        "Por favor ingrese una capacidad máxima válida");
                return;
            }

            double descIni;
            if (textDescuentoIni.getText().isEmpty()) {
                this.showMessage("Por favor ingrese un descuento inicial");
                return;
            } else {
                try {
                    descIni = Double.valueOf(textDescuentoIni.getText());
                } catch (NumberFormatException excepcionInutil) {
                    descIni = 0.0d;
                }
            }

            double descMax;
            if (textDescuentoMax.getText().isEmpty()) {
                this.showMessage("Por favor ingrese un descuento máximo");
                return;
            } else {
                try {
                    descMax = Double.valueOf(textDescuentoMax.getText());
                } catch (NumberFormatException excepcionInutil) {
                    descMax = 0.0d;
                }
            }

            double incDesc;
            if (textIncDescuento.getText().isEmpty()) {
                this.showMessage(
                        "Por favor ingrese la variación del descuento");
                return;
            } else {
                try {
                    incDesc = Double.valueOf(textIncDescuento.getText());
                } catch (NumberFormatException excepcionInutil) {
                    incDesc = 0.0d;
                }
            }

            double importe;
            if (textPrecioPP.getText().isEmpty()) {
                this.showMessage("Por favor ingrese el precio por persona");
                return;
            } else {
                try {
                    importe = Double.valueOf(textPrecioPP.getText());
                } catch (NumberFormatException excepcionInutil) {
                    importe = 0.0d;
                }
            }

            aloj.setDestino(ciudad);
            aloj.setCapacidad(capacidad);
            aloj.setFecha(dateFecha.getDate());
            aloj.setTipo(comboTipo.getSelectedIndex());
            aloj.setPrecioPorPersona(importe);
            descuento.setValue(descIni);
            descuento.setMax(descMax);
            descuento.setStep(incDesc);

            mainWindow.dispose();
        });

        // Click en Salir. Elimina la interfaz, el agente sigue funcionando
        buttonOcultar.addActionListener(event -> mainWindow.dispose());

        // Click en los radio-buttons
        hotelRadioButton.addActionListener(
                e -> comboTipo.setModel(catHotelModel));
        hostelRadioButton.addActionListener(
                e -> comboTipo.setModel(catHostelModel));
        casaDeAlquilerRadioButton.addActionListener(
                e -> comboTipo.setModel(catCasaAlqModel));

        // Crear una ventana principal, agrega el contenido y ajusta al tamaño
        mainWindow = new JFrame("Crear agente Lugar") ;
        mainWindow.getContentPane().add(panelLugar);
        mainWindow.pack();
        mainWindow.setVisible(true);

        setAlojamiento(agente.getAlojamiento());
    }

    private void setAlojamiento(Alojamiento a) {
        DiscountManager d = a.getDescuento();

        textCiudad.setText(a.getDestino());
        spinnerCapacidad.setValue(a.getCapacidad());
        dateFecha.setDate(a.getFecha());

        textDescuentoIni.setText(String.valueOf(d.getValue()));
        textDescuentoMax.setText(String.valueOf(d.getMax()));
        textIncDescuento.setText(String.valueOf(d.getStep()));
    }

    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(mainWindow, msg, mainWindow.getTitle()
                + " - Error", JOptionPane.WARNING_MESSAGE);
    }

    private void createUIComponents() {
        spinnerCapacidad = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        catHotelModel = new DefaultComboBoxModel<>(catHotel);
        catHostelModel = new DefaultComboBoxModel<>(catHostel);
        catCasaAlqModel = new DefaultComboBoxModel<>(catCasaAlq);
    }
}
