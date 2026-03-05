package com.mycompany.librarymanager;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class MainFrame extends JFrame {
    private JTable booksTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JLabel statsLabel;
    private List<Book> books;
    private JComboBox<String> filterCombo;
    private final String DATA_FILE = "library.dat";
    
    public MainFrame() {
        setTitle("Библиотека - Учёт книг");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        
        books = new ArrayList<>();

        initComponents();
        layoutComponents();
        loadBooks();
        refreshTable();
        updateStats();
    }
    
    private void initComponents() {
        String[] columns = {"Название", "Автор", "Жанр", "Год", "Статус", "Рейтинг"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        booksTable = new JTable(tableModel);
        booksTable.setFont(new Font("Arial", Font.PLAIN, 12));
        booksTable.setRowHeight(25);
        booksTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 12));
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                filterBooks();
            }
        });
        
        String[] filters = {"Все книги", "Прочитанные", "Непрочитанные", "Хочу прочитать"};
        filterCombo = new JComboBox<>(filters);
        filterCombo.setFont(new Font("Arial", Font.PLAIN, 12));
        filterCombo.addActionListener(e -> filterBooks());
        
        statsLabel = new JLabel("Всего книг: 0");
        statsLabel.setFont(new Font("Arial", Font.BOLD, 12));
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout(5, 5));
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBackground(new Color(240, 240, 240));
        topPanel.add(new JLabel("Поиск:"));
        topPanel.add(searchField);
        topPanel.add(new JLabel("Фильтр:"));
        topPanel.add(filterCombo);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        buttonPanel.setBackground(new Color(240, 240, 240));
        
        JButton addButton = new JButton("Добавить");
        JButton editButton = new JButton("Изменить");
        JButton deleteButton = new JButton("Удалить");
        JButton statusButton = new JButton("Статус");
        
        addButton.addActionListener(e -> addBook());
        editButton.addActionListener(e -> editBook());
        deleteButton.addActionListener(e -> deleteBook());
        statusButton.addActionListener(e -> changeStatus());
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(statusButton);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(240, 240, 240));
        bottomPanel.add(buttonPanel, BorderLayout.WEST);
        bottomPanel.add(statsLabel, BorderLayout.EAST);
        
        JScrollPane scrollPane = new JScrollPane(booksTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        
        setupMenu();
    }
    
    private void setupMenu() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu fileMenu = new JMenu("Файл");
        JMenuItem saveItem = new JMenuItem("Сохранить");
        JMenuItem loadItem = new JMenuItem("Загрузить");
        JMenuItem exitItem = new JMenuItem("Выход");
        
        saveItem.addActionListener(e -> saveBooks());
        loadItem.addActionListener(e -> loadBooks());
        exitItem.addActionListener(e -> System.exit(0));
        
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        JMenu helpMenu = new JMenu("Помощь");
        JMenuItem aboutItem = new JMenuItem("О программе");
        aboutItem.addActionListener(e -> showAbout());
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    private void addBook() {
        BookDialog dialog = new BookDialog(this, "Добавление книги", null);
        dialog.setVisible(true);
        
        Book book = dialog.getBook();
        if (book != null) {
            books.add(book);
            refreshTable();
            saveBooks();
            updateStats();
        }
    }
    
    private void editBook() {
        int row = booksTable.getSelectedRow();
        if (row >= 0) {
            Book book = books.get(row);
            BookDialog dialog = new BookDialog(this, "Редактирование книги", book);
            dialog.setVisible(true);
            
            Book updated = dialog.getBook();
            if (updated != null) {
                books.set(row, updated);
                refreshTable();
                saveBooks();
                updateStats();
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Выберите книгу для редактирования");
        }
    }
    
    private void deleteBook() {
        int row = booksTable.getSelectedRow();
        if (row >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Удалить выбранную книгу?",
                "Подтверждение",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                books.remove(row);
                refreshTable();
                saveBooks();
                updateStats();
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Выберите книгу для удаления");
        }
    }
    
    private void changeStatus() {
        int row = booksTable.getSelectedRow();
        if (row >= 0) {
            Book book = books.get(row);
            String[] statuses = {"Прочитана", "Не прочитана", "Хочу прочитать"};
            
            String newStatus = (String) JOptionPane.showInputDialog(
                this,
                "Выберите статус:",
                "Статус книги",
                JOptionPane.PLAIN_MESSAGE,
                null,
                statuses,
                book.getStatus());
            
            if (newStatus != null) {
                book.setStatus(newStatus);
                refreshTable();
                saveBooks();
                updateStats();
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Выберите книгу");
        }
    }
    
    private void filterBooks() {
        String searchText = searchField.getText().toLowerCase();
        String filter = (String) filterCombo.getSelectedItem();
        
        tableModel.setRowCount(0);
        
        for (Book book : books) {
            boolean matchesSearch = searchText.isEmpty() ||
                book.getTitle().toLowerCase().contains(searchText) ||
                book.getAuthor().toLowerCase().contains(searchText);
            
            boolean matchesFilter = filter.equals("Все книги") ||
                (filter.equals("Прочитанные") && book.getStatus().equals("Прочитана")) ||
                (filter.equals("Непрочитанные") && book.getStatus().equals("Не прочитана")) ||
                (filter.equals("Хочу прочитать") && book.getStatus().equals("Хочу прочитать"));
            
            if (matchesSearch && matchesFilter) {
                tableModel.addRow(new Object[]{
                    book.getTitle(),
                    book.getAuthor(),
                    book.getGenre(),
                    book.getYear(),
                    book.getStatus(),
                    book.getRating() > 0 ? book.getRating() : "-"
                });
            }
        }
    }
    
    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Book book : books) {
            tableModel.addRow(new Object[]{
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.getYear(),
                book.getStatus(),
                book.getRating() > 0 ? book.getRating() : "-"
            });
        }
    }
    
    private void updateStats() {
        int total = books.size();
        long read = books.stream().filter(b -> b.getStatus().equals("Прочитана")).count();
        long unread = books.stream().filter(b -> b.getStatus().equals("Не прочитана")).count();
        long want = books.stream().filter(b -> b.getStatus().equals("Хочу прочитать")).count();
        
        statsLabel.setText(String.format(
            "Всего: %d | Прочитано: %d | Не прочитано: %d | В планах: %d",
            total, read, unread, want));
    }
    
    private void saveBooks() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(DATA_FILE))) {
            oos.writeObject(books);
            JOptionPane.showMessageDialog(this, "Сохранено");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "Ошибка сохранения: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    private void loadBooks() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(file))) {
                books = (List<Book>) ois.readObject();
                refreshTable();
                updateStats();
                JOptionPane.showMessageDialog(this, "Данные загружены");
            } catch (IOException | ClassNotFoundException e) {
                JOptionPane.showMessageDialog(this, 
                    "Ошибка загрузки: " + e.getMessage());
            }
        }
    }
    
    private void showAbout() {
        JOptionPane.showMessageDialog(this,
            "Библиотека v1.0\n\n" +
            "Программа для учёта книг:\n" +
            "- Добавление и редактирование\n" +
            "- Поиск и фильтрация\n" +
            "- Статусы прочтения\n" +
            "- Автосохранение",
            "О программе",
            JOptionPane.INFORMATION_MESSAGE);
    }
}