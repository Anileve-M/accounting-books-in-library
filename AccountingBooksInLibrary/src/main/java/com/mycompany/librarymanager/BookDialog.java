package com.mycompany.librarymanager;

import javax.swing.*;
import java.awt.*;
import java.time.Year;

public class BookDialog extends JDialog {
    private JTextField titleField;
    private JTextField authorField;
    private JTextField genreField;
    private JTextField yearField;
    private JComboBox<String> statusCombo;
    private JTextField ratingField;
    private Book book;
    private boolean saved = false;
    
    public BookDialog(Frame owner, String title, Book existingBook) {
        super(owner, title, true);
        this.book = existingBook != null ? existingBook : 
            new Book("", "", "", Year.now().getValue());
        
        setSize(400, 300);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        
        initComponents();
        layoutComponents();
        loadBookData();
    }
    
    private void initComponents() {
        titleField = new JTextField(20);
        authorField = new JTextField(20);
        genreField = new JTextField(20);
        yearField = new JTextField(20);
        
        String[] statuses = {"Прочитана", "Не прочитана", "Хочу прочитать"};
        statusCombo = new JComboBox<>(statuses);
        
        ratingField = new JTextField(20);
    }
    
    private void layoutComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 3, 3, 3);
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Название:"), gbc);
        gbc.gridx = 1;
        panel.add(titleField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Автор:"), gbc);
        gbc.gridx = 1;
        panel.add(authorField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Жанр:"), gbc);
        gbc.gridx = 1;
        panel.add(genreField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Год:"), gbc);
        gbc.gridx = 1;
        panel.add(yearField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Статус:"), gbc);
        gbc.gridx = 1;
        panel.add(statusCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Рейтинг (0-10):"), gbc);
        gbc.gridx = 1;
        panel.add(ratingField, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton saveButton = new JButton("Сохранить");
        saveButton.addActionListener(e -> saveBook());
        
        JButton cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void loadBookData() {
        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        genreField.setText(book.getGenre());
        yearField.setText(String.valueOf(book.getYear()));
        statusCombo.setSelectedItem(book.getStatus());
        ratingField.setText(book.getRating() > 0 ? String.valueOf(book.getRating()) : "");
    }
    
    private void saveBook() {
        if (titleField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Введите название");
            return;
        }
        
        book.setTitle(titleField.getText().trim());
        book.setAuthor(authorField.getText().trim());
        book.setGenre(genreField.getText().trim());
        
        try {
            int year = Integer.parseInt(yearField.getText().trim());
            book.setYear(year);
        } catch (NumberFormatException e) {
            book.setYear(Year.now().getValue());
        }
        
        book.setStatus((String) statusCombo.getSelectedItem());
        
        try {
            double rating = Double.parseDouble(ratingField.getText().trim());
            if (rating >= 0 && rating <= 10) {
                book.setRating(rating);
            }
        } catch (NumberFormatException e) {
            book.setRating(0);
        }
        
        saved = true;
        dispose();
    }
    
    public Book getBook() {
        return saved ? book : null;
    }
}