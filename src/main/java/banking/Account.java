package banking;

import java.util.Random;

public class Account {
    private String number;
    private final String pin;
    private int balance;

    // for generating a new Account
    public Account() {
        generateCardNumber();
        pin = String.format("%4d", new Random().nextInt(9000) + 999);
    }

    // for working with the data from a database
    public Account(String number, String pin, int balance) {
        this.number = number;
        this.pin = pin;
        this.balance = balance;
    }

    // generate a number according to Luhn algorithm
    private void generateCardNumber() {
        String aid = String.format("%9d", new Random().nextInt(900000000) + 99999999);
        String number = "400000" + aid;
        int controlNumber = 0;
        for (int i = 1; i <= number.length(); ++i) {
            int n = Integer.parseInt(String.valueOf(number.charAt(i - 1)));
            if (i % 2 == 1) {
                n *= 2;
                if (n > 9) {
                    n -= 9;
                }
            }
            controlNumber += n;
        }
        int luhnNumber = controlNumber % 10 == 0 ? 0 : 10 - (controlNumber % 10);
        this.number = number + luhnNumber;
    }

    public boolean verifyCardNumber() {
        int controlNumber = 0;
        for (int i = 1; i < number.length(); ++i) {
            int n = Integer.parseInt(String.valueOf(number.charAt(i - 1)));
            if (i % 2 == 1) {
                n *= 2;
                if (n > 9) {
                    n -= 9;
                }
            }
            controlNumber += n;
        }
        int luhnNumber = controlNumber % 10 == 0 ? 0 : 10 - (controlNumber % 10);
        return luhnNumber == Integer.parseInt(String.valueOf(number.charAt(number.length() - 1)));
    }

    public String getNumber() {
        return number;
    }

    public String getPin() {
        return pin;
    }

    public int getBalance() {
        return balance;
    }
}
