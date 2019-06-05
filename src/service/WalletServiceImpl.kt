package com.inginen.service

import com.inginen.exception.*
import com.inginen.model.Wallet
import com.inginen.storage.WalletStorage
import java.util.concurrent.TimeUnit
import java.util.logging.Logger

/**
 * @author kharkov
 * @since 05.06.19
 */

class WalletServiceImpl(val walletStorage: WalletStorage) : WalletService {

    override fun get(id: Long): Wallet = walletStorage.get(id) ?: throw WalletNotFoundException(id)

    override fun getAll(): Collection<Wallet> = walletStorage.getAll()

    override fun create(balance: Double): Wallet {
        if (balance < MIN_AMOUNT) throw NegativeAmountException()
        val newWallet = walletStorage.create(balance)
        LOG.info("Created new wallet. Wallet: ${newWallet.id}, balance ${newWallet.balance}")
        return newWallet
    }

    override fun deposit(to: Long, amount: Double) {
        if (amount < MIN_AMOUNT) throw NegativeAmountException()
        val toWallet = walletStorage.get(to) ?: throw WalletNotFoundException(to)
        try {
            lock(listOf(toWallet))

            if ((toWallet.balance + amount) > MAX_BALANCE) throw ExceedingMaxBalanceException(to)

            toWallet.deposit(amount)
            LOG.info("Deposit operation. Wallet: $to, amount $amount. Current balance is ${toWallet.balance}")
        } finally {
            unlock(listOf(toWallet))
        }
    }

    override fun withdraw(from: Long, amount: Double) {
        if (amount < MIN_AMOUNT) throw NegativeAmountException()
        val fromWallet = walletStorage.get(from) ?: throw WalletNotFoundException(from)
        try {
            lock(listOf(fromWallet))

            if ((fromWallet.balance - amount) < MIN_AMOUNT) throw LowBalanceException(from)

            fromWallet.withdraw(amount)
            LOG.info("Withdraw operation. Wallet: $from, amount $amount. Current balance is ${fromWallet.balance}")
        } finally {
            unlock(listOf(fromWallet))
        }
    }

    override fun transfer(from: Long, to: Long, amount: Double) {
        if (from == to) throw UnsupportedOperation("Unable to transfer to your own wallet.")
        if (amount < MIN_AMOUNT) throw NegativeAmountException()
        val toWallet = walletStorage.get(to) ?: throw WalletNotFoundException(to)
        val fromWallet = walletStorage.get(from) ?: throw WalletNotFoundException(from)
        try {
            lock(listOf(toWallet, fromWallet))

            if ((toWallet.balance + amount) > MAX_BALANCE) throw ExceedingMaxBalanceException(to)
            if ((fromWallet.balance - amount) < MIN_AMOUNT) throw LowBalanceException(from)

            toWallet.deposit(amount)
            fromWallet.withdraw(amount)
            LOG.info("Transfer operation. Transferred from : $from to $to, amount $amount")
        } finally {
            unlock(listOf(toWallet, fromWallet))
        }
    }

    fun lock(wallets: List<Wallet>) {
        wallets.sortedBy { it.id }
            .forEach { tryLock(it) }
    }

    fun unlock(wallets: List<Wallet>) {
        wallets.sortedByDescending { it.id }
            .forEach { unlock(it) }
    }

    fun tryLock(wallet: Wallet) = wallet.lock.tryLock(2, TimeUnit.SECONDS)

    fun unlock(wallet: Wallet) = wallet.lock.unlock()

    companion object {
        const val MIN_AMOUNT = 0.0
        const val MAX_BALANCE = 1_000_000.0
        val LOG = Logger.getLogger(WalletServiceImpl::class.java.name)
    }
}