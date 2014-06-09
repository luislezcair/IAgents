package ia.agents.ui;

import ia.agents.ontology.*;
import jade.core.Agent;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class UIServicioAgencia extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel labelDestinoA;
    private JLabel labelTipoA;
    private JLabel labelCapacidadA;
    private JLabel labelFechaA;
    private JLabel labelPrecioA;
    private JLabel labelDescuentoA;
    private JLabel labelDestinoT;
    private JLabel labelTipoT;
    private JLabel labelCapacidadT;
    private JLabel labelFechaT;
    private JLabel labelPrecioT;
    private JLabel labelDescuentoT;
    private JLabel labelMontoA;
    private JLabel labelMontoT;
    private JLabel labelTotal;
    private JComboBox<String> comboPropuestas;
    private JLabel labelPersonasDias;
    private JLabel labelEstado;
    private List<PaqueteAgencia> propuestas;

    public UIServicioAgencia(Agent turista,
                             List<PaqueteAgencia> listaPropuestas) {
        propuestas = listaPropuestas;

        buttonOK.addActionListener(e -> dispose());
        buttonCancel.addActionListener(e -> dispose());

        comboPropuestas.addActionListener(e ->
                setPropuesta(comboPropuestas.getSelectedIndex()));

        // Cargamos el comboBox con las propuestas
        DefaultComboBoxModel<String> model =
                (DefaultComboBoxModel<String>) comboPropuestas.getModel();
        for(PaqueteAgencia pa : propuestas) {
            model.addElement("Propuesta de la agencia " +
                    pa.getAgencia().getLocalName());
        }

        // Cargamos la primera propuesta, que es la mejor
        comboPropuestas.setSelectedIndex(0);
        setPropuesta(0);

        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Propuestas para el turista " + turista.getLocalName());
        pack();
        setVisible(true);
    }

    private void setPropuesta(int index) {
        PaqueteAgencia pa = propuestas.get(index);
        Alojamiento a = pa.getAlojamiento();
        Transporte t = pa.getTransporte();
        Paquete p = pa.getPaquete();

        labelDestinoA.setText(a.getDestino());
        labelTipoA.setText(String.valueOf(a.getTipo())); //TODO: tipos
        labelCapacidadA.setText(String.valueOf(a.getCapacidad() + " personas"));
        labelFechaA.setText(a.getFecha().toString()); //TODO: formato
        labelPrecioA.setText(String.valueOf(a.getPrecioPorPersona()));
        labelDescuentoA.setText(String.valueOf(a.getDescuento().getValue()));//TODO: redondear a dos decimales con BigDecimal
        labelMontoA.setText(String.valueOf(a.getPrecio(p)));

        labelDestinoT.setText(t.getDestino());
        labelTipoT.setText(String.valueOf(t.getTipo())); //TODO: tipos
        labelCapacidadT.setText(String.valueOf(t.getCapacidad() + " personas"));
        labelFechaT.setText(t.getFecha().toString()); //TODO: formato
        labelPrecioT.setText(String.valueOf(t.getPrecioPorPersona()));
        labelDescuentoT.setText(String.valueOf(t.getDescuento().getValue()));
        //TODO: redondear a dos decimales con BigDecimal
        labelMontoT.setText(String.valueOf(t.getPrecio(p)));

        labelTotal.setText(String.valueOf(pa.getPrecioTotal()));

        labelPersonasDias.setText("Montos para " + p.getPersonas() + " por "
                + p.getDias() + " días");

        String text;
        Color color;
        if(index == 0) {
            text = "Aceptada";
            color = new Color(0, 150, 0);
        } else {
            text = "Rechazada";
            color = new Color(150, 0, 0);
        }
        labelEstado.setText(text);
        labelEstado.setForeground(color);

        SwingUtilities.invokeLater(this::pack);
    }

    private void createUIComponents() {
        comboPropuestas = new JComboBox<>(new DefaultComboBoxModel<>());
    }
}
