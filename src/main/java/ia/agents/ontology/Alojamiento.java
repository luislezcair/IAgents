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
    public Alojamiento() { }

    public Alojamiento(int capacidad, String destino, Date fecha,
                       double precioPorPersona, int tipo, DiscountManager ds) {
        super(capacidad, destino, fecha, precioPorPersona, tipo, ds);
    }

    public Alojamiento(Alojamiento other) {
        super(other);
    }

    @Override
    public String toString() {
        return "Alojamiento: " + super.toString();
    }
}
