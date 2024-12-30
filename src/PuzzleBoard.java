import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class PuzzleBoard extends JPanel {
    private final PuzzleTile[][] tiles;
    private final int size;
    private int emptyRow, emptyCol;
    private final GameController controller;
    private static final int TILE_SIZE = 80;
    private static final int GAP = 5;

    public PuzzleBoard(int size, GameController controller) {
        this.size = size;
        this.controller = controller;
        this.tiles = new PuzzleTile[size][size];

        setLayout(new BorderLayout());

        JPanel puzzlePanel = new JPanel(null) {
            @Override
            public Dimension getPreferredSize() {
                int totalSize = (TILE_SIZE * size) + (GAP * (size - 1));
                return new Dimension(totalSize, totalSize);
            }
        };
        puzzlePanel.setOpaque(false);
        add(puzzlePanel, BorderLayout.CENTER);
        initializeTiles(puzzlePanel);
    }

    private void initializeTiles(JPanel panel) {
        int count = 1;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (count < size * size) {
                    tiles[i][j] = createTile(String.valueOf(count), i, j);
                } else {
                    tiles[i][j] = createTile("", i, j);
                    emptyRow = i;
                    emptyCol = j;
                }
                panel.add(tiles[i][j]);
                count++;
            }
        }
    }

    private PuzzleTile createTile(String text, final int row, final int col) {
        PuzzleTile tile = new PuzzleTile(text);

        int x = col * (TILE_SIZE + GAP);
        int y = row * (TILE_SIZE + GAP);
        tile.setBounds(x, y, TILE_SIZE, TILE_SIZE);

        tile.addActionListener(e -> {
            if ((Math.abs(row - emptyRow) + Math.abs(col - emptyCol)) == 1) {
                moveTile(row, col);
                controller.incrementMoves();
                controller.checkWin();
            }
        });

        return tile;
    }

    public void acakPuzzle() {
        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            int direction = random.nextInt(4);
            switch (direction) {
                case 0 -> moveTile(emptyRow - 1, emptyCol);
                case 1 -> moveTile(emptyRow + 1, emptyCol);
                case 2 -> moveTile(emptyRow, emptyCol - 1);
                case 3 -> moveTile(emptyRow, emptyCol + 1);
            }
        }
        controller.resetGame();
    }

    private void moveTile(int row, int col) {
        if (row >= 0 && row < size && col >= 0 && col < size) {
            tiles[emptyRow][emptyCol].setText(tiles[row][col].getText());
            tiles[row][col].setText("");
            emptyRow = row;
            emptyCol = col;
        }
    }

    public void selesaikanPuzzle() {
        int count = 1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (count < size * size) {
                    tiles[i][j].setText(String.valueOf(count));
                } else {
                    tiles[i][j].setText("");
                    emptyRow = i;
                    emptyCol = j;
                }
                count++;
            }
        }
        controller.checkWin();
    }

    public boolean isComplete() {
        int count = 1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (count < size * size) {
                    if (!tiles[i][j].getText().equals(String.valueOf(count))) {
                        return false;
                    }
                }
                count++;
            }
        }
        return true;
    }

    @Override
    public Dimension getPreferredSize() {
        int totalSize = (TILE_SIZE * size) + (GAP * (size - 1));
        return new Dimension(totalSize, totalSize);
    }

    public static class PuzzleTile extends JButton {
        public PuzzleTile(String text) {
            super(text);

            // Set font besar
            setFont(new Font("Arial", Font.BOLD, 20));

            // Set warna latar belakang berbeda
            setBackground(getRandomColor());

            // Set warna teks berbeda
            setForeground(getRandomColor());

            setPreferredSize(new Dimension(TILE_SIZE, TILE_SIZE));
            setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }

        private Color getRandomColor() {
            Random rand = new Random();
            return new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Puzzle Slider");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GameController controller = new GameController();
        PuzzleBoard board = new PuzzleBoard(3, controller);

        frame.add(board);
        frame.pack();
        frame.setVisible(true);
    }
}
