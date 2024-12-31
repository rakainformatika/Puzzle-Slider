// Import library yang dibutuhkan untuk UI, event handling, dan audio
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import java.io.File;

// Class untuk mengontrol keseluruhan game termasuk UI dan logika game
public class GameController {
    // Deklarasi komponen-komponen UI dan variabel game
    private JFrame frame;                    // Frame utama game
    private PuzzleBoard puzzleBoard;         // Board puzzle
    private JLabel timerLabel, movesLabel;   // Label untuk timer dan moves
    private JButton shuffleButton, solveButton; // Button kontrol game
    private JComboBox<String> sizeSelector;  // Dropdown untuk pilihan ukuran
    private Timer timer;                     // Timer untuk menghitung waktu
    private int seconds = 0;                 // Penghitung waktu dalam detik
    private int moves = 0;                   // Penghitung langkah
    private JPanel mainPanel;                // Panel utama dengan background
    private Clip moveSound;                  // Sound effect untuk gerakan

    // Constructor class
    public GameController() {
        initializeUI();
    }

    // Method untuk memuat file suara
    private void loadSound() {
        try {
            File soundFile = new File("move.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            moveSound = AudioSystem.getClip();
            moveSound.open(audioIn);
        } catch (Exception e) {
            System.out.println("Error loading sound: " + e.getMessage());
        }
    }

    // Method untuk memainkan suara saat tile bergerak
    public void playMoveSound() {
        if (moveSound != null) {
            if (moveSound.isRunning()) {
                moveSound.stop();
            }
            moveSound.setFramePosition(0);
            moveSound.start();
        }
    }

    // Method untuk inisialisasi seluruh UI game
    private void initializeUI() {
        frame = new JFrame("Puzzle Slider");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(400, 500));

        // Listener untuk handle resize window
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });

        mainPanel = createBackgroundPanel();
        frame.add(mainPanel);

        // Setup panel atas dengan selector ukuran
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setOpaque(false);
        String[] sizes = {"3x3", "4x4", "5x5"};
        sizeSelector = new JComboBox<>(sizes);
        sizeSelector.setSelectedIndex(1); // Default 4x4
        sizeSelector.addActionListener(e -> changePuzzleSize());

        JLabel sizeLabel = new JLabel("Ukuran Puzzle: ");
        sizeLabel.setForeground(Color.WHITE);
        topPanel.add(sizeLabel);
        topPanel.add(sizeSelector);

        // Setup panel tengah dengan puzzle board
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        puzzleBoard = new PuzzleBoard(4, this); // Default 4x4
        centerPanel.add(puzzleBoard);

        // Setup panel bawah dengan kontrol
        JPanel controlPanel = createControlPanel();

        // Menambahkan semua panel ke mainPanel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        // Inisialisasi timer
        timer = new Timer(1000, e -> {
            seconds++;
            updateTimerLabel();
        });

        // Setup action listener untuk button
        shuffleButton.addActionListener(e -> puzzleBoard.acakPuzzle());
        solveButton.addActionListener(e -> puzzleBoard.selesaikanPuzzle());

        // Finalisasi setup frame
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        timer.start();
    }

    // Method untuk membuat panel background dengan gradient
    private JPanel createBackgroundPanel() {
        return new JPanel(new BorderLayout(10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(135, 206, 250),
                        0, h, new Color(65, 105, 225));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
    }

    // Method untuk membuat panel kontrol
    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        controlPanel.setOpaque(false);

        timerLabel = new JLabel("Waktu: 0 detik");
        movesLabel = new JLabel("Langkah: 0");
        shuffleButton = new JButton("Acak");
        solveButton = new JButton("Selesaikan");

        // Styling komponen
        styleLabel(timerLabel);
        styleLabel(movesLabel);
        styleButton(shuffleButton);
        styleButton(solveButton);

        // Menambahkan komponen ke panel
        controlPanel.add(timerLabel);
        controlPanel.add(movesLabel);
        controlPanel.add(shuffleButton);
        controlPanel.add(solveButton);

        return controlPanel;
    }

    // Method untuk mengganti ukuran puzzle
    private void changePuzzleSize() {
        String selected = (String) sizeSelector.getSelectedItem();
        int size = Character.getNumericValue(selected.charAt(0));

        // Hapus puzzle board yang ada
        Component[] components = ((JPanel)mainPanel.getComponent(1)).getComponents();
        for (Component comp : components) {
            if (comp instanceof PuzzleBoard) {
                ((JPanel)mainPanel.getComponent(1)).remove(comp);
                break;
            }
        }

        // Buat puzzle board baru dengan ukuran yang dipilih
        puzzleBoard = new PuzzleBoard(size, this);
        ((JPanel)mainPanel.getComponent(1)).add(puzzleBoard);

        // Refresh UI
        frame.pack();
        frame.revalidate();
        frame.repaint();

        resetGame();
    }

    // Method untuk styling label
    private void styleLabel(JLabel label) {
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 14));
    }

    // Method untuk styling button
    private void styleButton(JButton button) {
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(100, 30));
    }

    // Method untuk reset game state
    public void resetGame() {
        seconds = 0;
        moves = 0;
        updateTimerLabel();
        updateMovesLabel();
    }

    // Method untuk menambah hitungan langkah
    public void incrementMoves() {
        moves++;
        updateMovesLabel();
        playMoveSound();
    }

    // Method untuk update tampilan timer
    private void updateTimerLabel() {
        timerLabel.setText("Waktu: " + seconds + " detik");
    }

    // Method untuk update tampilan moves
    private void updateMovesLabel() {
        movesLabel.setText("Langkah: " + moves);
    }

    // Method untuk mengecek kondisi menang
    public void checkWin() {
        if (puzzleBoard.isComplete()) {
            timer.stop();
            JOptionPane.showMessageDialog(frame,
                    "Selamat! Anda menyelesaikan puzzle dalam " + seconds + " detik dan " + moves + " langkah!");
            resetGame();
        }
    }
}