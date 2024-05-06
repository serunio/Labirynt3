import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;

import static java.awt.Color.red;

public class Labirynt2 extends JPanel{
    static int[] mazeSize;
    public static Komorka2 start, stop;
    Komorka2[][] maze;
    public boolean created = false;
    AstarSearcher searcher;
    Painter painter;

    Labirynt2(File file) throws IOException
    {
        int size = 700;
        byte[] data = Files.readAllBytes(file.toPath());
        String type = mazeCheck(data);
        if (type.equals("Error")) {
            JOptionPane.showMessageDialog(null, "Niepoprawna struktura pliku");
            return;
        }

        if (type.equals("Text")) {
            if (getSize(data) == 1) {
                JOptionPane.showMessageDialog(null, "Niepoprawna struktura pliku");
                return;
            }
        } else if (type.equals("Binary")) {
            if (getSizeBinary(data) == 1) {
                JOptionPane.showMessageDialog(null, "Niepoprawna struktura pliku");
                return;
            }
        } else {
            JOptionPane.showMessageDialog(null, "Niepoprawna struktura pliku");
            return;
        }

        Komorka2.rozmiar = size / (Math.max(mazeSize[0], mazeSize[1]));
        if(Komorka2.rozmiar<1)
            Komorka2.rozmiar = 1;
        this.setBackground(red);
        this.setLayout(null);
        this.setPreferredSize(new Dimension(Komorka2.rozmiar*mazeSize[0], Komorka2.rozmiar*mazeSize[1]));

        maze = new Komorka2[mazeSize[0]][mazeSize[1]];
        if(type.equals("Text"))
            getMaze(data);
        else getMazeBinary(data);

        painter = new Painter(this);
        this.addMouseListener(painter);
        this.addMouseMotionListener(painter);
        searcher = new AstarSearcher(this);
        repaint();
        created = true;
    }

    String mazeCheck(byte[] data)
    {
        if(data.length < 4)
            return "Error";
        ByteBuffer buffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
        if(buffer.getInt(0) == 0x52524243)
        {
            if(data.length < 40)
                return "Error";
            return "Binary";
        }

        else if(buffer.get(0) == 'X')
            return "Text";
        else
            return "Error";
    }

    void getMaze(byte[] data) {
        int ix = 0, iy = 0;
        Typ type;
        start = new Komorka2(0, 0, Typ.WEJSCIE);
        stop = new Komorka2(0, 0, Typ.WYJSCIE);
        for (byte znak : data) {
            if(znak == '\r')
                continue;
            if (znak=='\n') {
                iy++;
                ix = 0;
                continue;
            }
            if ((char) znak == ' ') {
                type = Typ.PRZEJSCIE;
            } else {
                if (ix == 0 || iy == 0 || ix == (mazeSize[0] - 1) || iy == (mazeSize[1] - 1))
                    type = Typ.GRANICA;
                else
                    type = Typ.SCIANA;
            }
            if((char)znak == 'P')
                start.ZmienPozycje(ix, iy);
            if((char)znak == 'K')
                stop.ZmienPozycje(ix, iy);
            maze[ix][iy] = new Komorka2(ix, iy, type);

            ix++;
        }
    }

    void getMazeBinary(byte[] data)
    {
        ByteBuffer buffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
        start = new Komorka2(buffer.getShort(9)-1, buffer.getShort(11)-1, Typ.WEJSCIE);
        stop = new Komorka2(buffer.getShort(13)-1, buffer.getShort(15)-1, Typ.WYJSCIE);
        int counter = buffer.getInt(29);
        byte wall = buffer.get(38);
        byte path = buffer.get(39);
        int value, count;
        int[] xyKursor = {0, 0};

        for(int i = 0; i<counter; i++)
        {
            value = buffer.get(41 + i*3);
            count = (buffer.get(42 + i*3) & 0xFF) + 1;
            for(int j = 0; j<count; j++)
            {
                if(value == wall)
                    maze[xyKursor[0]][xyKursor[1]] = new Komorka2(xyKursor[0], xyKursor[1], Typ.SCIANA);
                else if(value == path)
                    maze[xyKursor[0]][xyKursor[1]] = new Komorka2(xyKursor[0], xyKursor[1], Typ.PRZEJSCIE);
                xyKursor[0]++;
                if(xyKursor[0] == mazeSize[0])
                {
                    xyKursor[0] = 0;
                    xyKursor[1]++;
                }
            }
        }
    }

    private int getSize(byte[] data) {
        int[] xy = {0, 0};
        int firstLineX;
        int ix;
        int i = 0;
        boolean crlf = false;
        while (data[i] != '\n') {
            i++;
            if(data[i] == '\r')
                crlf = true;
        }
        xy[0] = firstLineX = ix = i;

        if(crlf)
            xy[0]--;
        for (; i < data.length; i++) {
            if (data[i] == '\n') {
                if(ix != firstLineX)
                    return 1;
                xy[1]++;
                ix = 0;
            }
            else
                ix++;
        }
        mazeSize = xy;
        return 0;
    }

    private int getSizeBinary(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
        mazeSize = new int[2];
        mazeSize[0] = buffer.getShort(5);
        mazeSize[1] = buffer.getShort(7);
        int counter = buffer.getInt(29);
        int cellCount = mazeSize[0] * mazeSize[1];
        int count;

        for(int i = 0; i<counter; i++)
        {
            count = (buffer.get(42 + i*3) & 0xFF) + 1;
            cellCount -= count;
        }
        if(cellCount != 0)
            return 1;
        return 0;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2D = (Graphics2D)g;
        for (int i = 0; i < mazeSize[0]; i++)
            for (int j = 0; j < mazeSize[1]; j++)
            {
                g2D.setPaint(maze[i][j].kolor);
                g2D.fillRect(i*Komorka2.rozmiar, j*Komorka2.rozmiar, Komorka2.rozmiar, Komorka2.rozmiar);
            }
        g2D.setPaint(start.kolor);
        g2D.fillRect(start.x*Komorka2.rozmiar, start.y*Komorka2.rozmiar, Komorka2.rozmiar, Komorka2.rozmiar);
        g2D.setPaint(stop.kolor);
        g2D.fillRect(stop.x*Komorka2.rozmiar, stop.y*Komorka2.rozmiar, Komorka2.rozmiar, Komorka2.rozmiar);
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
                default -> data[i] = '.';
            }
            if (maze[x][y].x == Labirynt2.start.x && maze[x][y].y == Labirynt2.start.y) {
                data[i] = 'P';
            } else if (maze[x][y].x == Labirynt2.stop.x && maze[x][y].y == Labirynt2.stop.y) {
                data[i] = 'K';
            }
        }
        output.write(data);
        output.close();
    }

    void SaveToFileBinary(File file) throws IOException {
        FileOutputStream output = new FileOutputStream(file);
        ByteBuffer buffer = ByteBuffer.allocate(40 + mazeSize[0] * mazeSize[1] * 3).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(0x52524243); //0
        buffer.put((byte)0x1B); //4
        buffer.putShort((short) mazeSize[0]); //5
        buffer.putShort((short) mazeSize[1]); //7
        buffer.putShort((short) (start.x+1)); //9
        buffer.putShort((short) (start.y+1)); //11
        buffer.putShort((short) (stop.x+1)); //13
        buffer.putShort((short) (stop.y+1)); //15
        buffer.putLong(0); //17 Reserved
        buffer.putInt(0); //25 Reserved
        buffer.putInt(0);//29 counter
        buffer.putInt(0);//33 Solution Offset
        buffer.put((byte)'#'); //37
        buffer.put((byte)'X'); //38
        buffer.put((byte)' '); //39

        int ix = 0, iy = 0, counter = 0;
        Typ typKomorki;
        Typ typWSlowie;
        short count;
        while(ix < mazeSize[0] && iy < mazeSize[1])
        {
            typKomorki = maze[ix][iy].typ;
            if(typKomorki == Typ.GRANICA)
                typKomorki = Typ.SCIANA;
            typWSlowie = typKomorki;
            count = 0;
            while(typKomorki == typWSlowie)
            {
                ix++;
                count++;
                if(ix == mazeSize[0])
                {
                    ix = 0;
                    iy++;
                }
                if(iy == mazeSize[1])
                    break;
                typKomorki = maze[ix][iy].typ;
                if(typKomorki == Typ.GRANICA)
                    typKomorki = Typ.SCIANA;
            }
            buffer.put((byte)'#');
            if(typWSlowie == Typ.SCIANA)
                buffer.put((byte)'X');
            else
                buffer.put((byte)' ');
            buffer.put((byte)(count-1));
            counter++;
            System.out.println(ix + " " + iy + " " + counter);
        }
        buffer.putInt(29, counter);
        output.write(buffer.array());
        output.close();
    }
}
