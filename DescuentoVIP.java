public class DescuentoVIP implements EstrategiaDescuento {

    @Override
    public double calcularDescuento(double subtotal) {
        return subtotal * 0.20;
    }
}