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
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Main tile background dengan bentuk yang lebih lembut
            g2d.setColor(TILE_COLOR);
            g2d.fillRoundRect(2, 2, getWidth()-4, getHeight()-4, 15, 15);

            // Efek cahaya halus
            GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(255, 255, 255, 50),
                    0, getHeight(), new Color(0, 0, 0, 25)
            );
            g2d.setPaint(gradient);
            g2d.fillRoundRect(2, 2, getWidth()-4, getHeight()-4, 15, 15);

            g2d.dispose();
        }
        super.paintComponent(g);
    }

    @Override
    public Dimension getPreferredSize() {
        Container parent = getParent();
        if (parent != null) {
            int size = Math.min(parent.getWidth(), parent.getHeight()) / 5;
            return new Dimension(size - 10, size - 10);
        }
        return super.getPreferredSize();
    }
}