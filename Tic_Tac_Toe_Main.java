import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
class Head_Tic_Tac_Toe extends JPanel
{
    Image gameName;
    Head_Tic_Tac_Toe()
    {
        setBounds(291,20,800,100);
        setBackground(null);
        setLayout(null);
        gameName = new ImageIcon("src/Tic_Tac_Toe/Images/Game_Name.png").getImage();
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        g.drawImage(gameName,150,5,gameName.getWidth(null),gameName.getHeight(null)-5,null);
    }
}
class Score_Tic_Tac_Toe extends JPanel
{
    static int playerScore, computerScore;
    Score_Tic_Tac_Toe()
    {
        setBounds(291,165,800,60);
        setBackground(null);
        setLayout(null);
        setVisible(false);

        playerScore = 0;
        computerScore = 0;
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Times New Roman", Font.PLAIN,22));
        g.drawString("Player : "+playerScore,25,35);
        g.drawString("Computer : "+computerScore,635,35);
    }
}
class Computer
{
    static Random random;
    static ArrayList<Integer> filledBlocks;
    Computer()
    {
        filledBlocks = new ArrayList<>();
        random = new Random();
    }
    public static int compTurn()
    {
        int n = random.nextInt(0, 9);

        if (filledBlocks.contains(n))
            return compTurn();

        filledBlocks.add(n);

        return n;
    }
}
class Tic_Tac_Toe extends JPanel implements MouseListener
{
    int x, y, w, h;
    JButton [] button;
    String x_path, o_path;
    ImageIcon X, O;
    Color color;
    int doneMatch;
    Tic_Tac_Toe()
    {
        x = 526;
        y = 250;
        w = 330;
        h = 330;

        setLocation(x,y);
        setSize(w,h);
        setBackground(null);
        setLayout(new GridLayout(3,3));
        setVisible(false);

        doneMatch = 1;

        color = new Color(0,176,80);

        button = new JButton[9];

        for (int i=0; i<9; i++)
        {
            button[i] = new JButton();
            button[i].setBorder(BorderFactory.createLineBorder(color,2));
            button[i].setBackground(null);
            button[i].setContentAreaFilled(false);
            button[i].setFocusPainted(false);
            button[i].setOpaque(false);
            button[i].setFocusable(true);
            button[i].addMouseListener(this);
            add(button[i]);
        }

        x_path = "src/Tic_Tac_Toe/Images/X.png";
        o_path = "src/Tic_Tac_Toe/Images/O.png";

        X = new ImageIcon(x_path);
        O = new ImageIcon(o_path);
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        for (int i=0; i<9; i++)
        {
            if (e.getSource() == button[i])
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    if (!Computer.filledBlocks.contains(i))
                    {
                        button[i].setIcon(X);
                        Tic_Tac_Toe_Main.human.add(i);
                        Computer.filledBlocks.add(i);
                        isPlayerWin();
                    }

                    isDraw();

                    if (Computer.filledBlocks.size() % 2 != 0 && Computer.filledBlocks.size() <= 8)
                    {
                        int compMark = Computer.compTurn();
                        button[compMark].setIcon(O);
                        Tic_Tac_Toe_Main.com.add(compMark);
                        isComputerWin();
                    }
                }
            }
        }
    }
    private void resetBoard()
    {
        for (JButton jButton : button)
            jButton.setIcon(null);
        Tic_Tac_Toe_Main.human.clear();
        Tic_Tac_Toe_Main.com.clear();
        Computer.filledBlocks.clear();
    }
    Game_Records gameRecords = Menu.gameRecords;
    private void isPlayerWin()
    {
        if ((Tic_Tac_Toe_Main.human.contains(0) && Tic_Tac_Toe_Main.human.contains(1) && Tic_Tac_Toe_Main.human.contains(2)) ||
                (Tic_Tac_Toe_Main.human.contains(3) && Tic_Tac_Toe_Main.human.contains(4) && Tic_Tac_Toe_Main.human.contains(5)) ||
                (Tic_Tac_Toe_Main.human.contains(6) && Tic_Tac_Toe_Main.human.contains(7) && Tic_Tac_Toe_Main.human.contains(8)) ||
                (Tic_Tac_Toe_Main.human.contains(0) && Tic_Tac_Toe_Main.human.contains(3) && Tic_Tac_Toe_Main.human.contains(6)) ||
                (Tic_Tac_Toe_Main.human.contains(1) && Tic_Tac_Toe_Main.human.contains(4) && Tic_Tac_Toe_Main.human.contains(7)) ||
                (Tic_Tac_Toe_Main.human.contains(2) && Tic_Tac_Toe_Main.human.contains(5) && Tic_Tac_Toe_Main.human.contains(8)) ||
                (Tic_Tac_Toe_Main.human.contains(0) && Tic_Tac_Toe_Main.human.contains(4) && Tic_Tac_Toe_Main.human.contains(8)) ||
                (Tic_Tac_Toe_Main.human.contains(2) && Tic_Tac_Toe_Main.human.contains(4) && Tic_Tac_Toe_Main.human.contains(6)))
        {
            if (Menu.Mode == 0)
            {
                Score_Tic_Tac_Toe.playerScore += 1;
                Tic_Tac_Toe_Main.scoreTicTacToe.repaint();
                JOptionPane.showMessageDialog(Tic_Tac_Toe_Main.f,"You Win :)", "Result",JOptionPane.INFORMATION_MESSAGE);
                Score_Tic_Tac_Toe.playerScore = 0;
                Tic_Tac_Toe_Main.scoreTicTacToe.repaint();
                setVisible(false);
                Tic_Tac_Toe_Main.scoreTicTacToe.setVisible(false);
                Tic_Tac_Toe_Main.menu.setVisible(true);
                gameRecords.updateRecord(true,false);
            }
            else if (Menu.Mode == 1)
            {
                if (doneMatch < Menu.rounds)
                {
                    Score_Tic_Tac_Toe.playerScore += 1;
                    Tic_Tac_Toe_Main.scoreTicTacToe.repaint();
                    JOptionPane.showMessageDialog(Tic_Tac_Toe_Main.f,"You Win :)", "Result",JOptionPane.INFORMATION_MESSAGE);
                    doneMatch++;
                }
                else
                {
                    Score_Tic_Tac_Toe.playerScore += 1;
                    Tic_Tac_Toe_Main.scoreTicTacToe.repaint();
                    resetBoardWithBattle();
                }
            }
            resetBoard();
        }
    }
    private void isComputerWin()
    {
        if ((Tic_Tac_Toe_Main.com.contains(0) && Tic_Tac_Toe_Main.com.contains(1) && Tic_Tac_Toe_Main.com.contains(2)) ||
                (Tic_Tac_Toe_Main.com.contains(3) && Tic_Tac_Toe_Main.com.contains(4) && Tic_Tac_Toe_Main.com.contains(5)) ||
                (Tic_Tac_Toe_Main.com.contains(6) && Tic_Tac_Toe_Main.com.contains(7) && Tic_Tac_Toe_Main.com.contains(8)) ||
                (Tic_Tac_Toe_Main.com.contains(0) && Tic_Tac_Toe_Main.com.contains(3) && Tic_Tac_Toe_Main.com.contains(6)) ||
                (Tic_Tac_Toe_Main.com.contains(1) && Tic_Tac_Toe_Main.com.contains(4) && Tic_Tac_Toe_Main.com.contains(7)) ||
                (Tic_Tac_Toe_Main.com.contains(2) && Tic_Tac_Toe_Main.com.contains(5) && Tic_Tac_Toe_Main.com.contains(8)) ||
                (Tic_Tac_Toe_Main.com.contains(0) && Tic_Tac_Toe_Main.com.contains(4) && Tic_Tac_Toe_Main.com.contains(8)) ||
                (Tic_Tac_Toe_Main.com.contains(2) && Tic_Tac_Toe_Main.com.contains(4) && Tic_Tac_Toe_Main.com.contains(6)))
        {
            if (Menu.Mode == 0)
            {
                Score_Tic_Tac_Toe.computerScore += 1;
                Tic_Tac_Toe_Main.scoreTicTacToe.repaint();
                JOptionPane.showMessageDialog(Tic_Tac_Toe_Main.f,"Computer Win :(", "Result",JOptionPane.INFORMATION_MESSAGE);
                Score_Tic_Tac_Toe.computerScore = 0;
                Tic_Tac_Toe_Main.scoreTicTacToe.repaint();
                setVisible(false);
                Tic_Tac_Toe_Main.menu.setVisible(true);
                gameRecords.updateRecord(false,true);
            }
            else if (Menu.Mode == 1)
            {
                if (doneMatch < Menu.rounds)
                {
                    Score_Tic_Tac_Toe.computerScore += 1;
                    Tic_Tac_Toe_Main.scoreTicTacToe.repaint();
                    JOptionPane.showMessageDialog(Tic_Tac_Toe_Main.f,"Computer Win :(", "Result",JOptionPane.INFORMATION_MESSAGE);
                    doneMatch++;
                }
                else
                {
                    Score_Tic_Tac_Toe.computerScore += 1;
                    Tic_Tac_Toe_Main.scoreTicTacToe.repaint();
                    resetBoardWithBattle();
                }
            }
            resetBoard();
        }
    }
    private void isDraw()
    {
        if (Computer.filledBlocks.size() == 9)
        {
            if (Menu.Mode == 0)
            {
                JOptionPane.showMessageDialog(Tic_Tac_Toe_Main.f,"X Draw X","Result",JOptionPane.INFORMATION_MESSAGE);
                setVisible(false);
                Tic_Tac_Toe_Main.menu.setVisible(true);
                gameRecords.updateRecord(false,false);
            }
            else if (Menu.Mode == 1)
            {
                if (doneMatch < Menu.rounds)
                {
                    JOptionPane.showMessageDialog(Tic_Tac_Toe_Main.f,"X Draw X","Result",JOptionPane.INFORMATION_MESSAGE);
                    doneMatch++;
                }
                else
                {
                    resetBoardWithBattle();
                }
            }
            resetBoard();
        }
    }
    private void winner()
    {
        String winner;
        if (Score_Tic_Tac_Toe.playerScore > Score_Tic_Tac_Toe.computerScore)
        {
            winner = "Player won";
        }
        else if (Score_Tic_Tac_Toe.playerScore < Score_Tic_Tac_Toe.computerScore)
        {
            winner = "Computer won";
        }
        else
            winner = "Draw";
        JOptionPane.showMessageDialog(Tic_Tac_Toe_Main.f,winner,"Final Results",JOptionPane.INFORMATION_MESSAGE);
    }
    private void resetBoardWithBattle()
    {
        winner();
        doneMatch = 1;
        Score_Tic_Tac_Toe.playerScore = 0;
        Score_Tic_Tac_Toe.computerScore = 0;
        Tic_Tac_Toe_Main.scoreTicTacToe.repaint();
        setVisible(false);
        Tic_Tac_Toe_Main.scoreTicTacToe.setVisible(false);
        Tic_Tac_Toe_Main.menu.setVisible(true);
    }
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);

        g.setColor(Color.BLACK);
        g.drawRect(0,0,w-1,h-1);
        g.drawRect(1,1,w-2,h-2);
        g.drawRect(1,1,w-3,h-3);
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}

class Menu extends JPanel implements MouseListener
{
    private int x, y, w, h;
    static int Mode;
    private boolean doesShowStats;
    protected static int rounds;
    private JButton quickMatches, battle, stats, exit, play, back,  reset, close;
    private JLabel selectRounds, matchStats, matchPlayedLabel, playerWonLabel, computerWonLabel;
    private JSlider slider;
    private ImageIcon quickMatchIcon, quickMatchIconHover, battleIcon, battleIconHover, statsIcon, statsIconHover,  exitIcon, exitIconHover, playIcon, playIconHover, backIcon, backIconHover;
    private ImageIcon resetIcon, resetHover, closeIcon, closeIconHover;
    private int quickMatchX, quickMatchY, battleX, battleY,statsX, statsY, exitX, exitY, playX, playY, backX, backY, resetX, resetY, closeX, closeY;
    Border border = null;
    static Game_Records gameRecords;
    Menu()
    {
        w = 1275;
        h = 550;
        x = (1382 - w) / 2;
        y = 150;

        setBounds(x, y, w, h);
        setLayout(null);
        setBackground(Color.BLACK);
        setBorder(border);

        gameRecords = new Game_Records("Tic Tac Toe");

        doesShowStats = false;
        rounds = 5;

        loadImages();
        loadMainMenu();
        loadBattleMenu();
        loadStats();
    }
    private void loadImages()
    {
        quickMatchIcon = new ImageIcon("src/Tic_Tac_Toe/Images/Quick Match Full.png");
        quickMatchIconHover = new ImageIcon("src/Tic_Tac_Toe/Images/Quick Match Hover.png");
        battleIcon = new ImageIcon("src/Tic_Tac_Toe/Images/Battle Full.png");
        battleIconHover = new ImageIcon("src/Tic_Tac_Toe/Images/Battle Hover.png");
        statsIcon = new ImageIcon("src/Tic_Tac_Toe/Images/Stats Icon.png");
        statsIconHover = new ImageIcon("src/Tic_Tac_Toe/Images/Stats Hover.png");
        exitIcon = new ImageIcon("src/Tic_Tac_Toe/Images/Exit Icon.png");
        exitIconHover = new ImageIcon("src/Tic_Tac_Toe/Images/Exit Hover.png");

        playIcon = new ImageIcon("src/Tic_Tac_Toe/Images/Play.png");
        playIconHover = new ImageIcon("src/Tic_Tac_Toe/Images/Play Hover.png");
        backIcon = new ImageIcon("src/Tic_Tac_Toe/Images/Back Icon.png");
        backIconHover = new ImageIcon("src/Tic_Tac_Toe/Images/Back Hover.png");

        resetIcon = new ImageIcon("src/Tic_Tac_Toe/Images/Reset Icon.png");
        resetHover = new ImageIcon("src/Tic_Tac_Toe/Images/Reset Hover.png");
        closeIcon = new ImageIcon("src/Tic_Tac_Toe/Images/Close Icon.png");
        closeIconHover = new ImageIcon("src/Tic_Tac_Toe/Images/Close Hover.png");
    }
    private void loadMainMenu()
    {
        quickMatchX = (w-quickMatchIcon.getIconWidth())/2;
        quickMatchY = 65;
        battleX = (w-battleIcon.getIconWidth())/2;
        battleY = 170;
        statsX = (w-statsIcon.getIconWidth())/2;
        statsY = 270;
        exitX = (w-exitIcon.getIconWidth())/2;
        exitY = 360;

        quickMatches = new JButton();
        quickMatches.setBounds(quickMatchX,quickMatchY,quickMatchIcon.getIconWidth(),quickMatchIcon.getIconHeight());
        quickMatches.setIcon(quickMatchIcon);
        quickMatches.addMouseListener(this);
        quickMatches.setBackground(null);
        quickMatches.setContentAreaFilled(false);
        quickMatches.setOpaque(false);
        quickMatches.setFocusPainted(false);
        quickMatches.setBorder(border);
        quickMatches.setBorderPainted(false);
        quickMatches.setToolTipText("Play a Quick Match with Computer");
        add(quickMatches);

        battle = new JButton();
        battle.setBounds(battleX,battleY,battleIcon.getIconWidth(),battleIcon.getIconHeight());
        battle.setIcon(battleIcon);
        battle.addMouseListener(this);
        battle.setBackground(null);
        battle.setContentAreaFilled(false);
        battle.setOpaque(false);
        battle.setFocusPainted(false);
        battle.setBorder(border);
        battle.setBorderPainted(false);
        battle.setToolTipText("Play a Series of Match with Computer");
        add(battle);

        stats = new JButton();
        stats.setBounds(statsX,statsY,statsIcon.getIconWidth(),statsIcon.getIconHeight());
        stats.setIcon(statsIcon);
        stats.addMouseListener(this);
        stats.setBackground(null);
        stats.setContentAreaFilled(false);
        stats.setOpaque(false);
        stats.setFocusPainted(false);
        stats.setBorder(border);
        stats.setBorderPainted(false);
        add(stats);

        exit = new JButton();
        exit.setBounds(exitX,exitY,exitIcon.getIconWidth(),exitIcon.getIconHeight());
        exit.setIcon(exitIcon);
        exit.addMouseListener(this);
        exit.setBackground(null);
        exit.setContentAreaFilled(false);
        exit.setOpaque(false);
        exit.setFocusPainted(false);
        exit.setBorder(border);
        exit.setBorderPainted(false);
        add(exit);
    }
    private void loadBattleMenu()
    {
        selectRounds = new JLabel();
        selectRounds.setBounds((w-450)/2, 55, 450, 30);
        selectRounds.setText("Select Rounds Limit");
        selectRounds.setHorizontalAlignment(0);
        selectRounds.setFont(new Font("FixedsysTTF", Font.PLAIN, 28));
        selectRounds.setForeground(Color.WHITE);
        selectRounds.setBorder(border);
        selectRounds.setVisible(false);
        add(selectRounds);

        slider = new JSlider();
        slider.setBounds((w-325)/2, 115, 325, 100);
        slider.setBackground(null);
        slider.setOpaque(false);
        slider.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        slider.setForeground(Color.WHITE);
        slider.setBorder(border);
        slider.setMinimum(1);
        slider.setMaximum(10);
        slider.setMajorTickSpacing(1);
        slider.setMinorTickSpacing(0);
        slider.setValue(5);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        slider.setPaintTrack(true);
        slider.setVisible(false);
        slider.addChangeListener(e -> rounds = slider.getValue());
        add(slider);

        playX = (w-playIcon.getIconWidth())/2;
        playY = 250;
        backX = (w-backIcon.getIconWidth())/2;
        backY = 325;

        play = new JButton(playIcon);
        play.setBounds(playX,playY,playIcon.getIconWidth(),playIcon.getIconHeight());
        play.setBackground(null);
        play.setContentAreaFilled(false);
        play.setOpaque(false);
        play.setFocusPainted(false);
        play.setBorder(border);
        play.setBorderPainted(false);
        play.addMouseListener(this);
        play.setVisible(false);
        add(play);

        back = new JButton(backIcon);
        back.setBounds(backX,backY,backIcon.getIconWidth(),backIcon.getIconHeight());
        back.setBackground(null);
        back.setContentAreaFilled(false);
        back.setOpaque(false);
        back.setFocusPainted(false);
        back.setBorder(border);
        back.setBorderPainted(false);
        back.addMouseListener(this);
        back.setVisible(false);
        add(back);
    }
    private void loadStats()
    {
        resetX = (w/2)-resetIcon.getIconWidth()-75;
        resetY = 350;
        closeX = (w/2)+closeIcon.getIconWidth()+25;
        closeY = 350;

        matchStats = new JLabel("Match Stats");
        matchStats.setBounds((this.getWidth()-200)/2,70,200,30);
        matchStats.setFont(new Font("FixedsysTTF", Font.PLAIN, 28));
        matchStats.setHorizontalAlignment(0);
        matchStats.setForeground(Color.WHITE);
        matchStats.setVisible(false);
        add(matchStats);

        matchPlayedLabel = new JLabel("Match Played : ");
        matchPlayedLabel.setBounds((w-225)/2-75,150,225,30);
        matchPlayedLabel.setForeground(Color.WHITE);
        matchPlayedLabel.setFont(new Font("FixedsysTTF", Font.PLAIN, 24));
        matchPlayedLabel.setBorder(border);
        matchPlayedLabel.setHorizontalAlignment(4);
        matchPlayedLabel.setVisible(false);
        add(matchPlayedLabel);

        playerWonLabel = new JLabel("Won : ");
        playerWonLabel.setBounds((w-225)/2-75,210,225,30);
        playerWonLabel.setForeground(Color.WHITE);
        playerWonLabel.setFont(new Font("FixedsysTTF", Font.PLAIN, 24));
        playerWonLabel.setBorder(border);
        playerWonLabel.setHorizontalAlignment(4);
        playerWonLabel.setVisible(false);
        add(playerWonLabel);

        computerWonLabel = new JLabel("Loss : ");
        computerWonLabel.setBounds((w-225)/2-75,270,225,30);
        computerWonLabel.setForeground(Color.WHITE);
        computerWonLabel.setFont(new Font("FixedsysTTF", Font.PLAIN, 24));
        computerWonLabel.setBorder(border);
        computerWonLabel.setHorizontalAlignment(4);
        computerWonLabel.setVisible(false);
        add(computerWonLabel);

        reset = new JButton(resetIcon);
        reset.setBounds(resetX,resetY,resetIcon.getIconWidth(),resetIcon.getIconHeight());
        reset.setBackground(null);
        reset.setContentAreaFilled(false);
        reset.setBorder(border);
        reset.setBorderPainted(false);
        reset.addMouseListener(this);
        reset.setVisible(false);
        add(reset);

        close = new JButton(closeIcon);
        close.setBounds(closeX,closeY,closeIcon.getIconWidth(),closeIcon.getIconHeight());
        close.setBackground(null);
        close.setContentAreaFilled(false);
        close.setBorder(border);
        close.setBorderPainted(false);
        close.addMouseListener(this);
        close.setVisible(false);
        add(close);
    }
    private void showMatchStats()
    {
        matchStats.setVisible(true);
        matchPlayedLabel.setVisible(true);
        playerWonLabel.setVisible(true);
        computerWonLabel.setVisible(true);
        reset.setVisible(true);
        close.setVisible(true);
        doesShowStats = true;
        repaint();
    }
    protected void hideMatchStats()
    {
        matchStats.setVisible(false);
        matchPlayedLabel.setVisible(false);
        playerWonLabel.setVisible(false);
        computerWonLabel.setVisible(false);
        reset.setVisible(false);
        close.setVisible(false);
        doesShowStats = false;
        repaint();
    }
    private void hideBattleMenu()
    {
        selectRounds.setVisible(false);
        slider.setVisible(false);
        play.setVisible(false);
        back.setVisible(false);
    }
    private void showBattleMenu()
    {
        selectRounds.setVisible(true);
        slider.setVisible(true);
        play.setVisible(true);
        back.setVisible(true);
    }
    private void hideMainMenu()
    {
        quickMatches.setVisible(false);
        battle.setVisible(false);
        stats.setVisible(false);
        exit.setVisible(false);
    }
    private void showMainMenu()
    {
        quickMatches.setVisible(true);
        battle.setVisible(true);
        stats.setVisible(true);
        exit.setVisible(true);
    }
    private void runGame()
    {
        this.setVisible(false);
        Tic_Tac_Toe_Main.game.setVisible(true);
        Tic_Tac_Toe_Main.scoreTicTacToe.setVisible(true);
        new Computer();
        hideBattleMenu();
        showMainMenu();
    }
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        g.setFont(new Font("FixedsysTTF", Font.PLAIN, 24));
        g.setColor(Color.WHITE);

        if (doesShowStats)
        {
            g.drawString(String.valueOf(gameRecords.getMatchPlayed()),((w-225)/2-75)+225+5,150+22);
            g.drawString(String.valueOf(gameRecords.getPlayerWon()),((w-225)/2-75)+225+5,210+22);
            g.drawString(String.valueOf(gameRecords.getComputerWon()),((w-225)/2-75)+225+5,270+22);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        if (e.getSource() == quickMatches && e.getButton() == MouseEvent.BUTTON1)
        {
            Mode = 0;
            runGame();
        }
        else if (e.getSource() == battle && e.getButton() == MouseEvent.BUTTON1)
        {
            Mode = 1;
            hideMainMenu();
            showBattleMenu();
        }
        else if (e.getSource() == stats && e.getButton() == MouseEvent.BUTTON1)
        {
            hideMainMenu();
            showMatchStats();
        }
        else if (e.getSource() == exit && e.getButton() == MouseEvent.BUTTON1)
        {
            Tic_Tac_Toe_Main.f.dispose();
            Game_Emulator.f.setVisible(true);
        }
        if (e.getSource() == reset && e.getButton() == MouseEvent.BUTTON1)
        {
            int option = JOptionPane.showConfirmDialog(Snake_Main.f, "Are you sure to reset all Records?", "Reset", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION)
            {
                gameRecords.updateRecord(new int[]{0,0,0});
                gameRecords.updateMatchesRecord();
                repaint();
            }
        }
        else if (e.getSource() == close && e.getButton() == MouseEvent.BUTTON1)
        {
            hideMatchStats();
            showMainMenu();
        }
        if (e.getSource() == play && e.getButton() == MouseEvent.BUTTON1)
        {
            runGame();
        }
        else if (e.getSource() == back && e.getButton() == MouseEvent.BUTTON1)
        {
            hideBattleMenu();
            showMainMenu();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        if (e.getSource() == quickMatches)
        {
            int tempX = (quickMatchIconHover.getIconWidth()-quickMatchIcon.getIconWidth())/2;
            int tempY = (quickMatchIconHover.getIconHeight()-quickMatchIcon.getIconHeight())/2;
            quickMatches.setBounds(quickMatchX-tempX,quickMatchY-tempY,quickMatchIconHover.getIconWidth(),quickMatchIconHover.getIconHeight());
            quickMatches.setIcon(quickMatchIconHover);
        }
        else if (e.getSource() == battle)
        {
            int tempX = (battleIconHover.getIconWidth()-battleIcon.getIconWidth())/2;
            int tempY = (battleIconHover.getIconHeight()-battleIcon.getIconHeight())/2;
            battle.setBounds(battleX-tempX,battleY-tempY,battleIconHover.getIconWidth(),battleIconHover.getIconHeight());
            battle.setIcon(battleIconHover);
        }
        else if (e.getSource() == stats)
        {
            int tempX = (statsIconHover.getIconWidth()-statsIcon.getIconWidth())/2;
            int tempY = (statsIconHover.getIconHeight()-statsIcon.getIconHeight())/2;
            stats.setBounds(statsX-tempX,statsY-tempY,statsIconHover.getIconWidth(),statsIconHover.getIconHeight());
            stats.setIcon(statsIconHover);
        }
        else if (e.getSource() == exit)
        {
            int tempX = (exitIconHover.getIconWidth()-exitIcon.getIconWidth())/2;
            int tempY = (exitIconHover.getIconHeight()-exitIcon.getIconHeight())/2;
            exit.setBounds(exitX-tempX,exitY-tempY,exitIconHover.getIconWidth(),exitIconHover.getIconHeight());
            exit.setIcon(exitIconHover);
        }
        if (e.getSource() == play)
        {
            int tempX = (playIconHover.getIconWidth()-playIcon.getIconWidth())/2;
            int tempY = (playIconHover.getIconHeight()-playIcon.getIconHeight())/2;
            play.setBounds(playX-tempX,playY-tempY,playIconHover.getIconWidth(),playIconHover.getIconHeight());
            play.setIcon(playIconHover);
        }
        else if (e.getSource() == back)
        {
            int tempX = (backIconHover.getIconWidth()-backIcon.getIconWidth())/2;
            int tempY = (backIconHover.getIconHeight()-backIcon.getIconHeight())/2;
            back.setBounds(backX-tempX,backY-tempY,backIconHover.getIconWidth(),backIconHover.getIconHeight());
            back.setIcon(backIconHover);
        }
        if (e.getSource() == reset)
        {
            int tempX = (resetHover.getIconWidth() - resetIcon.getIconWidth())/2;
            int tempY = (resetHover.getIconHeight() - resetIcon.getIconHeight())/2;
            reset.setBounds(resetX-tempX,resetY-tempY,resetHover.getIconWidth(),resetHover.getIconHeight());
            reset.setIcon(resetHover);
        }
        else if (e.getSource() == close)
        {
            int tempX = (closeIconHover.getIconWidth() - closeIcon.getIconWidth()) / 2;
            int tempY = (closeIconHover.getIconHeight() - closeIcon.getIconHeight()) / 2;
            close.setBounds(closeX - tempX, closeY - tempY, closeIconHover.getIconWidth(), closeIconHover.getIconHeight());
            close.setIcon(closeIconHover);
        }
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        if (e.getSource() == quickMatches)
        {
            quickMatches.setBounds(quickMatchX,quickMatchY,quickMatchIcon.getIconWidth(),quickMatchIcon.getIconHeight());
            quickMatches.setIcon(quickMatchIcon);
        }
        else if (e.getSource() == battle)
        {
            battle.setBounds(battleX,battleY,battleIcon.getIconWidth(),battleIcon.getIconHeight());
            battle.setIcon(battleIcon);
        }
        else if (e.getSource() == stats)
        {
            stats.setBounds(statsX,statsY,statsIcon.getIconWidth(),statsIcon.getIconHeight());
            stats.setIcon(statsIcon);
        }
        else if (e.getSource() == exit)
        {
            exit.setBounds(exitX,exitY,exitIcon.getIconWidth(),exitIcon.getIconHeight());
            exit.setIcon(exitIcon);
        }
        if (e.getSource() == play)
        {
            play.setBounds(playX,playY,playIcon.getIconWidth(),playIcon.getIconHeight());
            play.setIcon(playIcon);
        }
        else if (e.getSource() == back)
        {
            back.setBounds(backX,backY,backIcon.getIconWidth(),backIcon.getIconHeight());
            back.setIcon(backIcon);
        }
        if (e.getSource() == reset)
        {
            reset.setBounds(resetX,resetY,resetIcon.getIconWidth(),resetIcon.getIconHeight());
            reset.setIcon(resetIcon);
        }
        else if (e.getSource() == close)
        {
            close.setBounds(closeX,closeY,closeIcon.getIconWidth(),closeIcon.getIconHeight());
            close.setIcon(closeIcon);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}
}
class Game_Records
{
    private int matchPlayed;
    private int playerWon;
    private int computerWon;
    private int [] record;
    private int [] info;
    private BufferedWriter writer;
    private BufferedReader reader;
    public int getMatchPlayed() {
       return matchPlayed;
    }
    public int getPlayerWon() {
        return playerWon;
    }
    public int getComputerWon() {
        return computerWon;
    }
    Game_Records(String fileName)
    {
        try
        {
            new File("src/High Score/"+fileName+".txt");
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,"File "+fileName+" is missing..!!","Error",JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        updateMatchesRecord();
    }
    protected void updateMatchesRecord()
    {
        record = getRecord();
        matchPlayed = record[0];
        playerWon = record[1];
        computerWon = record[2];
    }
    protected void updateRecord(int [] newRecord)
    {
        try
        {
            writer = new BufferedWriter(new FileWriter("src/High Score/Tic Tac Toe.txt"));
            writer.flush();
            writer.append("Played - ").append(String.valueOf(newRecord[0])).append("\n");
            writer.append("Won - ").append(String.valueOf(newRecord[1])).append("\n");
            writer.append("Loss - ").append(String.valueOf(newRecord[2])).append("\n");
            writer.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private int [] getRecord()
    {
        try
        {
            reader = new BufferedReader(new FileReader("src/High Score/Tic Tac Toe.txt"));
            String str;
            String [] data = new String[3];
            info = new int[3];
            int k = 0;
            while ((str = reader.readLine()) != null)
            {
                data[k] = str;
                k++;
            }
            info = getData(data);
            reader.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return info;
    }
    private int [] getData(String [] data)
    {
        String string;
        int [] reqData = new int[3];
        for (int i=0; i<data.length; i++)
        {
            string = data[i];
            if (string != null)
            {
                String [] tempData = string.replaceAll("\\s","").split("-");
                if (tempData.length == 2)
                {
                    try
                    {
                        reqData[i] = Integer.parseInt(tempData[1]);
                    }
                    catch (Exception e)
                    {
                        JOptionPane.showMessageDialog(null,"Unable to fetch High Score..!!","Error",JOptionPane.ERROR_MESSAGE);
                        return null;
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(null,"One or more files may be corrupted..!!","Error",JOptionPane.ERROR_MESSAGE);
                    return null;
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null,"Unable to fetch High Score..!!","Error",JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }
        return reqData;
    }
    protected void updateRecord(boolean increaseWin, boolean increaseLoss)
    {
        if (increaseWin)
            updateRecord(new int[]{matchPlayed+1,playerWon+1,computerWon});
        else if (increaseLoss)
            updateRecord(new int[]{matchPlayed+1,playerWon,computerWon+1});
        else
            updateRecord(new int[]{matchPlayed+1,playerWon,computerWon});
        updateMatchesRecord();
    }
}
public class Tic_Tac_Toe_Main
{
    static JFrame f;
    static ArrayList<Integer> human, com;
    static Tic_Tac_Toe game;
    static Menu menu;
    static Score_Tic_Tac_Toe scoreTicTacToe;
    Tic_Tac_Toe_Main()
    {
        f = new JFrame("Tic Tak Toe");
        f.setBounds(-8, -1, 1382, 744);
        f.setLayout(null);
        f.setUndecorated(true);
        f.setLocationRelativeTo(null);
        f.setIconImage(new ImageIcon("src/Images/Icon/Tic Tac Toe.png").getImage());
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().setBackground(Color.BLACK);

        human = new ArrayList<>();
        com = new ArrayList<>();

        Head_Tic_Tac_Toe head = new Head_Tic_Tac_Toe();
        f.add(head);

        scoreTicTacToe = new Score_Tic_Tac_Toe();
        f.add(scoreTicTacToe);

        menu = new Menu();
        f.add(menu);

        game = new Tic_Tac_Toe();
        f.add(game);

        f.setVisible(true);
    }

    public static void main(String[] args)
    {
        new Tic_Tac_Toe_Main();
    }
}
