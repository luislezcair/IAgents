/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */

package ia.agents.util;

import ia.agents.ontology.ServicioAgencia;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Clase con algunos métodos útiles que se usan en varias lados.
 * Cohesión lógica diría nuestro buen amigo Torossi.
 */
public class Util {
    private final Random random = new Random();

    /**
     * Genera un número aleatorio entero en el rango [min, max] y lo devuelve.
     * r = min + random * ((max - min) + 1)
     */
    public final int getRandomNumber(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    /**
     * Genera un número aleatorio en coma flotante en el rango [min, max],
     * lo redondea a dos decimales y lo devuelve.
     * r = min + random * (max - min)
     * http://docs.oracle.com/javase/8/docs/api/java/math/BigDecimal.html#BigDecimal-double-"
     */
    public final BigDecimal getRandomNumber(String smin, String smax) {
        BigDecimal min = new BigDecimal(smin);
        BigDecimal max = new BigDecimal(smax);
        BigDecimal bd = new BigDecimal(random.nextDouble());
        bd = bd.multiply(max.subtract(min)).add(min);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd;
    }

    /**
     * Devuelve el número aleatorio como un String
     */
    public final String getRandomNumberString(String min, String max) {
        return getRandomNumber(min, max).toString();
    }

    /**
     * Obtiene la lista de agencias del arreglo de argumentos.
     * @param args El arreglo devuevlto por la llamada a getArguments() del agente
     * @return Lista de Strings con los nombres de las agencias
     */
    public static List<String> getAgencias(Object[] args) {
        //Object[] args = getArguments();
        if (args == null || args.length < 1 || args[0] == null ||
                !(args[0] instanceof List<?>)) {
            return new ArrayList<>();
        }

        // Convierte la lista que viene como argumento a una lista de strings
        // para evitar unchecked warnings
        List<?> argList = (List<?>) args[0];
        List<String> agencias = new ArrayList<>();
        argList.forEach(x -> agencias.add((String) x));
        return agencias;
    }

    /**
     * Busca en el arreglo el servicio pasado como parámetro al agente
     * @param args El arreglo devuevlto por la llamada a getArguments() del agente
     * @return El ServicioAgencia pasado como parámetro o null si no existe.
     */
    public static ServicioAgencia getServicio(Object [] args) {
        if(args == null || args.length < 2 ||
                !(args[1] instanceof ServicioAgencia)) {
            return null;
        }
        return (ServicioAgencia) args[1];
    }
}
