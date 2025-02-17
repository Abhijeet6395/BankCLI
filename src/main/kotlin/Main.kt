import java.util.UUID

enum class AccountType {
    Current,
    Savings
}

open class Person(var name: String, var email: String, var pin: Int) {//open is used to make classes inheritable classes by default are final.
    fun changePin() {
        println("Enter your email:")
        val email = readlnOrNull()
        if (email == this.email) {
            println("Enter new pin:")
            val newPin = readlnOrNull()?.toIntOrNull()
            if (newPin != null) {
                this.pin = newPin
                println("Pin changed successfully.")
            } else {
                println("Invalid pin.")
            }
        } else {
            println("Email does not match.")
        }
    }
}

class Account(var accountNumber: String, var accountType: AccountType, var balance: Int) {
    fun deposit(amount: Int): Boolean {
        return if (balance + amount > 1_000_000) {
            println("Deposit failed. Balance cannot exceed 1,000,000 rupees.")
            false
        } else {
            balance += amount
            println("Deposit successful. New Balance: $balance")
            true
        }
    }

    fun withdraw(amount: Int): Boolean {
        return if (amount > balance) {
            println("Withdrawal failed. Insufficient balance.")
            false
        } else {
            balance -= amount
            println("Withdrawal successful. New Balance: $balance")
            true
        }
    }
}

class User(name: String, email: String, pin: Int, var account: Account) : Person(name, email, pin) {
    fun displayDetails() {
        println("Name: $name, AccountNumber: ${account.accountNumber}, AccountType: ${account.accountType}, Balance: ${account.balance}")
    }
}

class BankManager(name: String, email: String, pin: Int, private var accountNumber: String, private var branch: String, private var role: String) : Person(name, email, pin) {
    fun displayDetails() {
        println("Name: $name, AccountNumber: $accountNumber, Branch: $branch, Role: $role")
    }
}

fun main() {
    val users = mutableListOf(
        User("Bond", "abc@gmail.com", 1234, Account(UUID.randomUUID().toString(), AccountType.Savings, 0)),
        User("Alice", "abc@gmail.com", 1234, Account(UUID.randomUUID().toString(), AccountType.Savings, 0)),
        User("Bond", "abc@gmail.com", 1234, Account(UUID.randomUUID().toString(), AccountType.Savings, 0))
    )
    val bankManager = BankManager("John", "abc@gmail.com", 1234, "123456", "MainBranch", "Manager")

    while (true) {
        println("Do you want to sign in as a customer or a banker?")
        println("Enter 1 for Banker")
        println("Enter 2 for Customer")

        when (readlnOrNull()?.toIntOrNull()) {
            1 -> {
                if (validatePin(bankManager)) {
                    bankManager.displayDetails()
                    manageBanker(users, bankManager)
                } else {
                    println("Invalid pin.")
                }
            }
            2 -> {
                println("Enter your name:")
                val name = readlnOrNull()
                val matchingUsers = users.filter { it.name == name }

                if (matchingUsers.isNotEmpty()) {
                    val user = selectUser(matchingUsers)
                    if (validatePin(user)) {
                        manageUser(users, user)
                    } else {
                        println("Invalid pin.")
                    }
                } else {
                    println("User not found.")
                }
            }
            else -> println("Invalid selection. Please enter 1 or 2.")
        }
    }
}

fun validatePin(person: Person): Boolean {
    var attempts = 0
    while (attempts < 3) {
        println("Enter pin:")
        val enteredPin = readlnOrNull()?.toIntOrNull()
        if (enteredPin == person.pin) {
            return true
        } else {
            attempts++
            println("Invalid pin.")
            if (attempts == 2) {
                println("Forgot pin? Enter 'yes' to reset pin or 'no' to try again:")
                if (readlnOrNull()?.uppercase() == "YES") {
                    resetPin(person)
                    return false
                }
            }
        }
    }
    return false
}

fun resetPin(person: Person) {
    println("Enter your email:")
    val email = readlnOrNull()
    if (email == person.email) {
        println("Enter new pin:")
        val newPin = readlnOrNull()?.toIntOrNull()
        if (newPin != null) {
            person.pin = newPin
            println("Pin changed successfully.")
        } else {
            println("Invalid pin.")
        }
    } else {
        println("Email does not match.")
    }
}

fun manageBanker(users: MutableList<User>, bankManager: BankManager) {
    while (true) {
        println("1. List Customer Details")
        println("2. Add Customer Details")
        println("3. Remove Customer Details")
        println("4. Change Pin")
        println("5. Go Back")

        when (readlnOrNull()?.toIntOrNull()) {
            1 -> listCustomerDetails(users)
            2 -> addUser(users)
            3 -> removeUser(users)
            4 -> bankManager.changePin()
            5 -> return
            else -> println("Invalid selection. Please enter 1, 2, 3, 4, or 5.")
        }
    }
}

fun listCustomerDetails(users: List<User>) {
    println("Customer Details:")
    users.forEach { it.displayDetails() }
}

fun addUser(users: MutableList<User>) {
    println("Enter the name of the new Customer:")
    val name = readlnOrNull()
    println("Enter the account type (SAVINGS or CURRENT):")
    val accountTypeInput = readlnOrNull()
    println("Enter the initial balance:")
    val balance = readlnOrNull()?.toIntOrNull()
    println("Set a default pin:")
    val pin = readlnOrNull()?.toIntOrNull()

    val accountType = try {
        AccountType.valueOf(accountTypeInput!!.uppercase())
    } catch (e: IllegalArgumentException) {
        println("Invalid account type. Please enter SAVINGS or CURRENT.")
        return
    }

    if (name != null && balance != null && pin != null) {
        users.add(User(name, "abc@gmail.com", pin, Account(UUID.randomUUID().toString(), accountType, balance)))
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
        println("Press 3 to change pin")
        println("Press 4 to go back")

        when (readlnOrNull()?.toIntOrNull()) {
            1 -> {
                editUserDetails(users, user)
                return
            }
            2 -> manageBalance(user)
            3 -> user.changePin()
            4 -> return
            else -> println("Invalid selection. Please enter 1, 2, 3, or 4.")
        }
    }
}

fun editUserDetails(users: MutableList<User>, user: User) {
    users.remove(user)
    println("Enter the new name for the user:")
    val newName = readlnOrNull()
    println("Enter the new account type (SAVINGS or CURRENT):")
    val newAccountTypeInput = readlnOrNull()
    println("Enter the new initial balance:")
    val newBalance = readlnOrNull()?.toIntOrNull()

    val newAccountType = try {
        AccountType.valueOf(newAccountTypeInput!!.uppercase())
    } catch (e: IllegalArgumentException) {
        println("Invalid account type. Please enter SAVINGS or CURRENT.")
        return
    }

    if (newName != null && newBalance != null) {
        users.add(User(newName, user.email, user.pin, Account(UUID.randomUUID().toString(), newAccountType, newBalance)))
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
