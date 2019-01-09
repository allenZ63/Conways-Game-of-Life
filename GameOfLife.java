package gameoflife;

import static gameoflife.GameOfLife.g;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class GameOfLife implements MouseListener {

    int m = 10;
    int n = 10;
    ArrayList<Integer> al = new ArrayList<Integer>();
    boolean[][] displayArray = new boolean[m][n];
    JLabel[][] labels = new JLabel[m][n];
    JPanel panel = new JPanel();
    JFrame frame = new JFrame();
    static GameOfLife g = new GameOfLife();
    volatile private boolean mouseDown = false;
    volatile private boolean isRunning = false;
    boolean useMouse = false;

    public static void main(String[] args) throws InterruptedException {
        g = new GameOfLife();
        g.init();
    }

    private void init() throws InterruptedException {
        String detect = g.scanDetection();
        if (detect.equals("Click")) {
            useMouse = true;
        }
        g.scanOptions();
        g.setup();
        g.start();
        g.setVisible();
        TimeUnit.SECONDS.sleep(3);

        if (detect.equals("Auto")) {
            for (int i = 0; i < 100; i++) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GameOfLife.class.getName()).log(Level.SEVERE, null, ex);
                }
                g.calculateArray();
                g.setVisible();
            }
        }

    }

    private synchronized boolean checkAndMark() {
        if (isRunning) {
            return false;
        }
        isRunning = true;
        return true;
    }

    public String scanDetection() {
        System.out.println("Click or Auto");
        Scanner option = new Scanner(System.in);
        String input1 = option.nextLine();
        return input1;
    }

    public void scanOptions() {
        System.out.println("Options: Glider, Small Exploder, Exploder, 10 Cell Row");
        Scanner option = new Scanner(System.in);
        String input1 = option.nextLine();
        if (input1.equals("Glider")) {
            al.add(4);
            al.add(4);
            al.add(5);
            al.add(5);
            al.add(6);
            al.add(5);
            al.add(6);
            al.add(4);
            al.add(6);
            al.add(3);
        }
        if (input1.equals("Small Exploder")) {
            al.add(4);
            al.add(4);
            al.add(6);
            al.add(4);
            al.add(5);
            al.add(3);
            al.add(5);
            al.add(5);
            al.add(4);
            al.add(3);
            al.add(4);
            al.add(5);
            al.add(3);
            al.add(4);
        }
        if (input1.equals("Exploder")) {
            al.add(2);
            al.add(3);
            al.add(3);
            al.add(3);
            al.add(4);
            al.add(3);
            al.add(5);
            al.add(3);
            al.add(6);
            al.add(3);

            al.add(2);
            al.add(5);
            al.add(6);
            al.add(5);

            al.add(2);
            al.add(7);
            al.add(3);
            al.add(7);
            al.add(4);
            al.add(7);
            al.add(5);
            al.add(7);
            al.add(6);
            al.add(7);
        }
        if (input1.equals("10 Cell Row")) {
            al.add(4);
            al.add(0);
            al.add(4);
            al.add(1);
            al.add(4);
            al.add(2);
            al.add(4);
            al.add(3);
            al.add(4);
            al.add(4);
            al.add(4);
            al.add(5);
            al.add(4);
            al.add(6);
            al.add(4);
            al.add(7);
            al.add(4);
            al.add(8);
            al.add(4);
            al.add(9);
        }
    }

    private void setup() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        panel.setLayout(new GridLayout(m, n, 1, 1));
        panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        frame.add(panel);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                labels[i][j] = new JLabel();
                labels[i][j].setOpaque(true);
                labels[i][j].setBackground(Color.gray);
                panel.add(labels[i][j]);
                displayArray[i][j] = false;
            }
        }
        if (useMouse == true) {
            panel.addMouseListener(this);
        }
    }

    private void start() {
        for (int i = 0; i < al.size(); i += 2) {
            displayArray[al.get(i)][al.get(i + 1)] = true;
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (displayArray[i][j] == true) {
                    labels[i][j].setBackground(Color.yellow);
                }
            }
        }
    }

    private void calculateArray() {
        boolean[][] newArray = new boolean[m][n];
        int[][] count = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int iCount = i;
                int jCount = j;
                //checking for yellow boxes
                //centers
                if (i > 0 && j > 0 && i < m - 1 && j < n - 1) {
                    if (displayArray[iCount - 1][jCount - 1] == true) {
                        count[i][j]++;
                    }
                    if (displayArray[iCount - 1][jCount] == true) {
                        count[i][j]++;
                    }
                    if (displayArray[iCount - 1][jCount + 1] == true) {
                        count[i][j]++;
                    }
                    if (displayArray[iCount][jCount - 1] == true) {
                        count[i][j]++;
                    }
                    if (displayArray[iCount][jCount + 1] == true) {
                        count[i][j]++;
                    }
                    if (displayArray[iCount + 1][jCount - 1] == true) {
                        count[i][j]++;
                    }
                    if (displayArray[iCount + 1][jCount] == true) {
                        count[i][j]++;
                    }
                    if (displayArray[iCount + 1][jCount + 1] == true) {
                        count[i][j]++;
                    }
                }
                //top left corner-0,0
                if (i == 0 && j == 0) {
                    iCount = m - 1;
                    jCount = n - 1;
                    if (displayArray[iCount - 1][jCount - 1] == true) {
                        count[i][j]++;
                    }
                    jCount = j;
                    if (displayArray[iCount - 1][jCount] == true) {
                        count[i][j]++;
                    }
                    if (displayArray[iCount - 1][jCount + 1] == true) {
                        count[i][j]++;
                    }
                    iCount = i;
                    jCount = n - 1;
                    if (displayArray[iCount][jCount - 1] == true) {
                        count[i][j]++;
                    }
                    jCount = j;
                    if (displayArray[iCount][jCount + 1] == true) {
                        count[i][j]++;
                    }
                    jCount = n - 1;
                    if (displayArray[iCount + 1][jCount - 1] == true) {
                        count[i][j]++;
                    }
                    jCount = j;
                    if (displayArray[iCount + 1][jCount] == true) {
                        count[i][j]++;
                    }
                    if (displayArray[iCount + 1][jCount + 1] == true) {
                        count[i][j]++;
                    }
                }
                //bottom left corner-9,0
                if (i == m - 1 && j == 0) {
                    jCount = n - 1;
                    if (displayArray[iCount - 1][jCount - 1] == true) {
                        count[i][j]++;
                    }
                    jCount = j;
                    if (displayArray[iCount - 1][jCount] == true) {
                        count[i][j]++;
                    }
                    if (displayArray[iCount - 1][jCount + 1] == true) {
                        count[i][j]++;
                    }
                    jCount = n - 1;
                    if (displayArray[iCount][jCount - 1] == true) {
                        count[i][j]++;
                    }
                    jCount = j;
                    if (displayArray[iCount][jCount + 1] == true) {
                        count[i][j]++;
                    }
                    iCount = 0;
                    jCount = n - 1;
                    if (displayArray[iCount + 1][jCount - 1] == true) {
                        count[i][j]++;
                    }
                    jCount = j;
                    if (displayArray[iCount + 1][jCount] == true) {
                        count[i][j]++;
                    }
                    if (displayArray[iCount + 1][jCount + 1] == true) {
                        count[i][j]++;
                    }
                }
                //top right corner-0,9
                if (i == 0 && j == n - 1) {
                    iCount = m - 1;
                    if (displayArray[iCount - 1][jCount - 1] == true) {
                        count[i][j]++;
                    }
                    if (displayArray[iCount - 1][jCount] == true) {
                        count[i][j]++;
                    }
                    jCount = 0;
                    if (displayArray[iCount - 1][jCount + 1] == true) {
                        count[i][j]++;
                    }
                    iCount = i;
                    jCount = j;
                    if (displayArray[iCount][jCount - 1] == true) {
                        count[i][j]++;
                    }
                    jCount = 0;
                    if (displayArray[iCount][jCount + 1] == true) {
                        count[i][j]++;
                    }
                    jCount = j;
                    if (displayArray[iCount + 1][jCount - 1] == true) {
                        count[i][j]++;
                    }
                    if (displayArray[iCount + 1][jCount] == true) {
                        count[i][j]++;
                    }
                    jCount = 0;
                    if (displayArray[iCount + 1][jCount + 1] == true) {
                        count[i][j]++;
                    }
                    jCount = j;
                }
                //bottom right corner-9,9
                if (i == m - 1 && j == n - 1) {
                    if (displayArray[iCount - 1][jCount - 1] == true) {
                        count[i][j]++;
                    }
                    if (displayArray[iCount - 1][jCount] == true) {
                        count[i][j]++;
                    }
                    jCount = 0;
                    if (displayArray[iCount - 1][jCount + 1] == true) {
                        count[i][j]++;
                    }
                    jCount = j;
                    if (displayArray[iCount][jCount - 1] == true) {
                        count[i][j]++;
                    }
                    jCount = 0;
                    if (displayArray[iCount][jCount + 1] == true) {
                        count[i][j]++;
                    }
                    iCount = 0;
                    jCount = j;
                    if (displayArray[iCount + 1][jCount - 1] == true) {
                        count[i][j]++;
                    }
                    if (displayArray[iCount + 1][jCount] == true) {
                        count[i][j]++;
                    }
                    jCount = 0;
                    if (displayArray[iCount + 1][jCount + 1] == true) {
                        count[i][j]++;
                    }
                    iCount = i;
                    jCount = j;
                }
                //left edge- -,0
                if (i != 0 && i != m - 1 && j == 0) {
                    jCount = n - 1;
                    if (displayArray[iCount - 1][jCount - 1] == true) {
                        count[i][j]++;
                    }
                    jCount = j;
                    if (displayArray[iCount - 1][jCount] == true) {
                        count[i][j]++;
                    }
                    if (displayArray[iCount - 1][jCount + 1] == true) {
                        count[i][j]++;
                    }
                    jCount = n - 1;
                    if (displayArray[iCount][jCount - 1] == true) {
                        count[i][j]++;
                    }
                    jCount = j;
                    if (displayArray[iCount][jCount + 1] == true) {
                        count[i][j]++;
                    }
                    jCount = n - 1;
                    if (displayArray[iCount + 1][jCount - 1] == true) {
                        count[i][j]++;
                    }
                    jCount = j;
                    if (displayArray[iCount + 1][jCount] == true) {
                        count[i][j]++;
                    }
                    if (displayArray[iCount + 1][jCount + 1] == true) {
                        count[i][j]++;
                    }
                }
                //right edge- -,9
                if (i != 0 && i != m - 1 && j == n - 1) {
                    if (displayArray[iCount - 1][jCount - 1] == true) {
                        count[i][j]++;
                    }
                    if (displayArray[iCount - 1][jCount] == true) {
                        count[i][j]++;
                    }
                    jCount = 0;
                    if (displayArray[iCount - 1][jCount + 1] == true) {
                        count[i][j]++;
                    }
                    jCount = j;
                    if (displayArray[iCount][jCount - 1] == true) {
                        count[i][j]++;
                    }
                    jCount = 0;
                    if (displayArray[iCount][jCount + 1] == true) {
                        count[i][j]++;
                    }
                    jCount = j;
                    if (displayArray[iCount + 1][jCount - 1] == true) {
                        count[i][j]++;
                    }
                    if (displayArray[iCount + 1][jCount] == true) {
                        count[i][j]++;
                    }
                    jCount = 0;
                    if (displayArray[iCount + 1][jCount + 1] == true) {
                        count[i][j]++;
                    }
                    jCount = j;
                }
                //top edge- 0,-
                if (i == 0 && j != 0 && j != n - 1) {
                    iCount = m - 1;
                    if (displayArray[iCount - 1][jCount - 1] == true) {
                        count[i][j]++;
                    }
                    if (displayArray[iCount - 1][jCount] == true) {
                        count[i][j]++;
                    }
                    if (displayArray[iCount - 1][jCount + 1] == true) {
                        count[i][j]++;
                    }
                    iCount = i;
                    if (displayArray[iCount][jCount - 1] == true) {
                        count[i][j]++;
                    }
                    if (displayArray[iCount][jCount + 1] == true) {
                        count[i][j]++;
                    }
                    if (displayArray[iCount + 1][jCount - 1] == true) {
                        count[i][j]++;
                    }
                    if (displayArray[iCount + 1][jCount] == true) {
                        count[i][j]++;
                    }
                    if (displayArray[iCount + 1][jCount + 1] == true) {
                        count[i][j]++;
                    }
                }
                //bottom edge- 9,-
                if (i == m - 1 && j != 0 && j != n - 1) {
                    if (displayArray[iCount - 1][jCount - 1] == true) {
                        count[i][j]++;
                    }
                    if (displayArray[iCount - 1][jCount] == true) {
                        count[i][j]++;
                    }
                    if (displayArray[iCount - 1][jCount + 1] == true) {
                        count[i][j]++;
                    }
                    if (displayArray[iCount][jCount - 1] == true) {
                        count[i][j]++;
                    }
                    if (displayArray[iCount][jCount + 1] == true) {
                        count[i][j]++;
                    }
                    iCount = 0;
                    if (displayArray[iCount + 1][jCount - 1] == true) {
                        count[i][j]++;
                    }
                    if (displayArray[iCount + 1][jCount] == true) {
                        count[i][j]++;
                    }
                    if (displayArray[iCount + 1][jCount + 1] == true) {
                        count[i][j]++;
                    }
                    iCount = i;
                }
                //setting boolean values for new array
                if (count[i][j] == 0 || count[i][j] == 1 || count[i][j] == 4 || count[i][j] == 5 || count[i][j] == 6 || count[i][j] == 7 || count[i][j] == 8) {
                    newArray[i][j] = false;
                }
                if (count[i][j] == 2) {
                    if (displayArray[i][j] == true) {
                        newArray[i][j] = true;
                    }
                    if (displayArray[i][j] == false) {
                        newArray[i][j] = false;
                    }
                }
                if (count[i][j] == 3) {
                    newArray[i][j] = true;
                }
            }
        }
        displayArray = newArray;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void setVisible() {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (displayArray[i][j] == true) { //setting new colors
                    labels[i][j].setBackground(Color.yellow);
                }
                if (displayArray[i][j] == false) {
                    labels[i][j].setBackground(Color.gray);
                }
            }
        }
        frame.setVisible(true);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //mousePressed(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            mouseDown = true;
            g.calculateArray();
            g.setVisible();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            mouseDown = false;
        }
    }
}



