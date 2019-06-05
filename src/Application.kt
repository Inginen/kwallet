package com.inginen

import com.inginen.route.wallet
import com.inginen.service.WalletService
import com.inginen.service.WalletServiceImpl
import com.inginen.storage.WalletStorage
import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.gson.*
import io.ktor.features.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

val storage: WalletStorage = WalletStorage()
val wallService: WalletService = WalletServiceImpl(storage)

@Suppress("unused")
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(CallLogging)
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
    routing {
        wallet(wallService)
    }
}

