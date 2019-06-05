package com.inginen.service

import com.inginen.exception.*
import com.inginen.model.Wallet
import com.inginen.storage.WalletStorage
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test

/**
 * @author kharkov
 * @since 05.06.19
 */

class WalletServiceTest {
    val storageMock = mockk<WalletStorage>()
    val walletService: WalletService = WalletServiceImpl(storageMock)

    @Test
    fun getWalletSuccessTest() {
        val testWallet = Wallet(1, 0.0)
        every { storageMock.get(any()) } returns testWallet
        Assert.assertEquals(walletService.get(1), testWallet)
    }

    @Test(expected = WalletNotFoundException::class)
    fun getNotExistingWalletTest() {
        every { storageMock.get(any()) } returns null
        walletService.get(1L)
    }

    @Test
    fun depositSuccessTest() {
        val testWallet = Wallet(1, 0.0)
        every { storageMock.get(any()) } returns testWallet

        walletService.deposit(1, 10.0)

        Assert.assertEquals(testWallet.balance, 10.0, 0.0)
    }

    @Test(expected = WalletNotFoundException::class)
    fun depositWithNotExistingWalletTest() {
        every { storageMock.get(any()) } returns null

        walletService.deposit(1, 10.0)
    }

    @Test(expected = NegativeAmountException::class)
    fun depositWithNegativeAmountTest() {
        val testWallet = Wallet(1, 0.0)
        every { storageMock.get(any()) } returns testWallet

        walletService.deposit(1, -10.0)
    }

    @Test(expected = ExceedingMaxBalanceException::class)
    fun depositExceedingMaxBalanceTest() {
        val testWallet = Wallet(1, 0.0)
        every { storageMock.get(any()) } returns testWallet

        walletService.deposit(1, 10000000000.0)
    }

    @Test
    fun withdrawSuccessTest() {
        val testWallet = Wallet(1, 10.0)
        every { storageMock.get(any()) } returns testWallet

        walletService.withdraw(1, 10.0)

        Assert.assertEquals(testWallet.balance, 0.0, 0.0)
    }

    @Test(expected = WalletNotFoundException::class)
    fun withdrawWithNotExistingWalletTest() {
        every { storageMock.get(any()) } returns null

        walletService.withdraw(1, 10.0)
    }

    @Test(expected = NegativeAmountException::class)
    fun withdrawWithNegativeAmountTest() {
        val testWallet = Wallet(1, 0.0)
        every { storageMock.get(any()) } returns testWallet

        walletService.withdraw(1, -10.0)
    }

    @Test(expected = LowBalanceException::class)
    fun withdrawFromWalletWithLowBalanceTest() {
        val testWallet = Wallet(1, 0.0)
        every { storageMock.get(any()) } returns testWallet

        walletService.withdraw(1, 10000000000.0)
    }

    @Test
    fun transferSuccessTest() {
        val testToWallet = Wallet(1, 10.0)
        val testfromWallet = Wallet(2, 10.0)
        every { storageMock.get(1) } returns testToWallet
        every { storageMock.get(2) } returns testfromWallet

        walletService.transfer(2, 1, 10.0)

        Assert.assertEquals(testToWallet.balance, 20.0, 0.0)
        Assert.assertEquals(testfromWallet.balance, 0.0, 0.0)
    }

    @Test(expected = WalletNotFoundException::class)
    fun transferWithNotExistingToWalletTest() {
        val testfromWallet = Wallet(2, 10.0)
        every { storageMock.get(1) } returns null
        every { storageMock.get(2) } returns testfromWallet

        walletService.transfer(2, 1, 10.0)
    }

    @Test(expected = WalletNotFoundException::class)
    fun transferWithNotExistingFromWalletTest() {
        val testToWallet = Wallet(1, 10.0)
        every { storageMock.get(1) } returns testToWallet
        every { storageMock.get(2) } returns null

        walletService.transfer(2, 1, 10.0)
    }

    @Test(expected = UnsupportedOperation::class)
    fun transferYourOwnWalletTest() {
        val testToWallet = Wallet(1, 10.0)
        every { storageMock.get(1) } returns testToWallet

        walletService.transfer(1, 1, 10.0)
    }

    @Test(expected = NegativeAmountException::class)
    fun transferWithNegativeAmountTest() {
        val testToWallet = Wallet(1, 10.0)
        val testfromWallet = Wallet(2, 10.0)
        every { storageMock.get(1) } returns testToWallet
        every { storageMock.get(2) } returns testfromWallet

        walletService.transfer(2, 1, -10.0)
    }

    @Test(expected = LowBalanceException::class)
    fun transferFromWalletWithLowBalanceTest() {
        val testToWallet = Wallet(1, 10.0)
        val testfromWallet = Wallet(2, 10.0)
        every { storageMock.get(1) } returns testToWallet
        every { storageMock.get(2) } returns testfromWallet

        walletService.transfer(2, 1, 20.0)
    }

    @Test(expected = ExceedingMaxBalanceException::class)
    fun transferExceedingMaxBalanceTest() {
        val testToWallet = Wallet(1, 600000.0)
        val testfromWallet = Wallet(2, 600000.0)
        every { storageMock.get(1) } returns testToWallet
        every { storageMock.get(2) } returns testfromWallet

        walletService.transfer(2, 1, 600000.0)
    }
}