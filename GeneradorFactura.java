import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class GeneradorFactura {

    public void generarFactura(String nombreCliente,
            List<String> nombresProductos,
            List<Double> preciosProductos,
            List<Integer> cantidades,
            double subtotal,
            double descuento,
            double impuesto,
            double total) {

        try {
            FileWriter writer = new FileWriter("factura_" + nombreCliente + ".txt");

            writer.write("FACTURA\n");
            writer.write("Cliente: " + nombreCliente + "\n");

            for (int i = 0; i < nombresProductos.size(); i++) {
                writer.write(nombresProductos.get(i)
                        + " x" + cantidades.get(i)
                        + " = $" + (preciosProductos.get(i) * cantidades.get(i))
                        + "\n");
            }

            writer.write("Subtotal: $" + subtotal + "\n");
            writer.write("Descuento: $" + descuento + "\n");
            writer.write("Impuesto: $" + impuesto + "\n");
            writer.write("TOTAL: $" + total + "\n");

            writer.close();

        } catch (IOException e) {
            System.out.println("Error al generar la factura: " + e.getMessage());
        }
    }
}