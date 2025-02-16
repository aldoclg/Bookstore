package com.bookstore.application.models;

public enum BookType {

    NEW_RELEASES {
        public double apply(double amount) {
            return 1;
        }
        public boolean hasDiscount(long loyaltyPoints) {
            return false;
        }
    },
    REGULAR {
        public double apply(double amount) {
            return amount >= 3 ? 0.9 : 1;
        }
        public boolean hasDiscount(long loyaltyPoints) {
            return loyaltyPoints >= 10;
        }
    },
    OLD_EDITIONS {
        public double apply(double amount) {
            return amount >= 3 ? 0.75 : 0.8;
        }
        public boolean hasDiscount(long loyaltyPoints) {
            return loyaltyPoints >= 10;
        }
    };

    public abstract double apply(double amount);
    public abstract boolean hasDiscount(long loyaltyPoints);
}
