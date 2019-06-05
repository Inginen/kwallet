Wallet Application

How to run:
Idea Intellij. Just run main fun from Application.kt

Default Host - localhost:8080

#API
Create new wallet:
1) POST /wallet
RequestBody:
```
{
    "balance" : 0.0 -- initial balance
}
```

Get wallet by id:
2) GET /wallet/{id}

Deposit to wallet:
3) POST /deposit
RequestBody:
```
{
    "to" : 1, -- wallet id
    "amount" : 0.0 -- sum to deposit
}
```

Withdraw from wallet:
4) POST /withdraw
RequestBody:
```
{
    "from" : 1, -- wallet id
    "amount" : 0.0 -- sum to withdraw
}
```

Transfer from wallet to another:
5) POST /transfer
RequestBody:
```
{
    "to" : 1, -- wallet id to deposit
    "from" : 2, -- wallet id to withdraw
    "amount" : 0.0 -- sum to transfer
}
```
