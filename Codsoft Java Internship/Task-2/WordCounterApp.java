import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class WordCounterApp extends JFrame {

    private JTextArea inputTextArea;
    private JButton countButton;
    private JLabel resultLabel;
    private JCheckBox ignoreCommonWordsCheckbox;

    private List<String> commonWords = Arrays.asList(
            "the", "and", "a", "an", "in", "on", "is", "are", "it", "of", "to"
    );

    public WordCounterApp() {
        setTitle("Word Counter");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set border for the content pane of the JFrame
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        ((JComponent) contentPane).setBorder(new EmptyBorder(20, 20, 20, 20)); // Add space around the frame

        // Create GUI components
        JTextArea instructionLabel = new JTextArea("Instructions:\n\n"
                + "1. Enter the text you want to count words for in the provided text area.\n"
                + "2. Click on the 'Count Words' button to get the total count of words.\n"
                + "3. Check the 'Ignore Common Words' option to exclude common words from counting.\n"
                + "4. The application will display the word count and additional statistics (unique words and frequency).\n");
        instructionLabel.setEditable(false);
        instructionLabel.setBackground(this.getBackground());
        contentPane.add(instructionLabel, BorderLayout.NORTH);

        inputTextArea = new JTextArea(100, 50);
        JScrollPane scrollPane = new JScrollPane(inputTextArea);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0)); // Add space at the top of the button panel
        countButton = new JButton("Count Words");
        countButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                countWords();
            }
        });
        buttonPanel.add(countButton);

        ignoreCommonWordsCheckbox = new JCheckBox("Ignore Common Words", true);
        buttonPanel.add(ignoreCommonWordsCheckbox);

        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        resultLabel = new JLabel("Word Count: ");
        resultLabel.setBorder(new EmptyBorder(0, 10, 0, 0)); // Add space at the left of the result label
        contentPane.add(resultLabel, BorderLayout.WEST);
    }

    private void countWords() {
        String inputText = inputTextArea.getText().trim();

        if (inputText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter text to count words.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Split the input text into an array of words using space or punctuation as delimiters
        String[] words = inputText.split("\\s+|\\p{Punct}");

        // Initialize a counter variable to keep track of the number of words
        int wordCount = 0;

        // Map to store word frequency
        Map<String, Integer> wordFrequencyMap = new HashMap<>();

        // Iterate through the array of words and increment the counter for each word encountered
        for (String word : words) {
            word = word.toLowerCase(); // Convert to lowercase for case-insensitive comparison

            if (!word.isEmpty()) {
                if (ignoreCommonWordsCheckbox.isSelected() && commonWords.contains(word)) {
                    continue; // Ignore common words
                }

                wordCount++;

                // Update word frequency
                int frequency = wordFrequencyMap.getOrDefault(word, 0);
                wordFrequencyMap.put(word, frequency + 1);
            }
        }

        // Display the total count of words to the user
        resultLabel.setText("Word Count: " + wordCount);

        // Display additional statistics if available
        if (!wordFrequencyMap.isEmpty()) {
            StringBuilder statistics = new StringBuilder("<html>Additional Statistics:<br>");
            statistics.append("Number of Unique Words: ").append(wordFrequencyMap.size()).append("<br>");

            // Sort words by frequency in descending order
            List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(wordFrequencyMap.entrySet());
            sortedEntries.sort(Map.Entry.<String, Integer>comparingByValue().reversed());

            for (Map.Entry<String, Integer> entry : sortedEntries) {
                statistics.append("Word '").append(entry.getKey()).append("': ").append(entry.getValue()).append(" occurrences<br>");
            }
            statistics.append("</html>");

            JOptionPane.showMessageDialog(this, statistics.toString(), "Word Statistics", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WordCounterApp().setVisible(true);
            }
        });
    }
}
