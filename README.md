# Wallet Application
<h2>Prerequisites</h2>
- Java 17 or above
- Maven
- Docker
- Docker Compose

<h2>Steps to run application</h2>
<h3>- 1:</h3>
run 'git clone https://github.com/patrikBLMelander/patrik_melander.wallet.git'

<h3>- 2:</h3>
cd to project root and run 'mvn clean install'

<h3>- 3:</h3>
run 'docker build -t patrik_melander_wallet:latest .'

<h3>- 4:</h3>
run 'docker-compose up -d'

<h3>- 5:</h3>
Access application at http://localhost:8080

<h2>API</h2>
<h3>- 1: Create account</h3>
POST http://localhost:8080/wallet/create-account
{
  "accountId": "Patrik",
  "deposit": 10
}
<h3>- 2: Deposit</h3>
Post http://localhost:8080/wallet/deposit
{
  "accountId": "1",
  "amount": 0.5
}
<h3>- 3: Withdraw</h3>
Post http://localhost:8080/wallet/withdraw
{
  "accountId": "1",
  "amount": 0.5
}
<h3>- 4: Transfer</h3>
Post http://localhost:8080/wallet/transfer
{
  "senderId": "accountId",
  "receiverId": "accountId",
  "transferAmount": 5
}
<h3>- 5: Get Transactions</h3>
Get http://localhost:8080/wallet/transactions/{accountId}
<h3>- 6: Get Balance</h3>
Get http://localhost:8080/wallet/transactions/{accountId}
