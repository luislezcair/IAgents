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
    public Transporte() { }

    public Transporte(int capacidad, String destino, Date fecha,
                      double precioPorPersona, int tipo, DiscountManager ds) {
        super(capacidad, destino, fecha, precioPorPersona, tipo, ds);
    }

    public Transporte(Transporte other) {
        super(other);
    }

    @Override
    public String toString() {
        return "Transporte: " + super.toString();
    }
}
