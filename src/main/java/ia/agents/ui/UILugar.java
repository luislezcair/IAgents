/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents.ui;

import ia.agents.AgenteLugar;
import ia.agents.negotiation.DiscountManager;
import ia.agents.ontology.Alojamiento;
import ia.agents.util.Util;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.util.Date;

public class UILugar {
    private JButton buttonCL;
    private JButton buttonOcultar;
    private JTextField textCiudad;
    private JSpinner spinnerCapacidad;
    private final String[] catHotel = {"Hotel 5 Estrellas (*****)",
                                 "Hotel 4 Estrellas (****)",
                                 "Hotel 3 Estrellas (***)",
                                 "Hotel 2 Estrellas (**)",
                                 "Hotel 1 Estrella (*)"};
    private final String[] catHostel = {"Premium", "Estándar"};
    private final String[] catCasaAlq = {"Premium", "Estándar"};
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
    private JButton buttonGenerar;
    private JFrame mainWindow;
    private final Util util = new Util();

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

            int tipo;
            if(hotelRadioButton.isSelected()) {
                tipo = 0;
            } else if(hostelRadioButton.isSelected()) {
                tipo = 1;
            } else {
                tipo = 2;
            }

            aloj.setDestino(ciudad);
            aloj.setCapacidad(capacidad);
            aloj.setFecha(dateFecha.getDate());
            aloj.setTipo(tipo);
            aloj.setPrecioPorPersona(importe);
            descuento.setValue(descIni);
            descuento.setMax(descMax);
            descuento.setStep(incDesc);

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
        mainWindow.getRootPane().setDefaultButton(buttonCL);

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

    private void showMessage(String msg) {
        JOptionPane.showMessageDialog(mainWindow, msg, mainWindow.getTitle()
                + " - Error", JOptionPane.WARNING_MESSAGE);
    }

    private void createUIComponents() {
        spinnerCapacidad = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        catHotelModel = new DefaultComboBoxModel<>(catHotel);
        catHostelModel = new DefaultComboBoxModel<>(catHostel);
        catCasaAlqModel = new DefaultComboBoxModel<>(catCasaAlq);
    }

    /**
     * Genera valores aleatorios para los campos para que sea más fácil 
     * completar los datos.
     */
    private void generateValues() {
        spinnerCapacidad.setValue(util.getRandomNumber(10, 50));
        dateFecha.setDate(new Date());
        textPrecioPP.setText(util.getRandomNumberString("300.0", "1000.0"));
        textDescuentoIni.setText(util.getRandomNumberString("0.0", "0.2"));
        textDescuentoMax.setText(util.getRandomNumberString("0.2", "0.5"));
        textIncDescuento.setText(util.getRandomNumberString("0.01", "0.1"));

        int tipo = util.getRandomNumber(1, 3);
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
