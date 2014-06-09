/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents.ontology;

import ia.agents.negotiation.DiscountManager;
import java.util.Date;

/**
 * Clase que representa un lugar de alojamiento para los turistas
 */
public class Alojamiento extends ServicioAgencia {
    private static final String[] tipos = {
            "Hotel", "Hostel", "Casa de alquiler"};

    public static final int TIPO_HOTEL = 0;
    public static final int TIPO_HOSTEL = 1;
    public static final int TIPO_CASA_ALQ = 2;

    public Alojamiento() { }

    public Alojamiento(int capacidad, String destino, Date fecha,
                       double precioPorPersona, int tipo, DiscountManager ds) {
        super(capacidad, destino, fecha, precioPorPersona, tipo, ds);
    }

    public Alojamiento(Alojamiento other) {
        super(other);
    }

    public static String[] getTipos() {
        return tipos;
    }

    @Override
    public void setTipo(int tipo) {
        int last = tipos.length - 1;
        if(tipo > last)
            super.setTipo(last);
        else
            super.setTipo(tipo);
    }

    @Override
    public String toString() {
        return "Alojamiento: " + super.toString();
    }
}
