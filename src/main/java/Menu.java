
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import static java.awt.Color.blue;

public class Menu extends JPanel implements ActionListener {
    JRadioButton sciana, przejscie, odwrot, start, stop;
    JButton saveButton, loadButton, solveButton;
    JFileChooser fileChooser = new JFileChooser();
    File file;
    JFrame frame;

    Menu(JFrame frame){
        this.frame = frame;
        this.setBackground(blue);
        this.setLayout(new FlowLayout());
        this.setPreferredSize(new Dimension(250, 100));

        sciana = new JRadioButton("sciana");
        przejscie = new JRadioButton("przejscie");
        odwrot = new JRadioButton("odwrot");
        start = new JRadioButton("start");
        stop = new JRadioButton("stop");

        sciana.addActionListener(this);
        przejscie.addActionListener(this);
        odwrot.addActionListener(this);
        start.addActionListener(this);
        stop.addActionListener(this);

        ButtonGroup group = new ButtonGroup();
        group.add(sciana);
        group.add(przejscie);
        group.add(odwrot);
        group.add(start);
        group.add(stop);

        this.add(sciana);
        this.add(przejscie);
        this.add(odwrot);
        this.add(start);
        this.add(stop);

        saveButton = new JButton();
        saveButton.setText("Zapisz");
        saveButton.addActionListener(this);
        this.add(saveButton);

        loadButton = new JButton();
        loadButton.setText("Załaduj");
        loadButton.addActionListener(this);
        this.add(loadButton);

        solveButton = new JButton();
        solveButton.setText("Rozwiąż");
        solveButton.addActionListener(this);
        this.add(solveButton);

        fileChooser.setCurrentDirectory(new File("."));

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sciana)
            Labirynt.brush = Brush.SCIANA;
        else if (e.getSource() == przejscie)
            Labirynt.brush = Brush.PRZEJSCIE;
        else if (e.getSource() == odwrot)
            Labirynt.brush = Brush.ODWROT;
        else if (e.getSource() == start)
            Labirynt.brush = Brush.WEJSCIE;
        else if (e.getSource() == stop)
            Labirynt.brush = Brush.WYJSCIE;
        else if (e.getSource() == saveButton && Main.labirynt != null) {

            int response = fileChooser.showSaveDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
                file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                try {
                    Main.labirynt.SaveToFile(file);

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } else if (e.getSource() == loadButton) {

            int response = fileChooser.showOpenDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
                file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                try {
                    if(Main.labirynt != null)
                        frame.remove(Main.labirynt);
                    Main.labirynt = new Labirynt(file);
                    frame.add(Main.labirynt);
                    frame.pack();


                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } else if (e.getSource() == solveButton) {
            try {
                Main.labirynt.Astar();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
