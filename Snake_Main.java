import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Random;
interface basicResources
{
    Font font = new Font("FixedsysTTF", Font.BOLD, 24);
    Image rightMouth = new ImageIcon("src/Snake_Mania/Images/rightMouth.png").getImage();
    Image leftMouth = new ImageIcon("src/Snake_Mania/Images/leftMouth.png").getImage();
    Image upMouth = new ImageIcon("src/Snake_Mania/Images/upMouth.png").getImage();
    Image downMouth = new ImageIcon("src/Snake_Mania/Images/downMouth.png").getImage();
    Image body = new ImageIcon("src/Snake_Mania/Images/body.png").getImage();
    Image enemy = new ImageIcon("src/Snake_Mania/Images/enemy.png").getImage();
}
class Sounds_Snake_Mania
{
    File backgroundMusicFile, gameOverFile;
    AudioInputStream backgroundMusic, gameOverMusic;
    static Clip backgroundMusicClip, gameOverClip;
    Sounds_Snake_Mania()
    {
        backgroundMusicFile = new File("src/Snake_Mania/Sound/Background Music.wav");
        gameOverFile = new File("src/Snake_Mania/Sound/Game Over.wav");
        try
        {
            backgroundMusic = AudioSystem.getAudioInputStream(backgroundMusicFile);
            backgroundMusicClip = AudioSystem.getClip();
            backgroundMusicClip.open(backgroundMusic);
            backgroundMusicClip.loop(100);
            backgroundMusicClip.start();

            gameOverMusic = AudioSystem.getAudioInputStream(gameOverFile);
            gameOverClip = AudioSystem.getClip();
            gameOverClip.open(gameOverMusic);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    protected static void playGameOverMusic(){
        gameOverClip.start();
    }
    protected static void stopMusic() {
        backgroundMusicClip.stop();
    }

}
class Head_Snake_Mania extends JPanel implements basicResources
{
    int imageX = 392;
    void setImageX(int x) {
        imageX = x;
    }
    Head_Snake_Mania()
    {
        setBounds(46, 20, 1275, 85);
        setBorder(null);
        setLayout(null);
        setBackground(new Color(68,114,196));
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        add(Snake.playerScore);
        add(Snake.scoreLabel);
        if (Game_Mode_Snake_Mania.mode == 1)
        {
            add(Snake.highScore);
            add(Snake.highScoreLabel);
        }
        else if (Game_Mode_Snake_Mania.mode == 2)
        {
            add(Snake.target);
            add(Snake.levelNumber);
        }

        Image image = new ImageIcon("src/Snake_Mania/Images/Snake_Mania_Head.png").getImage();
        g.drawImage(image,imageX,8,490,70,null);
    }
}
class High_Score_Snake_Mania
{
    int currentHighScore;
    File file;
    BufferedReader reader;
    BufferedWriter writer;
    High_Score_Snake_Mania(String fileName)
    {
        try
        {
            file = new File("src/High Score/"+fileName+".txt");
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(Snake_Main.f,"File "+fileName+" is missing..!!","Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    public void updateRecord(int highScore)
    {
        try
        {
            writer = new BufferedWriter(new FileWriter("src/High Score/Snake Mania.txt"));
            writer.flush();
            String text = "Endless Mode - "+highScore;
            writer.write(text);
            writer.close();
        }
        catch (Exception e)
        {
           e.printStackTrace();
        }
    }
    public int getRecord()
    {
        try
        {
            reader = new BufferedReader(new FileReader("src/High Score/Snake Mania.txt"));
            String data = reader.readLine();
            currentHighScore = getData(data);
            reader.close();
            return currentHighScore;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return currentHighScore;
    }
    private int getData(String string)
    {
        int reqData = 0;
        if (string != null)
        {
            String [] tempData = string.replaceAll("\\s","").split("-");
            if (tempData.length == 2)
            {
                try
                {
                    reqData = Integer.parseInt(tempData[1]);
                }
                catch (Exception e)
                {
                    JOptionPane.showMessageDialog(Snake_Main.f,"Unable to fetch High Score..!!","Error",JOptionPane.ERROR_MESSAGE);
                    return reqData;
                }
            }
            else
            {
                JOptionPane.showMessageDialog(Snake_Main.f,"One or more files may be corrupted..!!","Error",JOptionPane.ERROR_MESSAGE);
            }
        }
        else
        {
            JOptionPane.showMessageDialog(Snake_Main.f,"Unable to fetch High Score..!!","Error",JOptionPane.ERROR_MESSAGE);
        }
        return reqData;
    }
}
class Snake extends JPanel implements basicResources, KeyListener, ActionListener
{
    private Head_Snake_Mania head;
    static JLabel playerScore, scoreLabel, levelNumber, target, highScore, highScoreLabel;
    static boolean play = false;
    boolean end = false;
    int score = 0, foodTarget = 10;
    private int count = 2;
    int snakeLength = 2;
    int delay = 100;
    int [] snakeX = new int[100];
    int [] snakeY = new int[100];
    int moveRight = 25;
    int moveLeft = -25;
    int moveUp = -25;
    int moveDown = 25;
    boolean up = false;
    boolean down = false;
    boolean right = true;
    boolean left = false;
    int enemyPosX, enemyPosY;
    Random enemyX = new Random();
    Random enemyY = new Random();
    Timer timer;
    private Image mouthDirection = rightMouth;
    Rectangle topWall, bottomWall, leftWall, rightWall, centreWall, snakeHead, enemyRect;
    static int currentHighScore;
    Snake(Head_Snake_Mania head)
    {
        this.head = head;

        setBounds(46,125,1275,550);
        setBackground(null);
        setOpaque(false);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(true);
        setVisible(false);

        playerScore = new JLabel(String.valueOf(score));
        playerScore.setBounds(10,3,100,48);
        playerScore.setForeground(Color.ORANGE);
        playerScore.setHorizontalAlignment(0);
        playerScore.setFont(new Font("FixedsysTTF", Font.BOLD, 45));

        scoreLabel = new JLabel("Score");
        scoreLabel.setBounds(10,50,100,28);
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setHorizontalAlignment(0);
        scoreLabel.setFont(font);

        highScore = new JLabel();
        highScore.setBounds(1100,3,150,48);
        highScore.setForeground(Color.ORANGE);
        highScore.setHorizontalAlignment(0);
        highScore.setFont(new Font("FixedsysTTF", Font.BOLD, 45));

        highScoreLabel = new JLabel("High Score");
        highScoreLabel.setBounds(1100,50,150,28);
        highScoreLabel.setForeground(Color.WHITE);
        highScoreLabel.setHorizontalAlignment(0);
        highScoreLabel.setFont(font);

        target = new JLabel("Target : " + foodTarget);
        target.setBounds(1100,13,150,30);
        target.setForeground(Color.WHITE);
        target.setFont(font);

        levelNumber = new JLabel("Level " + Snake_Main.level);
        levelNumber.setBounds(1100,48,125,30);
        levelNumber.setForeground(Color.WHITE);
        levelNumber.setFont(font);

        defaultSnakeLocation();
        createRectangle();
        generateEnemy();

        timer = new Timer(delay, this);
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);

        // Play Area
        g.setColor(Color.BLACK);
        g.fillRect(0,0,1275,550);
        g.setColor(Color.WHITE);
        g.drawRect(0,0,1274,549);

        g.setColor(Color.ORANGE);

        if (Snake_Main.level == 2)
        {
            timer.setDelay(90);
            g.drawRect(0,0,1275,1); // Top
            g.drawRect(0,548,1275,1); // Bottom
            g.drawRect(0,0,1,550); // Left
            g.drawRect(1273,0,1,550); // Right
        }
        else if (Snake_Main.level == 3)
        {
            timer.setDelay(80);

            g.drawRect(0,0,1275,1); // Top
            g.drawRect(0,548,1275,1); // Bottom
            g.drawRect(0,0,1,550); // Left
            g.drawRect(1273,0,1,550); // Right

            g.drawRect(849,0,1,183); // Top
            g.drawRect(424,366,1,183); // Bottom
            g.drawRect(0,182,425,1); // Left
            g.drawRect(850,366,425,1); // Right

            g.fillRect(575,212,125,125); // Centre
            g.setColor(Color.BLACK);
            g.fillRect(575+25,212+25,75,75); // Centre of Centre
        }

        // Snake Head
        g.drawImage(mouthDirection,snakeX[0],snakeY[0],25,25,null);

        // Snake Body
        for (int i=1; i<=snakeLength; i++)
            g.drawImage(body, snakeX[i], snakeY[i],25,25,null);

        // Enemy
        g.drawImage(enemy, enemyPosX, enemyPosY, 25,25,null);

        // Game Over
        if (Snake_Main.level == 1)
        {
            gameOverLevel1();
        }
        else if (Snake_Main.level == 2 && Game_Mode_Snake_Mania.mode == 2)
        {
            gameOverLevel1();
            gameOverLevel2();
        }
        else if (Snake_Main.level == 3 && Game_Mode_Snake_Mania.mode == 2)
        {
            gameOverLevel1();
            gameOverLevel2();
            gameOverLevel3();
        }
        if (end)
        {
            // 1275,550
            g.setColor(Color.BLACK);
            g.fillRect(425,195,480,125);

            g.setColor(Color.GREEN);

            g.setFont(new Font("FixedsysTTF", Font.BOLD, 36));
            g.drawString("GAME OVER",565,225);

            g.setFont(new Font("FixedsysTTF", Font.BOLD, 30));
            g.drawString("Press ESC to return to Menu",425,295);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (play)
        {
            // Body
            for (int i=snakeLength; i>=1; i--)
            {
                snakeX[i] = snakeX[i-1];
                snakeY[i] = snakeY[i-1];
            }

            // Movement
            if (up)
                snakeY[0] += moveUp;
            if (down)
                snakeY[0] += moveDown;
            if (right)
                snakeX[0] += moveRight;
            if (left)
                snakeX[0] += moveLeft;

            // Direction (1275, 550)
            if (Snake_Main.level == 1)
            {
                if (snakeX[0] < 0)
                    snakeX[0] = 1275;
                if (snakeX[0] > 1275)
                    snakeX[0] = 0;
                if (snakeY[0] < 0)
                    snakeY[0] = 550;
                if (snakeY[0] > 550)
                    snakeY[0] = 0;
            }

            // Generate New Enemy
            snakeHead = new Rectangle(snakeX[0], snakeY[0],25,25);
            enemyRect = new Rectangle(enemyPosX, enemyPosY, 25,25);
            if (snakeHead.intersects(enemyRect))
            {
                if (Snake_Main.level == 1 || Snake_Main.level == 2)
                    generateEnemy();
                else if (Snake_Main.level == 3)
                    generateEnemyLevel3();

                score++;
                snakeLength++;
                if (Game_Mode_Snake_Mania.mode == 2)
                    foodTarget--;

                // Score
                updateScore();

                // Increase Snake Length
                int tailX = snakeX[snakeLength - 2];
                int tailY = snakeY[snakeLength - 2];

                if (snakeX[snakeLength - 2] > snakeX[snakeLength - 1]) {
                    snakeX[snakeLength] = tailX - 25;
                    snakeY[snakeLength] = tailY;
                } else if (snakeX[snakeLength - 2] < snakeX[snakeLength - 1]) {
                    snakeX[snakeLength] = tailX + 25;
                    snakeY[snakeLength] = tailY;
                } else if (snakeY[snakeLength - 2] > snakeY[snakeLength - 1]) {
                    snakeX[snakeLength] = tailX;
                    snakeY[snakeLength] = tailY - 25;
                } else {
                    snakeX[snakeLength] = tailX;
                    snakeY[snakeLength] = tailY + 25;
                }

            }
        }

        // Change Level
        if (Game_Mode_Snake_Mania.mode == 2)
            changeLevel();

        repaint();
    }
    @Override
    public void keyPressed(KeyEvent e)
    {
        if ((e.getKeyCode() == KeyEvent.VK_ESCAPE) && end)
        {
            end = false;
            this.setVisible(false);
            reset();
            count++;
            Snake_Main.gameMode.setVisible(true);
            Snake_Main.f.getContentPane().setBackground(Color.BLACK);
            Snake_Main.gameMode.requestFocusInWindow();
            head.setImageX(392);
            target.setVisible(false);
            levelNumber.setVisible(false);
            highScore.setVisible(false);
            highScoreLabel.setVisible(false);
            updateScore();
            updateLevel();
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE && !end)
        {
            if (!timer.isRunning())
                timer.start();
            play = (count % 2) == 0;
            count++;
            updateScore();
            updateLevel();
            repaint();
        }
        if (play)
        {
            if (e.getKeyCode() == KeyEvent.VK_UP && !down)
            {
                mouthDirection = upMouth;
                up = true;
                right = false;
                left = false;
            }
            else if (e.getKeyCode() == KeyEvent.VK_DOWN && !up)
            {
                mouthDirection = downMouth;
                down = true;
                right = false;
                left = false;
            }
            else if (e.getKeyCode() == KeyEvent.VK_RIGHT && !left)
            {
                mouthDirection = rightMouth;
                right = true;
                up = false;
                down = false;
            }
            else if (e.getKeyCode() == KeyEvent.VK_LEFT && !right)
            {
                mouthDirection = leftMouth;
                left = true;
                up = false;
                down = false;
            }
            repaint();
        }
    }
    private void defaultSnakeLocation()
    {
        snakeX[0] = 150;
        snakeY[0] = 125;
        snakeX[1] = 125;
        snakeY[1] = 125;
        snakeX[2] = 100;
        snakeY[2] = 125;
    }
    private void createRectangle()
    {
        topWall = new Rectangle(849,0,1,183);
        bottomWall = new Rectangle(424,366,1,183);
        leftWall = new Rectangle(0,182,425,1);
        rightWall = new Rectangle(850,366,425,1);
        centreWall = new Rectangle(575,212,125,125);
        snakeHead = new Rectangle(snakeX[0], snakeY[0],25,25);
        enemyRect = new Rectangle(enemyPosX, enemyPosY, 25,25);
    }

    // Generate New Enemy
    private void generateEnemy()
    {
        enemyPosX = enemyX.nextInt(0,1250);
        enemyPosY = enemyY.nextInt(0,525);

        Rectangle newFood = new Rectangle(enemyPosX,enemyPosY,25,25);
        Rectangle [] snakeBody = new Rectangle [snakeLength];

        for (int i=0; i<snakeBody.length; i++)
            snakeBody[i] = new Rectangle(snakeX[i],snakeY[i],25,25);

        for (int i=0; i<snakeBody.length; i++)
        {
            if (snakeBody[i].intersects(newFood))
                generateEnemy();
        }
    }
    private void generateEnemyLevel3()
    {
        enemyPosX = enemyX.nextInt(0,1250);
        enemyPosY = enemyY.nextInt(0,525);

        topWall = new Rectangle(848,0,3,183);
        bottomWall = new Rectangle(423,366,3,183);
        leftWall = new Rectangle(0,181,425,3);
        rightWall = new Rectangle(850,365,425,3);
        centreWall = new Rectangle(575,212,125,125);
        enemyRect = new Rectangle(enemyPosX, enemyPosY, 25,25);

        boolean topWallProblem = enemyRect.intersects(topWall);
        boolean bottomWallProblem = enemyRect.intersects(bottomWall);
        boolean rightWallProblem = enemyRect.intersects(rightWall);
        boolean leftWallProblem = enemyRect.intersects(leftWall);
        boolean centreWallProblem = enemyRect.intersects(centreWall);

        for (int i=snakeLength-1; i>=0; i--)
        {
            if ((snakeX[i] == enemyPosX && snakeY[i] == enemyPosY) || topWallProblem || bottomWallProblem || rightWallProblem || leftWallProblem || centreWallProblem)
                generateEnemyLevel3();
        }
    }
    private void changeLevel()
    {
        if (score == 10 && Snake_Main.level == 1)
        {
            Snake_Main.level = 2;
            foodTarget = 15;
            updateLevel();
            reset();
        }
        if (score == 25 && Snake_Main.level == 2)
        {
            Snake_Main.level = 3;
            foodTarget = 20;
            updateLevel();
            reset();
        }
        if (score == 45 && Snake_Main.level == 3)
        {
            play = false;
            timer.stop();
            timer.setDelay(100);
            count++;
            end = true;
        }
    }
    private void reset()
    {
        play = false;
        timer.stop();
        timer.setDelay(100);
        snakeLength = 2;
        defaultSnakeLocation();
        mouthDirection = rightMouth;
        right = true;
        up = false;
        down = false;
        left = false;
        count++;
    }
    // Game Over
    private void gameOver()
    {
        reset();
        if (Game_Mode_Snake_Mania.mode == 1 && score > currentHighScore)
        {
            Snake_Main.highScoreSnakeMania.updateRecord(score);
            highScore.setText(String.valueOf(score));
        }
        Snake_Main.level = 1;
        score = 0;
        foodTarget = 10;
        updateScore();
        end = true;
        Sounds_Snake_Mania.stopMusic();
        Sounds_Snake_Mania.playGameOverMusic();
    }
    private void gameOverLevel1()
    {
        for (int i=snakeLength-1; i>0; i--)
        {
            if (snakeX[0] == snakeX[i] && snakeY[0] == snakeY[i])
            {
                gameOver();
            }
        }
    }
    private void gameOverLevel2()
    {
        if ((snakeX[0] < 0 ) || (snakeX[0] > 1250) || (snakeY[0] < 0) || (snakeY[0] > 525)) {
            gameOver();
        }
    }
    private void gameOverLevel3()
    {
        if (snakeHead.intersects(topWall) || snakeHead.intersects(bottomWall) || snakeHead.intersects(leftWall) || snakeHead.intersects(rightWall) || snakeHead.intersects(centreWall))
        {
            gameOver();
        }
    }

    // In-Game Updates
    private void updateScore()
    {
        playerScore.setText(String.valueOf(score));
        target.setText("Target : " + foodTarget);
        head.repaint();
    }
    private void updateLevel()
    {
        levelNumber.setText("Level " + Snake_Main.level);
        head.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
class Game_Mode_Snake_Mania extends JPanel implements MouseListener
{
    Snake snake;
    static int mode = 0;
    JButton endlessMode, adventureMode, reset, exit;
    ImageIcon endlessModeIcon, endlessModeHover, adventureModeIcon, adventureModeHover, resetIcon, resetHover, exitIcon, exitHover;
    Image leftSnake, rightSnake;
    int endlessModeX, endlessModeY, adventureModeX, adventureModeY, resetX, resetY, exitX, exitY;
    private int panelX = 46, panelY = 250;
    private int panelWidth = 1275, panelHeight = 445;
    Game_Mode_Snake_Mania(Snake snake)
    {
        this.snake = snake;

        setBounds(panelX,panelY,panelWidth,panelHeight);
        setLayout(null);
        setBackground(null);
        setOpaque(false);
        setBorder(null);

        loadImages();

        endlessModeX = (this.getWidth() - endlessModeIcon.getIconWidth())/2;
        adventureModeX = (this.getWidth() - adventureModeIcon.getIconWidth())/2;
        resetX = (this.getWidth() - resetIcon.getIconWidth())/2;
        exitX = (this.getWidth() - exitIcon.getIconWidth())/2;

        endlessModeY = 25;
        adventureModeY = 125;
        resetY = 225;
        exitY = 315;

        endlessMode = new JButton(endlessModeIcon);
        endlessMode.setBounds(endlessModeX,endlessModeY,endlessModeIcon.getIconWidth(),endlessModeIcon.getIconHeight());
        endlessMode.setBackground(null);
        endlessMode.setContentAreaFilled(false);
        endlessMode.setBorder(null);
        endlessMode.setBorderPainted(false);
        endlessMode.addMouseListener(this);
        add(endlessMode);

        adventureMode = new JButton(adventureModeIcon);
        adventureMode.setBounds(adventureModeX,adventureModeY,adventureModeIcon.getIconWidth(),adventureModeIcon.getIconHeight());
        adventureMode.setBackground(null);
        adventureMode.setContentAreaFilled(false);
        adventureMode.setBorder(null);
        adventureMode.setBorderPainted(false);
        adventureMode.addMouseListener(this);
        add(adventureMode);

        reset = new JButton(resetIcon);
        reset.setBounds(resetX,resetY,resetIcon.getIconWidth(),resetIcon.getIconHeight());
        reset.setBackground(null);
        reset.setContentAreaFilled(false);
        reset.setBorder(null);
        reset.setBorderPainted(false);
        reset.addMouseListener(this);
        add(reset);

        exit = new JButton(exitIcon);
        exit.setBounds(exitX,exitY,exitIcon.getIconWidth(),exitIcon.getIconHeight());
        exit.setBackground(null);
        exit.setContentAreaFilled(false);
        exit.setBorder(null);
        exit.setBorderPainted(false);
        exit.addMouseListener(this);
        add(exit);
    }
    private void loadImages()
    {
        endlessModeIcon = new ImageIcon("src/Snake_Mania/Images/Layout/Endless Mode Icon.png");
        endlessModeHover = new ImageIcon("src/Snake_Mania/Images/Layout/Endless Mode Hover.png");
        adventureModeIcon = new ImageIcon("src/Snake_Mania/Images/Layout/Adventure Mode Icon.png");
        adventureModeHover = new ImageIcon("src/Snake_Mania/Images/Layout/Adventure Mode Hover.png");
        resetIcon = new ImageIcon("src/Snake_Mania/Images/Layout/Reset Icon.png");
        resetHover = new ImageIcon("src/Snake_Mania/Images/Layout/Reset Hover.png");
        exitIcon = new ImageIcon("src/Snake_Mania/Images/Layout/Exit Icon.png");
        exitHover = new ImageIcon("src/Snake_Mania/Images/Layout/Exit Hover.png");
        leftSnake = new ImageIcon("src/Snake_Mania/Images/Left Snake.png").getImage();
        rightSnake = new ImageIcon("src/Snake_Mania/Images/Right Snake.png").getImage();
    }
    private void changePanel()
    {
        setVisible(false);
        Snake_Main.f.getContentPane().setBackground(Color.DARK_GRAY);
        snake.setVisible(true);
        snake.timer.start();
        snake.repaint();
        Snake_Main.head.setImageX(320);
        Snake_Main.head.repaint();
        snake.requestFocusInWindow();
        new Sounds_Snake_Mania();
    }
    High_Score_Snake_Mania highScoreSnakeMania = Snake_Main.highScoreSnakeMania;
    protected void getHighScore()
    {
        Snake.currentHighScore = highScoreSnakeMania.getRecord();
        Snake.highScore.setText(String.valueOf(Snake.currentHighScore));
    }
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        int snakeWidth = 310;
        int snakeHeight = 365;

        g.drawImage(leftSnake,5,panelHeight-snakeHeight,snakeWidth,snakeHeight,null);
        g.drawImage(rightSnake,panelWidth-snakeWidth-5,panelHeight-snakeHeight,snakeWidth,snakeHeight,null);
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        if (e.getSource() == endlessMode && e.getButton() == MouseEvent.BUTTON1)
        {
            mode = 1;
            changePanel();
            snake.timer.setDelay(78);
            getHighScore();
            Snake.highScore.setVisible(true);
            Snake.highScoreLabel.setVisible(true);
        }
        else if (e.getSource() == adventureMode && e.getButton() == MouseEvent.BUTTON1)
        {
            mode = 2;
            changePanel();
            Snake.target.setVisible(true);
            Snake.levelNumber.setVisible(true);
        }
        else if (e.getSource() == reset && e.getButton() == MouseEvent.BUTTON1)
        {
            int option = JOptionPane.showConfirmDialog(Snake_Main.f, "Are you sure to reset all Records?", "Reset", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION)
            {
                highScoreSnakeMania.updateRecord(0);
                JOptionPane.showMessageDialog(Snake_Main.f,"Records Reset","Reset", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        else if (e.getSource() == exit && e.getButton() == MouseEvent.BUTTON1)
        {
            Snake_Main.f.dispose();
            Game_Emulator.f.setVisible(true);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        if (e.getSource() == endlessMode)
        {
            int tempX = (endlessModeHover.getIconWidth() - endlessModeIcon.getIconWidth())/2;
            int tempY = (endlessModeHover.getIconHeight() - endlessModeIcon.getIconHeight())/2;
            endlessMode.setBounds(endlessModeX-tempX,endlessModeY-tempY,endlessModeHover.getIconWidth(),endlessModeHover.getIconHeight());
            endlessMode.setIcon(endlessModeHover);
        }
        else if (e.getSource() == adventureMode)
        {
            int tempX = (adventureModeHover.getIconWidth() - adventureModeIcon.getIconWidth())/2;
            int tempY = (adventureModeHover.getIconHeight() - adventureModeIcon.getIconHeight())/2;
            adventureMode.setBounds(adventureModeX-tempX,adventureModeY-tempY,adventureModeHover.getIconWidth(),adventureModeHover.getIconHeight());
            adventureMode.setIcon(adventureModeHover);
        }
        else if (e.getSource() == reset)
        {
            int tempX = (resetHover.getIconWidth() - resetIcon.getIconWidth())/2;
            int tempY = (resetHover.getIconHeight() - resetIcon.getIconHeight())/2;
            reset.setBounds(resetX-tempX,resetY-tempY,resetHover.getIconWidth(),resetHover.getIconHeight());
            reset.setIcon(resetHover);
        }
        else if (e.getSource() == exit)
        {
            int tempX = (exitHover.getIconWidth() - exitIcon.getIconWidth())/2;
            int tempY = (exitHover.getIconHeight() - exitIcon.getIconHeight())/2;
            exit.setBounds(exitX-tempX,exitY-tempY,exitHover.getIconWidth(),exitHover.getIconHeight());
            exit.setIcon(exitHover);
        }
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        if (e.getSource() == endlessMode)
        {
            endlessMode.setBounds(endlessModeX,endlessModeY,endlessModeIcon.getIconWidth(),endlessModeIcon.getIconHeight());
            endlessMode.setIcon(endlessModeIcon);
        }
        else if (e.getSource() == adventureMode)
        {
            adventureMode.setBounds(adventureModeX,adventureModeY,adventureModeIcon.getIconWidth(),adventureModeIcon.getIconHeight());
            adventureMode.setIcon(adventureModeIcon);
        }
        else if (e.getSource() == reset)
        {
            reset.setBounds(resetX,resetY,resetIcon.getIconWidth(),resetIcon.getIconHeight());
            reset.setIcon(resetIcon);
        }
        else if (e.getSource() == exit)
        {
            exit.setBounds(exitX,exitY,exitIcon.getIconWidth(),exitIcon.getIconHeight());
            exit.setIcon(exitIcon);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}
}
public class Snake_Main
{
    static int level = 1;
    public static JFrame f;
    static Game_Mode_Snake_Mania gameMode;
    static Head_Snake_Mania head;
    static Snake snake;
    static High_Score_Snake_Mania highScoreSnakeMania;
    public Snake_Main()
    {
        f = new JFrame("Snake Mania");
        f.setBounds(-8,-1,1382,744);
        f.getContentPane().setBackground(Color.BLACK);
        f.setLayout(null);
        f.setUndecorated(true);
        f.setLocationRelativeTo(null);
        f.setIconImage(new ImageIcon("src/Images/Icon/Snake Mania.png").getImage());
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        highScoreSnakeMania = new High_Score_Snake_Mania("Snake Mania");

        head = new Head_Snake_Mania();
        f.add(head);

        snake = new Snake(head);
        f.add(snake);

        gameMode = new Game_Mode_Snake_Mania(snake);
        f.add(gameMode);

        f.setVisible(true);
    }

    public static void main(String[] args)
    {
        new Snake_Main();
    }
}