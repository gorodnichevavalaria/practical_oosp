import java.util.*;
import java.util.regex.*;

class MathModel {
    public double calculate(String expression) {
        expression = preprocess(expression);
        return evaluate(expression);
    }

    private String preprocess(String expr) {
        expr = expr.replace("**", "^");
        expr = expr.replaceAll("log\\(([^)]+)\\)", "l($1)");
        expr = expr.replaceAll("exp\\(([^)]+)\\)", "e($1)");
        if (expr.startsWith("-")) {
            expr = "0" + expr;
        }
        expr = expr.replaceAll("\\(-", "(0-");
        return expr;
    }

    private double evaluate(String expr) {
        Stack<Double> numbers = new Stack<>();
        Stack<String> operators = new Stack<>();
        expr = expr.replaceAll("\\s+", "");

        for (int i = 0; i < expr.length(); ) {
            char c = expr.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                StringBuilder num = new StringBuilder();
                while (i < expr.length() && (Character.isDigit(expr.charAt(i)) || expr.charAt(i) == '.'))
                    num.append(expr.charAt(i++));
                numbers.push(Double.parseDouble(num.toString()));
            } else if (c == '(') {
                operators.push("(");
                i++;
            } else if (c == ')') {
                while (!operators.peek().equals("(")) {
                    process(numbers, operators.pop());
                }
                operators.pop();
                i++;
            } else if (Character.isLetter(c)) {
                StringBuilder func = new StringBuilder();
                while (i < expr.length() && Character.isLetter(expr.charAt(i)))
                    func.append(expr.charAt(i++));
                operators.push(func.toString());
            } else if (c == '!') {
                double val = numbers.pop();
                numbers.push(factorial(val));
                i++;
            } else {
                String op = String.valueOf(c);
                if (i + 1 < expr.length() && expr.charAt(i) == '/' && expr.charAt(i + 1) == '/') {
                    op = "//";
                    i += 2;
                } else {
                    i++;
                }

                while (!operators.empty() && !operators.peek().equals("(") &&
                        precedence(op) <= precedence(operators.peek())) {
                    process(numbers, operators.pop());
                }
                operators.push(op);
            }
        }

        while (!operators.empty())
            process(numbers, operators.pop());

        return numbers.pop();
    }

    private void process(Stack<Double> numbers, String op) {
        if (op.equals("l")) {
            numbers.push(Math.log(numbers.pop()) / Math.log(2));
        } else if (op.equals("e")) {
            numbers.push(Math.exp(numbers.pop()));
        } else {
            double b = numbers.pop();
            double a = numbers.empty() ? 0 : numbers.pop();
            switch (op) {
                case "+": numbers.push(a + b); break;
                case "-": numbers.push(a - b); break;
                case "*": numbers.push(a * b); break;
                case "/": numbers.push(a / b); break;
                case "//": numbers.push((double)((int)(a / b))); break;
                case "^": numbers.push(Math.pow(a, b)); break;
                default: throw new RuntimeException("Неизвестный оператор: " + op);
            }
        }
    }

    private int precedence(String op) {
        return switch (op) {
            case "+", "-" -> 1;
            case "*", "/", "//" -> 2;
            case "^" -> 3;
            case "l", "e" -> 4;
            default -> 0;
        };
    }

    private double factorial(double n) {
        if (n < 0 || n != Math.floor(n))
            throw new RuntimeException("Факториал определён только для неотрицательных целых чисел.");
        double result = 1;
        for (int i = 2; i <= (int)n; i++) result *= i;
        return result;
    }
}

class MathView {
    public void showResult(double result) {
        System.out.println("Результат: " + result);
    }

    public void showError(String msg) {
        System.out.println("Ошибка: " + msg);
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
        String eq = view.getInput();
        try {
            validate(eq);
            double result = model.calculate(eq);
            view.showResult(result);
        } catch (Exception e) {
            view.showError(e.getMessage() != null ? e.getMessage() : "Неизвестная ошибка");
        }
    }

    private void validate(String eq) {
        if (eq.isEmpty()) throw new RuntimeException("Уравнение не должно быть пустым");

        int balance = 0;
        int countOperands = 0;
        boolean prevDigit = false;

        for (int i = 0; i < eq.length(); i++) {
            char c = eq.charAt(i);
            if (c == '(') balance++;
            else if (c == ')') balance--;
            if (balance < 0) throw new RuntimeException("Ошибка в скобках");

            if (Character.isDigit(c)) {
                if (!prevDigit) {
                    countOperands++;
                    prevDigit = true;
                }
            } else {
                prevDigit = false;
            }
        }

        if (balance != 0) throw new RuntimeException("Неправильное количество скобок");
        if (countOperands > 15) throw new RuntimeException("Слишком много чисел (макс. 15)");
        if (!Character.isDigit(eq.charAt(0)) && eq.charAt(0) != '-' && eq.charAt(0) != '(')
            throw new RuntimeException("Уравнение должно начинаться с числа, знака минус или открывающей скобки");
        if (!Character.isDigit(eq.charAt(eq.length() - 1)) && eq.charAt(eq.length() - 1) != ')')
            throw new RuntimeException("Уравнение должно заканчиваться числом или закрывающей скобкой");
    }
}

public class AdvancedCalculator {
    public static void main(String[] args) {
        MathModel model = new MathModel();
        MathView view = new MathView();
        MathController controller = new MathController(model, view);
        controller.process();
    }
}
