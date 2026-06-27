package estrategia;
public class DescuentoRegular implements EstrategiaDescuento {

    @Override
    public double calcularDescuento(double subtotal) {
        return subtotal * 0.05;
    }
}