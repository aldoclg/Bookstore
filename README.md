# Requirements

- Java 21
- Maven 3.9

# Installing dependencies

    mvn install

# Running application

    mvn spring-boot:run

# Endpoints

__url__:  `localhost:8081/bookstore/`

## Buying one or several books and calculating the price

__POST__ `/customers/{customer_id}/purchases`

### Path parameters

- **customer_id** - (number) the customer id

### Request body

- **data** - a list of books to purchase
- **data[*].book_id** -(number) the id of the book
- **data[*].amount** - (number) the amount required
- **data[*].apply_discount** - (boolean) if it wants to apply discount or not. When true resets the customer loyalty points

### Response body

- **data** - an object with the result
- **data[*].price** -(number) the total price of the purchase

### Curl

```shell
curl --request POST \
  --url http://localhost:8081/bookstore/customers/{customer_id}/purchases \
  --header 'Content-Type: application/json' \
  --data '{
	"data": [
		{
			"book_id": 0,
			"amount": 0,
			"apply_discount": false
		}
	]
}'
```

## Returning the loyalty points

__GET__ /customers/{customer_id}/loyalty-points

### Path parameters

- **customer_id** - (number) the customer id

### Response body

- **data** - an object with the result
- **data[*].loyalty_points** -(number) the customer`s loyalty points

### Curl

```shell
curl --request GET --header "Accept: application/json" http://localhost:8081/bookstore/customers/{customer_id}/loyalty-points 
```

## Returning the books available to purchase

__GET__ /books?page=0&size=100

### Query parameters

- ***page*** (optional) - (number) the page number, Default 0
- ***size*** (optional) - (number) the page size, Default 100

### Response Body

- **data** - an object with the result
- **data[*].book_id** -(number) the id of the book
- **data[*].name** -(string) the book`s name
- **data[*].price** -(number) the book`s price

### Curl

```shell
curl --request GET \
  --url 'http://localhost:8081/bookstore/books?page=0&size=100' \
  --header 'Content-Type: application/json'
```