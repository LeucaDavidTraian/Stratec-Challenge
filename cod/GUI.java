import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class GUI {
    private JTextField inputFilePathField1;
    private JTextArea outputArea;

    // Predefined C++ file path, you have to change it manually for every cpp if you didn't download it as in the ReadMe
    private static final String CPP_FILE_PATH = "\"C:\\cod\\Executables\\stage1_escape_velocity.exe\"";  // Windows

    public GUI() {
        JFrame frame = new JFrame("Stage1:Planetary escape velocity");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));

        // text pannels
        panel.add(new JLabel("Enter the path to the planetary data file:"));
        inputFilePathField1 = new JTextField();
        panel.add(inputFilePathField1);

        frame.add(panel, BorderLayout.NORTH);

        // Make the text area scrollable
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Bottom panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JButton runButton = new JButton("Run C++ Program");
        JButton newWindowButton = new JButton("Next step");

        buttonPanel.add(runButton);
        buttonPanel.add(newWindowButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputFilePath1 = inputFilePathField1.getText();
                runCppProgram(inputFilePath1);
            }
        });

        newWindowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(Stage2::new); // Open the second step
            }
        });

        frame.setVisible(true);
    }

    private void runCppProgram(String inputFilePath1) {
        String exeFile = "program.exe"; // Windows
        
        String compileCommand = "g++ " + CPP_FILE_PATH + " -o " + exeFile;

        outputArea.setText(""); // Clear previous output

        if (!runCommand(compileCommand)) {
            outputArea.append("Compilation failed.\n");
            return;
        }

        // Execute the compiled C++ program
        String executeCommand = exeFile + " " + inputFilePath1;
        runCommand(executeCommand);
    }

    private boolean runCommand(String command) {
        try {
            ProcessBuilder builder = new ProcessBuilder();
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                builder.command("cmd.exe", "/c", command);
            } else {
                builder.command("sh", "-c", command);
            }

            builder.redirectErrorStream(true);
            Process process = builder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    outputArea.append(line + "\n");
                }
            }

            process.waitFor();
            return process.exitValue() == 0;
        } catch (Exception e) {
            outputArea.append("Error: " + e.getMessage() + "\n");
            return false;
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }
}

 /// ----------------STage2 Window--------------identical mostly but with two text fields(this will be a theme for the rest of the windows)
     class Stage2 {
        private JTextField inputFilePathField1;
        private JTextField inputFilePathField2;
        private JTextArea outputArea;
    
        
        private static final String CPP_FILE_PATH = "\"C:\\cod\\Executables\\Stage2.exe\"";  // Windows if downloaded correctly
    
        public Stage2() {
            JFrame frame = new JFrame("Stage2:Escape times");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(600, 400);
            frame.setLayout(new BorderLayout());
    
            JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
    
            panel.add(new JLabel("Enter the path to the planetary data file:"));
            inputFilePathField1 = new JTextField();
            panel.add(inputFilePathField1);
    
            panel.add(new JLabel("Enter the path of the Rocket data file:"));
            inputFilePathField2 = new JTextField();
            panel.add(inputFilePathField2);
    
            frame.add(panel, BorderLayout.NORTH);
    
            // Make the text area scrollable
            outputArea = new JTextArea();
            outputArea.setEditable(false);
            outputArea.setLineWrap(true);
            outputArea.setWrapStyleWord(true);
            JScrollPane scrollPane = new JScrollPane(outputArea);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            frame.add(scrollPane, BorderLayout.CENTER);
    
            // Bottom panel for buttons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
    
            JButton runButton = new JButton("Run C++ Program");
            JButton newWindowButton = new JButton("Next step");
    
            buttonPanel.add(runButton);
            buttonPanel.add(newWindowButton);
    
            frame.add(buttonPanel, BorderLayout.SOUTH);
    
            runButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String inputFilePath1 = inputFilePathField1.getText();
                    String inputFilePath2 = inputFilePathField2.getText();
                    runCppProgram(inputFilePath1, inputFilePath2);
                }
            });
    
            newWindowButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SwingUtilities.invokeLater(Stage3::new); 
                }
            });
    
            frame.setVisible(true);
        }
    
        private void runCppProgram(String inputFilePath1, String inputFilePath2) {
            String exeFile = "program.exe"; // Windows
            
            String compileCommand = "g++ " + CPP_FILE_PATH + " -o " + exeFile;
    
            outputArea.setText(""); // Clear previous output
    
            if (!runCommand(compileCommand)) {
                outputArea.append("Compilation failed.\n");
                return;
            }
    
            // Execute the compiled C++ program with two input files
            String executeCommand = exeFile + " " + inputFilePath1 + " " + inputFilePath2;
            runCommand(executeCommand);
        }
    
        private boolean runCommand(String command) {
            try {
                ProcessBuilder builder = new ProcessBuilder();
                if (System.getProperty("os.name").toLowerCase().contains("win")) {
                    builder.command("cmd.exe", "/c", command);
                } else {
                    builder.command("sh", "-c", command);
                }
    
                builder.redirectErrorStream(true);
                Process process = builder.start();
    
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        outputArea.append(line + "\n");
                    }
                }
    
                process.waitFor();
                return process.exitValue() == 0;
            } catch (Exception e) {
                outputArea.append("Error: " + e.getMessage() + "\n");
                return false;
            }
        }
    }

    //-------------------Step3 ------------------------
    class Stage3 {
        private JTextField inputFilePathField1;
        private JTextField inputFilePathField2;
        private JTextField inputFilePathField3;
        private JTextArea outputArea;
    
       
        private static final String CPP_FILE_PATH = "\"C:\\cod\\Executables\\stage3final.exe\"";
       
    
        public Stage3() {
            JFrame frame = new JFrame("Stage3:Static travel");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(600, 400);
            frame.setLayout(new BorderLayout());
    
            JPanel panel = new JPanel(new GridLayout(3, 3, 5, 5));
    
            panel.add(new JLabel("Enter the path to the planetary data file:"));
            inputFilePathField1 = new JTextField();
            panel.add(inputFilePathField1);
    
            panel.add(new JLabel("Enter the path of the Rocket data file:"));
            inputFilePathField2 = new JTextField();
            panel.add(inputFilePathField2);

            panel.add(new JLabel("Enter the path of the orbit data file:"));
            inputFilePathField3 = new JTextField();
            panel.add(inputFilePathField3);
    
            frame.add(panel, BorderLayout.NORTH);
    
            // Making the text area scrollable
            outputArea = new JTextArea();
            outputArea.setEditable(false);
            outputArea.setLineWrap(true);
            outputArea.setWrapStyleWord(true);
            JScrollPane scrollPane = new JScrollPane(outputArea);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            frame.add(scrollPane, BorderLayout.CENTER);
    
            // Bottom panel for buttons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
    
            JButton runButton = new JButton("Run C++ Program");
            JButton newWindowButton = new JButton("Next Step");
    
            buttonPanel.add(runButton);
            buttonPanel.add(newWindowButton);
    
            frame.add(buttonPanel, BorderLayout.SOUTH);
    
            runButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String inputFilePath1 = inputFilePathField1.getText();
                    String inputFilePath2 = inputFilePathField2.getText();
                    String inputFilePath3 = inputFilePathField3.getText();
                    runCppProgram(inputFilePath1, inputFilePath2, inputFilePath3);
                }
            });
    
            newWindowButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SwingUtilities.invokeLater(Stage4::new);
                }
            });
    
            frame.setVisible(true);
        }
    
        private void runCppProgram(String inputFilePath1, String inputFilePath2, String inputFilePath3) {
            String exeFile = "program.exe"; // Windows
            // String exeFile = "./program"; // Linux/macOS
            String compileCommand = "g++ " + CPP_FILE_PATH + " -o " + exeFile;
    
            outputArea.setText(""); // Clear previous output
    
            if (!runCommand(compileCommand)) {
                outputArea.append("Compilation failed.\n");
                return;
            }
    
            // Execute the compiled C++ program with three input files(3 in * mea)
            String executeCommand = exeFile + " " + inputFilePath1 + " " + inputFilePath2 + " " + inputFilePath3;
            runCommand(executeCommand);
        }
    
        private boolean runCommand(String command) {
            try {
                ProcessBuilder builder = new ProcessBuilder();
                if (System.getProperty("os.name").toLowerCase().contains("win")) {
                    builder.command("cmd.exe", "/c", command);
                } else {
                    builder.command("sh", "-c", command);
                }
    
                builder.redirectErrorStream(true);
                Process process = builder.start();
    
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        outputArea.append(line + "\n");
                    }
                }
    
                process.waitFor();
                return process.exitValue() == 0;
            } catch (Exception e) {
                outputArea.append("Error: " + e.getMessage() + "\n");
                return false;
            }
        }
    }
    //-----------------------Stage 4------------------------- nothing new around here
    class Stage4 {
        private JTextField inputFilePathField1;
        private JTextField inputFilePathField2;
        private JTextArea outputArea;
        
        private static final String CPP_FILE_PATH = "\"C:\\cod\\Executables\\stage4att2.exe\"";  // Windows
    
        public Stage4() {
            JFrame frame = new JFrame("Stage4: Simplified travel");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(600, 400);
            frame.setLayout(new BorderLayout());
    
            JPanel panel = new JPanel(new GridLayout(2, 3, 5, 5));
    
            panel.add(new JLabel("Enter the path to the planetary data file:"));
            inputFilePathField1 = new JTextField();
            panel.add(inputFilePathField1);
    
            panel.add(new JLabel("Enter the number of days elapsed: "));
            inputFilePathField2 = new JTextField();
            panel.add(inputFilePathField2);

            frame.add(panel, BorderLayout.NORTH);
    
            // Making the text area scrollable
            outputArea = new JTextArea();
            outputArea.setEditable(false);
            outputArea.setLineWrap(true);
            outputArea.setWrapStyleWord(true);
            JScrollPane scrollPane = new JScrollPane(outputArea);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            frame.add(scrollPane, BorderLayout.CENTER);
    
            // Bottom panel for buttons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
    
            JButton runButton = new JButton("Run C++ Program");
            JButton newWindowButton = new JButton("Next Step");
    
            buttonPanel.add(runButton);
            buttonPanel.add(newWindowButton);
    
            frame.add(buttonPanel, BorderLayout.SOUTH);
    
            runButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String inputFilePath1 = inputFilePathField1.getText();
                    String inputFilePath2 = inputFilePathField1.getText();
                    runCppProgram(inputFilePath1, inputFilePath2);
                }
            });
    
            newWindowButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SwingUtilities.invokeLater(Stage5::new); 
                }
            });
    
            frame.setVisible(true);
        }
    
        private void runCppProgram(String inputFilePath1, String inputFilePath2) {
            String exeFile = "program.exe"; // Windows
            // String exeFile = "./program"; // Linux/macOS
            String compileCommand = "g++ " + CPP_FILE_PATH + " -o " + exeFile;
    
            outputArea.setText(""); // Clear previous output
    
            if (!runCommand(compileCommand)) {
                outputArea.append("Compilation failed.\n");
                return;
            }
    
            // Execute the compiled C++ program with two input files
            String executeCommand = exeFile + " " + inputFilePath1;
            runCommand(executeCommand);
        }
    
        private boolean runCommand(String command) {
            try {
                ProcessBuilder builder = new ProcessBuilder();
                if (System.getProperty("os.name").toLowerCase().contains("win")) {
                    builder.command("cmd.exe", "/c", command);
                } else {
                    builder.command("sh", "-c", command);
                }
    
                builder.redirectErrorStream(true);
                Process process = builder.start();
    
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        outputArea.append(line + "\n");
                    }
                }
    
                process.waitFor();
                return process.exitValue() == 0;
            } catch (Exception e) {
                outputArea.append("Error: " + e.getMessage() + "\n");
                return false;
            }
        }
    }
    // --------------------------Stage 5---------------------------------
    class Stage5 {
        private JTextField inputFilePathField1;
        private JTextField inputFilePathField2;
        private JTextField inputFilePathField3;
        private JTextArea outputArea;
    
        // Predefined C++ file path (Update this to your actual path if needed)
        private static final String CPP_FILE_PATH = "\"C:\\cod\\Executables\\stage5att.exe\"";  // Windows
    
        public Stage5() {
            JFrame frame = new JFrame("Stage5:Simplified interplanetary travel");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(600, 400);
            frame.setLayout(new BorderLayout());
    
            JPanel panel = new JPanel(new GridLayout(3, 3, 5, 5));
    
            panel.add(new JLabel("Enter the path to the planetary data file:"));
            inputFilePathField1 = new JTextField();
            panel.add(inputFilePathField1);
    
            panel.add(new JLabel("Enter the path of the Rocket data file:"));
            inputFilePathField2 = new JTextField();
            panel.add(inputFilePathField2);

            panel.add(new JLabel("Enter the path of the orbit data file: "));
            inputFilePathField3 = new JTextField();
            panel.add(inputFilePathField3);
    
            frame.add(panel, BorderLayout.NORTH);
    
            // Making the text area scrollable
            outputArea = new JTextArea();
            outputArea.setEditable(false);
            outputArea.setLineWrap(true);
            outputArea.setWrapStyleWord(true);
            JScrollPane scrollPane = new JScrollPane(outputArea);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            frame.add(scrollPane, BorderLayout.CENTER);
    
            // Bottom panel for buttons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
    
            JButton runButton = new JButton("Run C++ Program");
            JButton newWindowButton = new JButton("Next Step");
    
            buttonPanel.add(runButton);
            buttonPanel.add(newWindowButton);
    
            frame.add(buttonPanel, BorderLayout.SOUTH);
    
            runButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String inputFilePath1 = inputFilePathField1.getText();
                    String inputFilePath2 = inputFilePathField2.getText();
                    String inputFilePath3 = inputFilePathField3.getText();
                    runCppProgram(inputFilePath1, inputFilePath2, inputFilePath3);
                }
            });
    
            newWindowButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SwingUtilities.invokeLater(Stage6::new); 
                }
            });
    
            frame.setVisible(true);
        }
    
        private void runCppProgram(String inputFilePath1, String inputFilePath2, String inputFilePath3) {
            String exeFile = "program.exe"; // Windows
            // String exeFile = "./program"; // Linux/macOS
            String compileCommand = "g++ " + CPP_FILE_PATH + " -o " + exeFile;
    
            outputArea.setText(""); // Clear previous output
    
            if (!runCommand(compileCommand)) {
                outputArea.append("Compilation failed.\n");
                return;
            }
    
            // Execute the compiled C++ program with two input files
            String executeCommand = exeFile + " " + inputFilePath1 + " " + inputFilePath2 + " " + inputFilePath3;
            runCommand(executeCommand);
        }
    
        private boolean runCommand(String command) {
            try {
                ProcessBuilder builder = new ProcessBuilder();
                if (System.getProperty("os.name").toLowerCase().contains("win")) {
                    builder.command("cmd.exe", "/c", command);
                } else {
                    builder.command("sh", "-c", command);
                }
    
                builder.redirectErrorStream(true);
                Process process = builder.start();
    
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        outputArea.append(line + "\n");
                    }
                }
    
                process.waitFor();
                return process.exitValue() == 0;
            } catch (Exception e) {
                outputArea.append("Error: " + e.getMessage() + "\n");
                return false;
            }
        }
    }
    //-------------------------------Stage 6------------------------------------
    class Stage6 {
        private JTextField inputFilePathField1;
        private JTextField inputFilePathField2;
        private JTextField inputFilePathField3;
        private JTextArea outputArea;
    
        private static final String CPP_FILE_PATH = "\"C:\\cod\\Executables\\stage6fisrttry.exe\"";  // Windows
    
        public Stage6() {
            JFrame frame = new JFrame("Stage6:Interplanetary travel");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(600, 400);
            frame.setLayout(new BorderLayout());
    
            JPanel panel = new JPanel(new GridLayout(3, 3, 5, 5));
    
            panel.add(new JLabel("Enter the path to the planetary data file:"));
            inputFilePathField1 = new JTextField();
            panel.add(inputFilePathField1);
    
            panel.add(new JLabel("Enter the path of the Rocket data fil: "));
            inputFilePathField2 = new JTextField();
            panel.add(inputFilePathField2);

            panel.add(new JLabel("Enter the path of the orbit data file: "));
            inputFilePathField3 = new JTextField();
            panel.add(inputFilePathField3);
    
            frame.add(panel, BorderLayout.NORTH);
    
            // Making the text area scrollable
            outputArea = new JTextArea();
            outputArea.setEditable(false);
            outputArea.setLineWrap(true);
            outputArea.setWrapStyleWord(true);
            JScrollPane scrollPane = new JScrollPane(outputArea);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            frame.add(scrollPane, BorderLayout.CENTER);
    
            // Bottom panel for buttons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
    
            JButton runButton = new JButton("Run C++ Program");
    
            buttonPanel.add(runButton);
    
            frame.add(buttonPanel, BorderLayout.SOUTH);
    
            runButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String inputFilePath1 = inputFilePathField1.getText();
                    String inputFilePath2 = inputFilePathField2.getText();
                    String inputFilePath3 = inputFilePathField3.getText();
                    runCppProgram(inputFilePath1, inputFilePath2, inputFilePath3);
                }
            });
    
            frame.setVisible(true);
        }
    
        private void runCppProgram(String inputFilePath1, String inputFilePath2, String inputFilePath3) {
            String exeFile = "program.exe"; // Windows
            // String exeFile = "./program"; // Linux/macOS
            String compileCommand = "g++ " + CPP_FILE_PATH + " -o " + exeFile;
    
            outputArea.setText(""); // Clear previous output
    
            if (!runCommand(compileCommand)) {
                outputArea.append("Compilation failed.\n");
                return;
            }
    
            // Execute the compiled C++ program with two input files
            String executeCommand = exeFile + " " + inputFilePath1 + " " + inputFilePath2 + " " + inputFilePath3;
            runCommand(executeCommand);
        }
    
        private boolean runCommand(String command) {
            try {
                ProcessBuilder builder = new ProcessBuilder();
                if (System.getProperty("os.name").toLowerCase().contains("win")) {
                    builder.command("cmd.exe", "/c", command);
                } else {
                    builder.command("sh", "-c", command);
                }
    
                builder.redirectErrorStream(true);
                Process process = builder.start();
    
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        outputArea.append(line + "\n");
                    }
                }
    
                process.waitFor();
                return process.exitValue() == 0;
            } catch (Exception e) {
                outputArea.append("Error: " + e.getMessage() + "\n");
                return false;
            }
        }
    }