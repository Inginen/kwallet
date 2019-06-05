package com.inginen.service

import com.inginen.storage.WalletStorage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ThreadLocalRandom

/**
 * @author kharkov
 * @since 05.06.19
 */
class WalletServiceStressTest {

    private val walletInitialBalance = 1000.0
    private val walletsNumber = 10L

    private val operationsNumber = 1_000 // you can set over 1 million, just need to turn off logging for quick completion. Did not haw much time sorry

    private val latch: CountDownLatch = CountDownLatch(operationsNumber)
    private val walletService: WalletService = WalletServiceImpl(WalletStorage())

    @Before
    fun init() {
        // prepare test wallets
        for (i in 1..walletsNumber)
            walletService.create(walletInitialBalance)
    }

    @Test
    fun testHugeNumberOfConcurrentOperations() {
        Assert.assertEquals(
            (walletsNumber * walletInitialBalance).toInt(),
            walletService.getAll().sumBy { it.balance.toInt() })

        for (i in 1..operationsNumber) {
            GlobalScope.launch {
                randomTransferOperation()
            }
        }
        // waiting for coroutines to complete
        latch.await()
        //check total wallets balance wasn't changed
        Assert.assertEquals(
            (walletsNumber * walletInitialBalance).toInt(),
            walletService.getAll().sumBy { it.balance.toInt() })
    }

    private fun randomTransferOperation() {
        try {
            walletService.transfer(getRandomWalletId(), getRandomWalletId(), 10.0)
        } catch (e: Exception) {
            //just proceed. No need to process in test purpose
        } finally {
            latch.countDown()
        }
    }

    private fun getRandomWalletId(): Long = ThreadLocalRandom.current().nextLong(walletsNumber)
}