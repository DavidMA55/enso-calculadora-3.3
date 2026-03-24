/**
 * @name        Calculator Controller class
 * @package     calculator
 * @file        Controller.java
 * @description Controller class that implements EventHandler interface to call suitable
 * model methods, and updates the View accordingly.
 */


package calculator;

import calculator.domain.BinaryOperatorModes;
import calculator.domain.UnaryOperatorModes;

public class Controller implements EventHandler {
    
    private final CalculatorModel model;
    private final View view;
    private StringBuilder displayBuffer;
    private boolean resetingInput = false;
    private boolean operatorPressed = false;

    public Controller(CalculatorModel model, View view) {
        this.model = model;
        this.view = view;
        this.displayBuffer = new StringBuilder();
        view.setActionListener(this);
    }
    
    @Override
    public void onNumberPressed(int number) {

        // After a user presses equals and gets a result, 
        // the next number press should start a new input
        if (resetingInput) {
            displayBuffer = new StringBuilder();
            view.clearDisplay();
            resetingInput = false;
        }

        displayBuffer.append(number);
        view.setDisplay(displayBuffer.toString());
        operatorPressed = false;
    }
    
    @Override
    public void onDecimalPressed() {

        // After a user presses equals and gets a result, 
        // the next decimal press should start a new input
        if (resetingInput) {
            displayBuffer = new StringBuilder();
            view.clearDisplay();
            resetingInput = false;
        }
       
        // Prevent multiple decimal separators in the current number
        if (!displayBuffer.toString().contains(",")) {
            // Handle leading decimal separator by prepending a "0"
            if (displayBuffer.length() == 0) {
                displayBuffer.append("0");
            }
            displayBuffer.append(",");
            view.setDisplay(displayBuffer.toString());
        }
    }
    
    @Override
    public void onExpPressed() {
        
        // After a user presses equals and gets a result, 
        // the next exp press should start a new input with "0E"
        if (resetingInput) {
            displayBuffer = new StringBuilder();
            view.clearDisplay();
            resetingInput = false;
        }
        
        // Prevent multiple 'E' in the current number (AC3)
        String currentBuffer = displayBuffer.toString().toUpperCase();
        if (!currentBuffer.contains("E")) {
            // Handle empty buffer or only negative sign by prepending "0"
            if (displayBuffer.length() == 0 || displayBuffer.toString().equals("-")) {
                displayBuffer.append("0");
            }
            displayBuffer.append("E");
            view.setDisplay(displayBuffer.toString());
        }
    }

    @Override
    public void onSpecialValuePressed(double value) {

        // Constants (pi, e) behave as a fresh numeric input.
        displayBuffer = new StringBuilder();
        view.clearDisplay();
        displayBuffer.append(Double.toString(value));
        view.setDisplay(displayBuffer.toString());
        resetingInput = false;
    }
    
    @Override
    public void onBinaryOperatorPressed(BinaryOperatorModes mode) {

        if (displayBuffer.length() > 0) {

            Double num = view.getDisplayValue();
            Double result = model.calculateBinary(mode, num);

            displayBuffer = new StringBuilder();
            if (result != null) {
                String formattedResult = formatResult(result);
                view.setDisplay(formattedResult);
                displayBuffer.append(formattedResult);
            }
            resetingInput = true;
            operatorPressed = true;
        }
    }
    
    @Override
    public void onUnaryOperatorPressed(UnaryOperatorModes mode) {

        if (displayBuffer.length() > 0) {

            Double num = view.getDisplayValue();
            Double result = model.calculateUnary(mode, num);

            displayBuffer = new StringBuilder();
            String formattedResult = formatResult(result);
            view.setDisplay(formattedResult);
            displayBuffer.append(formattedResult);
            resetingInput = true;
        }
    }
    
    @Override
    public void onEqualsPressed() {

        if (displayBuffer.length() > 0) {

            Double num = view.getDisplayValue();
            Double result = model.calculateEqual(num);

            displayBuffer = new StringBuilder();
            String formattedResult = formatResult(result);
            view.setDisplay(formattedResult);
            displayBuffer.append(formattedResult);
            resetingInput = true;
        }
    }
    
    @Override
    public void onClearPressed() {
        displayBuffer = new StringBuilder();
        model.reset();
        view.clearDisplay();
        resetingInput = false;
    }
    
    private String formatResult(Double result) {
        if (Double.isNaN(result)) {
            return "NaN";
        }
        else if (Double.isInfinite(result)) {
            if (result > 0) {
                return "Inf";
            } else {
                return "-Inf";
            }
        }
        else {
            String formatted = String.format(java.util.Locale.US, "%.10f", result);
            String normalized = formatted.replaceAll("0*$", "").replaceAll("\\.$", "");
            return normalized.replace('.', ',');
        }
    }

    @Override
    public void onBackspacePressed() {
        if (operatorPressed) return;

        String current = view.getDisplayText();

        if (current.length() <= 1) {
            view.setDisplayText("0");
        } else {
            view.setDisplayText(current.substring(0, current.length() - 1));
        }
    }
}
