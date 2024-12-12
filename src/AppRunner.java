import enums.ActionLetter;
import model.*;
import payment.CardPaymentReceiver;
import payment.CoinPaymentReceiver;
import payment.Payment;
import util.UniversalArray;
import util.UniversalArrayImpl;

import java.util.Scanner;

public class AppRunner {
    private final UniversalArray<Product> products = new UniversalArrayImpl<>();
    private Payment paymentObj = new CardPaymentReceiver();
    private int balance = getCurrentAmount();

    public void setPaymentObj(Payment paymentObj) {
        this.paymentObj = paymentObj;
    }

    public void initialize(){
        paymentObj.initialize();
    }

    public int acceptMoney(int amount) {
        paymentObj.acceptMoney(amount);
        balance += amount;
        return balance;
    }

    public int getCurrentAmount() {
        paymentObj.getCurrentAmount();
        return balance;
    }

    private static boolean isExit = false;

    private AppRunner() {
        products.addAll(new Product[]{
                new Water(ActionLetter.B, 20),
                new CocaCola(ActionLetter.C, 50),
                new Soda(ActionLetter.D, 30),
                new Snickers(ActionLetter.E, 80),
                new Mars(ActionLetter.F, 80),
                new Pistachios(ActionLetter.G, 130)
        });
        balance = acceptMoney(100);
    }

    public static void run() {
        AppRunner app = new AppRunner();
        while (!isExit) {
            app.startSimulation();
        }
    }

    private void startSimulation() {
        while (true) {
            print("В автомате доступны: ");
            showProducts(products);

            while (true) {
                print("Выберите способ оплаты:\n1. Монеты\n2. Карта");
                String paymentChoice = fromConsole().trim();

                if ("1".equals(paymentChoice)) {
                    setPaymentObj(new CoinPaymentReceiver());
                    initialize();
                    break;
                } else if ("2".equals(paymentChoice)) {
                    setPaymentObj(new CardPaymentReceiver());
                    initialize();
                    break;
                } else if ("h".equals(paymentChoice)) {
                    return;
                } else {
                    print("Недопустимый выбор. Попробуйте еще раз или введите 'h' для выхода.");
                }
            }

            print("Денег на балансе: " + balance);

            UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
            allowProducts.addAll(getAllowedProducts().toArray());
            chooseAction(allowProducts);

            print("Хотите совершить новую покупку? Введите 'y' для продолжения или 'h' для выхода.");
            String continueChoice = fromConsole().trim();
            if ("h".equals(continueChoice)) {
                return;
            }
        }
    }

    private UniversalArray<Product> getAllowedProducts() {
        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        for (int i = 0; i < products.size(); i++) {
            if (balance >= products.get(i).getPrice()) {
                allowProducts.add(products.get(i));
            }
        }
        return allowProducts;
    }

    private void chooseAction(UniversalArray<Product> products) {
        while (true) {
            UniversalArray<Product> allowedProducts = getAllowedProducts(products);
            showActions(allowedProducts);
            print(" a - Пополнить баланс");
            print(" h - Выйти/поменять тип оплаты");

            String action = fromConsole().toLowerCase();
            if ("h".equals(action)) {
                isExit = true;
                break;
            } else if ("a".equals(action)) {
                balance = acceptMoney(20);
                System.out.println("Ваш баланс: " + getCurrentAmount());
            } else {
                boolean isValidAction;
                ActionLetter actionLetter = null;

                try {
                    actionLetter = ActionLetter.valueOf(action.toUpperCase());
                    isValidAction = true;
                } catch (IllegalArgumentException e) {
                    isValidAction = false;
                }

                if (isValidAction) {
                    for (int i = 0; i < allowedProducts.size(); i++) {
                        if (allowedProducts.get(i).getActionLetter() == actionLetter) {
                            int productPrice = allowedProducts.get(i).getPrice();
                            if (balance >= productPrice) {
                                balance = acceptMoney(-productPrice);
                                print("Вы купили " + allowedProducts.get(i).getName() + "\nВаш баланс: " + balance);
                            } else {
                                print("У вас недостаточно средств для покупки " + allowedProducts.get(i).getName());
                            }
                            showProducts(allowedProducts);
                        }
                    }
                } else {
                    print("Недопустимая буква. Попробуйте еще раз или введите 'h' для выхода.");
                }
            }
        }
    }

    private UniversalArray<Product> getAllowedProducts(UniversalArray<Product> products) {
        UniversalArray<Product> allowedProducts = new UniversalArrayImpl<>();
        for (int i = 0; i < products.size(); i++) {
            if (balance >= products.get(i).getPrice()) {
                allowedProducts.add(products.get(i));
            }
        }
        return allowedProducts;
    }



    private void showActions(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(String.format(" %s - %s", products.get(i).getActionLetter().getValue(), products.get(i).getName()));
        }
    }

    private String fromConsole() {
        return new Scanner(System.in).nextLine();
    }

    private void showProducts(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(products.get(i).toString());
        }
    }

    private void print(String msg) {
        System.out.println(msg);
    }
}
