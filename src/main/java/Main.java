import javax.swing.*;
import java.awt.*;

public class Main {
    static Labirynt labirynt;
    static JFrame frame;
    public static void main(String[] args) {

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);
        //frame.setLocationRelativeTo(null);

        Menu menu = new Menu(frame);
        frame.add(menu, BorderLayout.EAST);

        frame.pack();

        frame.getContentPane().repaint();

    }
}
