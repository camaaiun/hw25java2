package payment;

import util.FileUtil;
import payment.Login;

import java.util.ArrayList;
import java.util.Scanner;

public class CardPaymentReceiver implements Payment {
    private String cardNumber;
    private int currentAmount;

    FileUtil util = new FileUtil("data/logins.json");

    @Override
    public void initialize() {
        Scanner scan = new Scanner(System.in);
        Login[] logins = util.readFile();
        if (logins == null) {
            logins = new Login[0];
        }

        ArrayList<Login> loginList = new ArrayList<>();
        for (Login l : logins) {
            loginList.add(l);
        }

        do {
            System.out.print("Введите логин (номер карты, 4 цифры): ");
            cardNumber = scan.nextLine();

            if (isValidCardNumber(cardNumber)) {
                Login foundLogin = null;
                for (Login l : loginList) {
                    if (l.getLogin().equals(cardNumber)) {
                        foundLogin = l;
                        break;
                    }
                }

                if (foundLogin != null) {
                    System.out.println("Введите пароль:");
                    String passwordUser = scan.nextLine();

                    if (foundLogin.getPassword().equals(passwordUser)) {
                        System.out.println("Добро пожаловать!");
                        break;
                    } else {
                        System.out.println("Неверный пароль!");
                    }
                } else {
                    System.out.println("Пользователь с таким логином не найден!");
                }

                System.out.println("Хотите создать нового пользователя? (yes/no)");
                String response = scan.nextLine();
                if ("yes".equalsIgnoreCase(response)) {
                    Login login = new Login();

                    String newLogin;
                    do {
                        System.out.println("Запишите логин (номер карты, 4 цифры):");
                        newLogin = scan.nextLine();
                        if (!isValidCardNumber(newLogin)) {
                            System.out.println("Номер карты должен состоять ровно из 4 цифр. Повторите ввод.");
                        }
                    } while (!isValidCardNumber(newLogin));

                    login.setLogin(newLogin);

                    System.out.println("Запишите пароль:");
                    String newPassword = scan.nextLine();
                    login.setPassword(newPassword);

                    loginList.add(login);
                    Login[] updatedLogins = loginList.toArray(new Login[0]);
                    util.writeFile(updatedLogins);

                    System.out.println("Новый пользователь успешно создан и сохранен.");
                }

            } else {
                System.out.println("Номер карты должен состоять ровно из 4 цифр. Повторите ввод.");
            }

        } while (true);
    }

    private boolean isValidCardNumber(String cardNumber) {
        return cardNumber.matches("\\d{4}");
    }

    @Override
    public void acceptMoney(int amount) {
        currentAmount += amount;
        if (amount >= 0) {
            System.out.println("Вы пополнили карту на " + amount);
        } else {
            System.out.println("У вас с карты снялось - " + amount);
        }
    }

    @Override
    public int getCurrentAmount() {
        return currentAmount;
    }
}
