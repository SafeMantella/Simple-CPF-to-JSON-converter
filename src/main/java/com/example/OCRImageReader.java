package com.example;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class OCRImageReader extends JFrame {
    private JTextArea textArea;
    private JButton loadButton;
    private JLabel imageLabel;
    private JPanel imagePanel;
    private final Tesseract tesseract;

    public OCRImageReader() {
        // Configurar janela
        setTitle("OCR Image Reader");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(800, 600);

        tesseract = new Tesseract();
        tesseract.setDatapath("/usr/share/tesseract-ocr/4.00/tessdata/");
        tesseract.setLanguage("por+eng"); 

        createComponents();
        layoutComponents();
        setLocationRelativeTo(null);
    }

    private void createComponents() {
        textArea = new JTextArea();
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        
        loadButton = new JButton("Carregar Imagem");
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadImage();
            }
        });

        imagePanel = new JPanel();
        imagePanel.setLayout(new BorderLayout());
        imageLabel = new JLabel();
        imagePanel.add(imageLabel, BorderLayout.CENTER);
    }

    private void layoutComponents() {
        JPanel topPanel = new JPanel();
        topPanel.add(loadButton);
        add(topPanel, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            new JScrollPane(imagePanel),
            new JScrollPane(textArea)
        );
        splitPane.setResizeWeight(0.5);
        add(splitPane, BorderLayout.CENTER);
    }

    private String getTextAsJson(String text) {
        String[] lines = text.split("\n");
        StringBuilder json = new StringBuilder("{\n");
        //percorre todas as linhas do texto
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            
            if (line.contains(".") && line.contains("-")) { //CPF
                String[] parts = line.split(" ");
                for (int j = 0; j < parts.length; j++) {
                    if (parts[j].contains(".") && parts[j].contains("-")) {
                        json.append("\t\"cpf\": \"").append(parts[j]).append("\",\n");
                    }
                }
            }

            if (line.toLowerCase().contains("nome")) {
                line = lines[i + 1];
                String[] parts = line.split(" ");
                String nome = "";
                for (int j = 0; j < parts.length; j++) {
                    if (parts[j].equals(parts[j].toUpperCase()) && parts[j].matches("[a-zA-Z]+")) {
                        nome += parts[j] + " ";
                    }
                }
                nome = nome.trim();
                json.append("\t\"nome\": \"").append(nome).append("\",\n");
            }

            if (line.contains("/")) { //data de nascimento
                String[] parts = line.split(" ");
                for (int j = 0; j < parts.length; j++) {
                    if (parts[j].contains("/")) {
                        json.append("\t\"data_nascimento\": \"").append(parts[j]).append("\",\n");
                    }
                }
            }
        }

        json.append("\n}");

        return json.toString();
    }
    

    private void loadImage() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                ImageIcon imageIcon = new ImageIcon(selectedFile.getPath());
                Image image = imageIcon.getImage().getScaledInstance(
                    400, -1, Image.SCALE_SMOOTH
                );
                imageLabel.setIcon(new ImageIcon(image));
                
                String text = tesseract.doOCR(selectedFile);
                String json = getTextAsJson(text);

                textArea.setText(json); // Exibir texto extraído
                
            } catch (TesseractException ex) {
                JOptionPane.showMessageDialog(
                    this,
                    "Erro ao processar a imagem: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            OCRImageReader app = new OCRImageReader();
            app.setVisible(true);
        });
    }
}