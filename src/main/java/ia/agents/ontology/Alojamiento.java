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
    //TODO: Esto haría sangrar los ojos a Torossi
    private static final String[] tipos = {
            "Hotel", "Hostel", "Casa de alquiler"};

    private static final String[] catHotel = {
            "Cinco Estrellas (*****)",
            "Cuatro Estrellas (****)",
            "Tres Estrellas (***)",
            "Dos Estrellas (**)",
            "Una Estrella (*)"};
    private static final String[] catHostel = {"Premium", "Estándar"};
    private static final String[] catCasaAlq = {"Premium", "Estándar"};

    private static  final String[][] categorias =
            {catHotel, catHostel, catCasaAlq};

    public static final int TIPO_HOTEL = 0;
    public static final int TIPO_HOSTEL = 1;
    public static final int TIPO_CASA_ALQ = 2;

    public Alojamiento() { }

    public Alojamiento(int capacidad, String destino, Date fecha,
                       double precioPorPersona, int tipo, int categoria,
                       DiscountManager ds) {
        super(capacidad, destino, fecha, precioPorPersona, tipo, categoria, ds);
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

    public static String[][] getCategorias() {
        return categorias;
    }

    @Override
    public String toString() {
        return "Alojamiento: " + super.toString();
    }
}
