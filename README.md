
1. send request to
http://localhost:8080/api/merchant/payment/pull/stripe/start

2. enter amount
- amount is in cents and AED

3. enter card 
- 4242 4242 4242 4242 01/33 555

4. make sure webhook is listening to 

stripe listen --forward-to http://localhost:8080/api/merchant/payment/pull/stripe/webhooks

5. make sure application config has 
- api key
- public key
- webhook secret