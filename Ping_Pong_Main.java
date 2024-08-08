import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.io.File;
import java.util.Enumeration;
import java.util.Map;

class Sounds_Ping_Pong
{
    static File leftFile, rightFile;
    static AudioInputStream leftMusic, rightMusic;
    static Clip leftClip, rightClip;

    Sounds_Ping_Pong() {
        leftFile = new File("src/Ping_Pong/Sounds/Left.wav");
        rightFile = new File("src/Ping_Pong/Sounds/Right.wav");

        try {
            leftMusic = AudioSystem.getAudioInputStream(leftFile);
            leftClip = AudioSystem.getClip();
            leftClip.open(leftMusic);

            rightMusic = AudioSystem.getAudioInputStream(rightFile);
            rightClip = AudioSystem.getClip();
            rightClip.open(rightMusic);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static void playLeft() {
        try
        {
            if (leftClip.isRunning()) {
                leftClip.stop();
            }

            leftClip.setFramePosition(0);
            leftClip.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static void playRight() {
        try
        {
            if (rightClip.isRunning()) {
                rightClip.stop();
            }

            rightClip.setFramePosition(0);
            rightClip.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Name_Ping extends JPanel
{
    Image image;
    Name_Ping()
    {
        setBounds(5,15,135,615);
        setBackground(null);
        setOpaque(false);
//        setBorder(new LineBorder(Color.WHITE,1));

        image = new ImageIcon("src/Ping_Pong/Images/Ping Image.png").getImage();
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        g.drawImage(image,0,7,image.getWidth(null),image.getHeight(null),null);
    }
}
class Name_Pong extends JPanel
{
    Image image;
    Name_Pong()
    {
        setBounds(1227,15,135,615);
        setBackground(null);
        setOpaque(false);
//        setBorder(new LineBorder(Color.WHITE,1));

        image = new ImageIcon("src/Ping_Pong/Images/Pong Image.png").getImage();
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        g.drawImage(image,0,7,image.getWidth(null),image.getHeight(null),null);
    }
}
class Paddle_Controller implements KeyListener
{
    private boolean upKeyPressed = false;
    private boolean downKeyPressed = false;
    private boolean wKeyPressed = false;
    private boolean sKeyPressed = false;
    public boolean isUpKeyPressed() {
        return upKeyPressed;
    }
    public boolean isDownKeyPressed() {
        return downKeyPressed;
    }
    public boolean isWKeyPressed() {
        return wKeyPressed;
    }
    public boolean isSKeyPressed() {
        return sKeyPressed;
    }
    @Override
    public void keyPressed(KeyEvent e)
    {
        switch (e.getKeyCode())
        {
            case KeyEvent.VK_UP -> upKeyPressed = true;
            case KeyEvent.VK_DOWN -> downKeyPressed = true;
            case KeyEvent.VK_W -> wKeyPressed = true;
            case KeyEvent.VK_S -> sKeyPressed = true;
        }
    }
    @Override
    public void keyReleased(KeyEvent e)
    {
        switch (e.getKeyCode())
        {
            case KeyEvent.VK_UP -> upKeyPressed = false;
            case KeyEvent.VK_DOWN -> downKeyPressed = false;
            case KeyEvent.VK_W -> wKeyPressed = false;
            case KeyEvent.VK_S -> sKeyPressed = false;
        }
    }
    public void releaseKeys()
    {
        upKeyPressed = false;
        downKeyPressed = false;
        wKeyPressed = false;
        sKeyPressed = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
class Game_Timer
{
    private Timer timer;
    private double duration;
    private boolean running;
    private double remainingTime;
    public Game_Timer(double duration)
    {
        this.duration = duration;
        remainingTime = this.duration;

        timer = new Timer(1000, e ->
        {
            if (running && !Ping_Pong_Main.pingPong.isTimeUp)
            {
                remainingTime -= 1000;
                updateTimeLabel(remainingTime);

                if (remainingTime <= 0)
                {
                    stop();
                    Ping_Pong_Main.pingPong.isTimeUp = true;
                }
            }
        });
    }
    public void start()
    {
        if (!running)
        {
            timer.start();
            running = true;
        }
    }
    public void stop()
    {
        if (running)
        {
            timer.stop();
            running = false;
        }
    }
    public void pause() {
        stop();
    }
    public void resume()
    {
        if (!running)
        {
            timer.start();
            running = true;
        }
    }
    public void updateTimeLabel(double remainingTime) {
        Ping_Pong_Main.pingPong.timeLabel.setText(getFormattedTime(remainingTime));
    }
    protected String getFormattedTime(double timeInMillis)
    {
        long minutes = (long) (timeInMillis / 1000) / 60;
        long seconds = (long) (timeInMillis / 1000) % 60;
        return String.format("%d:%02d", minutes, seconds);
    }
}
class ConfirmWindow extends JDialog
{
    Ping_Pong pingPong;
    Font font = new Font("FixedsysTTF",Font.PLAIN,24);
    private void runGame()
    {
        dispose();

        pingPong.isGameOver = true;
        pingPong.isTimeUp = false;

        pingPong.player1Score = 0;
        pingPong.player2Score = 0;

        pingPong.player1.setText(pingPong.player1Name+" : "+pingPong.player1Score);
        pingPong.player2.setText(pingPong.player2Name+" : "+pingPong.player2Score);

        pingPong.setGameTimer(pingPong.currentMatchTime);
        pingPong.setDefaultPosition();
        pingPong.requestFocusInWindow();
        pingPong.repaint();

        new Sounds_Ping_Pong();
    }
    ConfirmWindow()
    {
        pingPong = Ping_Pong_Main.pingPong;
        Color bgColor = Color.DARK_GRAY;

        JPanel p = new JPanel();
        p.setLayout(null);
        p.setBackground(bgColor);

        setSize(600,325);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setContentPane(p);

        JLabel timeUp = new JLabel("Time Up..!!");
        timeUp.setBounds(50,50,500,50);
        timeUp.setForeground(Color.RED);
        timeUp.setFont(new Font("FixedsysTTF",Font.PLAIN,34));
        timeUp.setHorizontalAlignment(0);
        p.add(timeUp);

        JLabel l = new JLabel();
        l.setBounds(50,150,500,30);
        l.setForeground(Color.WHITE);
        l.setFont(font);
        l.setHorizontalAlignment(0);
        String text = pingPong.winner.equals("DRAW") ? "X Match Draw X" : (pingPong.winner + " Wins");
        l.setText(text);
        p.add(l);

        JButton rematch = new JButton("[REMATCH]");
        rematch.setBounds(75,getHeight()-65, 175,30);
        rematch.setFont(font);
        rematch.setForeground(Color.WHITE);
        rematch.setBackground(bgColor);
        rematch.setBorder(null);
        rematch.setBorderPainted(false);
        rematch.setFocusPainted(false);
        rematch.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                runGame();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                rematch.setForeground(bgColor);
                rematch.setBackground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                rematch.setForeground(Color.WHITE);
                rematch.setBackground(bgColor);

            }
        });
        p.add(rematch);

        JButton quit = new JButton("[QUIT]");
        quit.setBounds(375,getHeight()-65,125,30);
        quit.setFont(font);
        quit.setForeground(Color.WHITE);
        quit.setBackground(bgColor);
        quit.setBorder(null);
        quit.setBorderPainted(false);
        quit.setFocusPainted(false);
        quit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                dispose();
                pingPong.resetGame();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                quit.setForeground(bgColor);
                quit.setBackground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                quit.setForeground(Color.WHITE);
                quit.setBackground(bgColor);
            }
        });
        p.add(quit);

        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setVisible(true);
    }
}
class Ping_Pong extends JPanel implements ActionListener, KeyListener
{
    private boolean play = false;
    protected boolean isGameOver = false;
    protected String winner;
    public boolean isTimeUp = false;
    public JLabel timeLabel;
    public double currentMatchTime;
    public JLabel player1, player2;
    protected int player1Score;
    protected int player2Score;
    public String player1Name, player2Name;
    private int panelWidth = 1076;
    private int panelHeight = 675;
    private int height = 615;
    private int ballX, ballY, ballWidth, ballHeight;
    private int dirX, dirY;
    private int paddleLeft, paddleRight, paddleWidth, paddleHeight, paddleDistance;
    private int paddleSpeed = 10;
    protected Timer timer;
    private Paddle_Controller controller;
    private Game_Mode_Ping_Pong gameMode = Ping_Pong_Main.gameModePingPong;
    Game_Timer gameTimer;
    Font font = new Font("FixedsysTTF",Font.PLAIN,28);
    Ping_Pong()
    {
        int x = (1382 - panelWidth)/2 - 8;
        int y = 15;

        controller = new Paddle_Controller();

        setBounds(x,y, panelWidth,panelHeight);
        setBackground(null);
        setOpaque(false);
        setLayout(null);
        setVisible(false);
        setFocusable(true);
        setFocusTraversalKeysEnabled(true);
        addKeyListener(this);

        ballWidth = 25;
        ballHeight = 25;
        paddleWidth = 6;
        paddleHeight = 120;
        paddleDistance = 45;

        setDefaultPosition();
        loadPlayerName();
        clock();

        timer = new Timer(1,this);
        timer.start();
    }
    private void clock()
    {
        timeLabel = new JLabel();
        timeLabel.setBounds((panelWidth-130)/2,height+16,130,35);
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(new Font("FixedsysTTF", Font.PLAIN, 32));
        timeLabel.setHorizontalAlignment(0);
    }
    public void setGameTimer(double matchTime)
    {
        currentMatchTime = matchTime;
        double duration = matchTime * 60 * 1000;
        gameTimer = new Game_Timer(duration);
        timeLabel.setText(gameTimer.getFormattedTime(duration));
        add(timeLabel);
    }
    public void loadPlayerName()
    {
        int labelWidth = 310;

        player1 = new JLabel(player1Name+" : "+player1Score);
        player1.setBounds(5,height+16,labelWidth,30);
        player1.setForeground(Color.WHITE);
        player1.setFont(font);
        player1.setHorizontalAlignment(SwingConstants.LEFT);
        add(player1);

        player2 = new JLabel(player2Name+" : "+player2Score);
        player2.setBounds(panelWidth-labelWidth-5,height+16,labelWidth,30);
        player2.setForeground(Color.WHITE);
        player2.setFont(font);
        player2.setHorizontalAlignment(SwingConstants.RIGHT);
        add(player2);
    }
    protected void resetGame()
    {
        player1Score = 0;
        player2Score = 0;
        setDefaultPosition();
        repaint();
        showGameModeMenu();
        isGameOver = true;
        isTimeUp = false;
    }
    protected void setDefaultPosition()
    {
        ballX = 798;
        ballY = (this.getHeight()-25)/2;
        dirX = -8;
        dirY = -8;
        paddleLeft = height/2 - paddleHeight/2;
        paddleRight = height/2 - paddleHeight/2;
        controller.releaseKeys();
    }
    private void showGameModeMenu()
    {
        setVisible(false);
        gameMode.setVisible(true);
        gameMode.requestFocusInWindow();
    }
    private void declareWinner()
    {
        if (player1Score == player2Score)
            winner = "DRAW";
        else
        {
            if (player1Score > player2Score)
                winner = player1Name;
            else
                winner = player2Name;
        }
        new ConfirmWindow();
    }
    Rectangle ballRect() {
        return new Rectangle(ballX,ballY,ballWidth,ballHeight);
    }
    Rectangle paddleLeftRect() {
        return new Rectangle(paddleDistance,paddleLeft,paddleWidth,paddleHeight);
    }
    Rectangle paddleRightRect() {
        return new Rectangle(panelWidth -paddleDistance,paddleRight,paddleWidth,paddleHeight);
    }
    void movePaddleRightUp()
    {
        if (paddleRight <= 2)
            paddleRight = 2;
        else
            paddleRight -= paddleSpeed;
    }
    void movePaddleRightDown() {
        if (paddleRight >= height - paddleHeight - 2)
            paddleRight = height - paddleHeight - 2;
        else
            paddleRight += paddleSpeed;
    }
    void movePaddleLeftUp() {
        if (paddleLeft <= 2)
            paddleLeft = 2;
        else
            paddleLeft -= paddleSpeed;
    }
    void movePaddleLeftDown() {
        if (paddleLeft >= height - paddleHeight - 2)
            paddleLeft = height - paddleHeight - 2;
        else
            paddleLeft += paddleSpeed;
    }
    private void collision()
    {
        int deflectingAngle = 14;
        if (ballRect().intersects(paddleLeftRect()))
        {
            int deltaY = ballY + ballHeight / 2 - (paddleLeft + paddleHeight / 2);
            dirX = Math.abs(dirX);
            dirY = deltaY / deflectingAngle;
            if (dirY != 0)
            {
                if (dirY <= 2)
                    dirY *= 4;
                else
                    dirY *= 3;
            }
            else
                dirY = 1;
            Sounds_Ping_Pong.playLeft();
        }
        else if (ballRect().intersects(paddleRightRect()))
        {
            int deltaY = ballY + ballHeight / 2 - (paddleRight + paddleHeight / 2);
            dirX = -Math.abs(dirX);
            dirY = deltaY / deflectingAngle;
            if (dirY != 0)
            {
                if (dirY <= 2)
                    dirY *= 3;
                else
                    dirY *= 2;
            }
            else
                dirY = 1;
            Sounds_Ping_Pong.playRight();
        }
    }
    void autoPaddleLeft()
    {
        if (ballX <= panelWidth / 2 && dirX < 0)
        {
            int ballCenterY = ballY + ballHeight / 2;
            int deltaY = ballCenterY - (paddleLeft + paddleHeight / 2);

            if (Math.abs(deltaY) > paddleSpeed)
            {
                if (deltaY < 0)
                    paddleLeft -= paddleSpeed;
                else
                    paddleLeft += paddleSpeed;
            }
            else
            {
                paddleLeft = ballCenterY - paddleHeight / 2;
            }

            if (paddleLeft < 0)
                paddleLeft = 0;
            else if (paddleLeft > height - paddleHeight)
                paddleLeft = height - paddleHeight;
        }
    }
    private void stopGame()
    {
        play = false;
        timer.stop();
        gameTimer.pause();
    }
    protected void gameOver()
    {
        if (ballX <= 7)
        {
            isGameOver = true;
            stopGame();
            player2Score++;
            player2.setText(player2Name+" : "+player2Score);
        }
        else if (ballX > panelWidth-ballWidth-10)
        {
            isGameOver = true;
            stopGame();
            player1Score++;
            player1.setText(player1Name+" : "+player1Score);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (play)
        {
            // Ball Movement
            ballX += dirX;
            ballY += dirY;

            // Ball Direction
            if (ballY < 1 || ballY > height-ballHeight-8)
                dirY = -dirY;

            // Paddle Movement
            if (controller.isWKeyPressed() && gameMode.gameMode == 2)
                movePaddleLeftUp();

            if (controller.isSKeyPressed() && gameMode.gameMode == 2)
                movePaddleLeftDown();

            if (controller.isUpKeyPressed())
                movePaddleRightUp();

            if (controller.isDownKeyPressed())
                movePaddleRightDown();

            // Paddle Collision
            if (gameMode.gameMode == 1)
            {
                autoPaddleLeft();
                if (ballRect().intersects(paddleLeftRect()))
                {
                    dirX = -dirX;
                    Sounds_Ping_Pong.playLeft();
                }
                else if (ballRect().intersects(paddleRightRect()))
                {
                    dirX = -dirX;
                    Sounds_Ping_Pong.playRight();
                }
            }
            else
                collision();

            // Game Over
            gameOver();

            // Time Up
            if (isTimeUp)
            {
                stopGame();
                declareWinner();
            }
        }

        repaint();
    }
    @Override
    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE && isGameOver)
        {
            resetGame();
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            if (isGameOver)
            {
                // Work if any player just scored
                isGameOver = false;
                setDefaultPosition();
                play = true;
                timer.start();
                gameTimer.start();
            }
            else
            {
                // To Pause the Game
                play = !play;
                if (play)
                    gameTimer.resume();
                else
                    gameTimer.pause();
            }
            repaint();
        }

        if (play)
            controller.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        if (play)
            controller.keyReleased(e);
    }

    @Override
    public void paint(Graphics G)
    {
        super.paint(G);
        Graphics2D g = (Graphics2D)G;

        // Border
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(2));
        g.drawRect(1,1,panelWidth-2,height-1);

        // Centre Partition
        float[] dashPattern = {10, 10};
        g.setStroke(new BasicStroke(2,BasicStroke.CAP_BUTT,BasicStroke.JOIN_ROUND,0,dashPattern,0));

        g.drawLine(panelWidth /2, 0, panelWidth /2,height);
        g.drawOval(panelWidth /2-60,height/2-60,120,120);

        // Ball
        g.setColor(Color.RED);
        g.fillOval(ballX,ballY,ballWidth,ballHeight);

        // Paddles
        g.setColor(Color.ORANGE);
        g.fillRect(paddleDistance,paddleLeft,paddleWidth,paddleHeight);
        g.fillRect(panelWidth-paddleDistance,paddleRight,paddleWidth,paddleHeight);
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
class Game_Mode_Ping_Pong extends JPanel implements MouseListener
{
    int gameMode = 0;
    int width = 1000;
    int height = Ping_Pong_Main.f.getHeight();
    JPanel homePanel, menuPanel;
    private JButton onePlayer, twoPlayer, exit;
    private Image paddleXballImage, paddleImage;
    private ImageIcon onePlayerIcon, twoPlayerIcon, exitIcon;
    private int onePlayerX, twoPlayerX, exitX;
    private ImageIcon onePlayerHover, twoPlayerHover, exitHover;
    private int onePlayerY, twoPlayerY, exitY;
    private JRadioButton [] matchTime;
    private ButtonGroup buttonGroup;
    private JLabel headLabel, player1Label, player2Label;
    protected JTextField player1Name, player2Name;
    private JButton play, back;
    private String [] matchTimes;
    private Border border = null;
    private Font font = new Font("FixedsysTTF", Font.PLAIN,24);
    Game_Mode_Ping_Pong()
    {
        int x = (Ping_Pong_Main.f.getWidth() - width)/2;
        int y = 0;
        setBounds(x,y,width,height);
        setBackground(null);
        setLayout(null);
        setBorder(border);

        homePanel = new JPanel();
        homePanel.setBounds(5,5,width-10,height-10);
        homePanel.setBackground(null);
        homePanel.setLayout(null);
        homePanel.setBorder(border);

        menuPanel = new JPanel();
        menuPanel.setBounds(5,5,width-10,height-10);
        menuPanel.setBackground(null);
        menuPanel.setLayout(null);
        menuPanel.setBorder(border);
        menuPanel.setVisible(false);

        loadImages();
        PingPongHomePanel();
        PingPongMenuPanel();

        add(homePanel);
        add(menuPanel);
    }
    private void PingPongHomePanel()
    {
        onePlayerX = (homePanel.getWidth() - onePlayerIcon.getIconWidth())/2;
        onePlayerY = 175;
        twoPlayerX = (homePanel.getWidth() - twoPlayerIcon.getIconWidth())/2;
        twoPlayerY = 310;
        exitX = (homePanel.getWidth() - exitIcon.getIconWidth())/2;
        exitY = 440;

        onePlayer = new JButton(onePlayerIcon);
        onePlayer.setBounds(onePlayerX,onePlayerY,onePlayerIcon.getIconWidth(),onePlayerIcon.getIconHeight());
        onePlayer.setBackground(null);
        onePlayer.setContentAreaFilled(false);
        onePlayer.setBorder(border);
        onePlayer.setBorderPainted(false);
        onePlayer.addMouseListener(this);
        homePanel.add(onePlayer);

        twoPlayer = new JButton(twoPlayerIcon);
        twoPlayer.setBounds(twoPlayerX,twoPlayerY,twoPlayerIcon.getIconWidth(),twoPlayerIcon.getIconHeight());
        twoPlayer.setBackground(null);
        twoPlayer.setContentAreaFilled(false);
        twoPlayer.setBorder(border);
        twoPlayer.setBorderPainted(false);
        twoPlayer.addMouseListener(this);
        homePanel.add(twoPlayer);

        exit = new JButton(exitIcon);
        exit.setBounds(exitX,exitY,exitIcon.getIconWidth(),exitIcon.getIconHeight());
        exit.setBackground(null);
        exit.setContentAreaFilled(false);
        exit.setBorder(border);
        exit.setBorderPainted(false);
        exit.addMouseListener(this);
        homePanel.add(exit);
    }
    private void PingPongMenuPanel()
    {
        headLabel = new JLabel("Select Match Time");
        headLabel.setBounds((menuPanel.getWidth()-300)/2,70,300,32);
        headLabel.setBorder(border);
        headLabel.setForeground(Color.WHITE);
        headLabel.setFont(new Font("FixedsysTTF", Font.PLAIN, 30));
        headLabel.setHorizontalAlignment(0);
        menuPanel.add(headLabel);

        int x, y, k=1;

        matchTimes = new String[]{"30 Seconds", "1 Minutes", "2 Minutes", "3 Minutes", "5 Minutes", "10 Minutes"};
        matchTime = new JRadioButton[6];

        buttonGroup = new ButtonGroup();

        for (int i=0; i<matchTime.length; i++)
        {
            matchTime[i] = new JRadioButton(matchTimes[i]);
            matchTime[i].setFont(font);
            matchTime[i].setBackground(Color.WHITE);
            matchTime[i].setForeground(Color.WHITE);
            matchTime[i].setBackground(null);
            matchTime[i].setBorder(border);
            matchTime[i].setFocusPainted(false);
            if (i<3)
            {
                x = 235;
                y = 100+(75*(i+1));
            }
            else
            {
                x = 590;
                y = 100+(75*k);
                k++;
            }
            matchTime[i].setBounds(x,y,175,50);
            buttonGroup.add(matchTime[i]);
            menuPanel.add(matchTime[i]);
        }
        matchTime[1].setSelected(true);

        int first = 290, second = 445;
        int line1Y = 430, line2Y = 475;

        player1Label = new JLabel("Player 1  -");
        player1Label.setBounds(first,line1Y,170,30);
        player1Label.setFont(font);
        player1Label.setForeground(Color.WHITE);
        player1Label.setBorder(border);
        menuPanel.add(player1Label);

        Font textFieldFont = font;
        Map attrib = textFieldFont.getAttributes();
        attrib.put(TextAttribute.UNDERLINE,TextAttribute.UNDERLINE_ON);

        player1Name = new JTextField();
        player1Name.setBounds(second,line1Y,300,30);
        player1Name.setFont(textFieldFont.deriveFont(attrib));
        player1Name.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        player1Name.setBackground(null);
        player1Name.setForeground(Color.WHITE);
        player1Name.setBorder(border);
        menuPanel.add(player1Name);

        player2Label = new JLabel("Player 2  -");
        player2Label.setBounds(first,line2Y,170,30);
        player2Label.setFont(font);
        player2Label.setForeground(Color.WHITE);
        player2Label.setBorder(border);
        menuPanel.add(player2Label);

        player2Name = new JTextField();
        player2Name.setBounds(second,line2Y,300,30);
        player2Name.setFont(textFieldFont.deriveFont(attrib));
        player2Name.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        player2Name.setBackground(null);
        player2Name.setForeground(Color.WHITE);
        player2Name.setBorder(border);
        menuPanel.add(player2Name);

        play = new JButton("PLAY");
        play.setBounds((this.getWidth()-100)/2,555,100,30);
        play.setFont(font);
        play.setForeground(Color.WHITE);
        play.setBackground(null);
        play.setBorder(border);
        play.setBorderPainted(false);
        play.setFocusPainted(false);
        play.setContentAreaFilled(true);
        play.addMouseListener(this);
        menuPanel.add(play);

        back = new JButton("BACK");
        back.setBounds((this.getWidth()-100)/2,600,100,30);
        back.setFont(font);
        back.setForeground(Color.WHITE);
        back.setBackground(Color.BLACK);
        back.setBorder(border);
        back.setBorderPainted(false);
        back.setFocusPainted(false);
        back.setContentAreaFilled(true);
        back.addMouseListener(this);
        menuPanel.add(back);
    }
    private void loadImages()
    {
        onePlayerIcon = new ImageIcon("src/Ping_Pong/Images/One Player Icon.png");
        onePlayerHover = new ImageIcon("src/Ping_Pong/Images/One Player Hover.png");

        twoPlayerIcon = new ImageIcon("src/Ping_Pong/Images/Two Player Icon.png");
        twoPlayerHover = new ImageIcon("src/Ping_Pong/Images/Two Player Hover.png");

        exitIcon = new ImageIcon("src/Ping_Pong/Images/Exit Icon.png");
        exitHover = new ImageIcon("src/Ping_Pong/Images/Exit Hover.png");

        paddleXballImage = new ImageIcon("src/Ping_Pong/Images/Paddle X Ball.png").getImage();
        paddleImage = new ImageIcon("src/Ping_Pong/Images/Paddle.png").getImage();
    }
    private String getSelectedButtonText(ButtonGroup buttonGroup)
    {
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();)
        {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected())
            {
                return button.getText();
            }
        }
        return null;
    }
    private void runGame()
    {
        setVisible(false);
        Ping_Pong pingPong = Ping_Pong_Main.pingPong;

        pingPong.player1Name = player1Name.getText();
        pingPong.player2Name = player2Name.getText();

        pingPong.player1.setText(pingPong.player1Name+" : "+0);
        pingPong.player2.setText(pingPong.player2Name+" : "+0);

        String getSelected = getSelectedButtonText(buttonGroup);
        String temp = (getSelected != null ? getSelected.split(" ") : new String[0])[0];
        double gameTime = Double.parseDouble(temp) == 30d ? 0.5 : Double.parseDouble(temp);

        pingPong.setGameTimer(gameTime);
        pingPong.setVisible(true);
        pingPong.requestFocusInWindow();
        pingPong.setDefaultPosition();

        new Sounds_Ping_Pong();
    }
    private void showMenu()
    {
        homePanel.setVisible(false);
        if (gameMode == 1)
        {
            player1Name.setText("Computer");
            player2Name.setText("");
            player1Name.setEditable(false);
        }
        else
        {
            player1Name.setText("");
            player2Name.setText("");
            player1Name.setEditable(true);
            player2Name.setEditable(true);
        }
        matchTime[1].setSelected(true);
        menuPanel.setVisible(true);
        menuPanel.requestFocusInWindow();
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        g.drawImage(paddleXballImage,12,height-paddleXballImage.getHeight(null)-45,paddleXballImage.getWidth(null),paddleXballImage.getHeight(null),null);
        g.drawImage(paddleImage,width-paddleImage.getWidth(null)-10,10,paddleImage.getWidth(null), paddleImage.getHeight(null),null);
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        if (e.getButton() == MouseEvent.BUTTON1)
        {
            if (e.getSource() == onePlayer)
            {
                gameMode = 1;
                showMenu();
            }
            else if (e.getSource() == twoPlayer)
            {
                gameMode = 2;
                showMenu();
            }
            else if (e.getSource() == exit)
            {
                Ping_Pong_Main.f.dispose();
                Game_Emulator.f.setVisible(true);
            }
            if (e.getSource() == play)
            {
                if (player1Name.getText().isEmpty() || player2Name.getText().isEmpty())
                {
                    JOptionPane.showMessageDialog(this,"Please enter Player Name..!!","Invalid Player Name",JOptionPane.WARNING_MESSAGE);
                }
                else if (player1Name.getText().length() > 15 || player2Name.getText().length() > 15)
                {
                    JOptionPane.showMessageDialog(this,"Player name can have at most 15 characters..!!","Invalid Player Name",JOptionPane.WARNING_MESSAGE);
                }
                else
                {
                    menuPanel.setVisible(false);
                    homePanel.setVisible(true);
                    runGame();
                }
            }
            else if (e.getSource() == back)
            {
                back.setBackground(Color.BLACK);
                menuPanel.setVisible(false);
                homePanel.setVisible(true);
                homePanel.requestFocusInWindow();
                matchTime[1].setSelected(true);
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        if (e.getSource() == onePlayer)
        {
            int tempX = (onePlayerHover.getIconWidth() - onePlayerIcon.getIconWidth())/2;
            int tempY = (onePlayerHover.getIconHeight() - onePlayerIcon.getIconHeight())/2;
            onePlayer.setBounds(onePlayerX-tempX,onePlayerY-tempY,onePlayerHover.getIconWidth(),onePlayerHover.getIconHeight());
            onePlayer.setIcon(onePlayerHover);
        }
        else if (e.getSource() == twoPlayer)
        {
            int tempX = (twoPlayerHover.getIconWidth() - twoPlayerIcon.getIconWidth())/2;
            int tempY = (twoPlayerHover.getIconHeight() - twoPlayerIcon.getIconHeight())/2;
            twoPlayer.setBounds(twoPlayerX-tempX,twoPlayerY-tempY,twoPlayerHover.getIconWidth(),twoPlayerHover.getIconHeight());
            twoPlayer.setIcon(twoPlayerHover);
        }
        else if (e.getSource() == exit)
        {
            int tempX = (exitHover.getIconWidth() - exitIcon.getIconWidth())/2;
            int tempY = (exitHover.getIconHeight() - exitIcon.getIconHeight())/2;
            exit.setBounds(exitX-tempX,exitY-tempY,exitHover.getIconWidth(),exitHover.getIconHeight());
            exit.setIcon(exitHover);
        }
        if (e.getSource() == play)
        {
            play.setForeground(Color.BLACK);
            play.setBackground(Color.WHITE);
        }
        else if (e.getSource() == back)
        {
            back.setForeground(Color.BLACK);
            back.setBackground(Color.WHITE);
        }
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        if (e.getSource() == onePlayer)
        {
            onePlayer.setBounds(onePlayerX,onePlayerY,onePlayerIcon.getIconWidth(),onePlayerIcon.getIconHeight());
            onePlayer.setIcon(onePlayerIcon);
        }
        else if (e.getSource() == twoPlayer)
        {
            twoPlayer.setBounds(twoPlayerX,twoPlayerY,twoPlayerIcon.getIconWidth(),twoPlayerIcon.getIconHeight());
            twoPlayer.setIcon(twoPlayerIcon);
        }
        else if (e.getSource() == exit)
        {
            exit.setBounds(exitX,exitY,exitIcon.getIconWidth(),exitIcon.getIconHeight());
            exit.setIcon(exitIcon);
        }
        if (e.getSource() == play)
        {
            play.setForeground(Color.WHITE);
            play.setBackground(Color.BLACK);
        }
        else if (e.getSource() == back)
        {
            back.setForeground(Color.WHITE);
            back.setBackground(Color.BLACK);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}
}
public class Ping_Pong_Main
{
    static JFrame f;
    static Game_Mode_Ping_Pong gameModePingPong;
    static Ping_Pong pingPong;
    Ping_Pong_Main()
    {
        f = new JFrame("Ping Pong");
        f.setBounds(-8, -1, 1382, 744);
        f.getContentPane().setBackground(Color.BLACK);
        f.setLayout(null);
        f.setUndecorated(true);
        f.setLocationRelativeTo(null);
        f.setIconImage(new ImageIcon("src/Images/Icon/Ping Pong.png").getImage());
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Name_Ping namePing = new Name_Ping();
        f.add(namePing);

        Name_Pong namePong = new Name_Pong();
        f.add(namePong);

        gameModePingPong = new Game_Mode_Ping_Pong();
        f.add(gameModePingPong);

        pingPong = new Ping_Pong();
        f.add(pingPong);

        f.setVisible(true);
    }

    public static void main(String[] args)
    {
        new Game_Emulator();
    }
}
