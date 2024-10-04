import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class PasswordManager extends JFrame {
    private HashMap<String, String> passwordStore;
    private JTextField nameField;
    private JPasswordField passwordField;
    private JTextArea displayArea;
    private final String filename = "passwords.txt";

    public PasswordManager() {
        passwordStore = new HashMap<>();
        loadPasswords();  // Parolaları dosyadan yükle

        // Arayüz bileşenlerini oluştur
        JLabel nameLabel = new JLabel("Kullanıcı Adı:");
        nameField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Parola:");
        passwordField = new JPasswordField(20);

        JButton saveButton = new JButton("Kaydet");
        JButton displayButton = new JButton("Görüntüle");

        displayArea = new JTextArea(10, 30);
        displayArea.setEditable(false);

        // Butonlara olay dinleyicileri ekle
        saveButton.addActionListener(new SaveButtonListener());
        displayButton.addActionListener(new DisplayButtonListener());

        // Arayüzü oluştur
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2));
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(saveButton);
        panel.add(displayButton);

        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        setTitle("Parola Yöneticisi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadPasswords() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    passwordStore.put(parts[0], parts[1]);
                }
            }
        } catch (FileNotFoundException e) {
            // Dosya yoksa, yeni bir HashMap oluştur
            passwordStore = new HashMap<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void savePasswords() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Map.Entry<String, String> entry : passwordStore.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText();
            String password = new String(passwordField.getPassword());

            if (!name.isEmpty() && !password.isEmpty()) {
                passwordStore.put(name, password);
                savePasswords(); // Parolaları kaydet
                nameField.setText("");
                passwordField.setText("");
                JOptionPane.showMessageDialog(null, "Parola kaydedildi!");
            } else {
                JOptionPane.showMessageDialog(null, "Lütfen tüm alanları doldurun.");
            }
        }
    }

    private class DisplayButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            displayArea.setText("");
            for (Map.Entry<String, String> entry : passwordStore.entrySet()) {
                displayArea.append("Kullanıcı Adı: " + entry.getKey() + ", Parola: " + entry.getValue() + "\n");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PasswordManager::new);
    }
}
