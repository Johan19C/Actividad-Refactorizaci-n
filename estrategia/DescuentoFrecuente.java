package estrategia;
public class DescuentoFrecuente implements EstrategiaDescuento {

    @Override
    public double calcularDescuento(double subtotal) {
        return subtotal * 0.10;
    }
}