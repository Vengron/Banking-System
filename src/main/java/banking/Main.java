package banking;

import database.CardDAO;

public class Main {
    public static void main(String[] args) {
        CardDAO cardDAO = new CardDAO(args[1]);
        Atm atm = new Atm(cardDAO);
        atm.run();
    }
}
