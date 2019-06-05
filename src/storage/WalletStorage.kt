package com.inginen.storage

import com.inginen.model.Wallet
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

/**
 * @author kharkov
 * @since 05.06.19
 */

class WalletStorage {
    private val wallets: ConcurrentHashMap<Long, Wallet> = ConcurrentHashMap()
    private val walletCounter: AtomicLong = AtomicLong()

    fun create(balance: Double = 0.0): Wallet {
        val walletId = walletCounter.incrementAndGet()
        val wallet = Wallet(walletId, balance)
        wallets[walletId] = wallet
        return wallet
    }

    fun get(id: Long): Wallet? = wallets[id]

    fun getAll(): Collection<Wallet> = wallets.values
}