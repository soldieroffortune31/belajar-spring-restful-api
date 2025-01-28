# Contact API Spec

## Create Contact

Endpoint : POST /api/contacts

Request Header : 
- X-API-TOKEN : Token (Mandatory)

Reqeust Body : 

```json
{
    "firstName" : "Aliefsufi",
    "lastName" : "Uzan Kafil Ardi",
    "email" : "uzan310596@gmail.com",
    "phone" : "085231728421"
}
```

Response Body (Success) :

```json
{   
    "data" : {
        "id" : "Random String",
        "firstName" : "Aliefsufi",
        "lastName" : "Uzan Kafil Ardi",
        "email" : "uzan310596@gmail.com",
        "phone" : "085231728421"
    }
}
```

Response Body (Failed) :

```json
{
    "errors" : "Email Format invalid...."
}
```

## Update Contact

Endpoint : PUT /api/contacts/{idContact}

Request Header : 
- X-API-TOKEN : Token (Mandatory)

Reqeust Body :

```json
{
    "firstName" : "Aliefsufi",
    "lastName" : "Uzan Kafil Ardi",
    "email" : "uzan310596@gmail.com",
    "phone" : "085231728421"
}
```

Response Body (Success) :

```json
{   
    "data" : {
        "id" : "Random String",
        "firstName" : "Aliefsufi",
        "lastName" : "Uzan Kafil Ardi",
        "email" : "uzan310596@gmail.com",
        "phone" : "085231728421"
    }
}
```

Response Body (Failed) :

```json
{
    "errors" : "Email Format invalid...."
}
```

## Get Contact

Endpoint : GET /api/contact/{idContact}

Request Header : 
- X-API-TOKEN : Token (Mandatory)

Response Body (Success) : 

```json
{   
    "data" : {
        "id" : "Random String",
        "firstName" : "Aliefsufi",
        "lastName" : "Uzan Kafil Ardi",
        "email" : "uzan310596@gmail.com",
        "phone" : "085231728421"
    }
}
```

Response Body (Failed, 404) : 

```json
{
    "errors" : "Contact is not found"
}
```

## Search Contact

Endpoint : GET /api/contacts

Query Param : 

- fistName : String (using like query fistName or lastName) Optional
- phone : String (using like query) Optional
- email : String (using like query) Optional
- page : Integer (default 0) 
- size : Integer (default 10)

Request Header : 
- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
    "data" : [
        {
            "id" : "Random String",
            "firstName" : "Aliefsufi",
            "lastName" : "Uzan Kafil Ardi",
            "email" : "uzan310596@gmail.com",
            "phone" : "085231728421"
        }
    ],
    "paging" : {
        "currentPage" : 0,
        "totalpage" : 10,
        "size" : 10
    }
}
```

Response Body (Failed, 401) :

```json
{
    "errors" : "Unauthorized"
}
```

## Remove Contact

Endpoint : DELETE /api/contact/{idContact}

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
    "errors" : "Contact is not found"
}
```