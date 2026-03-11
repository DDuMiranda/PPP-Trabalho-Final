import javax.swing.SwingUtilities;
import cartas.truco.ControladorTruco;

public class MainTruco {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ControladorTruco::new);
    }
}
