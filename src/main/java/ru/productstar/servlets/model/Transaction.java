package ru.productstar.servlets.model;


public class Transaction {
    private final String type; // income или expense
    private final String name;
    private final int amount;

    public Transaction(String type, String name, int amount) {
        this.type = type;
        this.name = name;
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                '}';
    }
}
