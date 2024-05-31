import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GenerationWindow extends JFrame {
    public GenerationWindow() {
        super("Generation Window");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 100);
        setVisible(true);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());
        setResizable(false);

        JTextField xField = new JTextField("width");
        JTextField yField = new JTextField("height");
        JTextField name = new JTextField("name");
        JButton generate = new JButton("Generate");
        JButton random = new JButton("Random");

        xField.setPreferredSize(new Dimension(50, 20));
        yField.setPreferredSize(new Dimension(50, 20));
        name.setPreferredSize(new Dimension(100, 20));

        random.addActionListener(a -> {
            xField.setText(String.valueOf((int)(Math.random() * 99) + 2));
            yField.setText(String.valueOf((int)(Math.random() * 99) + 2));
            name.setText("maze");
        });

        generate.addActionListener(a -> {
            int xValue;
            int yValue;
            String nameValue;
            try {
                xValue = Integer.parseInt(xField.getText());
                yValue = Integer.parseInt(yField.getText());
                nameValue = name.getText();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input");
                return;
            }

            if(xValue < 2 || yValue < 2 || xValue > 100 || yValue > 100) {
                JOptionPane.showMessageDialog(this, "Invalid input\nPodaj wartości z zakresu 2-100");
                return;
            }

            Runtime rt = Runtime.getRuntime();
            try {
                rt.exec(new String[]{"generator.exe", xValue+"", yValue+"", "files\\" + nameValue});
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.dispose();
        });

        add(xField);
        add(yField);
        add(name);
        add(generate);
        add(random);
    }
}
