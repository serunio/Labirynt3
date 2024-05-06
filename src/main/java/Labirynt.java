import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.Queue;

import static java.awt.Color.blue;
import static java.awt.Color.orange;
import static java.awt.Color.red;
import static java.awt.Color.yellow;
import static java.lang.Integer.MAX_VALUE;

public class Labirynt extends JPanel implements ActionListener {
    int[] mazeSize;
    public static Brush brush = Brush.DEFAULT;
    public static Komorka start, stop;
    Komorka[][] maze;
    int x = 0, y = 0;

    Labirynt(File file) throws IOException
    {
        int size = 700;
        byte[] data = Files.readAllBytes(file.toPath());
        mazeSize = getSize(data);
        Komorka.rozmiar = size / (Math.max(mazeSize[0], mazeSize[1]));
        this.setBackground(red);
        this.setLayout(null);
        this.setPreferredSize(new Dimension(Komorka.rozmiar*mazeSize[0], Komorka.rozmiar*mazeSize[1]));

        maze = new Komorka[mazeSize[0]][mazeSize[1]];

        int ix = 0, iy = 0;
        Typ type = null;

        this.add(start = new Komorka(0, 0, Typ.WEJSCIE));
        this.add(stop = new Komorka(0, 0, Typ.WYJSCIE));
        for (byte znak : data) {

            if (znak=='\n') {
                iy++;
                ix = 0;
                continue;
            }
            switch ((char) znak) {
                case ' ' -> type = Typ.PRZEJSCIE;
                case 'P', 'K', 'X' -> {
                    if (ix == 0 || iy == 0 || ix == mazeSize[0] - 1 || iy == mazeSize[1] - 1)
                        type = Typ.GRANICA;
                    else
                        type = Typ.SCIANA;
                }
            }
            if((char)znak == 'P')
                start.ZmienPozycje(ix, iy);
            if((char)znak == 'K')
                stop.ZmienPozycje(ix, iy);
            this.add(maze[ix][iy] = new Komorka(ix, iy, type));

            ix++;
        }
    }

    void SaveToFile(File file) throws IOException {
        FileOutputStream output = new FileOutputStream(file);
        byte[] data = new byte[(mazeSize[0] + 1) * mazeSize[1]];
        int x = -1, y = 0;
        for (int i = 0; i < data.length; i++) {
            x++;
            if (x == mazeSize[0]) {
                y++;
                x = -1;
                data[i] = '\n';
                continue;
            }

            switch (maze[x][y].typ) {
                case SCIANA, GRANICA -> data[i] = 'X';
                case PRZEJSCIE -> data[i] = ' ';
                default -> data[i] = '#';
            }
            if (maze[x][y].x == Labirynt.start.x && maze[x][y].y == Labirynt.start.y) {
                data[i] = 'P';
            } else if (maze[x][y].x == Labirynt.stop.x && maze[x][y].y == Labirynt.stop.y) {
                data[i] = 'K';
            }
        }
        output.write(data);
        output.close();
    }


    private int[] getSize(byte[] data) {
        int[] xy = {0, 0};
        int i = 0;
        while (data[i] != '\n') {
            i++;
        }
        xy[0] = i;
        for (; i < data.length; i++) {
            if (data[i] == '\n')
                xy[1]++;
        }
        return xy;
    }
    Timer szukanie = new Timer(1, this);
    Timer wynik = new Timer(0, this);
    Queue<Komorka> kolejkaSzukanie = new LinkedList<>();
    Queue<Komorka> kolejkaWynik = new LinkedList<>();
    void Astar() throws InterruptedException {
        int minWeight;
        x = start.x; y = start.y;
        maze[x][y].wagaDotarcia = 0;
        int result = 0;

        szukanie.start();
        while (true) {
            if (x - 1 >= 0)
                result += CheckAndConnectCell(x, y, x - 1, y);
            if (y - 1 >= 0)
                result += CheckAndConnectCell(x, y, x, y - 1);
            if (x + 1 < mazeSize[0])
                result += CheckAndConnectCell(x, y, x + 1, y);
            if (y + 1 < mazeSize[1])
                result += CheckAndConnectCell(x, y, x, y + 1);
            if(result > 0)
                break;

            maze[x][y].typ = Typ.ODWIEDZONA; //w tym miejscu zmienia sie kolor komorki


            //this.x = x; this.y = y;

            minWeight = MAX_VALUE / 2;
            int xNew=x, yNew=y;
            for (int j = 0; j < mazeSize[1]; j++)
                for (int i = 0; i < mazeSize[0]; i++)
                    if (maze[i][j].wagaDotarcia + maze[i][j].GetHeuristic(stop) < minWeight && maze[i][j].typ != Typ.ODWIEDZONA) {
                        xNew = i;
                        yNew = j;
                        minWeight = maze[i][j].wagaDotarcia + maze[i][j].GetHeuristic(stop);
                    }
            if(!(x==xNew && y==yNew)){
                x=xNew; y=yNew;
            }
            else return;

            //Main.frame.repaint();
            //this.repaint();
            kolejkaSzukanie.add(maze[x][y]);
        }
        int temp;
        while (x != start.x || y != start.y) {
            kolejkaWynik.add(maze[x][y]);
            temp = maze[x][y].poprzednia.x;
            y = maze[x][y].poprzednia.y;
            x = temp;
        }
    }

    int CheckAndConnectCell(int x, int y, int xNew, int yNew){
        if(xNew==stop.x && yNew==stop.y)
            return 1;
        if(maze[xNew][yNew].typ == Typ.PRZEJSCIE && 1 + maze[x][y].wagaDotarcia < maze[xNew][yNew].wagaDotarcia)
        {
            maze[xNew][yNew].wagaDotarcia = 1 + maze[x][y].wagaDotarcia;
            maze[xNew][yNew].poprzednia = maze[x][y];
        }
        return 0;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==szukanie)
        {
            if(kolejkaSzukanie.size()>1)
                kolejkaSzukanie.remove().setBackground(yellow);
            if(!kolejkaSzukanie.isEmpty()){
                kolejkaSzukanie.peek().setBackground(blue);
                szukanie.start();
            }
            if(kolejkaSzukanie.size()==1)
                wynik.start();
            this.repaint();
        }
        else if(e.getSource()==wynik && !kolejkaWynik.isEmpty())
        {
            kolejkaWynik.remove().setBackground(orange);
            wynik.start();
        }

    }
}
