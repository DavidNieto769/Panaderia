// import static org.junit.jupiter.api.Assertions.assertEquals;
import javax.swing.JFrame;

import src.main.java.Venta;
// import org.junit.jupiter.api.Test;

public class Main {
  public static void main(String[] args) {
    Venta jfVentanaInicio = new Venta();
    jfVentanaInicio.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    jfVentanaInicio.setVisible(true);
  }

  // @Test
  // void addition() {
  //     assertEquals(2, 1 + 1);
  // }
}