
# JCoinbase

**This project is still under development**

JCoinbase is an open source client for the Coinbase exchange platform API written in Java 15, but a Java 8 version is  
being considered. It allows you to make queries to the Coinbase API in a quick and easy way.

## Available features

- Request Coinbase public data

## Next features

- Users
- Account
- Addresses
- Transactions
- Buy / Sell
- Deposit
- Withdrawal
- Payment methods

## Getting started

To make requests to Coinbase API using JCoinbase, simply instantiate a new `JcoinbaseClient` via  
the `JCoinbaseClientFactory`

```java  
JCoinbaseClient client = JCoinbaseClientFactory.build(yourApiKey, yourSecret, desiredTimoutInSecond, followRedirects);  
```

- Api key : your coinbase generated api key in your profile settings.
- Secret : your coinbase generated secret in your profile settings.
- desiredTimeoutInSeconds : a long value to configure http request timeouts
- followRedirect : a boolean defining if the JCoinbaseClient should follow http redirects. In common cases you should  set it to false.

_Notice that, if you pass `null` for api key and secret, you won't be able to access non-public coinbase's data :  user, account, addresses, transactions, buy, sell, deposit, withdrawals and payment methods._

Then, you can simply call coinbase resources like that :

```java
ExchangeRates exchangeRates = client.data().getExchangeRates("BTC");  
```  

Please note that, by default, returned values use Vavr data types for objects like `Collections` or `Option`.  
So if you don't want to use Vavr, you can always get java objects via the api. 

For example :

```java
ExchangeRates exchangeRates = client.data().getExchangeRates("BTC");
io.vavr.collection.Map<String, BigDecimal> vavrRates = exchangeRates.getRates();
java.util.Map<String, BigDecimal> javaRates = exchangeRates.getRatesAsJavaMap();
```  

For further information on Vavr please take a look at : [https://www.vavr.io/](https://www.vavr.io/)