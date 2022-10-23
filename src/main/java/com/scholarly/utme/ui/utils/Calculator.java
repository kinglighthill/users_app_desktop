package com.scholarly.utme.ui.utils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collections;
import java.util.Locale;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {
    private boolean isRad;
    private String answer = "";

    private final String[] FUNCTIONS = {"asin", "acos", "atan", "sin", "cos", "tan", "log", "ln", "sqrt"};
    private final String OPERATORS = "+-*/^";
    private NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
    private Stack<String> stackOperations = new Stack<>();
    private Stack<String> stackRPN = new Stack<>();
    private Stack<String> stackAnswer = new Stack<>();

    private static final int MAX_PRECISION = 100;
    private static final int MIN_PRECISION = 0;

    public Calculator(boolean isRad) {
        setPrecision(MAX_PRECISION, MIN_PRECISION);
        setRad(isRad);
    }

    public void setRad(boolean rad) {
        isRad = rad;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setPrecision(int maxPrecision, int minPrecision) {
        numberFormat.setMaximumFractionDigits(maxPrecision);
        numberFormat.setMinimumFractionDigits(minPrecision);
    }

    public void parse(String expression) throws ParseException {
        /* cleaning stacks */
        stackOperations.clear();
        stackRPN.clear();

        if (expression.length() > 0) {
            String regexPil = "(\\d\\&pi)+";
            Pattern patternPil = Pattern.compile(regexPil);
            Matcher matcherPil = patternPil.matcher(expression);
            int diffPil = 0;
            while (matcherPil.find()) {
                int start = matcherPil.start() + diffPil;
                int end = matcherPil.end() + diffPil;
                String num = expression.substring(start, end);
                String replacement = num.substring(0,1) + "*" + num.substring(1);
                diffPil += replacement.length() - num.length();
                String begExp = expression.substring(0, start);
                String endExp = end >= expression.length() ? "" : expression.substring(end);
                expression = begExp + replacement + endExp;
            }
            String regexPir = "(\\&pi\\d)+";
            Pattern patternPir = Pattern.compile(regexPir);
            Matcher matcherPir = patternPir.matcher(expression);
            int diffPir = 0;
            while (matcherPir.find()) {
                int start = matcherPir.start() + diffPir;
                int end = matcherPir.end() + diffPir;
                String num = expression.substring(start, end);
                String replacement = num.substring(0,3) + "*" + num.substring(3);
                diffPir += replacement.length() - num.length();
                String begExp = expression.substring(0, start);
                String endExp = end >= expression.length() ? "" : expression.substring(end);
                expression = begExp + replacement + endExp;
            }
            String regexEl = "(\\d+e)+";
            Pattern patternEl = Pattern.compile(regexEl);
            Matcher matcherEl = patternEl.matcher(expression);
            int diffEl = 0;
            while (matcherEl.find()) {
                int start = matcherEl.start() + diffEl;
                int end = matcherEl.end() + diffEl;
                String num = expression.substring(start, end);
                String replacement = "exp()";
                diffEl += replacement.length() - num.length();
                String begExp = expression.substring(0, start);
                String endExp = end >= expression.length() ? "" : expression.substring(end);
                expression = begExp + replacement + endExp;
            }
            String regexEr = "(e\\d+)+";
            Pattern patternEr = Pattern.compile(regexEr);
            Matcher matcherEr = patternEr.matcher(expression);
            int diffEr = 0;
            while (matcherEr.find()) {
                int start = matcherEr.start() + diffEr;
                int end = matcherEr.end() + diffEr;
                String num = expression.substring(start, end);
                String replacement = "exp()";
                diffEr += replacement.length() - num.length();
                String begExp = expression.substring(0, start);
                String endExp = end >= expression.length() ? "" : expression.substring(end);
                expression = begExp + replacement + endExp;
            }

            expression = expression.replace(" ", "")
                    .replace("&div;", "/")
                    .replace("&times;", "*")
                    .replace("&infin;", String.valueOf(Double.POSITIVE_INFINITY))
                    .replace("&pi;", String.valueOf(Math.PI))
                    .replace("(-", "(0-")
                    .replace("(+", "(0+")
                    .replace("&radic;", "sqrt")
                    .replace("sin<sup>-1</sup>", "asin")
                    .replace("cos<sup>-1</sup>", "acos")
                    .replace("tan<sup>-1</sup>", "atan");
            if (expression.charAt(0) == '-' || expression.charAt(0) == '+') {
                expression = "0" + expression;
            }

            String regex = "\\d\\(";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(expression);
            int diff = 0;
            while (matcher.find()) {
                int start = matcher.start() + diff;
                int end = matcher.end() + diff;
                String num = expression.substring(start, end);
                String replacement = num.charAt(0) + "*" + num.charAt(1);
                diff += replacement.length() - num.length();
                String begExp = expression.substring(0, start);
                String endExp = end >= expression.length() ? "" : expression.substring(end);
                expression = begExp + replacement + endExp;
            }
            String regex2a = "\\d[a]";
            Pattern pattern2a = Pattern.compile(regex2a);
            Matcher matcher2a = pattern2a.matcher(expression);
            int diff2a = 0;
            while (matcher2a.find()) {
                int start = matcher2a.start() + diff2a;
                int end = matcher2a.end() + diff2a;
                String num = expression.substring(start, end);
                String replacement = num.charAt(0) + "*" + num.charAt(1);
                diff2a += replacement.length() - num.length();
                String begExp = expression.substring(0, start);
                String endExp = end >= expression.length() ? "" : expression.substring(end);
                expression = begExp + replacement + endExp;
            }
            String regex2c = "\\d[c]";
            Pattern pattern2c = Pattern.compile(regex2c);
            Matcher matcher2c = pattern2c.matcher(expression);
            int diff2c = 0;
            while (matcher2c.find()) {
                int start = matcher2c.start() + diff2c;
                int end = matcher2c.end() + diff2c;
                String num = expression.substring(start, end);
                String replacement = num.charAt(0) + "*" + num.charAt(1);
                diff2c += replacement.length() - num.length();
                String begExp = expression.substring(0, start);
                String endExp = end >= expression.length() ? "" : expression.substring(end);
                expression = begExp + replacement + endExp;
            }
            String regex2e = "\\d[e]";
            Pattern pattern2e = Pattern.compile(regex2e);
            Matcher matcher2e = pattern2e.matcher(expression);
            int diff2e = 0;
            while (matcher2e.find()) {
                int start = matcher2e.start() + diff2e;
                int end = matcher2e.end() + diff2e;
                String num = expression.substring(start, end);
                String replacement = num.charAt(0) + "*" + num.charAt(1);
                diff2e += replacement.length() - num.length();
                String begExp = expression.substring(0, start);
                String endExp = end >= expression.length() ? "" : expression.substring(end);
                expression = begExp + replacement + endExp;
            }
            String regex2l = "\\d[l]";
            Pattern pattern2l = Pattern.compile(regex2l);
            Matcher matcher2l = pattern2l.matcher(expression);
            int diff2l = 0;
            while (matcher2l.find()) {
                int start = matcher2l.start() + diff2l;
                int end = matcher2l.end() + diff2l;
                String num = expression.substring(start, end);
                String replacement = num.charAt(0) + "*" + num.charAt(1);
                diff2l += replacement.length() - num.length();
                String begExp = expression.substring(0, start);
                String endExp = end >= expression.length() ? "" : expression.substring(end);
                expression = begExp + replacement + endExp;
            }
            String regex2s = "\\d[s]";
            Pattern pattern2s = Pattern.compile(regex2s);
            Matcher matcher2s = pattern2s.matcher(expression);
            int diff2s = 0;
            while (matcher2s.find()) {
                int start = matcher2s.start() + diff2s;
                int end = matcher2s.end() + diff2s;
                String num = expression.substring(start, end);
                String replacement = num.charAt(0) + "*" + num.charAt(1);
                diff2s += replacement.length() - num.length();
                String begExp = expression.substring(0, start);
                String endExp = end >= expression.length() ? "" : expression.substring(end);
                expression = begExp + replacement + endExp;
            }
            String regex2t = "\\d[t]";
            Pattern pattern2t = Pattern.compile(regex2t);
            Matcher matcher2t = pattern2t.matcher(expression);
            int diff2t = 0;
            while (matcher2t.find()) {
                int start = matcher2t.start() + diff2t;
                int end = matcher2t.end() + diff2t;
                String num = expression.substring(start, end);
                String replacement = num.charAt(0) + "*" + num.charAt(1);
                diff2e += replacement.length() - num.length();
                String begExp = expression.substring(0, start);
                String endExp = end >= expression.length() ? "" : expression.substring(end);
                expression = begExp + replacement + endExp;
            }
            String regex3 = "\\)\\d";
            Pattern pattern3 = Pattern.compile(regex3);
            Matcher matcher3 = pattern3.matcher(expression);
            int diff3 = 0;
            while (matcher3.find()) {
                int start = matcher3.start() + diff3;
                int end = matcher3.end() + diff3;
                String num = expression.substring(start, end);
                String replacement = num.charAt(0) + "*" + num.charAt(1);
                diff3 += replacement.length() - num.length();
                String begExp = expression.substring(0, start);
                String endExp = end >= expression.length() ? "" : expression.substring(end);
                expression = begExp + replacement + endExp;
            }
            String regex4a = "\\)[a]";
            Pattern pattern4a = Pattern.compile(regex4a);
            Matcher matcher4a = pattern4a.matcher(expression);
            int diff4a = 0;
            while (matcher4a.find()) {
                int start = matcher4a.start() + diff4a;
                int end = matcher4a.end() + diff4a;
                String num = expression.substring(start, end);
                String replacement = num.charAt(0) + "*" + num.charAt(1);
                diff4a += replacement.length() - num.length();
                String begExp = expression.substring(0, start);
                String endExp = end >= expression.length() ? "" : expression.substring(end);
                expression = begExp + replacement + endExp;
            }
            String regex4c = "\\)[c]";
            Pattern pattern4c = Pattern.compile(regex4c);
            Matcher matcher4c = pattern4c.matcher(expression);
            int diff4c = 0;
            while (matcher4c.find()) {
                int start = matcher4c.start() + diff4c;
                int end = matcher4c.end() + diff4c;
                String num = expression.substring(start, end);
                String replacement = num.charAt(0) + "*" + num.charAt(1);
                diff4c += replacement.length() - num.length();
                String begExp = expression.substring(0, start);
                String endExp = end >= expression.length() ? "" : expression.substring(end);
                expression = begExp + replacement + endExp;
            }
            String regex4e = "\\)[e]";
            Pattern pattern4e = Pattern.compile(regex4e);
            Matcher matcher4e = pattern4e.matcher(expression);
            int diff4e = 0;
            while (matcher4e.find()) {
                int start = matcher4e.start() + diff4e;
                int end = matcher4e.end() + diff4e;
                String num = expression.substring(start, end);
                String replacement = num.charAt(0) + "*" + num.charAt(1);
                diff4c += replacement.length() - num.length();
                String begExp = expression.substring(0, start);
                String endExp = end >= expression.length() ? "" : expression.substring(end);
                expression = begExp + replacement + endExp;
            }
            String regex4l = "\\)[l]";
            Pattern pattern4l = Pattern.compile(regex4l);
            Matcher matcher4l = pattern4l.matcher(expression);
            int diff4l = 0;
            while (matcher4l.find()) {
                int start = matcher4l.start() + diff4l;
                int end = matcher4l.end() + diff4l;
                String num = expression.substring(start, end);
                String replacement = num.charAt(0) + "*" + num.charAt(1);
                diff4l += replacement.length() - num.length();
                String begExp = expression.substring(0, start);
                String endExp = end >= expression.length() ? "" : expression.substring(end);
                expression = begExp + replacement + endExp;
            }
            String regex4s = "\\)[s]";
            Pattern pattern4s = Pattern.compile(regex4s);
            Matcher matcher4s = pattern4s.matcher(expression);
            int diff4s = 0;
            while (matcher4s.find()) {
                int start = matcher4s.start() + diff4s;
                int end = matcher4s.end() + diff4s;
                String num = expression.substring(start, end);
                String replacement = num.charAt(0) + "*" + num.charAt(1);
                diff4s += replacement.length() - num.length();
                String begExp = expression.substring(0, start);
                String endExp = end >= expression.length() ? "" : expression.substring(end);
                expression = begExp + replacement + endExp;
            }
            String regex4t = "\\)[t]";
            Pattern pattern4t = Pattern.compile(regex4t);
            Matcher matcher4t = pattern4t.matcher(expression);
            int diff4t = 0;
            while (matcher4t.find()) {
                int start = matcher4t.start() + diff4t;
                int end = matcher4t.end() + diff4t;
                String num = expression.substring(start, end);
                String replacement = num.charAt(0) + "*" + num.charAt(1);
                diff4t += replacement.length() - num.length();
                String begExp = expression.substring(0, start);
                String endExp = end >= expression.length() ? "" : expression.substring(end);
                expression = begExp + replacement + endExp;
            }
            String regex5 = "\\)\\(";
            Pattern pattern5 = Pattern.compile(regex5);
            Matcher matcher5 = pattern5.matcher(expression);
            int diff5 = 0;
            while (matcher5.find()) {
                int start = matcher5.start() + diff5;
                int end = matcher5.end() + diff5;
                String num = expression.substring(start, end);
                String replacement = num.charAt(0) + "*" + num.charAt(1);
                diff5 += replacement.length() - num.length();
                String begExp = expression.substring(0, start);
                String endExp = end >= expression.length() ? "" : expression.substring(end);
                expression = begExp + replacement + endExp;
            }

            String regexFac = "(\\d+\\!)+";
            Pattern patternFac = Pattern.compile(regexFac);
            Matcher matcherFac = patternFac.matcher(expression);
            int diffFac = 0;
            while (matcherFac.find()) {
                int start = matcherFac.start() + diffFac;
                int end = matcherFac.end() + diffFac;
                String num = expression.substring(start, end - 1);
                String replacement = String.valueOf((int)fac(Double.parseDouble(num)));
                diffFac += replacement.length() - (num.length() + 1);
                String begExp = expression.substring(0, start);
                String endExp = end >= expression.length() ? "" : expression.substring(end);
                expression = begExp + replacement + endExp;
            }

            expression = expression.replace("e", String.valueOf(Math.E));
        }
        /* Buttonting input string into tokens */
        StringTokenizer stringTokenizer = new StringTokenizer(expression, OPERATORS + "()", true);

        /* loop for handling each token - shunting-yard algorithm */
        while (stringTokenizer.hasMoreTokens()) {
            String token = stringTokenizer.nextToken();
            if (isOpenBracket(token)) {
                stackOperations.push(token);
            }
            else if (isCloseBracket(token)) {
                while (!stackOperations.empty() && !isOpenBracket(stackOperations.lastElement())) {
                    stackRPN.push(stackOperations.pop());
                }
                if (!stackOperations.empty()) {
                    stackOperations.pop();
                }
                if (!stackOperations.empty() && isFunction(stackOperations.lastElement())) {
                    stackRPN.push(stackOperations.pop());
                }
            }
            else if (isNumber(token)) {
                stackRPN.push(token);
            }
            else if (isOperator(token)) {
                while (!stackOperations.empty() && isOperator(stackOperations.lastElement()) && getPrecedence(token) <=
                        getPrecedence(stackOperations.lastElement())) {
                    stackRPN.push(stackOperations.pop());
                }
                stackOperations.push(token);
            }
            else if (isFunction(token)) {
                stackOperations.push(token);
            }
            else {
                throw new ParseException("Unrecognized token: " + token, 0);
            }
        }
        while (!stackOperations.empty()) {
            stackRPN.push(stackOperations.pop());
        }

        /* reverse stack */
        Collections.reverse(stackRPN);
    }

    public String evaluate() throws Exception {
        /* check if is there something to evaluate */
        if (stackRPN.empty()) {
            return "";
        }
        /* clean answer stack */
        stackAnswer.clear();

        /* get the clone of the RPN stack for further evaluating */
        @SuppressWarnings("unchecked")
        Stack<String> stackRPN = (Stack<String>) this.stackRPN.clone();

        /* evaluating the RPN expression */
        while (!stackRPN.empty()) {
            String token = stackRPN.pop();
            if (isNumber(token)) {
                stackAnswer.push(token);
            }
            else if (isOperator(token)) {
                Double a = stackAnswer.isEmpty() ? null : Double.parseDouble(stackAnswer.pop());
                Double b = stackAnswer.isEmpty() ? null : Double.parseDouble(stackAnswer.pop());
                if (b != null) {
                    double c;
                    switch (token) {
                        case "+":
                            stackAnswer.push(numberFormat.format(b + a));
                            break;
                        case "-":
                            stackAnswer.push(numberFormat.format(b - a));
                            break;
                        case "*":
                            stackAnswer.push(numberFormat.format(b * a));
                            break;
                        case "/":
                            c = b/a ;
                            String s = numberFormat.format(c);
                            stackAnswer.push(s);
                            break;
                        case "^":
                            stackAnswer.push(numberFormat.format(pow(b, a)));
                            break;
                        case "!":
                            stackAnswer.push(numberFormat.format(fac(b)));
                            break;
                    }
                }
                else {
                    if (token.equals("!") && a != null) {
                        stackAnswer.push(numberFormat.format(fac(a)));
                    }
                }
            }
            else if (isFunction(token)) {
                if (stackAnswer.size() > 0) {
                    Double a = Double.parseDouble(stackAnswer.pop());
                    switch (token) {
                        case "asin":
                            stackAnswer.push(numberFormat.format(asin(a)));
                            break;
                        case "acos":
                            stackAnswer.push(numberFormat.format(acos(a)));
                            break;
                        case "atan":
                            stackAnswer.push(numberFormat.format(atan(a)));
                            break;
                        case "sin":
                            stackAnswer.push(numberFormat.format(sin(a)));
                            break;
                        case "cos":
                            stackAnswer.push(numberFormat.format(cos(a)));
                            break;
                        case "tan":
                            stackAnswer.push(numberFormat.format(tan(a)));
                            break;
                        case "ln":
                            stackAnswer.push(numberFormat.format(ln(a)));
                            break;
                        case "log":
                            stackAnswer.push(numberFormat.format(log(a)));
                            break;
                        case "sqrt":
                            stackAnswer.push(numberFormat.format(sqrt(a)));
                            break;
                    }
                }
            }
        }
        int stackSize = stackAnswer.size();

        if (stackSize > 1) {
            throw new ParseException("Some operator is missing", 0);
        }
        if (stackSize > 0) {
            answer = stackAnswer.pop();
            return answer;
        }
        else {
            return answer;
        }
    }

    private boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private boolean isFunction(String token) {
        for (String item : FUNCTIONS) {
            if (item.equals(token)) {
                return true;
            }
        }
        return false;
    }

    private boolean isOpenBracket(String token) {
        return token.equals("(");
    }

    private boolean isCloseBracket(String token) {
        return token.equals(")");
    }

    private boolean isOperator(String token) {
        return OPERATORS.contains(token);
    }

    private byte getPrecedence(String token) {
        if (token.equals("+") || token.equals("-")) {
            return 1;
        }
        else if (token.equals("*") || token.equals("/")) {
            return 2;
        }
        else if (token.equals("^")) {
            return 3;
        }
        return 4;
    }

    private double sin(double in) {
        double out;
        if (!isRad) {
            out = Math.sin(Math.toRadians(in));
            return out;
        }
        else {
            out = Math.sin(in);
            return out;
        }
    }

    private double cos(double in) {
        double out;
        if (!isRad) {
            out = Math.cos(Math.toRadians(in));
            return out;
        }
        else {
            out = Math.cos(in);
            return out;
        }
    }

    private double tan(double in) {
        double out;
        if (!isRad) {
            out = Math.tan(Math.toRadians(in));
            return out;
        }
        else {
            out = Math.tan(in);
            return out;
        }
    }

    private double asin(double in) {
        double out;
        if (!isRad) {
            out = Math.toDegrees(Math.asin(in));
            return out;
        }
        else {
            out = Math.asin(in);
            return out;
        }
    }

    private double acos(double in) {
        double out;
        if (!isRad) {
            out = Math.toDegrees(Math.acos(in));
            return out;
        }
        else {
            out = Math.acos(in);
            return out;
        }
    }

    private double atan(double in) {
        double out;
        if (!isRad) {
            out = Math.toDegrees(Math.atan(in));
            return out;
        }
        else {
            out = Math.atan(in);
            return out;
        }
    }

    private double log(double in) {
        double out = Math.log10(in);
        return out;
    }

    private double ln(double in) {
        double out = Math.log(in);
        return out;
    }

    private double fac(double in) {
        double y = 1;

        if (in == 0) {
            return y;
        }
        else if (in < 0) {
            return Double.NaN;
        }
        else {
            while (in >= 1) {
                y = y * in;
                --in;
            }
            return y;
        }
    }

    private double sqrt(double in) {
        double out = Math.sqrt(in);
        return out;
    }

    private double pow(double in, double index) {
        double out = Math.pow(in, index);
        return out;
    }

}
