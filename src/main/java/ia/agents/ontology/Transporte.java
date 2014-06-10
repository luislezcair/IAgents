/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents.ontology;

import ia.agents.negotiation.DiscountManager;

import java.util.Date;

/**
 * Clase que representa a una unidad de transporte
 */
public class Transporte extends ServicioAgencia {
    private static final String[] tipos = {
            "Avión", "Colectivo", "Otro"};

    public static final int TIPO_AVION = 0;
    public static final int TIPO_COLECTIVO = 1;
    public static final int TIPO_OTRO = 2;

    private static final String[] catAviones = {
            "Primera Clase", "Clase Turista"};
    private static final String[] catColectivos = {
            "Suite Premium", "Cama", "Semi-Cama"};
    private static final String[] catOtros = {
            "Combi", "Ferrocarril"};

    private static  final String[][] categorias =
            {catAviones, catColectivos, catOtros};

    public Transporte() { }

    public Transporte(int capacidad, String destino, Date fecha,
                      double precioPorPersona, int tipo, int categoria,
                      DiscountManager ds) {
        super(capacidad, destino, fecha, precioPorPersona, tipo, categoria, ds);
    }

    public Transporte(Transporte other) {
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
        return "Transporte: " + super.toString();
    }
}
