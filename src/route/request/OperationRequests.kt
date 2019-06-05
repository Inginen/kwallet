package com.inginen.route.request

/**
 * @author kharkov
 * @since 05.06.19
 */


class TransferRequest(val from: Long,val to: Long, val amount: Double)
class DepositRequest(val to: Long, val amount: Double)
class WithdrawRequest(val from: Long, val amount: Double)
class WalletRequest(val balance: Double)