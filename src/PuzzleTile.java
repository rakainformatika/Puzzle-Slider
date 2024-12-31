import javax.swing.*;  // Import untuk komponen GUI Swing
import java.awt.*;    // Import untuk komponen grafis dan layout

// Class PuzzleTile merupakan custom JButton yang digunakan untuk setiap tile dalam puzzle
public class PuzzleTile extends JButton {
    // Mendefinisikan warna default untuk tile (biru cornflower)
    private static final Color TILE_COLOR = new Color(100, 149, 237);
    // Mendefinisikan font default untuk angka pada tile
    private static final Font TILE_FONT = new Font("Arial", Font.BOLD, 24);

    // Constructor untuk membuat tile baru dengan text tertentu
    public PuzzleTile(String text) {
        super(text);  // Memanggil constructor parent (JButton)
        setFont(TILE_FONT);  // Mengatur font tile
        setForeground(Color.WHITE);  // Mengatur warna text menjadi putih
        setFocusPainted(false);  // Menghilangkan efek focus
        setBorderPainted(false);  // Menghilangkan border
        setContentAreaFilled(false);  // Menghilangkan area isi default
        setOpaque(false);  // Membuat button transparan
    }

    // Override method paintComponent untuk kustomisasi tampilan tile
    @Override
    protected void paintComponent(Graphics g) {
        // Hanya menggambar jika tile memiliki text (bukan tile kosong)
        if (!getText().isEmpty()) {
            // Membuat objek Graphics2D untuk pengaturan rendering yang lebih baik
            Graphics2D g2d = (Graphics2D) g.create();
            // Mengaktifkan anti-aliasing untuk gambar yang lebih halus
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Menggambar latar belakang tile dengan sudut membulat
            g2d.setColor(TILE_COLOR);
            g2d.fillRoundRect(2, 2, getWidth()-4, getHeight()-4, 15, 15);

            // Menambahkan efek gradien untuk memberikan kesan 3D
            GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(255, 255, 255, 50),  // Warna atas (putih transparan)
                    0, getHeight(), new Color(0, 0, 0, 25)  // Warna bawah (hitam transparan)
            );
            g2d.setPaint(gradient);
            g2d.fillRoundRect(2, 2, getWidth()-4, getHeight()-4, 15, 15);

            // Membersihkan resource graphics
            g2d.dispose();
        }
        super.paintComponent(g);  // Memanggil paintComponent parent untuk menggambar text
    }

    // Override method getPreferredSize untuk mengatur ukuran tile secara dinamis
    @Override
    public Dimension getPreferredSize() {
        Container parent = getParent();  // Mendapatkan container parent
        if (parent != null) {
            // Menghitung ukuran tile berdasarkan ukuran parent
            int size = Math.min(parent.getWidth(), parent.getHeight()) / 5;
            return new Dimension(size - 10, size - 10);  // Mengurangi 10 pixel untuk margin
        }
        return super.getPreferredSize();  // Mengembalikan ukuran default jika tidak ada parent
    }
}