import javax.swing.*;
import java.awt.*;

public class Main {
    static Labirynt2 labirynt;
    static JFrame frame;
    public static void main(String[] args) {

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);

        Menu menu = new Menu(frame);
        frame.add(menu, BorderLayout.EAST);

        frame.pack();

        frame.getContentPane().repaint();
    }
}
