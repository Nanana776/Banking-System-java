Implement a banking system with OOP
Completion requirements

Banking System Specification
Requirements
1. Classes and Attributes
BankAccount
Attributes:
accountNumber
accountHolderName
balance

Methods:
deposit
withdraw
getBalance() (returns balance)

Abstract: displayAccountDetails()
SavingsAccount
Extends BankAccount

Additional Attributes:
interestRate

Methods:
calculateInterest() calculates interest on balance and adds it
CurrentAccount
Extends BankAccount

Additional Attributes:
overdraftLimit

Methods:
Override withdraw to allow overdraft up to the limit.

2. Encapsulation
All attributes to be private.
Provide getter and setter methods where necessary.
Validate inputs; for example, a balance should not be negative.

3. Interfaces
Transaction

Methods:
transfer(BankAccount from, BankAccount to, double amount)
Reportable

Methods:
generateAccountReport().

4. Polymorphism
Employ polymorphism to deal with multiple kinds of accounts; for example, a BankAccount reference referring to a SavingsAccount or a CurrentAccount.

5. Exception Handling
Implement custom exceptions:
InsufficientFundsException: Thrown when withdrawal exceeds balance or overdraft limit.
InvalidTransactionException: Thrown for invalid operations (e.g., negative deposit).

6. Operations
Implement a class Bank to handle all accounts and offer the following operations:
Create Account
Create either a SavingsAccount or CurrentAccount based on user input.
Deposit Money
Add a specific amount to an account's balance.
Withdraw Money
Withdraw a specified amount from an account balance - with overdraft allowance for CurrentAccount.
Transfer Money
Transfer funds between two accounts.
Calculate Interest
Compute and apply interest for SavingsAccount objects.

7. Menu and Workflow
These are the options that users should be prompted to select what they want to do:

<1. Create Account
2. View Account Details
3. Deposit Money
4. Withdraw Money
5. Transfer Money
6. Calculate Interest (Savings Account)
7. Exit>

