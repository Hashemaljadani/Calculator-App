package com.hashim.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView inputNumbers;  // Display for current input
    private StringBuilder currentInput;  // User input tracker

    private double result = 0;   // Store current result
    private String operator = "";  // Store current operator
    private boolean isNewInput = true;  // Flag for new input after an operator or equals press

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        inputNumbers = findViewById(R.id.textView_input_numbers);
        currentInput = new StringBuilder();

        setNumberButtonListeners();
        setOperatorButtonListeners();
        setSpecialButtonListeners();
    }

    // Number Buttons
    private void setNumberButtonListeners() {
        int[] numberButtonIds = {
                R.id.button_zero, R.id.button_one, R.id.button_two,
                R.id.button_three, R.id.button_four, R.id.button_five,
                R.id.button_six, R.id.button_seven, R.id.button_eight,
                R.id.button_nine, R.id.button_dot
        };

        for (int id : numberButtonIds) {
            Button button = findViewById(id);
            button.setOnClickListener(v -> {
                if (isNewInput) {
                    currentInput.setLength(0);
                    isNewInput = false;
                }
                currentInput.append(button.getText().toString());
                inputNumbers.setText(currentInput.toString());
            });
        }
    }

    // Operators
    private void setOperatorButtonListeners() {
        int[] operatorButtonIds = {
                R.id.button_addition, R.id.button_subtraction,
                R.id.button_multiplication, R.id.button_division
        };

        for (int id : operatorButtonIds) {
            Button button = findViewById(id);
            button.setOnClickListener(v -> {
                if (currentInput.length() > 0) {
                    performCalculation();
                    operator = button.getText().toString();
                    isNewInput = true;
                }
            });
        }

        Button equalsButton = findViewById(R.id.button_equal);
        equalsButton.setOnClickListener(v -> {
            if (currentInput.length() > 0) {
                performCalculation();
                operator = "";
                isNewInput = true;
            }
        });
    }

    // Special Buttons (Clear and Modulus)
    private void setSpecialButtonListeners() {
        Button clearButton = findViewById(R.id.button_clear);
        clearButton.setOnClickListener(v -> clearInput());

        Button percentButton = findViewById(R.id.button_percent);
        percentButton.setOnClickListener(v -> {
            if (currentInput.length() > 0 && !operator.isEmpty()) {
                try {
                    double inputValue = Double.parseDouble(currentInput.toString());

                    // Perform modulus operation if an operator exists
                    result = result % inputValue;
                    inputNumbers.setText(String.valueOf(result));

                    currentInput.setLength(0);
                    currentInput.append(result);
                    operator = ""; // Reset operator after modulus
                    isNewInput = true;
                } catch (NumberFormatException e) {
                    inputNumbers.setText("Error");
                    currentInput.setLength(0);
                }
            }
        });
    }

    // Perform Calculation
    private void performCalculation() {
        double inputValue = currentInput.length() > 0 ? Double.parseDouble(currentInput.toString()) : 0;

        switch (operator) {
            case "+":
                result += inputValue;
                break;
            case "-":
                result -= inputValue;
                break;
            case "x":
                result *= inputValue;
                break;
            case "÷":
                if (inputValue != 0) {
                    result /= inputValue;
                } else {
                    inputNumbers.setText("Error: Division by 0");
                    clearInput();
                    return;
                }
                break;
            default:
                result = inputValue;
                break;
        }

        inputNumbers.setText(String.valueOf(result));
        currentInput.setLength(0);
        currentInput.append(result);
    }

    // Clear Input
    private void clearInput() {
        currentInput.setLength(0);
        result = 0;
        operator = "";
        inputNumbers.setText("");
        isNewInput = true;
    }
}
