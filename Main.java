import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

public class Main extends JFrame {

    private static final int GRID_SIZE = 4; // 4x4 grid
    private final ArrayList<Color> cardColors; //list pre farby
    private final JButton[] cards; //array JButtonov ako karty
    private final Color[] cardFaces; //array opacnej strany kariet
    private boolean firstCardFlipped = false;
    private int firstCardIndex;
    private int secondCardIndex;
    private Timer flipBackTimer;
    private boolean isTimerRunning = false;

    public Main() {

        setTitle("Pexeso Swing");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));
        setLocationRelativeTo(null);

        //array pre farby
        cardColors = new ArrayList<>();

        Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.PINK, Color.CYAN, Color.MAGENTA};

        cards = new JButton[GRID_SIZE * GRID_SIZE];
        cardFaces = new Color[GRID_SIZE * GRID_SIZE];

        //foreach ktory nastavi v jednom cykle 2 karty na jednu farbu
        for (Color color : colors) {
            cardColors.add(color);
            cardColors.add(color);
        }
        Collections.shuffle(cardColors);

        //vlozenie JButtonov do gridu a pridanie action listenerov
        for (int i = 0; i < GRID_SIZE * GRID_SIZE; i++) {
            cards[i] = new JButton();
            cardFaces[i] = cardColors.get(i);
            cards[i].setBackground(Color.WHITE);
            cards[i].setFocusPainted(false);
            cards[i].addActionListener(new MainListener());
            add(cards[i]);
        }

        // Timer pre obratenie karty naspat
        flipBackTimer = new Timer(1000, e -> {
            cards[firstCardIndex].setBackground(Color.WHITE);
            cards[firstCardIndex].setText("");
            cards[secondCardIndex].setBackground(Color.WHITE);
            cards[secondCardIndex].setText("");
            firstCardFlipped = false;
            isTimerRunning = false;
            flipBackTimer.stop();
        });

        setVisible(true);
    }

    //Spustac hry
    public static void main(String[] args) {
        new Main();
    }

    //trieda pre ActionListener
    private class MainListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //Pokial bezi prerusenie tak hrac neotoci dalsiu kartu
            if (isTimerRunning)
                return;

            //Algoritmus pre otocenie karty na ktoru hrac klikne mysou a zobrazenie farby
            for (int i = 0; i < cards.length; i++) {
                if (e.getSource() == cards[i]) {
                    if (!firstCardFlipped) {
                        firstCardIndex = i;
                        cards[i].setBackground(cardFaces[i]);
                        cards[i].setText(getColorName(cardFaces[i]));
                        firstCardFlipped = true;
                    } else if (i != firstCardIndex) {
                        secondCardIndex = i;
                        cards[i].setBackground(cardFaces[i]);
                        cards[i].setText(getColorName(cardFaces[i]));

                        //Hrac trafil 2 rovnake karty
                        if (cardFaces[firstCardIndex].equals(cardFaces[secondCardIndex])) {
                            firstCardFlipped = false;
                        } else {
                            isTimerRunning = true;
                            flipBackTimer.start();
                        }
                    }
                }
            }
        }

        // Metoda pre ziskanie nazvu farby
        private String getColorName(Color color) {
            if (color.equals(Color.RED)) return "Cervena";
            if (color.equals(Color.BLUE)) return "Modra";
            if (color.equals(Color.GREEN)) return "Zelena";
            if (color.equals(Color.YELLOW)) return "Zlta";
            if (color.equals(Color.ORANGE)) return "Oranzova";
            if (color.equals(Color.PINK)) return "Ruzova";
            if (color.equals(Color.CYAN)) return "Tyrkysova";
            if (color.equals(Color.MAGENTA)) return "Fialova";
            return " ";
        }
    }
}
