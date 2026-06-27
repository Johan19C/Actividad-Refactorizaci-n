package util;
public class ValidarCliente {

    public static boolean validarNombre(String nombre) {
        return nombre != null && !nombre.trim().isEmpty();
    }

    public static boolean validarEmail(String email) {
        return email != null && email.contains("@");
    }
}