
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import static java.awt.Color.blue;

public class Menu extends JPanel implements ActionListener {
    JRadioButton sciana, przejscie, odwrot, start, stop;
    JButton saveButton, saveBinaryButton, loadButton, solveButton, generateButton;
    JFileChooser fileChooser = new JFileChooser();
    JCheckBox animuj, pokazOdwiedzone, zoom;
    File file;
    JFrame frame;
    JScrollPane scrollPane;

    Menu(JFrame frame){
        this.frame = frame;
        this.setBackground(blue);
        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        this.setPreferredSize(new Dimension(250, 215));

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

        layout.getConstraints(sciana).setY(Spring.constant(5));
        layout.getConstraints(przejscie).setY(Spring.constant(30));
        layout.getConstraints(odwrot).setY(Spring.constant(55));
        layout.getConstraints(start).setY(Spring.constant(80));
        layout.getConstraints(stop).setY(Spring.constant(105));

        layout.getConstraints(sciana).setX(Spring.constant(5));
        layout.getConstraints(przejscie).setX(Spring.constant(5));
        layout.getConstraints(odwrot).setX(Spring.constant(5));
        layout.getConstraints(start).setX(Spring.constant(5));
        layout.getConstraints(stop).setX(Spring.constant(5));

        animuj = new JCheckBox();
        animuj.setText("Animuj");
        animuj.setFocusable(false);
        animuj.addActionListener(this);
        this.add(animuj);

        layout.getConstraints(animuj).setY(Spring.constant(5));
        layout.getConstraints(animuj).setX(Spring.constant(100));

        pokazOdwiedzone = new JCheckBox();
        pokazOdwiedzone.setText("Pokaz odwiedzone");
        pokazOdwiedzone.setFocusable(false);
        pokazOdwiedzone.addActionListener(this);
        this.add(pokazOdwiedzone);

        layout.getConstraints(pokazOdwiedzone).setY(Spring.constant(30));
        layout.getConstraints(pokazOdwiedzone).setX(Spring.constant(100));

        zoom = new JCheckBox();
        zoom.setText("Zoom");
        zoom.setFocusable(false);
        zoom.addActionListener(this);
        this.add(zoom);

        layout.getConstraints(zoom).setY(Spring.constant(55));
        layout.getConstraints(zoom).setX(Spring.constant(100));

        saveButton = new JButton();
        saveButton.setText("Zapisz");
        saveButton.addActionListener(this);
        this.add(saveButton);

        layout.getConstraints(saveButton).setY(Spring.constant(156));
        layout.getConstraints(saveButton).setX(Spring.constant(100));

        saveBinaryButton = new JButton();
        saveBinaryButton.setText("Zapisz Binarnie");
        saveBinaryButton.addActionListener(this);
        this.add(saveBinaryButton);

        layout.getConstraints(saveBinaryButton).setY(Spring.constant(182));
        layout.getConstraints(saveBinaryButton).setX(Spring.constant(100));

        loadButton = new JButton();
        loadButton.setText("Załaduj");
        loadButton.addActionListener(this);
        this.add(loadButton);

        layout.getConstraints(loadButton).setY(Spring.constant(130));
        layout.getConstraints(loadButton).setX(Spring.constant(100));

        solveButton = new JButton();
        solveButton.setText("Rozwiąż");
        solveButton.addActionListener(this);
        this.add(solveButton);

        layout.getConstraints(solveButton).setY(Spring.constant(92));
        layout.getConstraints(solveButton).setX(Spring.constant(100));

        generateButton = new JButton();
        generateButton.setText("Generuj");
        generateButton.addActionListener(this);
        this.add(generateButton);

        layout.getConstraints(generateButton).setY(Spring.constant(156));
        layout.getConstraints(generateButton).setX(Spring.constant(5));

        fileChooser.setCurrentDirectory(new File(".\\files"));

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
        else if (e.getSource() == saveButton || e.getSource() == saveBinaryButton) {
            if(Main.labirynt == null || !Main.labirynt.created)
            {
                JOptionPane.showMessageDialog(null, "Najpierw stwórz labirynt");
                return;
            }
            int response = fileChooser.showSaveDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
                file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                try {
                    if(e.getSource() == saveButton)
                        Main.labirynt.SaveToFile(file);
                    else
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
                    Labirynt newLabirynt = new Labirynt(file);
                    if(!newLabirynt.created)
                        return;
                    if(Main.labirynt != null)
                    {
                        //frame.remove(Main.labirynt);
                        zoom.setSelected(false);
                        frame.remove(scrollPane);
                    }

                    scrollPane = new JScrollPane(newLabirynt);
                    Dimension scrollPaneSize = new Dimension(newLabirynt.wymiary.width + 18, newLabirynt.wymiary.height + 18);
                    scrollPane.setPreferredSize(scrollPaneSize);
                    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                    frame.add(scrollPane);

                    Main.labirynt = newLabirynt;
                    //frame.add(Main.labirynt);
                    frame.pack();
                    frame.setLocationRelativeTo(null);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } else if (e.getSource() == solveButton) {
            if(Main.labirynt == null || !Main.labirynt.created)
            {
                JOptionPane.showMessageDialog(null, "Najpierw stwórz labirynt");
            }
            else
            {
                Main.labirynt.searcher.Astar();
            }

        }
        else if(e.getSource() == generateButton){
            new GenerationWindow();
        }
        else if(e.getSource() == animuj)
        {
            AstarSearcher.animujIsTrue = animuj.isSelected();
        }
        else if(e.getSource() == pokazOdwiedzone)
        {
            AstarSearcher.showSzukanieIsTrue = pokazOdwiedzone.isSelected();
        }
        else if(e.getSource() == zoom)
        {
            if(Main.labirynt == null)
            {
                JOptionPane.showMessageDialog(null, "Najpierw stwórz labirynt");
                zoom.setSelected(false);
                return;
            }
            if(zoom.isSelected())
            {
                Main.labirynt.wymiary = new Dimension(Main.labirynt.wymiary.width*3, Main.labirynt.wymiary.height*3);
                Komorka.rozmiar *= 3;
                Komorka.isZoomed = true;

            }
            else
            {
                Main.labirynt.wymiary = new Dimension(Main.labirynt.wymiary.width/3, Main.labirynt.wymiary.height/3);
                Komorka.rozmiar /= 3;
                Komorka.isZoomed = false;
            }
            Main.labirynt.setPreferredSize(Main.labirynt.wymiary);
            Main.labirynt.revalidate();
            Main.labirynt.repaint();
            scrollPane.revalidate();
            scrollPane.repaint();
        }
    }
}
