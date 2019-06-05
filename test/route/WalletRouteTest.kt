package com.inginen

import com.google.gson.Gson
import com.inginen.model.Wallet
import io.ktor.http.*
import kotlin.test.*
import io.ktor.server.testing.*

class WalletRouteTest {
    @Test
    fun testWalletApi() {
        withTestApplication({ module(testing = true) }) {

            //CREATE WALLET TEST
            handleRequest(HttpMethod.Post, "/wallet") {
                addHeader("Content-Type", "application/json")
                setBody(Gson().toJson(mapOf("balance" to 10.0)))
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                val createWallet = Gson().fromJson(response.content, Wallet::class.java)
                assertEquals(1, createWallet.id)
                assertEquals(10.0, createWallet.balance)
            }
            handleRequest(HttpMethod.Post, "/wallet") {
                addHeader("Content-Type", "application/json")
                setBody(Gson().toJson(mapOf("balance" to 10.0)))
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                val createWallet = Gson().fromJson(response.content, Wallet::class.java)
                assertEquals(2, createWallet.id)
                assertEquals(10.0, createWallet.balance)
            }

            //GET WALLET TEST
            handleRequest(HttpMethod.Get, "/wallet/1").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                val createWallet = Gson().fromJson(response.content, Wallet::class.java)
                assertEquals(1, createWallet.id)
                assertEquals(10.0, createWallet.balance)
            }

            //DEPOSIT TEST
            handleRequest(HttpMethod.Post, "/deposit") {
                addHeader("Content-Type", "application/json")
                setBody(Gson().toJson(mapOf("to" to 1, "amount" to 10.0)))
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            //WITHDRAW TEST
            handleRequest(HttpMethod.Post, "/withdraw") {
                addHeader("Content-Type", "application/json")
                setBody(Gson().toJson(mapOf("from" to 1, "amount" to 1.0)))
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            //TRANSFER TEST
            handleRequest(HttpMethod.Post, "/transfer") {
                addHeader("Content-Type", "application/json")
                setBody(Gson().toJson(mapOf("from" to 1, "to" to 2, "amount" to 1.0)))
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }
}
