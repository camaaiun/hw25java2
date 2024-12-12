package payment;

public class CoinPaymentReceiver implements Payment {
    private int currentAmount;

    @Override
    public void initialize() {}

    @Override
    public void acceptMoney(int amount) {
        currentAmount += amount;
        if (amount >= 0) {
            System.out.println("Вы пополнили автомат на " + amount);
        } else {
            System.out.println("У вас с баланса снялось - " + amount);
        }
    }

    @Override
    public int getCurrentAmount() {
        return currentAmount;
    }
}
