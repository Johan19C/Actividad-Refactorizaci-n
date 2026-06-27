import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class PedidoDAO {

    private Connection conexionBD;

    public PedidoDAO(Connection conexionBD) {
        this.conexionBD = conexionBD;
    }

    public void guardarPedido(String nombreCliente, double total) {
        try {
            Statement stmt = conexionBD.createStatement();
            String sql = "INSERT INTO pedidos (cliente, total) VALUES ('"
                    + nombreCliente + "', " + total + ")";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error al guardar el pedido: " + e.getMessage());
        }
    }

    public void cancelarPedido(int idPedido) {
        try {
            Statement stmt = conexionBD.createStatement();
            String sql = "DELETE FROM pedidos WHERE id = " + idPedido;
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error al cancelar el pedido: " + e.getMessage());
        }
    }
}