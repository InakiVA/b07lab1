class Polynomial {
    double[] coef;

    public Polynomial() {
        this.coef = new double[0];
    }

    public Polynomial(double[] coefficients) {
        this.coef = coefficients;
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
            added[i] = p.getValueToAdd(i) + this.getValueToAdd(i);
        }
        Polynomial a = new Polynomial(added);
        return a;
    }

    public int getLength() {
        return this.coef.length;
    }

    public double getValueToAdd(int index) { // we return 0 because we're just adding
        if (index >= this.getLength()) {
            return 0;
        }
        return this.coef[index];
    }

    public double evaluate(double input) {
        double total = 0;
        for (int i = 0; i < this.coef.length; i++)
            total += this.coef[i] * Math.pow(input, i);
        return total;
    }

    public boolean hasRoot(double input) {
        return this.evaluate(input) == 0 ? true : false;
    }

    public void printPolynomial() {
        for (int i = 0; i < this.coef.length; i++) {
            if (coef[i] != 0) {
                String text;
                if (coef[i] > 0 & i != 0)
                    text = "+" + String.valueOf(coef[i]);
                else
                    text = String.valueOf(coef[i]);
                if (i == 0)
                    System.out.print(text + " ");
                else if (i == 1)
                    System.out.print(text + "x ");
                else
                    System.out.print(text + "x^" + i + " ");

            }
        }
    }
}