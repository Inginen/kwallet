package com.inginen.service

import com.inginen.model.Wallet

/**
 * @author kharkov
 * @since 05.06.19
 */

interface WalletService {
    fun get(id: Long): Wallet
    fun getAll(): Collection<Wallet>
    fun create(balance: Double): Wallet

    fun deposit(to: Long, amount: Double)
    fun withdraw(from: Long, amount: Double)
    fun transfer(from: Long, to: Long, amount: Double)
}