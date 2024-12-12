package payment;

public interface Payment {
    void initialize();
    void acceptMoney(int amount);
    int getCurrentAmount();
}
