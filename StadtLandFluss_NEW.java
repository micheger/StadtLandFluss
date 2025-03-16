import java.awt.*;
import java.util.HashSet; // HashSet importieren
import java.util.Random;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class StadtLandFluss {

    private JFrame frame;
    private JTable table;
    private JButton generateTopButton;
    private JButton generateBotButton;
    private Random random;
    private DefaultTableModel model; // Model als Instanzvariable
    private HashSet<Character> usedLetters = new HashSet<>(); // HashSet für verwendete Buchstaben hinzufügen
    private int i = 0;

    public StadtLandFluss() {
        frame = new JFrame("Stadt-Land-Fluss");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 300); // Größe erhöht für bessere Lesbarkeit
        random = new Random();

        // Tabelle mit nicht editierbarem "Buchstabe"-Feld
        String[] columns = {"Buchstabe", "Stadt", "Land", "Fluss", "Name", "Beruf"};
        model = new DefaultTableModel(columns, 0) { // Model initialisieren
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0; // Nur die erste Spalte (Buchstabe) ist nicht editierbar
            }
        };
        table = new JTable(model);

        // Initiale Zeile mit Buchstaben hinzufügen
        addNewRowWithLetter();

        // Buttons
        generateTopButton = new JButton("Neue Runde");
        generateBotButton = new JButton("Spiel beenden");
        generateTopButton.setFocusable(false); // Fokus entfernen, damit Button nicht immer selektiert ist
        generateBotButton.setFocusable(false); // Fokus entfernen

        // Layout
        JPanel topPanel = new JPanel();
        topPanel.add(generateTopButton);
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(generateBotButton);

        frame.setLayout(new BorderLayout());
        frame.add(new JScrollPane(table), BorderLayout.CENTER); // JScrollPane für Scrollbalken, falls viele Runden
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Event-Handler
        generateTopButton.addActionListener(e -> overflow(i));
        generateBotButton.addActionListener(e -> endRound());
    }

    private void addNewRowWithLetter() {
        setI(i);
        // DefaultTableModel model = (DefaultTableModel) table.getModel(); // Nicht jedes Mal neu holen, Instanzvariable nutzen
        model.addRow(new Object[]{"", "", "", "", "", ""});
        generateRandomLetter(model.getRowCount() - 1); // Buchstabe für die neue Zeile
    }

    private void generateRandomLetter(int row) {
        char randomChar;
        do {
            randomChar = (char) ('A' + random.nextInt(26)); // Zufälligen Buchstaben generieren
        } while (usedLetters.contains(randomChar)); // Wiederhole, falls Buchstabe bereits verwendet wurde

        setValueAt(table, randomChar, row, 0); // Buchstaben in die Tabelle setzen
        usedLetters.add(randomChar); // Neuen Buchstaben zu den verwendeten Buchstaben hinzufügen
    }

    private void setValueAt(JTable table, Object value, int row, int column) {
        // DefaultTableModel model = (DefaultTableModel) table.getModel(); // Nicht jedes Mal neu holen, Instanzvariable nutzen
        if (row >= 0 && row < model.getRowCount() && column >= 0 && column < model.getColumnCount()) { // Sicherstellen, dass row und column gültig sind, >= 0 hinzugefügt
            model.setValueAt(value, row, column);
        } else {
            System.err.println("Ungültige Zeile oder Spalte beim Setzen des Wertes: Zeile=" + row + ", Spalte=" + column); // Fehlerbehandlung falls ungültig
        }
    }

    private void endRound() {
        // Hier könntest du die Punkteberechnung implementieren
        JOptionPane.showMessageDialog(frame,
                "Spiel beendet! Überprüft eure Antworten:\n" +
                        getResultsAsText(),
                "Ergebnisse der Runde", // Titel angepasst für Klarheit
                JOptionPane.INFORMATION_MESSAGE
        );
        System.exit(0);
    }

    private String getResultsAsText() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < table.getRowCount(); row++) {
            sb.append("Runde ").append(row + 1).append(" (Buchstabe: ")
                    .append(table.getValueAt(row, 0)).append("):\n"); // Buchstabe explizit anzeigen
            sb.append(" Stadt: ").append(table.getValueAt(row, 1)).append("\n");
            sb.append(" Land: ").append(table.getValueAt(row, 2)).append("\n");
            sb.append(" Fluss: ").append(table.getValueAt(row, 3)).append("\n");
            sb.append(" Name: ").append(table.getValueAt(row, 4)).append("\n");
            sb.append(" Beruf: ").append(table.getValueAt(row, 5)).append("\n");
            sb.append("\n"); // Leerzeile zwischen den Runden
        }
        return sb.toString();
    }

    public void show() {
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StadtLandFluss().show());
    }

    public void setI(int i) {
        this.i = i+1;
    }
    public void overflow(int i) {
        if(i == 25) {
            endRound(); 
        } else {
            addNewRowWithLetter();
        }
        
    }
}