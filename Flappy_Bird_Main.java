import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.LinkedList;
import java.util.Random;
interface FlappyBirdPanelBounds
{
    int x = 115, y = 1;
    int backgroundX = x, backgroundY = y;
    int backgroundHeight = 562;
    int scoreX = x, scoreY = y+backgroundHeight;
    int flappyBirdX = x, flappyBirdY = y;
}
class High_Score_Flappy_Bird
{
    int currentHighScore;
    String [] data;
    File file;
    BufferedWriter writer;
    BufferedReader reader;
    High_Score_Flappy_Bird(String fileName)
    {
        try
        {
            file = new File("src/High Score/"+fileName+".txt");
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,"File "+fileName+" is missing..!!","Error",JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    public void updateRecord(int mode, int highScore)
    {
        try
        {
            writer = new BufferedWriter(new FileWriter("src/High Score/Flappy Bird.txt"));
            writer.flush();
            for (int i=0; i<data.length; i++)
            {
                if (i != mode)
                {
                    writer.append(data[i]).append("\n");
                }
                else
                {
                    switch (mode)
                    {
                        case 0 -> writer.append("Easy - ").append(String.valueOf(highScore)).append("\n");
                        case 1 -> writer.append("Medium - ").append(String.valueOf(highScore)).append("\n");
                        case 2 -> writer.append("Hard - ").append(String.valueOf(highScore)).append("\n");
                    }
                }
            }
            writer.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void resetRecord()
    {
        try
        {
            writer = new BufferedWriter(new FileWriter("src/High Score/Flappy Bird.txt"));
            writer.flush();
            writer.append("Easy - 0\n");
            writer.append("Medium - 0\n");
            writer.append("Hard - 0\n");
            writer.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public int getRecord(int mode)
    {
        try
        {
            reader = new BufferedReader(new FileReader("src/High Score/Flappy Bird.txt"));
            String str;
            data = new String[3];
            int k = 0;
            while ((str = reader.readLine()) != null)
            {
                data[k] = str;
                k++;
            }
            currentHighScore = getData(data[mode]);
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
                    JOptionPane.showMessageDialog(null,"Unable to fetch High Score..!!","Error",JOptionPane.ERROR_MESSAGE);
                    return reqData;
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null,"One or more files may be corrupted..!!","Error",JOptionPane.ERROR_MESSAGE);
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null,"Unable to fetch High Score..!!","Error",JOptionPane.ERROR_MESSAGE);
        }
        return reqData;
    }
}
class Sounds_Flappy_Bird
{
    Clip hitClip, flyClip, pointClip;
    AudioInputStream hitAudio, flyAudio, pointAudio;
    File hitMusic, flyMusic, pointMusic;
    Sounds_Flappy_Bird()
    {
        hitMusic = new File("src/Flappy_Bird/Audio/hit.wav");
        flyMusic = new File("src/Flappy_Bird/Audio/wing.wav");
        pointMusic = new File("src/Flappy_Bird/Audio/point.wav");

        try
        {
            hitAudio = AudioSystem.getAudioInputStream(hitMusic);
            hitClip = AudioSystem.getClip();

            flyAudio = AudioSystem.getAudioInputStream(flyMusic);
            flyClip = AudioSystem.getClip();

            pointAudio = AudioSystem.getAudioInputStream(pointMusic);
            pointClip = AudioSystem.getClip();
        }
        catch (Exception e)
        {
            System.exit(0);
            JOptionPane.showMessageDialog(Flappy_Bird_Main.f,"Error","Unable to load Resources..!!",JOptionPane.ERROR_MESSAGE);
        }
    }
    public void hit()
    {
        try
        {
            hitAudio = AudioSystem.getAudioInputStream(hitMusic);
            hitClip = AudioSystem.getClip();
            hitClip.open(hitAudio);
            hitClip.start();
        }
        catch (Exception e)
        {
            System.exit(0);
            JOptionPane.showMessageDialog(Flappy_Bird_Main.f,"Error","Unable to load Resources..!!",JOptionPane.ERROR_MESSAGE);
        }
    }
    public void fly()
    {
        try
        {
            flyAudio = AudioSystem.getAudioInputStream(flyMusic);
            flyClip = AudioSystem.getClip();
            flyClip.open(flyAudio);
            flyClip.start();
        }
        catch (Exception e)
        {
            System.exit(0);
            JOptionPane.showMessageDialog(Flappy_Bird_Main.f,"Error","Unable to load Resources..!!",JOptionPane.ERROR_MESSAGE);
        }
    }
    public void point()
    {
        try
        {
            pointAudio = AudioSystem.getAudioInputStream(pointMusic);
            pointClip = AudioSystem.getClip();
            pointClip.open(pointAudio);
            pointClip.start();
        }
        catch (Exception e)
        {
            System.exit(0);
            JOptionPane.showMessageDialog(Flappy_Bird_Main.f,"Error","Unable to load Resources..!!",JOptionPane.ERROR_MESSAGE);
        }
    }
}
class Background_Flappy_Bird extends JPanel implements FlappyBirdPanelBounds
{
    private Image backgroundImage, baseImage, gameName, gameNameBase;
    Background_Flappy_Bird()
    {
        setBounds(backgroundX,backgroundY,1152,744);
        setBackground(null);
        setOpaque(false);

        backgroundImage = new ImageIcon("src/Flappy_Bird/Images/Background.png").getImage();
        baseImage = new ImageIcon("src/Flappy_Bird/Images/Base.png").getImage();
        gameName = new ImageIcon("src/Flappy_Bird/Images/Game Image.png").getImage();
        gameNameBase = new ImageIcon("src/Flappy_Bird/Images/Game Name at Base.png").getImage();
    }
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);

        // Background
        g.drawImage(backgroundImage,0,0,backgroundImage.getWidth(null),backgroundImage.getHeight(null),null);

        // Base
        g.drawImage(baseImage,0,backgroundImage.getHeight(null),baseImage.getWidth(null),baseImage.getHeight(null),null);

        // Name
        if (Flappy_Bird_Main.gameMode.isVisible())
        {
            int nameX = (1152-gameName.getWidth(null))/2;
            g.drawImage(gameName,nameX,10,gameName.getWidth(null),gameName.getHeight(null),null);
        }
        else
        {
            int x = (1152-gameNameBase.getWidth(null))/2;
            g.drawImage(gameNameBase,x,backgroundHeight+27,gameNameBase.getWidth(null),gameNameBase.getHeight(null),null);
        }
    }
}
class Pipes
{
    int pipeX;
    int [] pipeY;
    private int pipeWidth = 52, pipeHeight = 320;
    int upperPipe, lowerPipe;
    static int speed = 8;
    private int gap;
    Image upper_pipe, lower_pipe;
    Pipes()
    {
        upper_pipe = new ImageIcon("src/Flappy_Bird/Images/upperPipe.png").getImage();
        lower_pipe = new ImageIcon( "src/Flappy_Bird/Images/lowerPipe.png").getImage();

        pipeX = 1200;
        pipeY = generatePipes();
        upperPipe = pipeY[0];
        lowerPipe = pipeY[1];
    }
    public void paint(Graphics g)
    {
        // Upper Pipe
        g.drawImage(upper_pipe, pipeX, upperPipe, pipeWidth, pipeHeight, null);

        // Lower Pipe
        g.drawImage(lower_pipe, pipeX, lowerPipe, pipeWidth, pipeHeight, null);

        // Gap
        gap = upperPipe+pipeHeight;
    }
    public Rectangle upperPipeRect() {
        return new Rectangle(pipeX, upperPipe, pipeWidth, pipeHeight);
    }
    public Rectangle lowerPipeRect() {
        return new Rectangle(pipeX, lowerPipe, pipeWidth, pipeHeight);
    }
    public Rectangle gapRect() {
        return new Rectangle(pipeX,gap,pipeWidth,pipeHeight);
    }
    public int [] generatePipes()
    {
        Random random = new Random();

        int [] pipeY = new int[2];

        pipeY[0] = random.nextInt(-228, -10);
        int verticalGap = random.nextInt(100, 150);

        if (pipeY[0] < -220)
            verticalGap = 150;

        pipeY[1] = pipeY[0] + pipeHeight + verticalGap;

        if (pipeY[1] < 242)
            pipeY[1] = 245;

        return pipeY;
    }
    public void move()
    {
        pipeX -= speed;
    }
}
class Score_Flappy_Bird extends JPanel implements FlappyBirdPanelBounds
{
    static boolean showScore = true;
    Score_Flappy_Bird()
    {
        setBounds(scoreX,scoreY,1152,112);
        setBackground(null);
        setOpaque(false);
        setLayout(null);
    }
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);

        if (showScore)
        {
            add(Flappy_Bird.difficultyLevel);
            add(Flappy_Bird.scoreLabel);
            add(Flappy_Bird.playerScore);
            add(Flappy_Bird.highScoreLabel);
            add(Flappy_Bird.highScore);
        }
    }
}
class Flappy_Bird extends JPanel implements KeyListener, FlappyBirdPanelBounds
{
    static LinkedList<Pipes> pipes = new LinkedList<>();
    private Pipes tempPipes;
    private int add = 0;
    static int horizontalGap = 40;
    private int birdX = 50, birdY = backgroundHeight/2;
    private int velocity;
    private Image bird;
    static boolean gameOver = false;
    private Timer timer;
    protected static int score;
    protected static int currentHighScore;
    static JLabel scoreLabel, difficultyLevel, playerScore, highScoreLabel, highScore;
    private Sounds_Flappy_Bird sounds;
    private Score_Flappy_Bird scoreFlappyBirdPanel;
    Flappy_Bird(Score_Flappy_Bird scoreFlappyBirdPanel)
    {
        this.scoreFlappyBirdPanel = scoreFlappyBirdPanel;

        setBounds(flappyBirdX,flappyBirdY,1152,backgroundHeight);
        setBackground(null);
        setOpaque(false);
        setVisible(false);

        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(true);

        Font font = new Font("FixedsysTTF", Font.PLAIN, 24);
        int layer1 = 35, layer2 = 80;

        scoreLabel = new JLabel("Score : ");
        scoreLabel.setBounds(1152-173,layer1,110,30);
        scoreLabel.setFont(font);
        scoreLabel.setForeground(Color.BLACK);
        scoreLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        playerScore = new JLabel();
        playerScore.setBounds(1152-63,layer1,55,30);
        playerScore.setFont((font));
        playerScore.setForeground(Color.BLACK);

        highScoreLabel = new JLabel("Highest Score : ");
        highScoreLabel.setBounds(1152-273,layer2,210,30);
        highScoreLabel.setFont(font);
        highScoreLabel.setForeground(Color.BLACK);
        highScoreLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        highScore = new JLabel();
        highScore.setBounds(1152-63,layer2,55,30);
        highScore.setFont(font);
        highScore.setForeground(Color.BLACK);

        difficultyLevel = new JLabel();
        difficultyLevel.setBounds(15,layer1,280,30);
        difficultyLevel.setFont(font);
        difficultyLevel.setForeground(Color.BLACK);

        bird = new ImageIcon("src/Flappy_Bird/Images/bird.png").getImage();

        tempPipes = new Pipes();
        adder();

        velocity = 0;
        score = 0;

        sounds = new Sounds_Flappy_Bird();

        int delay = 25;
        timer = new Timer(delay, e ->
        {
            add++;

            if (add == horizontalGap) {
                adder();
                add = 0;
            }

            for (int i = 0; i< pipes.size(); i++)
            {
                tempPipes = pipes.get(i);
                tempPipes.move();
                if (tempPipes.pipeX <= -50) {
                    pipes.remove();
                }
            }

            velocity += 1;
            birdY += velocity;

            repaint();
        });
    }
    protected void hideScore()
    {
        scoreLabel.setVisible(false);
        playerScore.setVisible(false);
        difficultyLevel.setVisible(false);
        highScore.setVisible(false);
        highScoreLabel.setVisible(false);
    }
    private void showScore()
    {
        scoreLabel.setVisible(true);
        playerScore.setVisible(true);
        difficultyLevel.setVisible(true);
        highScore.setVisible(true);
        highScoreLabel.setVisible(true);
    }
    private void updateScore()
    {
        playerScore.setText(String.valueOf(score));
        difficultyLevel.setText("Difficulty : "+ Game_Mode_Flappy_Bird.mode);
        scoreFlappyBirdPanel.repaint();
    }
    private void addPipes(Pipes p) {
        pipes.add(p);
    }
    private void adder() {
        addPipes(new Pipes());
    }
    private Rectangle birdRect() {
        return new Rectangle(birdX, birdY, 34, 24);
    }
    private void isGameOver()
    {
        for (Pipes pipe : pipes)
        {
            tempPipes = pipe;
            if (birdRect().intersects(tempPipes.upperPipeRect()) || birdRect().intersects(tempPipes.lowerPipeRect()) || birdY > backgroundHeight - 25 || birdY < -25)
            {
                sounds.hit();

                if (score > currentHighScore)
                {
                    Flappy_Bird_Main.highScoreFlappyBird.updateRecord(Flappy_Bird_Main.gameMode.modeNumber,score);
                    highScore.setText(String.valueOf(score));
                }

                Flappy_Bird_Main.play = false;
                gameOver = true;

                tempPipes.pipeX = 1200;

                tempPipes.pipeY = tempPipes.generatePipes();
                tempPipes.upperPipe = tempPipes.pipeY[0];
                tempPipes.lowerPipe = tempPipes.pipeY[1];

                pipes.clear();

                score = 0;
                birdY = backgroundHeight / 2;

                timer.stop();
            }
        }
    }
    private void showMenu()
    {
        this.setVisible(false);
        Game_Mode_Flappy_Bird gameMode = Flappy_Bird_Main.gameMode;
        gameMode.setVisible(true);
        gameMode.playButton.setVisible(true);
        gameMode.exitButton.setVisible(true);
        gameMode.resetButton.setVisible(true);
        gameMode.easyMode.setVisible(false);
        gameMode.mediumMode.setVisible(false);
        gameMode.hardMode.setVisible(false);
        gameMode.requestFocusInWindow();
        Flappy_Bird_Main.background.repaint();
    }
    static int bird_X_pipe = 40; // birdX = 50, pipeWidth = 52;
    private void Score()
    {
        for (Pipes pipe : pipes)
        {
            tempPipes = pipe;

            if (tempPipes.gapRect().getX() == bird_X_pipe)
            {
                score++;
                sounds.point();
                updateScore();
            }
        }
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);

        // Bird
        g.drawImage(bird, birdX, birdY, 34, 24, null);

        // Pipes
        for (Pipes pipe : pipes)
        {
            tempPipes = pipe;
            tempPipes.paint(g);
        }

        // Score
        Score();

        // Game Over
        isGameOver();
        if (gameOver)
        {
            g.setColor(Color.RED); // 1152
            g.setFont(new Font("FixedsysTTF", Font.PLAIN, 50));

            String overMessage= "GAME OVER";
            g.drawString(overMessage,450,this.getHeight()/2);

            g.setColor(Color.BLACK); // 1152
            g.setFont(new Font("FixedsysTTF", Font.PLAIN, 30));

            String pressEnter = "Press ENTER to Restart";
            g.drawString(pressEnter,386,this.getHeight()/2+65);

            String pressEscape= "Press ESC to return to Menu";
            g.drawString(pressEscape,341,this.getHeight()/2+130);
        }
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE && !Flappy_Bird_Main.play)
        {
            Score_Flappy_Bird.showScore = false;
            updateScore();
            hideScore();
            showMenu();
            gameOver = false;
            repaint();
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            Score_Flappy_Bird.showScore = true;
            updateScore();
            showScore();
            Flappy_Bird_Main.play = !Flappy_Bird_Main.play;
            timer.start();
            gameOver = false;
            repaint();
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE && Flappy_Bird_Main.play)
        {
            velocity = -10;
            sounds.fly();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
class Game_Mode_Flappy_Bird extends JPanel implements MouseListener
{
    static String mode = "";
    ImageIcon playIconImage,playHoverImage, resetIconImage, resetHoverImage,exitIconImage, exitHoverImage;
    ImageIcon easyIconImage, mediumIconImage, hardIconImage, easyHoverImage, mediumHoverImage, hardHoverImage;
    JButton playButton, resetButton, exitButton;
    JButton easyMode, mediumMode, hardMode;
    int playX, playY;
    int resetX, resetY;
    int exitX, exitY;
    int easyX, easyY;
    int mediumX, mediumY;
    int hardX, hardY;
    Game_Mode_Flappy_Bird()
    {
        setBounds(516,212,350,350);
        setLayout(null);
        setBackground(null);
        setOpaque(false);

        playIconImage = new ImageIcon("src/Flappy_Bird/Images/Labels/Play Icon.png");
        playHoverImage = new ImageIcon("src/Flappy_Bird/Images/Labels/Play Hover.png");
        resetIconImage = new ImageIcon("src/Flappy_Bird/Images/Labels/Reset Icon.png");
        resetHoverImage = new ImageIcon("src/Flappy_Bird/Images/Labels/Reset Hover.png");
        exitIconImage = new ImageIcon("src/Flappy_Bird/Images/Labels/Exit Icon.png");
        exitHoverImage = new ImageIcon("src/Flappy_Bird/Images/Labels/Exit Hover.png");

        easyIconImage = new ImageIcon("src/Flappy_Bird/Images/Labels/Easy Icon.png");
        easyHoverImage = new ImageIcon("src/Flappy_Bird/Images/Labels/Easy Hover.png");
        mediumIconImage = new ImageIcon("src/Flappy_Bird/Images/Labels/Medium Icon.png");
        mediumHoverImage = new ImageIcon("src/Flappy_Bird/Images/Labels/Medium Hover.png");
        hardIconImage = new ImageIcon("src/Flappy_Bird/Images/Labels/Hard Icon.png");
        hardHoverImage = new ImageIcon("src/Flappy_Bird/Images/Labels/Hard Hover.png");

        playX = (350-playIconImage.getIconWidth())/2;
        playY = 6;

        resetX = (350-resetIconImage.getIconWidth())/2;
        resetY = 135;

        exitX = (350-exitIconImage.getIconWidth())/2;
        exitY = 265;

        playButton = new JButton(playIconImage);
        playButton.setBounds(playX,playY, playIconImage.getIconWidth(), playIconImage.getIconHeight());
        playButton.setBackground(null);
        playButton.setContentAreaFilled(false);
        playButton.setBorder(null);
        playButton.setBorderPainted(false);
        playButton.addMouseListener(this);
        add(playButton);

        resetButton = new JButton(resetIconImage);
        resetButton.setBounds(resetX,resetY, resetIconImage.getIconWidth(),resetHoverImage.getIconHeight());
        resetButton.setBackground(null);
        resetButton.setContentAreaFilled(false);
        resetButton.setBorder(null);
        resetButton.setBorderPainted(false);
        resetButton.addMouseListener(this);
        add(resetButton);

        exitButton = new JButton(exitIconImage);
        exitButton.setBounds(exitX,exitY, exitIconImage.getIconWidth(), exitIconImage.getIconHeight());
        exitButton.setBackground(null);
        exitButton.setContentAreaFilled(false);
        exitButton.setBorder(null);
        exitButton.setBorderPainted(false);
        exitButton.addMouseListener(this);
        add(exitButton);

        easyX = (350-easyIconImage.getIconWidth())/2;
        easyY = 25;
        mediumX = (350-mediumIconImage.getIconWidth())/2;
        mediumY = 150;
        hardX = (350-hardIconImage.getIconWidth())/2;
        hardY = 275;

        easyMode = new JButton(easyIconImage);
        easyMode.setBounds(easyX,easyY, easyIconImage.getIconWidth(), easyIconImage.getIconHeight());
        easyMode.setBackground(null);
        easyMode.setContentAreaFilled(false);
        easyMode.setBorder(null);
        easyMode.setBorderPainted(false);
        easyMode.addMouseListener(this);
        easyMode.setVisible(false);
        add(easyMode);

        mediumMode = new JButton(mediumIconImage);
        mediumMode.setBounds(mediumX,mediumY, mediumIconImage.getIconWidth(), mediumIconImage.getIconHeight());
        mediumMode.setBackground(null);
        mediumMode.setContentAreaFilled(false);
        mediumMode.setBorder(null);
        mediumMode.setBorderPainted(false);
        mediumMode.addMouseListener(this);
        mediumMode.setVisible(false);
        add(mediumMode);

        hardMode = new JButton(hardIconImage);
        hardMode.setBounds(hardX,hardY, hardIconImage.getIconWidth(), hardIconImage.getIconHeight());
        hardMode.setBackground(null);
        hardMode.setContentAreaFilled(false);
        hardMode.setBorder(null);
        hardMode.setBorderPainted(false);
        hardMode.addMouseListener(this);
        hardMode.setVisible(false);
        add(hardMode);
    }
    private void chooseDifficulty()
    {
        playButton.setVisible(false);
        exitButton.setVisible(false);
        resetButton.setVisible(false);
        easyMode.setVisible(true);
        mediumMode.setVisible(true);
        hardMode.setVisible(true);
    }
    String currentMode;
    protected int modeNumber;
    protected void getHighScore()
    {
        Flappy_Bird.currentHighScore = Flappy_Bird_Main.highScoreFlappyBird.getRecord(modeNumber);
        Flappy_Bird.highScore.setText(String.valueOf(Flappy_Bird.currentHighScore));
    }
    private void runGame()
    {
        Score_Flappy_Bird.showScore = true;
        new Score_Flappy_Bird().repaint();
        this.setVisible(false);

        Flappy_Bird_Main.background.repaint();

        currentMode = mode;
        getHighScore();

        Flappy_Bird_Main.flappyBird.setVisible(true);
        Flappy_Bird_Main.flappyBird.requestFocusInWindow();
        Flappy_Bird_Main.flappyBird.hideScore();
    }
    @Override
    public void mouseClicked(MouseEvent e)
    {
        if (e.getSource() == playButton && e.getButton() == MouseEvent.BUTTON1)
        {
            chooseDifficulty();
        }
        else if (e.getSource() == resetButton && e.getButton() == MouseEvent.BUTTON1)
        {
            int option = JOptionPane.showConfirmDialog(Flappy_Bird_Main.f, "Are you sure to reset all Records?", "Reset", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION)
            {
                Flappy_Bird_Main.highScoreFlappyBird.resetRecord();
                JOptionPane.showMessageDialog(Flappy_Bird_Main.f,"Records Reset","Reset", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        else if (e.getSource() == exitButton && e.getButton() == MouseEvent.BUTTON1)
        {
            Flappy_Bird_Main.f.dispose();
            Game_Emulator.f.setVisible(true);
        }
        if (e.getSource() == easyMode && e.getButton() == MouseEvent.BUTTON1)
        {
            modeNumber = 0;
            mode = "Easy";
            Pipes.speed = 8;
            Flappy_Bird.bird_X_pipe = 40;
            Flappy_Bird.horizontalGap = 45;
            runGame();
        }
        else if (e.getSource() == mediumMode && e.getButton() == MouseEvent.BUTTON1)
        {
            modeNumber = 1;
            mode = "Medium";
            Pipes.speed = 10;
            Flappy_Bird.bird_X_pipe = 40;
            Flappy_Bird.horizontalGap = 35;
            runGame();
        }
        else if (e.getSource() == hardMode && e.getButton() == MouseEvent.BUTTON1)
        {
            modeNumber = 2;
            mode = "Hard";
            Pipes.speed = 12;
            Flappy_Bird.bird_X_pipe = 36;
            Flappy_Bird.horizontalGap = 25;
            runGame();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        if (e.getSource() == playButton)
        {
            int tempPlayX = (playHoverImage.getIconWidth() - playIconImage.getIconWidth())/2;
            int tempPlayY = (playHoverImage.getIconHeight() - playIconImage.getIconHeight())/2;
            playButton.setBounds(playX-tempPlayX,playY-tempPlayY, playHoverImage.getIconWidth(), playHoverImage.getIconHeight());
            playButton.setIcon(playHoverImage);
        }
        else if (e.getSource() == resetButton)
        {
            int tempResetX = (resetHoverImage.getIconWidth() - resetIconImage.getIconWidth())/2;
            int tempResetY = (resetHoverImage.getIconHeight() - resetIconImage.getIconHeight())/2;
            resetButton.setBounds(resetX-tempResetX,resetY-tempResetY, resetHoverImage.getIconWidth(), resetHoverImage.getIconHeight());
            resetButton.setIcon(resetHoverImage);
        }
        else if (e.getSource() == exitButton)
        {
            int tempExitX = (exitHoverImage.getIconWidth() - exitIconImage.getIconWidth())/2;
            int tempExitY = (exitHoverImage.getIconHeight() - exitIconImage.getIconHeight())/2;
            exitButton.setBounds(exitX-tempExitX,exitY-tempExitY, exitHoverImage.getIconWidth(), exitHoverImage.getIconHeight());
            exitButton.setIcon(exitHoverImage);
        }
        if (e.getSource() == easyMode)
        {
            int tempEasyX = (easyHoverImage.getIconWidth() - easyIconImage.getIconWidth())/2;
            int tempEasyY = (easyHoverImage.getIconHeight() - easyIconImage.getIconHeight())/2;
            easyMode.setBounds(easyX-tempEasyX,easyY-tempEasyY,easyHoverImage.getIconWidth(),easyHoverImage.getIconHeight());
            easyMode.setIcon(easyHoverImage);
        }
        else if (e.getSource() == mediumMode)
        {
            int tempMediumX = (mediumHoverImage.getIconWidth() - mediumIconImage.getIconWidth())/2;
            int tempMediumY = (mediumHoverImage.getIconHeight() - mediumIconImage.getIconHeight())/2;
            mediumMode.setBounds(mediumX-tempMediumX,mediumY-tempMediumY,mediumHoverImage.getIconWidth(),mediumHoverImage.getIconHeight());
            mediumMode.setIcon(mediumHoverImage);
        }
        else if (e.getSource() == hardMode)
        {
            int tempHardX = (hardHoverImage.getIconWidth() - hardIconImage.getIconWidth())/2;
            int tempHardY = (hardHoverImage.getIconHeight() - hardIconImage.getIconHeight())/2;
            hardMode.setBounds(hardX-tempHardX,hardY-tempHardY,hardHoverImage.getIconWidth(),hardHoverImage.getIconHeight());
            hardMode.setIcon(hardHoverImage);
        }
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        if (e.getSource() == playButton)
        {
            playButton.setBounds(playX,playY, playIconImage.getIconWidth(), playIconImage.getIconHeight());
            playButton.setIcon(playIconImage);
        }
        else if (e.getSource() == resetButton)
        {
            resetButton.setBounds(resetX,resetY, resetIconImage.getIconWidth(), resetIconImage.getIconHeight());
            resetButton.setIcon(resetIconImage);
        }
        else if (e.getSource() == exitButton)
        {
            exitButton.setBounds(exitX,exitY, exitIconImage.getIconWidth(), exitIconImage.getIconHeight());
            exitButton.setIcon(exitIconImage);
        }
        if (e.getSource() == easyMode)
        {
            easyMode.setBounds(easyX,easyY, easyIconImage.getIconWidth(), easyIconImage.getIconHeight());
            easyMode.setIcon(easyIconImage);
        }
        else if (e.getSource() == mediumMode)
        {
            mediumMode.setBounds(mediumX,mediumY, mediumIconImage.getIconWidth(), mediumIconImage.getIconHeight());
            mediumMode.setIcon(mediumIconImage);
        }
        else if (e.getSource() == hardMode)
        {
            hardMode.setBounds(hardX,hardY, hardIconImage.getIconWidth(), hardIconImage.getIconHeight());
            hardMode.setIcon(hardIconImage);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}
}
public class Flappy_Bird_Main
{
    static JFrame f;
    static boolean play;
    static Flappy_Bird flappyBird;
    static Game_Mode_Flappy_Bird gameMode;
    static Score_Flappy_Bird scoreFlappyBird;
    static Background_Flappy_Bird background;
    static High_Score_Flappy_Bird highScoreFlappyBird;
    Flappy_Bird_Main()
    {
        f = new JFrame("Flappy Bird");
        f.setBounds(-8, -1, 1382, 744);
        f.getContentPane().setBackground(Color.BLACK);
        f.setLayout(null);
        f.setUndecorated(true);
        f.setLocationRelativeTo(null);
        f.setIconImage(new ImageIcon("src/Images/Icon/Flappy Bird.png").getImage());
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        highScoreFlappyBird = new High_Score_Flappy_Bird("Flappy Bird");

        gameMode = new Game_Mode_Flappy_Bird();
        f.add(gameMode);

        scoreFlappyBird = new Score_Flappy_Bird();
        f.add(scoreFlappyBird);

        flappyBird = new Flappy_Bird(scoreFlappyBird);
        f.add(flappyBird);

        background = new Background_Flappy_Bird();
        f.add(background);

        f.setVisible(true);

    }
    public static void main(String[] args)
    {
        new Game_Emulator();
    }
}
