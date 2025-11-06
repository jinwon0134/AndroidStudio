package com.example.calculator;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    EditText et_expression;
    Button btn_0, btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9;
    Button btn_plus, btn_minus, btn_multiply, btn_divide, btn_percent, btn_dot;
    Button btn_clear, btn_equal, btn_left_paren, btn_right_paren; // 추가

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_expression = findViewById(R.id.et_expression);

        // 숫자 버튼 초기화
        btn_0 = findViewById(R.id.btn_0);
        btn_1 = findViewById(R.id.btn_1);
        btn_2 = findViewById(R.id.btn_2);
        btn_3 = findViewById(R.id.btn_3);
        btn_4 = findViewById(R.id.btn_4);
        btn_5 = findViewById(R.id.btn_5);
        btn_6 = findViewById(R.id.btn_6);
        btn_7 = findViewById(R.id.btn_7);
        btn_8 = findViewById(R.id.btn_8);
        btn_9 = findViewById(R.id.btn_9);

        // 연산 버튼 초기화
        btn_plus = findViewById(R.id.btn_plus);
        btn_minus = findViewById(R.id.btn_minus);
        btn_multiply = findViewById(R.id.btn_multiply);
        btn_divide = findViewById(R.id.btn_slash);
        btn_percent = findViewById(R.id.btn_percent);
        btn_dot = findViewById(R.id.btn_dot);

        // 괄호 버튼 추가
        btn_left_paren = findViewById(R.id.btn_left_paren);
        btn_right_paren = findViewById(R.id.btn_right_paren);

        // 기능 버튼
        btn_clear = findViewById(R.id.btn_reset);
        btn_equal = findViewById(R.id.btn_equal);

        // 입력 처리
        setInput(btn_0, "0");
        setInput(btn_1, "1");
        setInput(btn_2, "2");
        setInput(btn_3, "3");
        setInput(btn_4, "4");
        setInput(btn_5, "5");
        setInput(btn_6, "6");
        setInput(btn_7, "7");
        setInput(btn_8, "8");
        setInput(btn_9, "9");
        setInput(btn_dot, ".");
        setInput(btn_plus, "+");
        setInput(btn_minus, "-");
        setInput(btn_multiply, "*");
        setInput(btn_divide, "/");
        setInput(btn_percent, "%");
        setInput(btn_left_paren, "(");   // 추가
        setInput(btn_right_paren, ")");  // 추가

        // C 버튼
        btn_clear.setOnClickListener(v -> et_expression.setText(""));

        // = 버튼 → 계산
        btn_equal.setOnClickListener(v -> {
            String expr = et_expression.getText().toString();
            try {
                double result = evaluate(expr);
                et_expression.setText(String.valueOf(result));
            } catch (Exception e) {
                et_expression.setText("Error");
            }
        });
    }

    // 버튼 입력 처리
    private void setInput(Button button, String value) {
        button.setOnClickListener(v -> {
            String current = et_expression.getText().toString();
            et_expression.setText(current + value);
        });
    }

    // 수식 계산
    private double evaluate(String expression) {
        String postfix = infixToPostfix(expression); // 중위 -> 후위 변환
        return evalPostfix(postfix); // 후위 계산
    }

    // 연산자 우선순위
    private int precedence(char op) {
        if (op == '+' || op == '-') return 1;
        if (op == '*' || op == '/' || op == '%') return 2;
        return -1;
    }

    // ✅ 괄호 처리 추가된 중위 → 후위 변환
    private String infixToPostfix(String exp) {
        StringBuilder result = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < exp.length(); i++) {
            char c = exp.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                result.append(c);
            }
            else if (c == '(') {
                stack.push(c);
            }
            else if (c == ')') {
                result.append(" ");
                while (!stack.isEmpty() && stack.peek() != '(') {
                    result.append(stack.pop()).append(" ");
                }
                if (!stack.isEmpty() && stack.peek() == '(')
                    stack.pop(); // '(' 제거
            }
            else { // 연산자
                result.append(" ");
                while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(c)) {
                    result.append(stack.pop()).append(" ");
                }
                stack.push(c);
            }
        }

        while (!stack.isEmpty()) {
            result.append(" ").append(stack.pop());
        }

        return result.toString();
    }

    // 후위표기식 계산
    private double evalPostfix(String exp) {
        Stack<Double> stack = new Stack<>();
        String[] tokens = exp.trim().split("\\s+");

        for (String token : tokens) {
            if (token.matches("-?\\d+(\\.\\d+)?")) {
                stack.push(Double.parseDouble(token));
            } else {
                double b = stack.pop();
                double a = stack.pop();
                switch (token) {
                    case "+": stack.push(a + b); break;
                    case "-": stack.push(a - b); break;
                    case "*": stack.push(a * b); break;
                    case "/": stack.push(a / b); break;
                    case "%": stack.push(a % b); break;
                }
            }
        }
        return stack.pop();
    }
}
