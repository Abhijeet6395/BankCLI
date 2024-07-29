import java.util.UUID

open class Person(var name: String)

class Account(var accountNumber: String, var accountType: String, var balance: Int) {
    fun deposit(amount: Int): Boolean {
        if (balance + amount > 1_000_000) {
            println("Deposit failed. Balance cannot exceed 1,000,000 rupees.")
            return false
        }
        balance += amount
        println("Deposit successful. New Balance: $balance")
        return true
    }

    fun withdraw(amount: Int): Boolean {
        if (amount > balance) {
            println("Withdrawal failed. Insufficient balance.")
            return false
        }
        balance -= amount
        println("Withdrawal successful. New Balance: $balance")
        return true
    }
}

class User(name: String, var account: Account) : Person(name) {
    fun displayDetails() {
        println("Name: $name, AccountNumber: ${account.accountNumber}, AccountType: ${account.accountType}, Balance: ${account.balance}")
    }
}

class BankManager(name: String, var accountNumber: String, var branch: String, var role: String) : Person(name) {
    fun displayDetails() {
        println("Name: $name, AccountNumber: $accountNumber, Branch: $branch, Role: $role")
    }
}

fun main() {
    val users = mutableListOf(
        User("Bond", Account(UUID.randomUUID().toString(), "Savings", 0)),
        User("Alice", Account(UUID.randomUUID().toString(), "Checking", 0)),
        User("Bond", Account(UUID.randomUUID().toString(), "Savings", 0))
    )
    val bankManager = BankManager("John", "123456", "MainBranch", "Manager")

    while (true) {
        println("Do you want to sign in as a customer or a banker?")
        println("Enter 1 for Banker")
        println("Enter 2 for User")

        when (readlnOrNull()?.toIntOrNull()) {
            1 -> {
                bankManager.displayDetails()
                manageBanker(users)
            }
            2 -> {
                println("Enter your name:")
                val name = readlnOrNull()
                val matchingUsers = users.filter { it.name == name }

                if (matchingUsers.isNotEmpty()) {
                    val user = selectUser(matchingUsers)
                    manageUser(users, user)
                } else {
                    println("User not found.")
                }
            }
            else -> println("Invalid selection. Please enter 1 or 2.")
        }
    }
}

fun manageBanker(users: MutableList<User>) {
    while (true) {
        println("1. List Customer Details")
        println("2. Add User Details")
        println("3. Remove User Details")
        println("4. Go Back")

        when (readlnOrNull()?.toIntOrNull()) {
            1 -> listCustomerDetails(users)
            2 -> addUser(users)
            3 -> removeUser(users)
            4 -> return
            else -> println("Invalid selection. Please enter 1, 2, 3, or 4.")
        }
    }
}

fun listCustomerDetails(users: List<User>) {
    println("User Details:")
    users.forEach { it.displayDetails() }
}

fun addUser(users: MutableList<User>) {
    println("Enter the name of the new user:")
    val name = readlnOrNull()
    println("Enter the account type:")
    val accountType = readlnOrNull()
    println("Enter the initial balance:")
    val balance = readlnOrNull()?.toIntOrNull()

    if (name != null && accountType != null && balance !=   null) {
        users.add(User(name, Account(UUID.randomUUID().toString(), accountType, balance)))
        println("User added successfully.")
    } else {
        println("Invalid input.")
    }
}

fun removeUser(users: MutableList<User>) {
    println("Enter the name of the user to remove:")
    val name = readlnOrNull()
    val matchingUsers = users.filter { it.name == name }

    if (matchingUsers.isNotEmpty()) {
        val user = selectUser(matchingUsers)
        users.remove(user)
        println("User removed successfully.")
    } else {
        println("User not found.")
    }
}

fun selectUser(matchingUsers: List<User>): User {
    return if (matchingUsers.size > 1) {
        println("Multiple users found. Please choose one:")
        matchingUsers.forEachIndexed { index, user ->
            println("${index + 1}. AccountNumber: ${user.account.accountNumber}, AccountType: ${user.account.accountType}, Balance: ${user.account.balance}")
        }
        val choice = readlnOrNull()?.toIntOrNull()
        if (choice != null && choice in 1..matchingUsers.size) {
            matchingUsers[choice - 1]
        } else {
            println("Invalid selection. Choosing the first user.")
            matchingUsers[0]
        }
    } else {
        matchingUsers[0]
    }
}

fun manageUser(users: MutableList<User>, user: User) {
    while (true) {
        user.displayDetails()
        println("Press 1 to edit user details")
        println("Press 2 to manage balance")
        println("Press 3 to go back")

        when (readlnOrNull()?.toIntOrNull()) {
            1 -> {
                editUserDetails(users, user)
                return // Exit after editing user details
            }
            2 -> manageBalance(user)
            3 -> return
            else -> println("Invalid selection. Please enter 1, 2, or 3.")
        }
    }
}

fun editUserDetails(users: MutableList<User>, user: User) {
    users.remove(user)
    println("Enter the new name for the user:")
    val newName = readlnOrNull()
    println("Enter the new account type:")
    val newAccountType = readlnOrNull()
    println("Enter the new initial balance:")
    val newBalance = readlnOrNull()?.toIntOrNull()

    if (newName != null && newAccountType != null && newBalance != null) {
        users.add(User(newName, Account(UUID.randomUUID().toString(), newAccountType, newBalance)))
        println("User detail updated successfully.")
    } else {
        println("Invalid input.")
    }
}

fun manageBalance(user: User) {
    while (true) {
        println("Current Balance: ${user.account.balance}")
        println("Press 1 to deposit money")
        println("Press 2 to withdraw money")
        println("Press 3 to go back")

        when (readlnOrNull()?.toIntOrNull()) {
            1 -> {
                println("Enter the amount to deposit:")
                val amount = readlnOrNull()?.toIntOrNull()
                if (amount != null && amount > 0) {
                    user.account.deposit(amount)
                } else {
                    println("Invalid amount.")
                }
            }
            2 -> {
                println("Enter the amount to withdraw:")
                val amount = readlnOrNull()?.toIntOrNull()
                if (amount != null && amount > 0) {
                    user.account.withdraw(amount)
                } else {
                    println("Invalid amount.")
                }
            }
            3 -> return
            else -> println("Invalid selection. Please enter 1, 2, or 3.")
        }
    }
}
