package calculator;

import javax.swing.*;
import java.awt.*;

public class SwingView extends JFrame implements View {

    private JTextField display;
    private EventHandler handler;

    private JPanel panel;

    // Configuración
    private ConfigLoader config;

    public SwingView(EventHandler handler) {
        this.handler = handler;
        this.config = new ConfigLoader();

        initUI();
    }

    private void initUI() {

        // 🔤 Fuente desde config
        String fontName = config.get("font.name", "Arial");
        int fontSize = Integer.parseInt(config.get("font.size", "16"));
        Font font = new Font(fontName, Font.PLAIN, fontSize);

        // 🎨 Colores desde config
        Color bgColor = Color.decode(config.get("bg.color", "#000000"));
        Color buttonColor = Color.decode(config.get("button.color", "#cccccc"));
        Color textColor = Color.decode(config.get("text.color", "#ffffff"));

        setTitle("Calculator");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(bgColor);

        // 📟 Display
        display = new JTextField("0");
        display.setEditable(false);
        display.setFont(font);
        display.setBackground(bgColor);
        display.setForeground(textColor);
        display.setHorizontalAlignment(JTextField.RIGHT);

        panel.add(display, BorderLayout.NORTH);

        // 🔢 Panel de botones
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(5, 4, 5, 5));
        buttons.setBackground(bgColor);

        // 🔘 Crear botones
        String[] btnLabels = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "=", "+",
                "CE", "⌫"
        };

        for (String text : btnLabels) {
            JButton button = new JButton(text);
            button.setFont(font);
            button.setBackground(buttonColor);
            button.setForeground(textColor);

            button.addActionListener(e -> handleButton(text));

            buttons.add(button);
        }

        panel.add(buttons, BorderLayout.CENTER);

        add(panel);
        setVisible(true);
    }

    // 🎯 Manejo de botones
    private void handleButton(String text) {

        if (text.matches("[0-9]")) {
            handler.onDigitPressed(Integer.parseInt(text));
        } else if (text.equals(".")) {
            handler.onDecimalPointPressed();
        } else if (text.equals("+")) {
            handler.onAddPressed();
        } else if (text.equals("-")) {
            handler.onSubtractPressed();
        } else if (text.equals("*")) {
            handler.onMultiplyPressed();
        } else if (text.equals("/")) {
            handler.onDividePressed();
        } else if (text.equals("=")) {
            handler.onEqualsPressed();
        } else if (text.equals("CE")) {
            handler.onClearPressed();
        } else if (text.equals("⌫")) {
            handler.onBackspacePressed(); // 👈 NUEVO
        }
    }

    // 📟 Métodos nuevos para controlador
    @Override
    public String getDisplayText() {
        return display.getText();
    }

    @Override
    public void setDisplayText(String text) {
        display.setText(text);
    }

    // (si ya tienes este método, mantenlo)
    @Override
    public void showResult(String result) {
        display.setText(result);
    }
}