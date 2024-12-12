import java.util.*;

class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}

class InvalidTransactionException extends Exception {
    public InvalidTransactionException(String message) {
        super(message);
    }
}

abstract class BankAccount {
    private String accountNumber;
    private String accountHolderName;
    private double balance;

    public BankAccount(String accountNumber, String accountHolderName, double initialBalance) throws InvalidTransactionException {
        if (initialBalance < 0) {
            throw new InvalidTransactionException("Initial balance cannot be negative.");
        }
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = initialBalance;
        }

    public String getAccountNumber() {
        return accountNumber;
        }

    public String getAccountHolderName() {
        return accountHolderName;
        }

    public double getBalance() {
        return balance;
        }

    protected void setBalance(double balance) {
        this.balance = balance;
            }

    public void deposit(double amount) throws InvalidTransactionException {
        if (amount<=0) {
            throw new InvalidTransactionException("Deposit can't be a negative number.");
        }
        this.balance += amount;
        }

    public void withdraw(double amount) throws InsufficientFundsException, InvalidTransactionException {
        if (amount<=0) {
            throw new InvalidTransactionException("Withdrawal can't be a negative number.");
             }
        if (amount>this.balance) {
            throw new InsufficientFundsException("Insufficient funds.");
            }
        this.balance -= amount;
    }

    public abstract void displayAccountDetails();
    }

class SavingsAccount extends BankAccount {
    private double interestRate;

    public SavingsAccount(String accountNumber, String accountHolderName, double initialBalance, double interestRate) throws InvalidTransactionException {
        super(accountNumber, accountHolderName, initialBalance);
        this.interestRate = interestRate;
        }

    public void calculateInterest() {
        double interest = getBalance() * (interestRate/100);
        setBalance(getBalance() + interest);
        }

    @Override
    public void displayAccountDetails() {
        System.out.println("Savings Account: " + getAccountNumber() + ", Holder: " + getAccountHolderName() + ", Balance: " + getBalance());
        }
    }

class CurrentAccount extends BankAccount {
    private double overdraftLimit;

    public CurrentAccount(String accountNumber, String accountHolderName, double initialBalance, double overdraftLimit) throws InvalidTransactionException {
        super(accountNumber, accountHolderName, initialBalance);
        this.overdraftLimit = overdraftLimit;
                }

    @Override
    public void withdraw(double amount) throws InsufficientFundsException, InvalidTransactionException {
        if (amount<=0) {
            throw new InvalidTransactionException("Withdrawal can't be a negative number.");
             }
        if (amount > getBalance()+overdraftLimit) {
            throw new InsufficientFundsException("Withdrawal exceeds overdraft limit.");
                }
        setBalance(getBalance()-amount);
     }

    @Override
    public void displayAccountDetails() {
        System.out.println("Current Account: " + getAccountNumber() + ", Holder: " + getAccountHolderName() + ", Balance: " + getBalance());
            }
        }

interface Transaction {
    void transfer(BankAccount from, BankAccount to, double amount) throws InsufficientFundsException, InvalidTransactionException;
        }

interface Reportable {
    void generateAccountReport(BankAccount account);
}

class Bank implements Transaction, Reportable {
    private List<BankAccount> accounts = new ArrayList<>();

    public void createAccount(BankAccount account) {
        accounts.add(account);
        System.out.println("Account created successfully. Welcome!");
        }

    public BankAccount findAccount(String accountNumber) {
        for (BankAccount account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
            }
            return null;
    }

    @Override
    public void transfer(BankAccount from, BankAccount to, double amount) throws InsufficientFundsException, InvalidTransactionException {
        from.withdraw(amount);
        to.deposit(amount);
        System.out.println("Transfer successful.");
    }

    @Override
    public void generateAccountReport(BankAccount account) {
        account.displayAccountDetails();
    }

    public void calculateInterestForSavingsAccounts() {
        for (BankAccount account : accounts) {
            if (account instanceof SavingsAccount) {
                SavingsAccount savingsAccount = (SavingsAccount) account;
                double initialBalance = savingsAccount.getBalance();
                savingsAccount.calculateInterest();
                double interest = savingsAccount.getBalance() - initialBalance;
                System.out.println("Earned interest: " + interest);
            }
        }
    }
    
}

public class BankingSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Bank bank = new Bank();

        while (true) {
            System.out.println("\n1. Create Account");
            System.out.println("2. View Account Details");
            System.out.println("3. Deposit Money");
            System.out.println("4. Withdraw Money");
            System.out.println("5. Transfer Money");
            System.out.println("6. Calculate Interest (Savings Account)");
            System.out.println("7. Exit");
            System.out.print("Enter your choice number: ");
            int choice = scanner.nextInt();

            try {
                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter Account Type (Savings/Current): ");
                        String type = scanner.next();
                        System.out.print("Enter Account Number: ");
                        String accountNumber = scanner.next();
                        System.out.print("Enter Account Holder Name: ");
                        String accountHolderName = scanner.next();
                        System.out.print("Enter Initial Balance: ");
                        double initialBalance = scanner.nextDouble();

                        if (type.equalsIgnoreCase("Savings")) {
                            System.out.print("Enter Interest Rate: ");
                            double interestRate = scanner.nextDouble();
                            bank.createAccount(new SavingsAccount(accountNumber, accountHolderName, initialBalance, interestRate));
                        } else if (type.equalsIgnoreCase("Current")) {
                            System.out.print("Enter Overdraft Limit: ");
                            double overdraftLimit = scanner.nextDouble();
                            bank.createAccount(new CurrentAccount(accountNumber, accountHolderName, initialBalance, overdraftLimit));
                        } else {
                            System.out.println("Invalid account type.");
                        }
                    }
                    case 2 -> {
                        System.out.print("Enter Account Number: ");
                        String accountNumber = scanner.next();
                        BankAccount account = bank.findAccount(accountNumber);
                        if (account != null) {
                            account.displayAccountDetails();
                        } else {
                            System.out.println("Account not found.");
                        }
                    }
                    case 3 -> {
                        System.out.print("Enter Account Number: ");
                        String accountNumber = scanner.next();
                        System.out.print("Enter Deposit Amount: ");
                        double amount = scanner.nextDouble();
                        BankAccount account = bank.findAccount(accountNumber);
                        if (account != null) {
                            account.deposit(amount);
                            System.out.println("Deposit successful.");
                        } else {
                            System.out.println("Account not found.");
                        }
                    }
                    case 4 -> {
                        System.out.print("Enter Account Number: ");
                        String accountNumber = scanner.next();
                        System.out.print("Enter Withdrawal Amount: ");
                        double amount = scanner.nextDouble();
                        BankAccount account = bank.findAccount(accountNumber);
                        if (account != null) {
                            account.withdraw(amount);
                            System.out.println("Withdrawal successful.");
                        } else {
                            System.out.println("Account not found.");
                        }
                    }
                    case 5 -> {
                        System.out.print("Enter your Account Number: ");
                        String fromAccountNumber = scanner.next();
                        System.out.print("Enter recipient Account Number: ");
                        String toAccountNumber = scanner.next();
                        System.out.print("Enter Transfer Amount: ");
                        double amount = scanner.nextDouble();

                        BankAccount fromAccount = bank.findAccount(fromAccountNumber);
                        BankAccount toAccount = bank.findAccount(toAccountNumber);

                        if (fromAccount != null && toAccount != null) {
                            bank.transfer(fromAccount, toAccount, amount);
                        } else {
                            System.out.println("Account(s) not found.");
                        }
                    }
                    case 6 -> bank.calculateInterestForSavingsAccounts();
                    case 7 -> {
                        System.out.println("Exiting...");
                        scanner.close();
                        return;
                    }
                    default -> System.out.println("Invalid. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
