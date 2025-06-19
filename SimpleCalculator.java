import java.util.Scanner;
import java.util.Stack;

class MathModel {
    public double calculate(String equation) {
        return evaluateExpression(equation);
    }

    private double evaluateExpression(String expr) {
        expr = expr.replaceAll("//", "#");
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operations = new Stack<>();

        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);

            if (c == ' ') continue;

            if (c == '-' && (i == 0 || expr.charAt(i - 1) == '(')) {
                StringBuilder numStr = new StringBuilder("-");
                i++;
                while (i < expr.length() && (Character.isDigit(expr.charAt(i)) || expr.charAt(i) == '.')) {
                    numStr.append(expr.charAt(i++));
                }
                i--;
                numbers.push(Double.parseDouble(numStr.toString()));
            }
            else if (Character.isDigit(c) || c == '.') {
                StringBuilder numStr = new StringBuilder();
                while (i < expr.length() && (Character.isDigit(expr.charAt(i)) || expr.charAt(i) == '.')) {
                    numStr.append(expr.charAt(i++));
                }
                i--;
                numbers.push(Double.parseDouble(numStr.toString()));
            }
            else if (c == '(') {
                operations.push(c);
            }
            else if (c == ')') {
                while (!operations.isEmpty() && operations.peek() != '(') {
                    numbers.push(applyOperation(operations.pop(), numbers.pop(), numbers.pop()));
                }
                if (!operations.isEmpty()) operations.pop();
            }
            else if (isOperator(c)) {
                while (!operations.isEmpty() && hasPrecedence(c, operations.peek())) {
                    numbers.push(applyOperation(operations.pop(), numbers.pop(), numbers.pop()));
                }
                operations.push(c);
            }
        }

        while (!operations.isEmpty()) {
            numbers.push(applyOperation(operations.pop(), numbers.pop(), numbers.pop()));
        }

        return numbers.pop();
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^' || c == '#';
    }

    private boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') return false;
        if (op1 == '^' && op2 == '^') return false;
        if ((op1 == '*' || op1 == '/' || op1 == '#') && (op2 == '+' || op2 == '-')) return false;
        if ((op1 == '+' || op1 == '-') && (op2 == '*' || op2 == '/' || op2 == '^' || op2 == '#')) return false;
        return true;
    }

    private double applyOperation(char op, double b, double a) {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/': return a / b;
            case '#': return (double)((int)(a / b));
            case '^': return Math.pow(a, b);
            default: throw new RuntimeException("Неизвестная операция: " + op);
        }
    }
}

class MathView {
    public void showResult(double result) {
        System.out.println("Результат: " + result);
    }

    public void showError(String message) {
        System.out.println("Ошибка: " + message);
    }

    public String getInput() {
        System.out.print("Введите уравнение: ");
        return new Scanner(System.in).nextLine();
    }
}

class MathController {
    private MathModel model;
    private MathView view;

    public MathController(MathModel model, MathView view) {
        this.model = model;
        this.view = view;
    }

    public void process() {
        String equation = view.getInput();

        try {
            validateEquation(equation);
            double result = model.calculate(equation);
            view.showResult(result);
        } catch (Exception e) {
            view.showError(e.getMessage() != null ? e.getMessage() : "Неизвестная ошибка");
        }
    }

    private void validateEquation(String equation) {
        if (equation.isEmpty()) {
            throw new RuntimeException("Уравнение не может быть пустым");
        }

        String sanitized = equation.replaceAll("\\s+", "").replaceAll("//", "#");

        if (!Character.isDigit(sanitized.charAt(0))) {
            throw new RuntimeException("Уравнение должно начинаться с числа");
        }

        if (!Character.isDigit(sanitized.charAt(sanitized.length() - 1))) {
            throw new RuntimeException("Уравнение должно заканчиваться числом");
        }

        int countNumbers = 0;
        for (int i = 0; i < sanitized.length(); i++) {
            char c = sanitized.charAt(i);
            if (Character.isDigit(c) && (i == 0 || !Character.isDigit(sanitized.charAt(i - 1)))) {
                countNumbers++;
            }
        }

        if (countNumbers > 100) {
            throw new RuntimeException("Уравнение не должно содержать более 100 чисел");
        }
    }
}

public class SimpleCalculator {
    public static void main(String[] args) {
        MathModel model = new MathModel();
        MathView view = new MathView();
        MathController controller = new MathController(model, view);
        controller.process();
    }
}