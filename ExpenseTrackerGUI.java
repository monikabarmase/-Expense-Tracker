import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Expense {
    private String category;
    private double amount;

    public Expense(String category, double amount) {
        this.category = category;
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }
}

public class ExpenseTrackerGUI extends JFrame {
    private List<Expense> expenses;
    private DefaultListModel<String> expenseListModel;
    private JList<String> expenseList;
    private JTextField categoryField;
    private JTextField amountField;
    private JTextArea summaryArea;

    public ExpenseTrackerGUI() {
        expenses = new ArrayList<>();
        expenseListModel = new DefaultListModel<>();
        expenseList = new JList<>(expenseListModel);
        setupUI();
    }

    private void setupUI() {
        setTitle("Expense Tracker");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));

        inputPanel.add(new JLabel("Category:"));
        categoryField = new JTextField();
        inputPanel.add(categoryField);

        inputPanel.add(new JLabel("Amount:"));
        amountField = new JTextField();
        inputPanel.add(amountField);

        JButton addButton = new JButton("Add Expense");
        addButton.addActionListener(new AddExpenseListener());
        inputPanel.add(addButton);

        JButton deleteButton = new JButton("Delete Expense");
        deleteButton.addActionListener(new DeleteExpenseListener());
        inputPanel.add(deleteButton);

        add(inputPanel, BorderLayout.NORTH);

        // Expense List
        add(new JScrollPane(expenseList), BorderLayout.CENTER);

        // Summary Area
        summaryArea = new JTextArea();
        summaryArea.setEditable(false);
        add(new JScrollPane(summaryArea), BorderLayout.SOUTH);

        JButton summaryButton = new JButton("Display Summary");
        summaryButton.addActionListener(new SummaryListener());
        add(summaryButton, BorderLayout.EAST);

        setVisible(true);
    }

    private void updateExpenseList() {
        expenseListModel.clear();
        for (Expense expense : expenses) {
            expenseListModel.addElement(expense.getCategory() + ": " + expense.getAmount());
        }
    }

    private class AddExpenseListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String category = categoryField.getText();
            double amount;
            try {
                amount = Double.parseDouble(amountField.getText());
                expenses.add(new Expense(category, amount));
                categoryField.setText("");
                amountField.setText("");
                updateExpenseList();
                JOptionPane.showMessageDialog(null, "Expense added successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid amount. Please enter a valid number.");
            }
        }
    }

    private class DeleteExpenseListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedIndex = expenseList.getSelectedIndex();
            if (selectedIndex != -1) {
                expenses.remove(selectedIndex);
                updateExpenseList();
                JOptionPane.showMessageDialog(null, "Expense deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Please select an expense to delete.");
            }
        }
    }

    private class SummaryListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Map<String, Double> summary = new HashMap<>();
            for (Expense expense : expenses) {
                summary.put(expense.getCategory(),
                        summary.getOrDefault(expense.getCategory(), 0.0) + expense.getAmount());
            }

            StringBuilder summaryText = new StringBuilder("Expense Summary:\n");
            double total = 0;
            for (Map.Entry<String, Double> entry : summary.entrySet()) {
                summaryText.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                total += entry.getValue();
            }
            summaryText.append("Total Spending: ").append(total);
            summaryArea.setText(summaryText.toString());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ExpenseTrackerGUI::new);
    }
}
