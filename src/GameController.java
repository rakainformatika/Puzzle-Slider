import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import java.io.File;

public class GameController {
    private JFrame frame;
    private PuzzleBoard puzzleBoard;
    private JLabel timerLabel, movesLabel;
    private JButton shuffleButton, solveButton;
    private JComboBox<String> sizeSelector;
    private Timer timer;
    private int seconds = 0;
    private int moves = 0;
    private JPanel mainPanel;
    private Clip moveSound;

    public GameController() {
        loadSound();
        initializeUI();
    }

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

    public void playMoveSound() {
        if (moveSound != null) {
            if (moveSound.isRunning()) {
                moveSound.stop();
            }
            moveSound.setFramePosition(0);
            moveSound.start();
        }
    }

    private void initializeUI() {
        frame = new JFrame("Puzzle Slider");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(400, 500));

        // Tambahkan ComponentListener untuk handle resize
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });

        mainPanel = createBackgroundPanel();
        frame.add(mainPanel);

        // Top Panel dengan Size Selector
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

        // Center Panel untuk Puzzle
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        puzzleBoard = new PuzzleBoard(4, this); // Default 4x4
        centerPanel.add(puzzleBoard);

        // Bottom Panel untuk Controls
        JPanel controlPanel = createControlPanel();

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        timer = new Timer(1000, e -> {
            seconds++;
            updateTimerLabel();
        });

        shuffleButton.addActionListener(e -> puzzleBoard.acakPuzzle());
        solveButton.addActionListener(e -> puzzleBoard.selesaikanPuzzle());

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        timer.start();
    }

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

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        controlPanel.setOpaque(false);

        timerLabel = new JLabel("Waktu: 0 detik");
        movesLabel = new JLabel("Langkah: 0");
        shuffleButton = new JButton("Acak");
        solveButton = new JButton("Selesaikan");

        styleLabel(timerLabel);
        styleLabel(movesLabel);
        styleButton(shuffleButton);
        styleButton(solveButton);

        controlPanel.add(timerLabel);
        controlPanel.add(movesLabel);
        controlPanel.add(shuffleButton);
        controlPanel.add(solveButton);

        return controlPanel;
    }

    private void changePuzzleSize() {
        String selected = (String) sizeSelector.getSelectedItem();
        int size = Character.getNumericValue(selected.charAt(0));

        // Hapus puzzle board lama
        Component[] components = ((JPanel)mainPanel.getComponent(1)).getComponents();
        for (Component comp : components) {
            if (comp instanceof PuzzleBoard) {
                ((JPanel)mainPanel.getComponent(1)).remove(comp);
                break;
            }
        }

        // Buat puzzle board baru
        puzzleBoard = new PuzzleBoard(size, this);
        ((JPanel)mainPanel.getComponent(1)).add(puzzleBoard);

        // Refresh frame
        frame.pack();
        frame.revalidate();
        frame.repaint();

        resetGame();
    }

    private void styleLabel(JLabel label) {
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 14));
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(100, 30));
    }

    public void resetGame() {
        seconds = 0;
        moves = 0;
        updateTimerLabel();
        updateMovesLabel();
    }

    public void incrementMoves() {
        moves++;
        updateMovesLabel();
        playMoveSound();
    }

    private void updateTimerLabel() {
        timerLabel.setText("Waktu: " + seconds + " detik");
    }

    private void updateMovesLabel() {
        movesLabel.setText("Langkah: " + moves);
    }

    public void checkWin() {
        if (puzzleBoard.isComplete()) {
            timer.stop();
            JOptionPane.showMessageDialog(frame,
                    "Selamat! Anda menyelesaikan puzzle dalam " + seconds + " detik dan " + moves + " langkah!");
            resetGame();
        }
    }
}
