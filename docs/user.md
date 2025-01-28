# User API Spec

## Register User

Endpoint : POST /api/users

Request Body : 

```json
{
    "username" : "alief",
    "password" : "rahasia",
    "name" : "Aliefsufi Uzan Kafil Ardi"
}
```

Response Body (Success) :

```json
{
    "data" : "OK"
}
```

Response Body (Failed) : 

```json
{
    "errors" : "Username tidak boleh kosong"
}
```

## Login User

Endpoint : POST /api/auth/login

Request Body : 

```json
{
    "username" : "alief",
    "password" : "rahasia"
}
```

Response Body (Success) :

```json
{
    "data" : {
        "token" : "TOKEN",
        "expiredAt" : "121212838"  //milliseconds
    }
}
```

Response Body (Failed, 401) : 

```json
{
    "errors" : "username atau password salah"
}
```

## Get User

Endpoint : GET /api/users/current

Request Header : 
- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
    "data" : {
        "username" : "alief",
        "name" : "Aliefsufi Uzan Kafil Ardi"
    }
}
```

Response Body (Failed, 401) : 

```json
{
    "errors" : "Unauthorized"
}
```

## Update User

Endpoint : PATCH /api/users/current

Request Header : 
- X-API-TOKEN : Token (Mandatory)

Request Body : 

```json
{
    "name" : "Aliefsufi",  //kirim nama jika hanya ingin update nama
    "password" : "new password" //kirim password jika hanya ingin update password
}
```

Response Body (Success) :

```json
{
    "data" : "OK"
}
```

Response Body (Failed, 401) : 

```json
{
    "errors" : "Unauthorized"
}
```

## Logout User

End point : DELETE /api/auth/delete

Request Header : 
- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
    "data" : "OK"
}