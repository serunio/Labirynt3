import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static java.awt.Color.BLACK;
import static java.awt.Color.GREEN;
import static java.awt.Color.ORANGE;
import static java.awt.Color.RED;
import static java.awt.Color.WHITE;
import static java.awt.Color.YELLOW;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Math.abs;

public class Komorka extends JLabel implements MouseListener {

    static boolean isPressed;
    static int rozmiar;
    Typ typ;
    int x, y;
    public int wagaDotarcia = MAX_VALUE / 2;
    public Komorka poprzednia;

    Komorka(int x, int y, Typ typ) {
        this.typ = typ;
        ZmienPozycje(x, y);

        switch (this.typ) {
            case SCIANA, GRANICA -> this.setBackground(BLACK);
            case PRZEJSCIE -> this.setBackground(WHITE);
            case WEJSCIE -> this.setBackground(GREEN);
            case WYJSCIE -> this.setBackground(RED);
            default -> this.setBackground(YELLOW);
        }
        this.setOpaque(true);
        if (this.typ != Typ.WYJSCIE && this.typ != Typ.WEJSCIE)
            this.addMouseListener(this);
    }

    public void ZmienPozycje(int x, int y) {
        this.x = x;
        this.y = y;
        this.setBounds(x * rozmiar, y * rozmiar, rozmiar, rozmiar);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        isPressed = true;
        Brush();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isPressed = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (isPressed) {
            Brush();
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    void Brush() {
        switch (Labirynt.brush) {
            case ODWROT -> {
                if (this.typ == Typ.PRZEJSCIE) {
                    ZmienTyp(Typ.SCIANA);
                } else if (this.typ == Typ.SCIANA) {
                    ZmienTyp(Typ.PRZEJSCIE);
                }
            }
            case PRZEJSCIE -> {
                if (this.typ != Typ.GRANICA) {
                    ZmienTyp(Typ.PRZEJSCIE);
                }
            }
            case SCIANA -> {
                if (this.typ != Typ.GRANICA) {
                    ZmienTyp(Typ.SCIANA);
                }
            }
            case WEJSCIE -> Labirynt.start.ZmienPozycje(x, y);
            case WYJSCIE -> Labirynt.stop.ZmienPozycje(x, y);
        }
    }

    void ZmienTyp(Typ typ) {
        this.typ = typ;
        switch (typ) {
            case PRZEJSCIE -> this.setBackground(WHITE);
            case SCIANA -> this.setBackground(BLACK);
            case ODWIEDZONA -> this.setBackground(YELLOW);
            case ROZWIAZANIE -> this.setBackground(ORANGE);
        }
        this.repaint();
        Main.labirynt.repaint();
    }

    int GetHeuristic(Komorka finalCell) {
        return abs(finalCell.x - this.x) + abs(finalCell.y - this.y);
    }
}