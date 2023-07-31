import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class NumberGame extends JFrame {

    // Instance variables
    private int randomNumber;
    private int attempts;
    private int maxAttempts;
    private int rounds;
    private int roundsWon;
    private boolean gameInProgress;

    // GUI components
    private JTextField guessField;
    private JLabel instructionLabel;
    private JLabel marqueeLabel;
    private JButton startButton;
    private JButton submitButton;
    private JLabel feedbackLabel;
    private JLabel scoreLabel;
    private JButton newRoundButton;

    // Marquee animation variables
    private Timer marqueeTimer;
    private int marqueePosition;

    public NumberGame() {
        setTitle("Number Guessing Game");
        setSize(800, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Get the dimensions of the full screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) (screenSize.width * 0.5);
        int screenHeight = (int) (screenSize.height * 0.5);

        // Center the window on the screen
        setLocation((screenSize.width - screenWidth) / 2, (screenSize.height - screenHeight) / 2);

        setLayout(new GridBagLayout());

        // Create GUI components
        marqueeLabel = new JLabel("********** Number Guessing Game! **********");
        marqueeLabel.setForeground(Color.BLUE);
        marqueeLabel.setFont(new Font("Arial", Font.BOLD, 25));
        add(marqueeLabel, createGridBagConstraints(0, 0, 3, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH));

        instructionLabel = new JLabel("<html><br>Instructions: Guess the number between 1 and 100. There will be 7 chances to enter your guess. "
                + "After completed the chances, your score will be displayed. If you want to play again, click on the New Round button.<br></html>");
        instructionLabel.setForeground(Color.RED);
        instructionLabel.setFont(new Font("Arial", Font.BOLD, 13));
        add(instructionLabel, createGridBagConstraints(0, 1, 3, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH));

        guessField = new JTextField(8);
        add(new JLabel("<html>Enter your guess:</html> "), createGridBagConstraints(0, 2, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH));
        add(guessField, createGridBagConstraints(1, 2, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH));
        guessField.setFont(new Font("Arial", Font.BOLD, 15));

        submitButton = new JButton("Submit");
        add(submitButton, createGridBagConstraints(0, 3, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH));
        submitButton.setFont(new Font("Arial", Font.BOLD, 15));
        submitButton.setEnabled(false);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkGuess();
            }
        });

        newRoundButton = new JButton("New Round");
        add(newRoundButton, createGridBagConstraints(1, 3, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH));
        newRoundButton.setFont(new Font("Arial", Font.BOLD, 15));
        newRoundButton.setEnabled(false);
        newRoundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startNewRound();
            }
        });

        feedbackLabel = new JLabel("");
        add(feedbackLabel, createGridBagConstraints(0, 4, 2, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH));

        scoreLabel = new JLabel("Score: 0");
        add(scoreLabel, createGridBagConstraints(0, 5, 2, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH));

        startButton = new JButton("Start Game");
        add(startButton, createGridBagConstraints(0, 6, 2, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH));
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        setupGame(); // Initialize game settings

        // Initialize marquee animation variables
        marqueePosition = 0;
        marqueeTimer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                marqueePosition++;
                if (marqueePosition > getWidth()) {
                    marqueePosition = -marqueeLabel.getPreferredSize().width;
                }
                marqueeLabel.setLocation(marqueePosition, marqueeLabel.getY());
            }
        });
        marqueeTimer.start();
    }

    private void setupGame() {
        rounds = 0;
        roundsWon = 0;
        maxAttempts = 7;
        gameInProgress = false;
        scoreLabel.setText("Score: 0");
        newRoundButton.setEnabled(false);
    }

    private void startGame() {
        marqueeLabel.setVisible(true); //  marquee text after starting the game
        instructionLabel.setVisible(false); // Show instructions before starting the game
        startButton.setEnabled(false); // Disable the Start Game button
        startButton.setVisible(false); //show start button before starting the game
        submitButton.setEnabled(true); // Enable the Submit button for user input
        startNewRound(); // Start a new round
    }

    private void startNewRound() {
        // Generate a new random number for the current round
        randomNumber = generateRandomNumber(1, 100);

        // Reset attempts and feedback for the new round
        attempts = 0;
        feedbackLabel.setText("");

        // Clear the input field
        guessField.setText("");

        // Enable game in progress
        gameInProgress = true;

        // Enable the Submit button for user input
        submitButton.setEnabled(true);

        // Disable the New Round button until the current round is over
        newRoundButton.setEnabled(false);

        // Increment the round counter
        rounds++;
    }

    private void checkGuess() {
        try {
            int userGuess = Integer.parseInt(guessField.getText());
            attempts++;

            if (userGuess == randomNumber) {
                // User guessed the correct number
                gameInProgress = false;
                feedbackLabel.setText("Congratulations! You guessed the number in " + attempts + " attempts.");
                roundsWon++;
                scoreLabel.setText("Score: " + roundsWon);
                submitButton.setEnabled(false);
                newRoundButton.setEnabled(true);
            } else if (userGuess < randomNumber) {
                // User's guess is too low
                feedbackLabel.setText("Try again. Your guess is too low.");
            } else {
                // User's guess is too high
                feedbackLabel.setText("Try again. Your guess is too high.");
            }

            if (attempts == maxAttempts && gameInProgress) {
                // User has reached the maximum number of attempts
                gameInProgress = false;
                feedbackLabel.setText("You have reached the maximum number of attempts. The correct number was: " + randomNumber);
                submitButton.setEnabled(false);
                newRoundButton.setEnabled(true);
            }
        } catch (NumberFormatException e) {
            // Invalid input, user did not enter a valid number
            feedbackLabel.setText("Invalid input. Please enter a valid number.");
        }
    }

    private int generateRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    private GridBagConstraints createGridBagConstraints(int gridx, int gridy, int gridwidth, int gridheight, int anchor, int fill) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;
        gbc.anchor = anchor;
        gbc.fill = fill;
        gbc.insets = new Insets(12, 12, 12, 12); // Add some padding
        return gbc;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new NumberGame().setVisible(true);
            }
        });
    }
}
