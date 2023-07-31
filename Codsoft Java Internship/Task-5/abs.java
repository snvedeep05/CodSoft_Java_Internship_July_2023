import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Contact class to represent individual contacts
class Contact {
    private String name;
    private String phoneNumber;
    private String emailAddress;

    public Contact(String name, String phoneNumber, String emailAddress) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    // Getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Phone: " + phoneNumber + ", Email: " + emailAddress;
    }
}

// AddressBook class to manage the collection of contacts
class AddressBook {
    private List<Contact> contacts;
    private File dataFile; // The file to store contact data

    public AddressBook() {
        contacts = new ArrayList<>();
        dataFile = new File("contacts.txt");
        loadContacts(); // Load contacts from the file when the system is initialized
    }

    public void addContact(Contact contact) {
        contacts.add(contact);
    }

    public void removeContact(Contact contact) {
        contacts.remove(contact);
    }

    public Contact searchContact(String name) {
        for (Contact contact : contacts) {
            if (contact.getName().equalsIgnoreCase(name)) {
                return contact;
            }
        }
        return null;
    }

    public List<Contact> getAllContacts() {
        return contacts;
    }

    // Method to load contacts from the file
    private void loadContacts() {
        if (dataFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 3) {
                        String name = parts[0].trim();
                        String phoneNumber = parts[1].trim();
                        String emailAddress = parts[2].trim();
                        Contact contact = new Contact(name, phoneNumber, emailAddress);
                        contacts.add(contact);
                    }
                }
            } catch (IOException e) {
                // Error occurred while reading the file
                // You can handle the exception according to your requirement
                e.printStackTrace();
            }
        }
    }

    // Method to save contacts to the file
    public void saveContacts() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile))) {
            for (Contact contact : contacts) {
                writer.write(contact.getName() + "," + contact.getPhoneNumber() + "," + contact.getEmailAddress());
                writer.newLine();
            }
        } catch (IOException e) {
            // Error occurred while writing the file
            // You can handle the exception according to your requirement
            e.printStackTrace();
        }
    }
}

// AddressBookGUI class to create the GUI for interacting with the address book system
class AddressBookGUI extends JFrame {
    private AddressBook addressBook;

    // GUI components...
    private JTextField nameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextArea outputArea;

    public AddressBookGUI() {
        setTitle("Address Book System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        addressBook = new AddressBook();

        JPanel inputPanel = new JPanel(new GridLayout(6, 2));
        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField(20);
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Phone Number:"));
        phoneField = new JTextField(15);
        inputPanel.add(phoneField);

        inputPanel.add(new JLabel("Email Address:"));
        emailField = new JTextField(30);
        inputPanel.add(emailField);

        JButton addButton = new JButton("Add Contact");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addContact();
            }
        });
        inputPanel.add(addButton);

        JButton displayButton = new JButton("Display All Contacts");
        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayContacts();
            }
        });
        inputPanel.add(displayButton);

        JButton removeButton = new JButton("Remove Contact");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeContact();
            }
        });
        inputPanel.add(removeButton);

        JButton searchButton = new JButton("Search Contact");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchContact();
            }
        });
        inputPanel.add(searchButton);

        JButton editButton = new JButton("Edit Contact");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editContact();
            }
        });
        inputPanel.add(editButton);

        add(inputPanel, BorderLayout.NORTH);

        outputArea = new JTextArea(10, 30);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane, BorderLayout.CENTER);

         // Add padding to the input panel
        javax.swing.border.EmptyBorder inputPanelPadding = new javax.swing.border.EmptyBorder(30, 30, 30, 30);
        inputPanel.setBorder(inputPanelPadding);

        // Add padding to the output area
        javax.swing.border.EmptyBorder outputAreaPadding = new javax.swing.border.EmptyBorder(30, 30, 30, 30);
        outputArea.setBorder(outputAreaPadding);
    }

    // Method to add a contact
    private void addContact() {
        // Implementation for adding a contact
        // Use the addressBook to add the contact to the collection
        String name = nameField.getText();
        String phoneNumber = phoneField.getText();
        String emailAddress = emailField.getText();

        if (name.isEmpty() || phoneNumber.isEmpty() || emailAddress.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all the fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Contact contact = new Contact(name, phoneNumber, emailAddress);
        addressBook.addContact(contact);
        outputArea.append("Contact added: " + contact.toString() + "\n");
        clearInputFields();
    }

    // Method to display all contacts
    private void displayContacts() {
        // Implementation for displaying all contacts
        // Use the addressBook to get all contacts and show them in the outputArea
        List<Contact> contacts = addressBook.getAllContacts();
        outputArea.setText("");
        if (contacts.isEmpty()) {
            outputArea.append("No contacts found.\n");
        } else {
            for (Contact contact : contacts) {
                outputArea.append(contact.toString() + "\n");
            }
        }
    }

    // Method to remove a contact
    private void removeContact() {
        // Implementation for removing a contact
        String name = nameField.getText();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the name of the contact to remove.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Contact contactToRemove = addressBook.searchContact(name);
        if (contactToRemove != null) {
            addressBook.removeContact(contactToRemove);
            outputArea.append("Contact removed: " + contactToRemove.toString() + "\n");
            clearInputFields();
        } else {
            JOptionPane.showMessageDialog(this, "Contact not found with name: " + name, "Contact Not Found", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to search for a contact
    private void searchContact() {
        // Implementation for searching a contact
        String name = nameField.getText();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the name of the contact to search.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Contact contact = addressBook.searchContact(name);
        if (contact != null) {
            outputArea.setText("Contact found: " + contact.toString() + "\n");
        } else {
            outputArea.setText("Contact not found with name: " + name + "\n");
        }
    }

    // Method to edit an existing contact
    private void editContact() {
        // Implementation for editing a contact
        String name = nameField.getText();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the name of the contact to edit.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Contact contactToEdit = addressBook.searchContact(name);
        if (contactToEdit != null) {
            String phoneNumber = phoneField.getText();
            String emailAddress = emailField.getText();
            if (!phoneNumber.isEmpty()) {
                contactToEdit.setPhoneNumber(phoneNumber);
            }
            if (!emailAddress.isEmpty()) {
                contactToEdit.setEmailAddress(emailAddress);
            }
            outputArea.append("Contact edited: " + contactToEdit.toString() + "\n");
            clearInputFields();
        } else {
            JOptionPane.showMessageDialog(this, "Contact not found with name: " + name, "Contact Not Found", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearInputFields() {
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
    }

    // Method to get the AddressBook object
    public AddressBook getAddressBook() {
        return addressBook;
    }
}

public class abs {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                AddressBookGUI gui = new AddressBookGUI();
                gui.setVisible(true);

                // Save contacts to the file when the application is closed
                gui.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                        gui.getAddressBook().saveContacts();
                    }
                });
            }
        });
    }
}
