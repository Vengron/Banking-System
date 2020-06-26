package banking;

import database.CardDAO;

import java.util.Scanner;

public class Atm {
    private Scanner scanner;
    private Account loggedAccount;
    private final CardDAO cardDAO;

    public Atm(CardDAO cardDAO) {
        this.cardDAO = cardDAO;
        cardDAO.createTable();
    }

    public void run() {
        scanner = new Scanner(System.in);
        while(true) {
            System.out.print("1. Create an account\n" +
                    "2. Log into account\n" +
                    "0. Exit\n");
            int command = Integer.parseInt(scanner.nextLine());
            switch (command) {
                case 1: createAccount();
                    break;
                case 2: logIntoAccount();
                    break;
                case 0:
                    System.out.println("\nBye!");
                    System.exit(0);
                default:
                    System.out.println("Unknown command");
                    break;
            }
        }
    }

    private void createAccount() {
        Account account = new Account();
        cardDAO.addCard(account.getNumber(), account.getPin());
        System.out.printf("Your card has been created\n" +
                        "Your card number:\n%s\n" +
                        "Your card PIN:\n%s\n\n",
                account.getNumber(), account.getPin());

    }

    private void logIntoAccount() {
        System.out.println("Enter your card number: ");
        String cardNumber = scanner.nextLine();
        System.out.println("Enter your PIN: ");
        String pin = scanner.nextLine();
        if (authenticate(cardNumber, pin)) {
            System.out.println("You have successfully logged in!\n");
            workWithAccount();
        } else {
            System.out.println("Wrong card number or PIN!\n");
        }

    }

    private boolean authenticate(String cardNumber, String pin) {
        Account account =  cardDAO.selectAccount(cardNumber);
        if (account != null && account.getPin().equals(pin)) {
            loggedAccount = account;
            return true;
        }
        return false;
    }

    private void workWithAccount() {
        while (true) {
            System.out.print("1. Balance\n" +
                    "2. Add income\n" +
                    "3. Do transfer\n" +
                    "4. Close account\n" +
                    "5. Log out\n" +
                    "0. Exit\n");
            int command = Integer.parseInt(scanner.nextLine());
            switch (command) {
                case 1:
                    System.out.printf("\nBalance: $%d\n\n", loggedAccount.getBalance());
                    break;
                case 2:
                    System.out.println("\nEnter income: ");
                    int income = Integer.parseInt(scanner.nextLine());
                    cardDAO.updateBalance(loggedAccount.getNumber(), income);
                    loggedAccount = cardDAO.selectAccount(loggedAccount.getNumber());
                    System.out.println("Income was added!\n");
                    break;
                case 3:
                    doTransfer();
                    break;
                case 4:
                    cardDAO.closeAccount(loggedAccount.getNumber());
                    loggedAccount = null;
                    System.out.println("\nThe account has been closed!\n");
                    return;
                case 5:
                    loggedAccount = null;
                    System.out.println("\nYou have successfully logged out!\n");
                    return;
                case 0:
                    System.out.println("\nBye!");
                    System.exit(0);
                default:
                    System.out.println("Unknown command");
                    break;
            }
        }
    }

    private void doTransfer() {
        System.out.println("\nTransfer\nEnter card number: ");
        String number = scanner.nextLine();
        if (loggedAccount.getNumber().equals(number)) {
            System.out.println("You can't transfer money to the same account!\n");
            return;
        } else if (!loggedAccount.verifyCardNumber()) {
            System.out.println("Probably you made mistake in the card number. Please try again!\n");
            return;
        }

        Account transferAccount = cardDAO.selectAccount(number);
        if (transferAccount == null) {
            System.out.println("Such a card does not exist.\n");
            return;
        }

        System.out.println("Enter how much money you want to transfer:");
        int amount = Integer.parseInt(scanner.nextLine());
        if (amount > loggedAccount.getBalance()) {
            System.out.println("Not enough money!\n");
            return;
        }

        cardDAO.updateBalance(loggedAccount.getNumber(), -amount);
        cardDAO.updateBalance(transferAccount.getNumber(), amount);
        System.out.println("Success!\n");
    }

}
