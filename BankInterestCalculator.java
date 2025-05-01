import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class BankInterestCalculator extends JFrame {
    // Form components
    private JTextField nameField, idField, accountField, amountField;
    private JComboBox<String> accountTypeCombo;
    private JTextArea resultArea;
    private JButton calculateButton, printButton, clearButton;
    
    // Interest rates (example rates - adjust for Sri Lankan banks)
    private final double SAVINGS_RATE = 0.05; // 5% annual
    private final double FIXED_DEPOSIT_RATE = 0.085; // 8.5% annual
    
    public BankInterestCalculator() {
        setTitle("Sri Lanka Bank Interest Calculator");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        
        // Add components to form panel
        formPanel.add(new JLabel("Customer Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);
        
        formPanel.add(new JLabel("NIC/ID Number:"));
        idField = new JTextField();
        formPanel.add(idField);
        
        formPanel.add(new JLabel("Account Number:"));
        accountField = new JTextField();
        formPanel.add(accountField);
        
        formPanel.add(new JLabel("Account Type:"));
        String[] accountTypes = {"Savings Account", "Fixed Deposit"};
        accountTypeCombo = new JComboBox<>(accountTypes);
        formPanel.add(accountTypeCombo);
        
        formPanel.add(new JLabel("Deposit Amount (LKR):"));
        amountField = new JTextField();
        formPanel.add(amountField);
        
        // Add form panel to main panel
        mainPanel.add(formPanel, BorderLayout.NORTH);
        
        // Result area
        resultArea = new JTextArea(10, 40);
        resultArea.setEditable(false);
        resultArea.setBorder(BorderFactory.createTitledBorder("Interest Calculation Results"));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        calculateButton = new JButton("Calculate Interest");
        printButton = new JButton("Print Statement");
        clearButton = new JButton("Clear");
        
        buttonPanel.add(calculateButton);
        buttonPanel.add(printButton);
        buttonPanel.add(clearButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add action listeners
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateInterest();
            }
        });
        
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printStatement();
            }
        });
        
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });
        
        // Add main panel to frame
        add(mainPanel);
    }
    
    private void calculateInterest() {
        try {
            // Get input values
            String name = nameField.getText().trim();
            String id = idField.getText().trim();
            String accountNo = accountField.getText().trim();
            String accountType = (String) accountTypeCombo.getSelectedItem();
            double amount = Double.parseDouble(amountField.getText().trim());
            
            if (name.isEmpty() || id.isEmpty() || accountNo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all customer details", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Deposit amount must be positive", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Calculate interest
            double annualRate = accountType.equals("Savings Account") ? SAVINGS_RATE : FIXED_DEPOSIT_RATE;
            double monthlyRate = annualRate / 12;
            double monthlyInterest = amount * monthlyRate;
            double totalAmount = amount + monthlyInterest;
            
            // Format numbers for display
            DecimalFormat df = new DecimalFormat("#,##0.00");
            
            // Display results
            resultArea.setText("");
            resultArea.append("Customer Name: " + name + "\n");
            resultArea.append("NIC/ID: " + id + "\n");
            resultArea.append("Account Number: " + accountNo + "\n");
            resultArea.append("Account Type: " + accountType + "\n");
            resultArea.append("Deposit Amount: LKR " + df.format(amount) + "\n");
            resultArea.append("Annual Interest Rate: " + (annualRate * 100) + "%\n");
            resultArea.append("Monthly Interest Rate: " + (monthlyRate * 100) + "%\n");
            resultArea.append("--------------------------------\n");
            resultArea.append("Monthly Interest: LKR " + df.format(monthlyInterest) + "\n");
            resultArea.append("Total Amount After Interest: LKR " + df.format(totalAmount) + "\n");
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid deposit amount", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void printStatement() {
        if (resultArea.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please calculate interest first", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // Print the result area content
            resultArea.print();
            
            // Alternatively, you could create a more formal printable document
            JOptionPane.showMessageDialog(this, "Statement sent to printer", "Print", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error printing statement: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearForm() {
        nameField.setText("");
        idField.setText("");
        accountField.setText("");
        amountField.setText("");
        accountTypeCombo.setSelectedIndex(0);
        resultArea.setText("");
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // Set look and feel to system default for better appearance
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                BankInterestCalculator calculator = new BankInterestCalculator();
                calculator.setVisible(true);
            }
        });
    }
}