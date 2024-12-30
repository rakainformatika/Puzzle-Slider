// PuzzleTile.java
import javax.swing.*;
import java.awt.*;

public class PuzzleTile extends JButton {
    private static final Color TILE_COLOR = new Color(100, 149, 237);
    private static final Font TILE_FONT = new Font("Arial", Font.BOLD, 24);

    public PuzzleTile(String text) {
        super(text);
        setFont(TILE_FONT);
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (!getText().isEmpty()) {
            Graphics2D g2d = (Graphics2D) g.create();
            int width = getWidth();
            int height = getHeight();

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Main tile background
            g2d.setColor(TILE_COLOR);
            g2d.fillRoundRect(0, 0, width, height, 15, 15);

            // Highlight effect
            g2d.setColor(TILE_COLOR.brighter());
            g2d.drawRoundRect(0, 0, width - 1, height - 1, 15, 15);

            // Shadow effect
            g2d.setColor(TILE_COLOR.darker());
            g2d.drawRoundRect(1, 1, width - 2, height - 2, 15, 15);

            g2d.dispose();
        }
        super.paintComponent(g);
    }
}