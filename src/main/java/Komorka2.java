import java.awt.Color;

import static java.awt.Color.BLACK;
import static java.awt.Color.ORANGE;
import static java.awt.Color.WHITE;
import static java.awt.Color.YELLOW;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Math.abs;

public class Komorka2 {
    public static int rozmiar;
    Typ typ;
    Color kolor;
    int x, y;
    public int wagaDotarcia = MAX_VALUE / 2;
    public Komorka2 poprzednia;

    Komorka2(int x, int y, Typ typ)
    {
        ZmienTyp(typ);
        this.x = x;
        this.y = y;
    }

    public void ZmienTyp(Typ typ)
    {
        this.typ = typ;
        switch (typ) {
            case PRZEJSCIE -> this.kolor = WHITE;
            case SCIANA, GRANICA -> this.kolor = BLACK;
            case ODWIEDZONA -> this.kolor = YELLOW;
            case ROZWIAZANIE -> this.kolor = ORANGE;
            case WEJSCIE -> this.kolor = Color.GREEN;
            case WYJSCIE -> this.kolor = Color.RED;
        }
    }

    public void ZmienPozycje(int x, int y) {
        if(x < 0 || y < 0 || x >= Labirynt2.mazeSize[0] || y >= Labirynt2.mazeSize[1])
            return;
        this.x = x;
        this.y = y;
    }

    int GetHeuristic(Komorka2 finalCell) {
        return (abs(finalCell.x - this.x) + abs(finalCell.y - this.y));
    }

}
