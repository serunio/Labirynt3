import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Queue;

import static java.awt.Color.blue;
import static java.awt.Color.orange;
import static java.awt.Color.yellow;
import static java.lang.Integer.MAX_VALUE;

public class AstarSearcher implements ActionListener {

    Komorka2[][] maze;
    int[] mazeSize;
    Komorka2 start, stop;
    Labirynt2 labirynt;
    AstarSearcher(Labirynt2 labirynt){
        this.labirynt = labirynt;
        maze = labirynt.maze;
        mazeSize = Labirynt2.mazeSize;
        start = Labirynt2.start;
        stop = Labirynt2.stop;
    }
    Timer animacjaSzukanie = new Timer(0, this);
    Timer animacjaWynik = new Timer(0, this);
    boolean animacjaSzukanieIsFinished = false;
    boolean znalezionoWynikIsTrue = false;
    static boolean animujIsTrue = false;
    static boolean showSzukanieIsTrue = false;
    boolean animationIsRunning = false;
    Queue<Komorka2> kolejkaSzukanie = new LinkedList<>();
    Queue<Komorka2> kolejkaWynik = new LinkedList<>();
    LinkedList<Komorka2> doPrzeszukania = new LinkedList<>();

    void Astar() throws InterruptedException {
        Clear();
        int x = start.x, y = start.y;
        int result = 0;
        animacjaSzukanieIsFinished = false;
        animationIsRunning = false;
        znalezionoWynikIsTrue = false;
        maze[x][y].wagaDotarcia = 0;
        doPrzeszukania.add(maze[x][y]);
        while (true) {
            if (x - 1 >= 0)
                result += CheckAndConnectCell(x, y, x - 1, y);
            if (y - 1 >= 0)
                result += CheckAndConnectCell(x, y, x, y - 1);
            if (x + 1 < mazeSize[0])
                result += CheckAndConnectCell(x, y, x + 1, y);
            if (y + 1 < mazeSize[1])
                result += CheckAndConnectCell(x, y, x, y + 1);
            if(!kolejkaSzukanie.isEmpty())
                maze[x][y].typ = Typ.ODWIEDZONA;
            if(result > 0)
            {
                znalezionoWynikIsTrue = true;
                break;
            }
            doPrzeszukania.remove(maze[x][y]);
            if(doPrzeszukania.isEmpty())
                return;

            doPrzeszukania.sort((a, b) -> a.wagaDotarcia + a.GetHeuristic(stop) - b.wagaDotarcia - b.GetHeuristic(stop));

            x = doPrzeszukania.getFirst().x;
            y = doPrzeszukania.getFirst().y;
            kolejkaSzukanie.add(maze[x][y]);
        }
        int temp;
        while (x != start.x || y != start.y) {
            kolejkaWynik.add(maze[x][y]);
            temp = maze[x][y].poprzednia.x;
            y = maze[x][y].poprzednia.y;
            x = temp;
        }
        doPrzeszukania.clear();
        if(animujIsTrue)
        {
            if(showSzukanieIsTrue && !animacjaSzukanieIsFinished){
                animacjaSzukanie.start();
                animationIsRunning = true;
            }

            else {
                LinkedList<Komorka2> tempQueue = new LinkedList<>(kolejkaWynik);
                kolejkaWynik.clear();
                tempQueue.descendingIterator().forEachRemaining(komorka2 -> kolejkaWynik.add(komorka2));
                animacjaSzukanieIsFinished = true;
                animacjaWynik.start();
            }
        } else
        {
            while(!kolejkaSzukanie.isEmpty() && showSzukanieIsTrue)
                kolejkaSzukanie.remove().kolor = yellow;
            while(!kolejkaWynik.isEmpty())
                kolejkaWynik.remove().kolor = orange;
        }
    }

    int CheckAndConnectCell(int x, int y, int xNew, int yNew){
        if(xNew==stop.x && yNew==stop.y)
            return 1;
        if(maze[xNew][yNew].typ == Typ.PRZEJSCIE && 1 + maze[x][y].wagaDotarcia < maze[xNew][yNew].wagaDotarcia)
        {
            maze[xNew][yNew].wagaDotarcia = 1 + maze[x][y].wagaDotarcia;
            maze[xNew][yNew].poprzednia = maze[x][y];
            doPrzeszukania.add(maze[xNew][yNew]);
        }
        return 0;
    }

    public void Clear()
    {
        kolejkaWynik.clear();
        kolejkaSzukanie.clear();
        animacjaSzukanieIsFinished = false;
        animationIsRunning = false;
        for (int i = 0; i < mazeSize[0]; i++)
            for (int j = 0; j < mazeSize[1]; j++)
            {
                maze[i][j].wagaDotarcia = MAX_VALUE / 2;
                maze[i][j].poprzednia = null;
                if(maze[i][j].typ == Typ.ODWIEDZONA)
                    maze[i][j].ZmienTyp(Typ.PRZEJSCIE);
            }

        labirynt.repaint();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == animacjaSzukanie && znalezionoWynikIsTrue && (animujIsTrue && showSzukanieIsTrue || animationIsRunning))
        {
            if(kolejkaSzukanie.size()>1)
                kolejkaSzukanie.remove().kolor = yellow;
            if(!kolejkaSzukanie.isEmpty()){
                kolejkaSzukanie.peek().kolor = blue;
                animacjaSzukanie.start();
            }
            if(kolejkaSzukanie.size()==1)
            {
                kolejkaSzukanie.remove();
                animacjaSzukanieIsFinished = true;
                animationIsRunning = false;
                animacjaWynik.start();
            }
            labirynt.repaint();
        }
        else if(e.getSource()== animacjaWynik && !kolejkaWynik.isEmpty() && animacjaSzukanieIsFinished)
        {
            kolejkaWynik.remove().kolor = orange;
            labirynt.repaint();
            animacjaWynik.start();
        }
    }
}
