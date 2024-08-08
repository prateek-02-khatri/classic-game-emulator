import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class Home_Screen extends JPanel
{
    Image emulator = new ImageIcon("src/Images/Main Emulator.png").getImage();
    JLabel creator, name; // 1382, 744
    JButton quit;
    private Border border = BorderFactory.createLineBorder(Color.WHITE,1);
    Home_Screen()
    {
        setBounds(Game_Emulator.f.getBounds());
        setLayout(null);
        setBackground(null);
        setOpaque(false);

        Font font1 = new Font("FixedsysTTF",Font.PLAIN,18);
        Font font2 = new Font("FixedsysTTF",Font.PLAIN,20);

        creator = new JLabel("Created by -");
        creator.setBounds(20,635,200,20);
        creator.setFont(font1);
        creator.setForeground(Color.WHITE);
        creator.setBorder(border);
        add(creator);

        name = new JLabel("Prateek Khatri");
        name.setBounds(20,660,200,20);
        name.setFont(font1);
        name.setForeground(Color.WHITE);
        name.setBorder(border);
        add(name);

        quit = new JButton("QUIT");
        quit.setBounds(1240,655,85,30);
        quit.setFont(font2);
        quit.setForeground(Color.WHITE);
        quit.setBackground(Color.BLACK);
//        quit.setBorder(border);
        quit.setBorderPainted(false);
        quit.setFocusPainted(false);
        quit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                super.mouseClicked(e);
                Game_Emulator.f.dispose();
                System.exit(0);
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                super.mouseEntered(e);
                quit.setForeground(Color.BLACK);
                quit.setBackground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                super.mouseExited(e);
                quit.setForeground(Color.WHITE);
                quit.setBackground(Color.BLACK);
            }
        });
        add(quit);
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        g.drawImage(emulator,91,12,1200,166,null);
    }
}
class Games extends JPanel implements MouseListener
{
    JLabel pingPongName, snakeManiaName, flappyBirdName, ticTacToeName;
    JButton pingPong, snakeMania, flappyBird, ticTacToe;
    int initialWidth = 200, initialHeight = 160;
    int hoverWidth = 210, hoverHeight = 170;
    int horizontalGap = 90, verticalGap = 75;
    int x1 = 80, x2 = x1+initialWidth+horizontalGap;
    int y1 = 25, y2 = y1+initialHeight+verticalGap; // 260
    ImageIcon pingPongIcon, snakeManiaIcon, flappyBirdIcon, ticTacToeIcon;
    ImageIcon pingPongHover, snakeManiaHover, flappyBirdHover, ticTacToeHover;
    Games()
    {
        setBounds(366,200,650,475);
        setLayout(null);
        setBackground(null);

        Font font = new Font("FixedsysTTF", Font.BOLD, 20);

        pingPongIcon = new ImageIcon("src/Images/Icon/Ping Pong.png");
        snakeManiaIcon = new ImageIcon("src/Images/Icon/Snake Mania.png");
        flappyBirdIcon = new ImageIcon("src/Images/Icon/Flappy Bird.png");
        ticTacToeIcon = new ImageIcon("src/Images/Icon/Tic Tac Toe.png");

        pingPongHover = new ImageIcon("src/Images/Hover_Icon/Ping Pong hover.png");
        snakeManiaHover = new ImageIcon("src/Images/Hover_Icon/Snake Mania hover.png");
        flappyBirdHover = new ImageIcon("src/Images/Hover_Icon/Flappy Bird hover.png");
        ticTacToeHover = new ImageIcon("src/Images/Hover_Icon/Tic Tac Toe hover.png");

        pingPongName = new JLabel("Ping Pong");
        pingPongName.setBounds(x1,205,initialWidth,30);
        pingPongName.setHorizontalAlignment(0);
        pingPongName.setFont(font);
        pingPongName.setForeground(Color.WHITE);
        add(pingPongName);

        snakeManiaName = new JLabel("Snake Mania");
        snakeManiaName.setBounds(x2,205,initialWidth,30);
        snakeManiaName.setHorizontalAlignment(0);
        snakeManiaName.setFont(font);
        snakeManiaName.setForeground(Color.WHITE);
        add(snakeManiaName);

        flappyBirdName = new JLabel("Flappy Bird");
        flappyBirdName.setBounds(x1,435,initialWidth,30);
        flappyBirdName.setHorizontalAlignment(0);
        flappyBirdName.setFont(font);
        flappyBirdName.setForeground(Color.WHITE);
        add(flappyBirdName);

        ticTacToeName = new JLabel("Tic Tac Toe");
        ticTacToeName.setBounds(x2,435,initialWidth,30);
        ticTacToeName.setHorizontalAlignment(0);
        ticTacToeName.setFont(font);
        ticTacToeName.setForeground(Color.WHITE);
        add(ticTacToeName);

        /*
        (x1,y1)     (x2,y1)
        (x1,y2)     (x2,y2)
        */

        pingPong = new JButton(new ImageIcon(pingPongIcon.getImage()));
        pingPong.setBounds(x1,y1,initialWidth, initialHeight);
        pingPong.setBackground(null);
        pingPong.setContentAreaFilled(false);
        pingPong.setBorder(null);
        pingPong.setBorderPainted(false);
        pingPong.addMouseListener(this);
        add(pingPong);

        snakeMania = new JButton(new ImageIcon(snakeManiaIcon.getImage()));
        snakeMania.setBounds(x2,y1,initialWidth, initialHeight);
        snakeMania.setBackground(null);
        snakeMania.setContentAreaFilled(false);
        snakeMania.setBorder(null);
        snakeMania.setBorderPainted(false);
        snakeMania.addMouseListener(this);
        add(snakeMania);

        flappyBird = new JButton(new ImageIcon(flappyBirdIcon.getImage()));
        flappyBird.setBounds(x1,y2,initialWidth, initialHeight);
        flappyBird.setBackground(null);
        flappyBird.setContentAreaFilled(false);
        flappyBird.setBorder(null);
        flappyBird.setBorderPainted(false);
        flappyBird.addMouseListener(this);
        add(flappyBird);

        ticTacToe = new JButton(new ImageIcon(ticTacToeIcon.getImage()));
        ticTacToe.setBounds(x2,y2,initialWidth, initialHeight);
        ticTacToe.setBackground(null);
        ticTacToe.setContentAreaFilled(false);
        ticTacToe.setBorder(null);
        ticTacToe.setBorderPainted(false);
        ticTacToe.addMouseListener(this);
        add(ticTacToe);
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        if (e.getSource() == pingPong && e.getButton() == MouseEvent.BUTTON1)
        {
            new Ping_Pong_Main();
            Game_Emulator.f.setVisible(false);
        }
        else if (e.getSource() == snakeMania && e.getButton() == MouseEvent.BUTTON1)
        {
            new Snake_Main();
            Game_Emulator.f.setVisible(false);
        }
        else if (e.getSource() == flappyBird && e.getButton() == MouseEvent.BUTTON1)
        {
            new Flappy_Bird_Main();
            Game_Emulator.f.setVisible(false);
        }
        else if (e.getSource() == ticTacToe && e.getButton() == MouseEvent.BUTTON1)
        {
            new Tic_Tac_Toe_Main();
            Game_Emulator.f.setVisible(false);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        /*
        (x1,y1)     (x2,y1)
        (x1,y2)     (x2,y2)
        */

        if (e.getSource() == pingPong)
        {
            pingPong.setBounds(x1-5,y1-5,hoverWidth,hoverHeight);
            pingPong.setIcon(pingPongHover);
        }
        else if (e.getSource() == snakeMania)
        {
            snakeMania.setBounds(x2-5,y1-5,hoverWidth,hoverHeight);
            snakeMania.setIcon(snakeManiaHover);
        }
        else if (e.getSource() == flappyBird)
        {
            flappyBird.setBounds(x1-5,y2-5,hoverWidth,hoverHeight);
            flappyBird.setIcon(flappyBirdHover);
        }
        else if (e.getSource() == ticTacToe)
        {
            ticTacToe.setBounds(x2-5,y2-5,hoverWidth,hoverHeight);
            ticTacToe.setIcon(ticTacToeHover);
        }
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        /*
        (x1,y1)     (x2,y1)
        (x1,y2)     (x2,y2)
        */
        if (e.getSource() == pingPong)
        {
            pingPong.setBounds(x1,y1,initialWidth,initialHeight);
            pingPong.setIcon(pingPongIcon);
        }
        else if (e.getSource() == snakeMania)
        {
            snakeMania.setBounds(x2,y1,initialWidth,initialHeight);
            snakeMania.setIcon(snakeManiaIcon);
        }
        else if (e.getSource() == flappyBird)
        {
            flappyBird.setBounds(x1,y2,initialWidth,initialHeight);
            flappyBird.setIcon(flappyBirdIcon);
        }
        else if (e.getSource() == ticTacToe)
        {
            ticTacToe.setBounds(x2,y2,initialWidth,initialHeight);
            ticTacToe.setIcon(ticTacToeIcon);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}
}

public class Game_Emulator
{
    static JFrame f;
    Game_Emulator()
    {
        f = new JFrame("Classic Game Emulator");
        f.setBounds(-8, -1, 1382, 744);
        f.getContentPane().setBackground(Color.BLACK);
        f.setLayout(null);
        f.setUndecorated(true);
        f.setLocationRelativeTo(null);
        f.setIconImage(new ImageIcon("src/Images/Icon/Game Emulator.png").getImage());
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Games games = new Games();
        f.add(games);

        Home_Screen homeScreen = new Home_Screen();
        f.add(homeScreen);

        f.setVisible(true);
    }

    public static void main(String[] args)
    {
        new Game_Emulator();
    }
}
