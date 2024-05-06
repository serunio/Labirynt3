import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Painter implements MouseListener, MouseMotionListener {

    Komorka[][] maze;
    int[] mazeSize;
    Komorka start, stop;
    Labirynt labirynt;
    int lastX = -1, lastY = -1;
    public static Brush brush = Brush.DEFAULT;
    Painter(Labirynt labirynt)
    {
        this.labirynt = labirynt;
        maze = labirynt.maze;
        mazeSize = Labirynt.mazeSize;
        start = Labirynt.start;
        stop = Labirynt.stop;
    }
    void Brush(int x , int y)
    {
        labirynt.searcher.Clear();
        x /= Komorka.rozmiar;
        y /= Komorka.rozmiar;

        if(x < 0 || y < 0 || x >= mazeSize[0] || y >= mazeSize[1])
            return;

        switch (brush) {
            case ODWROT -> {
                if (maze[x][y].typ == Typ.PRZEJSCIE) {
                    maze[x][y].ZmienTyp(Typ.SCIANA);
                } else if (maze[x][y].typ == Typ.SCIANA) {
                    maze[x][y].ZmienTyp(Typ.PRZEJSCIE);
                }
            }
            case PRZEJSCIE -> {
                if (maze[x][y].typ != Typ.GRANICA) {
                    maze[x][y].ZmienTyp(Typ.PRZEJSCIE);
                }
            }
            case SCIANA -> {
                if (maze[x][y].typ != Typ.GRANICA) {
                    maze[x][y].ZmienTyp(Typ.SCIANA);
                }
            }
            case WEJSCIE -> {
                if(x!=stop.x || y!=stop.y)
                    start.ZmienPozycje(x, y);
            }
            case WYJSCIE -> {
                if(x!=start.x || y!=start.y)
                    stop.ZmienPozycje(x, y);
            }
        }
        labirynt.repaint();
    }
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        Brush(e.getX(), e.getY());
        lastX = e.getX()/ Komorka.rozmiar;
        lastY = e.getY()/ Komorka.rozmiar;
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }
    @Override
    public void mouseDragged(MouseEvent e) {
        if(lastX == e.getX()/ Komorka.rozmiar && lastY == e.getY()/ Komorka.rozmiar)
            return;
        Brush(e.getX(), e.getY());
        lastX = e.getX()/ Komorka.rozmiar;
        lastY = e.getY()/ Komorka.rozmiar;
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
