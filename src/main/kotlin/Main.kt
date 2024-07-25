fun main(){
    val bankerMap = mutableMapOf(
        "AccountNumber" to "123456",
        "Branch" to "MainBranch",
        "Role" to "Manager"
    )

    val userMap = mutableMapOf(
        "Name" to "Bond",
        "AccountNumber" to "654321",
        "AccountType" to "Savings",
        "Balance" to "50000"
    )

    val value = mapOf(1 to "Banker", 2 to "User")
    val maps = mapOf(1 to bankerMap, 2 to userMap)

    while (true) {
        println("Do you want to sign in as a customer or a banker?")
        println("Enter 1 for Banker")
        println("Enter 2 for User")

        val input = readlnOrNull()?.toIntOrNull()

        if (input in value.keys) {
            println("You selected: ${value[input]}")

            if (input == 1) {
                val selectedMap = maps[input]
                selectedMap?.forEach { (key, value) ->
                    println("$key: $value")
                }

                while (true) {
                    println("1. List Customer Details")
                    println("2. Add User Details")
                    println("3. Remove User Details")
                    println("4. Go Back")

                    when (readlnOrNull()?.toIntOrNull()) {
                        1 -> {
                            println("User Details:")
                            userMap.forEach { (key, value) ->
                                println("$key: $value")
                            }
                        }

                        2 -> {
                            println("Enter the key you want to add:")
                            val key = readlnOrNull()
                            println("Enter the value for $key:")
                            val value = readlnOrNull()
                            if (key != null && value != null) {
                                userMap[key] = value
                                println("User detail added successfully.")
                                println("Updated User Details:")
                                userMap.forEach { (k, v) ->
                                    println("$k: $v")
                                }
                            } else {
                                println("Invalid input.")
                            }
                        }

                        3 -> {
                            println("Enter the key you want to remove:")
                            val key = readlnOrNull()
                            if (key != null && userMap.containsKey(key)) {
                                userMap.remove(key)
                                println("User detail removed successfully.")
                                println("Updated User Details:")
                                userMap.forEach { (k, v) ->
                                    println("$k: $v")
                                }
                            } else {
                                println("Key not found or invalid input.")
                            }
                        }

                        4 -> {
                            break
                        }

                        else -> {
                            println("Invalid selection. Please enter 1, 2, 3, or 4.")
                        }
                    }
                }
            } else if (input == 2) {
                while (true) {
                    println("User Details:")
                    userMap.forEach { (key, value) ->
                        println("$key: $value")
                    }

                    println("Press 1 to edit user details")
                    println("Press 2 to manage balance")
                    println("Press 3 to go back")

                    when (readlnOrNull()?.toIntOrNull()) {
                        1 -> {
                            println("Enter the key you want to edit:")
                            val key = readlnOrNull()
                            println("Enter the new value for $key:")
                            val value = readlnOrNull()
                            if (key != null && value != null) {
                                userMap[key] = value
                                println("User detail updated successfully.")
                                println("Updated User Details:")
                                userMap.forEach { (k, v) ->
                                    println("$k: $v")
                                }
                            } else {
                                println("Invalid input.")
                            }
                        }

                        2 -> {
                            while (true) {
                                println("Current Balance: ${userMap["Balance"]}")
                                println("Press 1 to deposit money")
                                println("Press 2 to withdraw money")
                                println("Press 3 to go back")

                                when (readlnOrNull()?.toIntOrNull()) {
                                    1 -> {
                                        println("Enter the amount to deposit:")
                                        val amount = readlnOrNull()?.toIntOrNull()
                                        if (amount != null && amount > 0) {
                                            val currentBalance = userMap["Balance"]?.toIntOrNull() ?: 0
                                            val newBalance = currentBalance + amount
                                            if (newBalance <= 1_000_000) {
                                                userMap["Balance"] = newBalance.toString()
                                                println("Deposit successful. New Balance: $newBalance")
                                            } else {
                                                println("Deposit failed. Balance cannot exceed 1,000,000 rupees.")
                                            }
                                        } else {
                                            println("Invalid amount.")
                                        }
                                    }

                                    2 -> {
                                        println("Enter the amount to withdraw:")
                                        val amount = readlnOrNull()?.toIntOrNull()
                                        if (amount != null && amount > 0) {
                                            val currentBalance = userMap["Balance"]?.toIntOrNull() ?: 0
                                            if (amount <= currentBalance) {
                                                val newBalance = currentBalance - amount
                                                userMap["Balance"] = newBalance.toString()
                                                println("Withdrawal successful. New Balance: $newBalance")
                                            } else {
                                                println("Withdrawal failed. Insufficient balance.")
                                            }
                                        } else {
                                            println("Invalid amount.")
                                        }
                                    }

                                    3 -> {
                                        break
                                    }

                                    else -> {
                                        println("Invalid selection. Please enter 1, 2, or 3.")
                                    }
                                }
                            }
                        }

                        3 -> {
                            break
                        }

                        else -> {
                            println("Invalid selection. Please enter 1, 2, or 3.")
                        }
                    }
                }
            }
        } else {
            println("Invalid selection. Please enter 1 or 2.")
        }
    }
}
