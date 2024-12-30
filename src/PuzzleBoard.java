// PuzzleBoard.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class PuzzleBoard extends JPanel {
    private PuzzleTile[][] tiles;
    private int size;
    private int emptyRow, emptyCol;
    private GameController controller;
    private static final int TILE_SIZE = 80;
    private static final int GAP = 5;
    private JComboBox<String> sizeSelector;

    public PuzzleBoard(int size, GameController controller) {
        this.size = size;
        this.controller = controller;
        setLayout(new BorderLayout());

        // Create top panel for size selector
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setOpaque(false);

        String[] sizes = {"3x3", "4x4", "5x5"};
        sizeSelector = new JComboBox<>(sizes);
        sizeSelector.setSelectedIndex(size - 3); // Set current size
        sizeSelector.addActionListener(e -> changePuzzleSize());

        JLabel sizeLabel = new JLabel("Ukuran Puzzle: ");
        sizeLabel.setForeground(Color.WHITE);
        topPanel.add(sizeLabel);
        topPanel.add(sizeSelector);

        // Create puzzle panel
        JPanel puzzlePanel = new JPanel(null) {
            @Override
            public Dimension getPreferredSize() {
                int totalSize = (TILE_SIZE * size) + (GAP * (size - 1));
                return new Dimension(totalSize, totalSize);
            }
        };
        puzzlePanel.setOpaque(false);

        add(topPanel, BorderLayout.NORTH);
        add(puzzlePanel, BorderLayout.CENTER);

        initializeTiles(puzzlePanel);
    }

    private void initializeTiles(JPanel panel) {
        tiles = new PuzzleTile[size][size];
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

    private void changePuzzleSize() {
        String selected = (String) sizeSelector.getSelectedItem();
        int newSize = Character.getNumericValue(selected.charAt(0));
        if (newSize != size) {
            size = newSize;
            Component[] components = getComponents();
            for (Component comp : components) {
                if (comp instanceof JPanel && !(comp instanceof JComboBox)) {
                    JPanel puzzlePanel = (JPanel) comp;
                    puzzlePanel.removeAll();
                    initializeTiles(puzzlePanel);
                }
            }
            revalidate();
            repaint();
            controller.resetGame();
        }
    }

    public void acakPuzzle() {
        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            int direction = random.nextInt(4);
            switch (direction) {
                case 0: moveTile(emptyRow - 1, emptyCol); break;
                case 1: moveTile(emptyRow + 1, emptyCol); break;
                case 2: moveTile(emptyRow, emptyCol - 1); break;
                case 3: moveTile(emptyRow, emptyCol + 1); break;
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
        return new Dimension(totalSize + 20, totalSize + 60); // Extra space for size selector
    }
}