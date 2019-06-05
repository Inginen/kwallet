package com.inginen.model

import java.util.concurrent.locks.ReentrantLock

/**
 * @author kharkov
 * @since 05.06.19
 */

data class Wallet(val id: Long, var balance: Double, @Transient val lock: ReentrantLock = ReentrantLock()) {
    fun withdraw(amount: Double) {
        balance -= amount
    }

    fun deposit(amount: Double) {
        balance += amount
    }
}