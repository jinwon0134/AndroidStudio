package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    EditText et_expression;
    Button btn_0, btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9;
    Button btn_plus, btn_minus, btn_multiply, btn_divide, btn_percent, btn_dot;
    Button btn_clear, btn_equal;

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

        // 기능 버튼
        btn_clear = findViewById(R.id.btn_reset);
        btn_equal = findViewById(R.id.btn_equal); // xml에서 추가 필요 (= 버튼)

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
        // 1) 중위표기식 → 후위표기식 변환
        String postfix = infixToPostfix(expression);

        // 2) 후위표기식 계산
        return evalPostfix(postfix);
    }

    // 연산자 우선순위
    private int precedence(char op) {
        if (op == '+' || op == '-') return 1;
        if (op == '*' || op == '/' || op == '%') return 2;
        return -1;
    }

    // 중위 → 후위 변환 (Shunting-yard)
    private String infixToPostfix(String exp) {
        StringBuilder result = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < exp.length(); i++) {
            char c = exp.charAt(i);

            // 숫자일 경우
            if (Character.isDigit(c) || c == '.') {
                result.append(c);
            }
            // 연산자일 경우
            else {
                result.append(" "); // 숫자와 연산자 구분
                while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(c)) {
                    result.append(stack.pop()).append(" ");
                }
                stack.push(c);
            }
        }

        // 남은 연산자 처리
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
            if (token.matches("-?\\d+(\\.\\d+)?")) { // 숫자
                stack.push(Double.parseDouble(token));
            } else { // 연산자
                double b = stack.pop();
                double a = stack.pop();
                switch (token) {
                    case "+":
                        stack.push(a + b);
                        break;
                    case "-":
                        stack.push(a - b);
                        break;
                    case "*":
                        stack.push(a * b);
                        break;
                    case "/":
                        stack.push(a / b);
                        break;
                }

            }
        }
        return stack.pop();
    }
}
