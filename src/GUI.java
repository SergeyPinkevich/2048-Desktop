import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 * Created by serge on 22.01.2016.
 */

public class GUI {

    Game game = new Game();
    int size = game.SIZEFIELD;
    JFrame frame;
    JLabel[][] cells;
    JPanel panelField;
    Font fontForField = new Font("Open Sans", Font.BOLD, 52);
    Color[] colors = {
            new Color(238,228,218),
            new Color(237,224,200),
            new Color(241,177,121),
            new Color(245,149,99),
            new Color(245,124,92),
            new Color(245,94,60),
            new Color(235,233,142),
            new Color(237,203,97),
            new Color(237,199,80),
            new Color(235,196,64),
            new Color(236,193,45),
            new Color(255,61,62,255),
            new Color(255,30,28),
            new Color(255,30,28),
            new Color(255,30,28),
            new Color(255,30,28),
            new Color(255,30,28),
    };
    int[] fontSizes = {52, 52, 52, 46, 40, 34, 28, 24, 20, 16};

    public static void main(String[] args) {
        new GUI().buildGUI();
    }

    public void buildGUI() {
        frame = new JFrame("2048");
        frame.addWindowListener(new CloseListener());
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        GridLayout grid = new GridLayout(4, 4);

        JLabel scores = new JLabel("0");
        scores.setFont(fontForField);
        scores.setBounds(5, 0, 275, 50);

        JButton refreshButton = new JButton();
        refreshButton.addActionListener(new RefreshListener());
        refreshButton.setBackground(new Color(240, 240, 240));
        refreshButton.setBorderPainted(false);
        refreshButton.setFocusPainted(false);
        ImageIcon refreshIcon = new ImageIcon(getClass().getResource("/res/refresh.png"));
        refreshButton.setIcon(refreshIcon);
        refreshButton.setPreferredSize(new Dimension(40, 40));
        refreshButton.setBounds(285, 0, 50, 50);

        JButton saveButton = new JButton();
        saveButton.addActionListener(new SaveListener());
        saveButton.setBackground(new Color(240, 240, 240));
        saveButton.setBorderPainted(false);
        saveButton.setFocusPainted(false);
        ImageIcon saveIcon = new ImageIcon(getClass().getResource("/res/save.png"));
        saveButton.setIcon(saveIcon);
        saveButton.setPreferredSize(new Dimension(40, 40));
        saveButton.setBounds(385, 0, 50, 50);

        JButton loadButton = new JButton();
        loadButton.addActionListener(new LoadListener());
        loadButton.setBackground(new Color(240, 240, 240));
        loadButton.setBorderPainted(false);
        loadButton.setFocusPainted(false);
        ImageIcon loadIcon = new ImageIcon(getClass().getResource("/res/load.png"));
        loadButton.setIcon(loadIcon);
        loadButton.setPreferredSize(new Dimension(40, 40));
        loadButton.setBounds(335, 0, 50, 50);

        panelField = new JPanel(grid);
        panelField.setFocusable(true);
        panelField.setPreferredSize(new Dimension(440, 400));

        JPanel window = new JPanel();
        JPanel bottomLine = new JPanel();
        bottomLine.setLayout(null);
        window.setLayout(new BoxLayout(window, BoxLayout.Y_AXIS));
        bottomLine.add(scores);
        bottomLine.add(refreshButton);
        bottomLine.add(loadButton);
        bottomLine.add(saveButton);
        window.add(panelField);
        window.add(bottomLine);

        frame.getContentPane().add(window);

        game.initializeField();

        cells = new JLabel[size][size];
        Border border1 = BorderFactory.createLineBorder(new Color(200, 200, 200), 2);
        Border border2 = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, new Color(230, 230, 230), new Color(210, 210, 210));
        Border solidBorder = BorderFactory.createCompoundBorder(border1, border2);

        panelField.addKeyListener(new KeyboardListener());

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j] = new JLabel("");
                cells[i][j].setFont(fontForField);
                cells[i][j].setSize(100, 100);
                if (game.field[i][j] != 0)
                    cells[i][j].setText(String.valueOf(game.field[i][j]));
                cells[i][j].setHorizontalAlignment(JLabel.CENTER);
                cells[i][j].setVerticalAlignment(JLabel.CENTER);
                cells[i][j].setBorder(solidBorder);
                panelField.add(cells[i][j]);
            }
        }

        drawField();

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setSize(440, 540);
        frame.setVisible(true);

        Game.highscores = loadHighscores();

        while (true) {
            scores.setText(String.valueOf(Game.scores));
            if (game.checkGameOver()) {
                if (Game.scores > Game.highscores) {
                    Game.highscores = Game.scores;
                    saveHighscores();
                }
                JOptionPane.showMessageDialog(frame, "Your scores: " + Game.scores + "\n" + "Highscores: " +
                                Game.highscores, "Game over", JOptionPane.PLAIN_MESSAGE);
                game.initializeField();
                Game.scores = 0;
                drawField();
            }
        }
    }

    public void drawField() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                String num = String.valueOf(game.field[i][j]);
                for (int n = 1; n <= num.length(); n++) {
                    fontForField = new Font("Open Sans", Font.BOLD, fontSizes[n]);
                }
                cells[i][j].setFont(fontForField);
                for (int n = 2, color = 0; n <= Math.pow(2, 17); n *= 2, color++) {
                    cells[i][j].setOpaque(true);
                    if (game.field[i][j] == n) {
                        cells[i][j].setBackground(colors[color]);
                    }
                    if (game.field[i][j] > 4)
                        cells[i][j].setForeground(Color.white);
                    else
                        cells[i][j].setForeground(Color.black);
                }
                if (game.field[i][j] == 0) {
                    cells[i][j].setText("");
                    cells[i][j].setBackground(new Color(240, 240, 240));
                }
                else
                    cells[i][j].setText(String.valueOf(game.field[i][j]));
            }
        }
    }

    public void resetHighscores() {
        try {
            File file = new File("./save/highscores");
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            Game.highscores = 0;
            oos.writeObject(Game.highscores);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int loadHighscores() {
        try {
            File file = new File("./save/highscores");
            if (file.length() == 0)
                saveHighscores();
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Game.highscores = (int) ois.readObject();
            return Game.highscores;
        } catch (Exception ex) {
            ex.printStackTrace();
            saveHighscores();
            Game.highscores = 0;
            return Game.highscores;
        }
    }


    public void saveHighscores() {
        try {
            File file = new File("./save/highscores");
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(Game.highscores);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    class KeyboardListener implements KeyListener {
       @Override
       public void keyTyped(KeyEvent e) {

       }

       public void keyPressed(KeyEvent e) {
           int code = e.getKeyCode();
           switch (code) {
               case 38:
                   if (game.canMoveUp()) {
                       game.moveUp();
                       game.addNewCell();
                   }
                   break;
               case 40:
                   if (game.canMoveDown()) {
                       game.moveDown();
                       game.addNewCell();
                   }
                   break;
               case 37:
                   if (game.canMoveLeft()) {
                       game.moveLeft();
                       game.addNewCell();
                   }
                   break;
               case 39:
                   if (game.canMoveRight()) {
                       game.moveRight();
                       game.addNewCell();
                   }
                   break;
               default:
                   break;
           }
           drawField();
           frame.repaint();
       }

       @Override
       public void keyReleased(KeyEvent e) {

       }
    }

    class RefreshListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            game.initializeField();
            Game.scores = 0;
            drawField();
            panelField.requestFocus();
        }
    }

    class LoadListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            try {
                JFileChooser file = new JFileChooser();
                file.setCurrentDirectory(new File("./save/"));
                file.showOpenDialog(frame);
                FileInputStream fis = new FileInputStream(file.getSelectedFile());
                ObjectInputStream ois = new ObjectInputStream(fis);
                game.field = (int[][]) ois.readObject();
                Game.scores = (int) ois.readObject();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            drawField();
            panelField.requestFocus();
        }
    }

    class SaveListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            try {
                JFileChooser file = new JFileChooser();
                file.setCurrentDirectory(new File("./save/"));
                file.showSaveDialog(frame);
                FileOutputStream fos = new FileOutputStream(file.getSelectedFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(game.field);
                oos.writeObject(Game.scores);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            drawField();
            panelField.requestFocus();
        }
    }

    class CloseListener implements WindowListener {
        @Override
        public void windowOpened(WindowEvent e) {
            File saveFile = new File("./save/last_game");
            boolean failed = false;
            try {
                FileInputStream fis = new FileInputStream(saveFile);
                ObjectInputStream ois = new ObjectInputStream(fis);
                game.field = (int[][]) ois.readObject();
                Game.scores = (int) ois.readObject();
            } catch (Exception ex) {
                failed = true;
                ex.printStackTrace();
            }
            if (!game.checkGameOver() && !failed) {
                drawField();
                panelField.requestFocus();
            } else {
                Game.scores = 0;
                game.initializeField();
                drawField();
                panelField.requestFocus();
            }
        }

        @Override
        public void windowClosing(WindowEvent e) {
            File saveFile = new File("./save/last_game");
            try {
                FileOutputStream fos = new FileOutputStream(saveFile);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(game.field);
                oos.writeObject(Game.scores);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void windowClosed(WindowEvent e) {

        }

        @Override
        public void windowIconified(WindowEvent e) {

        }

        @Override
        public void windowDeiconified(WindowEvent e) {

        }

        @Override
        public void windowActivated(WindowEvent e) {

        }

        @Override
        public void windowDeactivated(WindowEvent e) {

        }
    }
}