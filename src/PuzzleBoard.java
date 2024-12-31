import javax.swing.*;
import java.awt.*;
import java.util.Random;

// Class untuk mengelola papan puzzle dan logika permainan
public class PuzzleBoard extends JPanel {
    private final PuzzleTile[][] tiles;          // Array 2D untuk menyimpan tile puzzle
    private final int size;                      // Ukuran puzzle (3x3, 4x4, atau 5x5)
    private int emptyRow, emptyCol;             // Posisi tile kosong
    private final GameController controller;     // Reference ke game controller
    private static final int TILE_SIZE = 80;     // Ukuran setiap tile dalam pixel
    private static final int GAP = 5;            // Jarak antar tile dalam pixel

    // Constructor - inisialisasi board puzzle
    public PuzzleBoard(int size, GameController controller) {
        this.size = size;
        this.controller = controller;
        this.tiles = new PuzzleTile[size][size];

        setLayout(new BorderLayout());

        // Panel khusus untuk menyimpan tiles dengan custom preferred size
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

    // Method untuk membuat dan menginisialisasi tiles
    private void initializeTiles(JPanel panel) {
        int count = 1;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (count < size * size) {
                    tiles[i][j] = createTile(String.valueOf(count), i, j);
                } else {
                    // Tile terakhir dibuat kosong
                    tiles[i][j] = createTile("", i, j);
                    emptyRow = i;
                    emptyCol = j;
                }
                panel.add(tiles[i][j]);
                count++;
            }
        }
    }

    // Method untuk membuat single tile dengan posisi tertentu
    private PuzzleTile createTile(String text, final int row, final int col) {
        PuzzleTile tile = new PuzzleTile(text);

        // Hitung posisi x,y berdasarkan row dan col
        int x = col * (TILE_SIZE + GAP);
        int y = row * (TILE_SIZE + GAP);
        tile.setBounds(x, y, TILE_SIZE, TILE_SIZE);

        // Tambahkan action listener untuk handle klik tile
        tile.addActionListener(e -> {
            // Cek apakah tile bersebelahan dengan tile kosong
            if ((Math.abs(row - emptyRow) + Math.abs(col - emptyCol)) == 1) {
                moveTile(row, col);
                controller.incrementMoves();
                controller.checkWin();
            }
        });

        return tile;
    }

    // Method untuk mengacak posisi puzzle
    public void acakPuzzle() {
        Random random = new Random();
        // Lakukan 1000 gerakan random
        for (int i = 0; i < 1000; i++) {
            int direction = random.nextInt(4);
            switch (direction) {
                case 0 -> moveTile(emptyRow - 1, emptyCol);    // Atas
                case 1 -> moveTile(emptyRow + 1, emptyCol);    // Bawah
                case 2 -> moveTile(emptyRow, emptyCol - 1);    // Kiri
                case 3 -> moveTile(emptyRow, emptyCol + 1);    // Kanan
            }
        }
        controller.resetGame();
    }

    // Method untuk memindahkan tile
    private void moveTile(int row, int col) {
        // Cek apakah posisi valid
        if (row >= 0 && row < size && col >= 0 && col < size) {
            // Tukar posisi tile dengan tile kosong
            tiles[emptyRow][emptyCol].setText(tiles[row][col].getText());
            tiles[row][col].setText("");
            emptyRow = row;
            emptyCol = col;
        }
    }

    // Method untuk mengembalikan puzzle ke posisi solved
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

    // Method untuk mengecek apakah puzzle sudah selesai
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

    // Override getPreferredSize untuk sizing yang tepat
    @Override
    public Dimension getPreferredSize() {
        int totalSize = (TILE_SIZE * size) + (GAP * (size - 1));
        return new Dimension(totalSize, totalSize);
    }

    // Inner class untuk tile puzzle
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

        // Method untuk mendapatkan warna random
        private Color getRandomColor() {
            Random rand = new Random();
            return new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
        }
    }

    // Method main untuk testing
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