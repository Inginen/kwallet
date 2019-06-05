package com.inginen.route

import com.inginen.exception.*
import com.inginen.route.request.DepositRequest
import com.inginen.route.request.TransferRequest
import com.inginen.route.request.WalletRequest
import com.inginen.route.request.WithdrawRequest
import com.inginen.service.WalletService
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.util.pipeline.PipelineContext
import java.lang.IllegalArgumentException

/**
 * @author kharkov
 * @since 05.06.19
 */

fun Route.wallet(walletService: WalletService) {

    get("/wallet/{id}") {
        handleException {
            val post = call.parameters["id"]?.toLongOrNull() ?: throw IllegalArgumentException("test")
            call.respond(walletService.get(post))
        }
    }
    post("/wallet") {
        handleException {
            val wallet = call.receive<WalletRequest>()
            call.respond(walletService.create(wallet.balance))
        }
    }
    post("/deposit") {
        handleException {
            val deposit = call.receive<DepositRequest>()
            walletService.deposit(deposit.to, deposit.amount)
            call.respond(HttpStatusCode.OK)
        }
    }
    post("/withdraw") {
        handleException {
            val withdraw = call.receive<WithdrawRequest>()
            walletService.withdraw(withdraw.from, withdraw.amount)
            call.respond(HttpStatusCode.OK)
        }
    }
    post("/transfer") {
        handleException {
            val transfer = call.receive<TransferRequest>()
            walletService.transfer(transfer.from, transfer.to, transfer.amount)
            call.respond(HttpStatusCode.OK)
        }
    }
}

private suspend fun <R> PipelineContext<*, ApplicationCall>.handleException(block: suspend () -> R): R? {
    return try {
        block()
    } catch (e: Exception) {
        when (e) {
            is WalletNotFoundException,
            is LowBalanceException,
            is ExceedingMaxBalanceException,
            is NegativeAmountException,
            is UnsupportedOperation -> call.respond(HttpStatusCode.Conflict, mapOf("error" to e.message))
            else -> call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.message))
        }
        null
    }
}