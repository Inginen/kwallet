package com.inginen.exception

/**
 * @author kharkov
 * @since 05.06.19
 */

class WalletNotFoundException(id: Long) : Exception("Wallet with id $id not found.")
class LowBalanceException(id: Long) : Exception("Unable to perform withdraw. Balance is to low for wallet $id.")
class ExceedingMaxBalanceException(id: Long) : Exception("Unable to perform deposit. Max balance is exceeded for wallet $id.")
class NegativeAmountException : Exception("Negative amount is not allowed.")
class UnsupportedOperation(message: String): Exception(message)