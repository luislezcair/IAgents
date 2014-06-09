package ia.agents.ui;

import ia.agents.ontology.*;
import jade.core.Agent;
import org.joda.time.DateTime;
import org.joda.time.format.*;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;

public class UIPaqueteAgencia extends JDialog {
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
    private JLabel labelPrecioFinalA;
    private JLabel labelPrecioFinalT;
    private JLabel labelAgenteA;
    private JLabel labelAgenteT;
    private List<PaqueteAgencia> propuestas;
    private DateTimeFormatter dateFormat;

    public UIPaqueteAgencia(Agent turista,
                            List<PaqueteAgencia> listaPropuestas) {
        propuestas = listaPropuestas;
        dateFormat = DateTimeFormat.longDate().withLocale(Locale.getDefault());

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

        // Carga y establece un ícono para la ventana
        ImageIcon icon = new ImageIcon(
                getClass().getResource("/icons/PaqueteAgencia.png"));
        setIconImage(icon.getImage());

        pack();
        setVisible(true);
    }

    private void setPropuesta(int index) {
        PaqueteAgencia pa = propuestas.get(index);
        Alojamiento a = pa.getAlojamiento();
        Transporte t = pa.getTransporte();
        Paquete p = pa.getPaquete();

        // Alojamiento
        labelAgenteA.setText(pa.getAgenteLugar().getName());
        labelDestinoA.setText(a.getDestino());
        labelTipoA.setText(Alojamiento.getTipos()[a.getTipo()]);
        labelCapacidadA.setText(
                String.valueOf(a.getCapacidad()) + " personas");

        // Damos formato a la fecha
        labelFechaA.setText(dateFormat.print(new DateTime(a.getFecha())));

        labelPrecioA.setText(money(a.getPrecioPorPersona()));
        labelDescuentoA.setText(percent(a.getDescuento().getValue()));
        labelMontoA.setText(money(a.getPrecio(p)));
        labelPrecioFinalA.setText(
                money(a.getPrecioPorPersona() *
                     (1.0 - a.getDescuento().getValue())));

        // Transporte
        labelAgenteT.setText(pa.getAgenteTransporte().getName());
        labelDestinoT.setText(t.getDestino());
        labelTipoT.setText(Transporte.getTipos()[t.getTipo()]);
        labelCapacidadT.setText(
                String.valueOf(t.getCapacidad()) + " personas");

        // Damos formato a la fecha
        labelFechaT.setText(dateFormat.print(new DateTime(t.getFecha())));

        labelPrecioT.setText(money(t.getPrecioPorPersona()));
        labelDescuentoT.setText(percent(t.getDescuento().getValue()));
        labelMontoT.setText(money(t.getPrecio(p)));
        labelPrecioFinalT.setText(
                money(t.getPrecioPorPersona() *
                        (1.0 - t.getDescuento().getValue())));

        labelTotal.setText(money(pa.getPrecioTotal()));

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

    /**
     * Devuelve un string con formato de moneda y redondeado a dos decimales
     */
    private String money(double val) {
        return "$ " +
            new BigDecimal(val)
            .setScale(2, RoundingMode.HALF_UP)
            .toString()
            .replace(".", ",");
    }

    /**
     * Devuelve un string con formato de porcentaje y redondeado a dos decimales
     */
    private String percent(double val) {
        return new BigDecimal(val)
                .multiply(new BigDecimal("100.0"))
                .setScale(2, RoundingMode.HALF_UP)
                .toString()
                .replace(".", ",") + " %";
    }

    private void createUIComponents() {
        comboPropuestas = new JComboBox<>(new DefaultComboBoxModel<>());
    }
}
