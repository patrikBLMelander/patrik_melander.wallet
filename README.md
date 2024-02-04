# Wallet Application
<h2>Prerequisites</h2>
- Java 17 or above <br>
- Maven<br>
- Docker<br>
- Docker Compose<br>

<h2>Steps to run application</h2>
1: run 'git clone https://github.com/patrikBLMelander/patrik_melander.wallet.git'<br>
2: cd to project root and run 'mvn clean install'<br>
3: run 'docker build -t patrik_melander_wallet:latest .'<br>
4: run 'docker-compose up -d'<br>
5: Access application at http://localhost:8080<br>
<h2>API</h2>
<h4>- 1: Create account</h3>
POST http://localhost:8080/wallet/create-account
{
  "accountId": "Patrik",
  "deposit": 10
}
<h4>- 2: Deposit</h3>
Post http://localhost:8080/wallet/deposit
{
  "accountId": "1",
  "amount": 0.5
}
<h4>- 3: Withdraw</h3>
Post http://localhost:8080/wallet/withdraw
{
  "accountId": "1",
  "amount": 0.5
}
<h4>- 4: Transfer</h3>
Post http://localhost:8080/wallet/transfer
{
  "senderId": "accountId",
  "receiverId": "accountId",
  "transferAmount": 5
}
<h4>- 5: Get Transactions</h3>
Get http://localhost:8080/wallet/transactions/{accountId}
<h4>- 6: Get Balance</h3>
Get http://localhost:8080/wallet/transactions/{accountId}
