import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Class representing the ATM machine
class ATM {
    private BankAccount bankAccount;

    public ATM(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public void withdraw(int amount) {
        if (amount <= 0) {
            showErrorMessage("Invalid amount for withdrawal.");
            return;
        }

        if (bankAccount.getBalance() >= amount) {
            bankAccount.withdraw(amount);
            showInfoMessage("Withdrawal successful. Current balance: " + bankAccount.getBalance());
        } else {
            showErrorMessage("Insufficient balance for withdrawal.");
        }
    }

    public void deposit(int amount) {
        if (amount <= 0) {
            showErrorMessage("Invalid amount for deposit.");
            return;
        }

        bankAccount.deposit(amount);
        showInfoMessage("Deposit successful. Current balance: " + bankAccount.getBalance());
    }

    public int checkBalance() {
        return bankAccount.getBalance();
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showInfoMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}

// Class representing the user's bank account
class BankAccount {
    private int balance;

    public BankAccount() {
        balance = 0;
    }

    public int getBalance() {
        return balance;
    }

    public void deposit(int amount) {
        balance += amount;
    }

    public void withdraw(int amount) {
        balance -= amount;
    }
}

// ATM GUI class
class ATMGUI extends JFrame {
    private ATM atm;

    // GUI components
    private JTextField amountField;
    private JTextArea balanceArea;

    public ATMGUI(ATM atm) {
        this.atm = atm;
        setTitle("ATM Machine");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        inputPanel.add(new JLabel("Enter Amount:"));
        amountField = new JTextField(10);
        inputPanel.add(amountField);

        JButton withdrawButton = new JButton("Withdraw");
        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleWithdraw();
            }
        });
        inputPanel.add(withdrawButton);

        JButton depositButton = new JButton("Deposit");
        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleDeposit();
            }
        });
        inputPanel.add(depositButton);

        add(inputPanel, BorderLayout.NORTH);

        balanceArea = new JTextArea(5, 20);
        balanceArea.setEditable(false);
        updateBalanceArea();
        JScrollPane scrollPane = new JScrollPane(balanceArea);
        add(scrollPane, BorderLayout.CENTER);

        // Add padding to the input panel
        javax.swing.border.EmptyBorder inputPanelPadding = new javax.swing.border.EmptyBorder(20, 20, 20, 20);
        inputPanel.setBorder(inputPanelPadding);

        
    }

    private void handleWithdraw() {
        try {
            int amount = Integer.parseInt(amountField.getText());
            atm.withdraw(amount);
            updateBalanceArea();
        } catch (NumberFormatException ex) {
            showErrorMessage("Invalid amount.");
        }
    }

    private void handleDeposit() {
        try {
            int amount = Integer.parseInt(amountField.getText());
            atm.deposit(amount);
            updateBalanceArea();
        } catch (NumberFormatException ex) {
            showErrorMessage("Invalid amount.");
        }
    }

    private void updateBalanceArea() {
        balanceArea.setText("Current Balance: " + atm.checkBalance());
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}

public class atmm {
    public static void main(String[] args) {
        BankAccount bankAccount = new BankAccount();
        ATM atm = new ATM(bankAccount);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ATMGUI gui = new ATMGUI(atm);
                gui.setVisible(true);
            }
        });
    }
}
