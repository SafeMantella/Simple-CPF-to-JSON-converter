# Simple CPF to JSON Converter

A Java application that uses Tesseract OCR to extract CPF numbers from images and convert them to JSON format. The application provides a simple graphical interface where users can load images containing CPF numbers and get the formatted JSON output.

## Features

- Load images containing CPF numbers through a file picker
- Display the loaded image
- Extract CPF numbers using Tesseract OCR
- Convert extracted CPFs to JSON format
- Split-pane interface showing both image and JSON output
- Real-time processing

## Requirements

- Java 17 or higher
- Maven
- Tesseract OCR

### Installing Requirements on Ubuntu

```bash
# Install Java
sudo apt install openjdk-17-jdk

# Install Maven
sudo apt install maven

# Install Tesseract and required libraries
sudo apt install tesseract-ocr
sudo apt install libtesseract-dev
sudo apt install tesseract-ocr-por
```

## Building and Running

1. Clone the repository:
```bash
git clone [repository-url]
cd Simple-CPF-to-JSON-converter
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn exec:java -Dexec.mainClass="com.example.OCRImageReader"
```

## Usage

1. Launch the application
2. Click the "Load Image" button
3. Select an image file containing CPF numbers
4. The image will be displayed on the left side
5. The JSON formatted output will appear on the right side

## Dependencies

- Tess4J (Tesseract for Java)
- SLF4J (Logging)

## Notes

- Supported image formats: PNG, JPEG, BMP, GIF
- Optimized for Brazilian CPF document numbers
- Requires Tesseract data files to be properly installed on the system
