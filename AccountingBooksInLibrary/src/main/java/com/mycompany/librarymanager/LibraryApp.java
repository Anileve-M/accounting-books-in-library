package com.mycompany.librarymanager;

import javax.swing.*;
import java.awt.*;

public class LibraryApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {}
            
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}