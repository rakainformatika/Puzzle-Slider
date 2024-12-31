import javax.swing.*;  // Import untuk komponen GUI Swing
import java.awt.*;    // Import untuk komponen AWT dasar

// Class utama yang berfungsi sebagai entry point aplikasi puzzle slider
public class PuzzleSlider {
    // Method main - titik awal eksekusi program
    public static void main(String[] args) {
        // SwingUtilities.invokeLater memastikan GUI dibuat di Event Dispatch Thread (EDT)
        // untuk menghindari masalah threading dan memastikan responsivitas UI
        SwingUtilities.invokeLater(() -> {
            try {
                // Mengatur tampilan aplikasi agar mengikuti look and feel sistem operasi
                // sehingga aplikasi terlihat native pada setiap platform
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Jika terjadi error saat mengatur look and feel,
                // error akan dicetak ke console namun aplikasi tetap berjalan
                e.printStackTrace();
            }
            // Membuat instance baru GameController yang akan menginisialisasi
            // dan mengelola seluruh komponen game
            new GameController();
        });
    }
}