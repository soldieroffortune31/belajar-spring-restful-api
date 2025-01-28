# Address API Spec

## Create Address

End Point : POST /api/contacts/{idContact}/adresses

Request Header : 
- X-API-TOKEN : Token (Mandatory)

Request Body :

```json
{
    "street" : "jalan",
    "city" : "kota",
    "province" : "provinsi",
    "country" : "country"
}
```

Response Body (Success) :

```json
{
    "data" : {
        "id" : 1,
        "street" : "jalan",
        "city" : "kota",
        "province" : "provinsi",
        "country" : "country"
    }
}
```

Response Body (Failed) : 

```json
{
    "errors" : "Contact is not found"
}
```

## Update Address

End Point : PUT /api/contacts/{idContact}/addresses/{idAddress}

Request Header : 
- X-API-TOKEN : Token (Mandatory)

Request Body :

```json
{
    "street" : "jalan",
    "city" : "kota",
    "province" : "provinsi",
    "country" : "country"
}
```

Response Body (Success) :

```json
{
    "data" : {
        "id" : 1,
        "street" : "jalan",
        "city" : "kota",
        "province" : "provinsi",
        "country" : "country"
    }
}

Response Body (Failed) : 

```json
{
    "errors" : "Contact is not found"
}
```

## Get Address

End Point : GET /api/contacts/{idContact}/addresses/{idAdress}

Request Header : 
- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
    "data" : {
        "id" : 1,
        "street" : "jalan",
        "city" : "kota",
        "province" : "provinsi",
        "country" : "country"
    }
}
```

Response Body (Failed) : 

```json
{
    "errors" : "Address is not found"
}
```

## Remove Address

End Point : DELETE /api/contacts/{idContact}/addresses/{idAdress}

Request Header : 
- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
    "data" : "OK"
}
```

Response Body (Failed) : 

```json
{
    "errors" : "Address is not found"
}
```

## List Adrress

End Point : GET /api/contacts/{idContact}/addresses

Request Header : 
- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
    "data" : [
        {
            "id" : 1,
            "street" : "jalan",
            "city" : "kota",
            "province" : "provinsi",
            "country" : "country"
        }
    ]
}
```

Response Body (Failed) : 

```json
{
    "errors" : "Contact is not found"
}
```