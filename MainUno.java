import javax.swing.SwingUtilities;
import cartas.uno.ControladorUno;

public class MainUno {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ControladorUno::new);
    }
}
