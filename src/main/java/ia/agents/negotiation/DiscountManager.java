/**
 * Created by IA - Grupo 3.
 * Part of IAgents
 */
package ia.agents.negotiation;

/**
 * Clase que maneja el descuento de un servicio. A cada nueva propuesta que
 * recibe el servicio, le suma una cantidad step al descuento actual (value)
 * hasta llegar al máximo max. En este caso, es una oferta final.
 */
public class DiscountManager {
    private double max;
    private double step;
    private double value;

    public DiscountManager() { }

    public DiscountManager(double value, double max, double step) {
        this.value = value;
        this.max = max;
        this.step = step;
    }

    public DiscountManager(DiscountManager other) {
        this.value = other.value;
        this.step = other.step;
        this.max = other.max;
    }

    public boolean isMax() {
        return value >= max;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double val) {
        value = val;
    }

    public double getStep() {
        return step;
    }

    public void setStep(double step) {
        this.step = step;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public void updateValue() {
        value += step;
        if(value > max)
            value = max;
    }

    @Override
    public String toString() {
        return "value: " + value + " max: " + max + " step: " + step;
    }
}
