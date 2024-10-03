import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class Polynomial {
    double[] coef;
    int[] exp;

    public Polynomial() {
        this.coef = new double[0];
        this.exp = new int[0];
        removeRedundancy();
        orderByExponent();
    }

    public Polynomial(double[] coefficients) { // in case no exponents are given, have them all be 1
        this.coef = coefficients;
        this.exp = new int[coefficients.length];
        for (int i = 0; i < coefficients.length; i++)
            this.exp[i] = 1;
        removeRedundancy();
        orderByExponent();
    }

    public Polynomial(double[] coefficients, int[] exp) {
        this.coef = coefficients;
        this.exp = exp;
        removeRedundancy();
        orderByExponent();
    }

    public Polynomial(File file) {
        String line = "";
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine())
                line = scanner.nextLine();
            scanner.close();
            formatFileType(line);
            removeRedundancy();
            orderByExponent();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }

    public void formatFileType(String s_text) { // only works if exponens are non-negative
        ArrayList<String> operations = new ArrayList<>();
        ArrayList<Double> coef = new ArrayList<>();
        ArrayList<Integer> exp = new ArrayList<>();
        String text = "" + s_text.charAt(0);

        for (int i = 1; i < s_text.length() + 1; i++) {
            if (i == s_text.length() || s_text.charAt((i)) == '+' || s_text.charAt((i)) == '-') {
                operations.add(text);
                if (i < s_text.length()) {
                    if (s_text.charAt((i)) == '-')
                        text = "-";
                    else
                        text = "";
                }
            } else
                text += s_text.charAt((i));

        }
        System.out.println("Operations: " + operations);

        for (String s : operations) {
            boolean is_exp = false;
            int c_exp = 0;
            double c_coef = 1;
            String s_coef = "", s_exp = "";

            for (char c : s.toCharArray()) {
                if (c == 'x') {
                    is_exp = true;
                    c_exp = 1;
                } else {
                    if (!is_exp)
                        s_coef += c;
                    else
                        s_exp += c;
                }
            }
            if (s_coef.length() > 0)
                c_coef = Double.parseDouble(s_coef);
            if (s_exp.length() > 0)
                c_exp = Integer.parseInt(s_exp);
            coef.add(c_coef);
            exp.add(c_exp);
        }

        System.out.println("Coefficients: " + coef);
        System.out.println("Exponents: " + exp);

        double[] coef_a = new double[coef.size()];
        int[] exp_a = new int[exp.size()];

        for (int i = 0; i < coef_a.length; i++)
            coef_a[i] = coef.get(i);

        for (int i = 0; i < exp_a.length; i++)
            exp_a[i] = exp.get(i);
        this.coef = coef_a;
        this.exp = exp_a;
        removeRedundancy();
    }

    public Polynomial add(Polynomial p) {
        int max_length = 0;
        if (p.getLength() > this.getLength()) {
            max_length = p.getLength();
        } else {
            max_length = this.getLength();
        }
        double[] added = new double[max_length];
        for (int i = 0; i < max_length; i++) {
            added[i] = p.getCoefficientToAdd(i) + this.getCoefficientToAdd(i);
        }
        Polynomial a = new Polynomial(added);
        return a;
    }

    public Polynomial multiply(Polynomial p) {
        double[] coef_n = new double[this.getLength() * p.getLength()];
        int[] exp_n = new int[this.getLength() * p.getLength()];
        int position = 0;
        for (int i = 0; i < this.coef.length; i++) {
            double c1 = this.coef[i];
            int e1 = this.exp[i];
            for (int j = 0; j < p.coef.length; j++) {
                double c2 = p.coef[j];
                int e2 = p.exp[j];
                coef_n[position] = c1 * c2;
                exp_n[position] = e1 + e2;
                position += 1;
            }
        }
        Polynomial a = new Polynomial(coef_n, exp_n);
        a.removeRedundancy();
        a.orderByExponent();
        return a;
    }

    public void removeRedundancy() {
        ArrayList<Double> coef_n = new ArrayList<>();
        ArrayList<Integer> exp_n = new ArrayList<>();
        int found_pos = 0;
        for (int i = 0; i < this.exp.length; i++) {
            boolean found = false;
            for (int j = 0; j < exp_n.size() & !found; j++) {
                if (exp_n.get(j) == this.exp[i]) {
                    found_pos = j;
                    found = true;
                }
            }
            if (!found) {
                exp_n.add(this.exp[i]);
                coef_n.add(this.coef[i]);
            } else {
                double currentValue = coef_n.get(found_pos);
                currentValue += this.coef[i];
                coef_n.set(found_pos, currentValue);
            }
        }
        double[] coef_f = new double[coef_n.size()];
        int[] exp_f = new int[exp_n.size()];
        for (int i = 0; i < coef_n.size(); i++) {
            coef_f[i] = coef_n.get(i);
            exp_f[i] = exp_n.get(i);
        }
        this.coef = coef_f;
        this.exp = exp_f;
    }

    public int getLength() {
        return this.coef.length;
    }

    public double getCoefficientToAdd(int index) { // we return 0 because we're just adding
        if (index >= this.getLength()) {
            return 0;
        }
        return this.coef[index];
    }

    public int getExponentToAdd(int index) { // we return 1 because we're adding them when multiplying
        if (index >= this.getLength()) {
            return 0;
        }
        return this.exp[index];
    }

    public double evaluate(double input) {
        double total = 0;
        for (int i = 0; i < this.coef.length; i++)
            total += this.coef[i] * Math.pow(input, this.exp[i]);
        return total;
    }

    public boolean hasRoot(double input) {
        return this.evaluate(input) == 0 ? true : false;
    }

    public void printCoefficients() {
        for (double d : this.coef)
            System.out.print(d + " ");
    }

    public void printExponents() {
        for (int i : this.exp)
            System.out.print(i + " ");
    }

    public String stringPolynomial() {
        ArrayList<String> texts = new ArrayList<>();
        String c_text, e_text, final_text;
        double n_coef;
        int n_exp;
        boolean started = false;
        for (int i = 0; i < this.coef.length; i++) {
            e_text = "";
            c_text = "";
            n_coef = this.coef[i];
            n_exp = this.exp[i];
            if (n_coef == 0)
                continue;
            else if (n_coef == 1) {
                started = true;
                c_text = "";
            } else if (n_coef > 0 & started)
                c_text = "+" + n_coef;
            else {
                started = true;
                c_text = "" + n_coef;
            }
            if (n_exp == 0 & n_coef == 1)
                e_text = "1";
            else if (n_exp == 0)
                e_text = "";
            else if (n_exp == 1)
                e_text = "x";
            else
                e_text = "x" + n_exp;
            final_text = c_text + e_text;
            texts.add(final_text);
        }
        final_text = "";
        for (String s : texts)
            final_text += s;
        return final_text;
    }

    public void saveToFile(String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(stringPolynomial());
            System.out.println("Polynomial saved to file: " + fileName);
        } catch (IOException e) {
            System.out.println("An error occurred while saving the file: " + e.getMessage());
        }
    }

    public void orderByExponent() {
        // we assume redundancy has been removed
        for (int i = 0; i < this.exp.length; i++) {
            for (int j = i + 1; j < this.exp.length; j++) {
                int a_exp = this.exp[i], b_exp = this.exp[j];
                double a_c = this.coef[i], b_c = this.coef[j];
                if (b_exp < a_exp) {
                    this.exp[i] = b_exp;
                    this.exp[j] = a_exp;
                    this.coef[i] = b_c;
                    this.coef[j] = a_c;
                }
            }
        }
    }

}
