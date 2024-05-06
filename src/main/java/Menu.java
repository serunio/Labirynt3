
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
    JCheckBox animuj, pokazOdwiedzone;
    File file;
    JFrame frame;

    Menu(JFrame frame){
        this.frame = frame;
        this.setBackground(blue);
        this.setLayout(new FlowLayout());
        this.setPreferredSize(new Dimension(250, 150));

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

        animuj = new JCheckBox();
        animuj.setText("Animuj");
        animuj.setFocusable(false);
        animuj.addActionListener(this);
        this.add(animuj);

        pokazOdwiedzone = new JCheckBox();
        pokazOdwiedzone.setText("Pokaz odwiedzone");
        pokazOdwiedzone.setFocusable(false);
        pokazOdwiedzone.addActionListener(this);
        this.add(pokazOdwiedzone);

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

        fileChooser.setCurrentDirectory(new File(".\\src\\main\\resources"));

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sciana)
            Painter.brush = Brush.SCIANA;
        else if (e.getSource() == przejscie)
            Painter.brush = Brush.PRZEJSCIE;
        else if (e.getSource() == odwrot)
            Painter.brush = Brush.ODWROT;
        else if (e.getSource() == start)
            Painter.brush = Brush.WEJSCIE;
        else if (e.getSource() == stop)
            Painter.brush = Brush.WYJSCIE;
        else if (e.getSource() == saveButton && Main.labirynt != null) {

            int response = fileChooser.showSaveDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
                file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                try {
                    Main.labirynt.SaveToFileBinary(file);

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } else if (e.getSource() == loadButton) {

            int response = fileChooser.showOpenDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
                file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                try {
                    Labirynt2 newLabirynt = new Labirynt2(file);
                    if(!newLabirynt.created)
                        return;
                    if(Main.labirynt != null)
                        frame.remove(Main.labirynt);
                    Main.labirynt = newLabirynt;
                    frame.add(Main.labirynt);
                    frame.pack();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } else if (e.getSource() == solveButton) {
            if(Main.labirynt == null || !Main.labirynt.created)
            {
                JOptionPane.showMessageDialog(null, "Najpierw stwórz labirynt");
                return;
            }

            try {
                Main.labirynt.searcher.Astar();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
        else if(e.getSource() == animuj)
        {
            AstarSearcher.animujIsTrue = animuj.isSelected();
        }
        else if(e.getSource() == pokazOdwiedzone)
        {
            AstarSearcher.showSzukanieIsTrue = pokazOdwiedzone.isSelected();
        }
    }
}
