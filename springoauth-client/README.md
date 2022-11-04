Using Authorization Code flow:

1. GET https://${okta_auth_server_url}/oauth2/default/v1/authorize?client_id=${client_id}&redirect_uri=http://localhost:4200/home&nonce=dfdf&response_type=code&scope=openid&state=122312 
2. Use auth code returned in url and make POST request to /token endpoint to obtain token from okta: https://dev-30002792.okta.com/oauth2/default/v1/token
3. Body of /token POST request grant_type: authorization_code, code: ${code}, redirect_uri: http://localhost:4200/home
4. Use the token in Authorization Header to access resource server endpoint https://localhost:8081/access, otherwise https://localhost:8081/access will throw HTTP 404 unauthorized message
