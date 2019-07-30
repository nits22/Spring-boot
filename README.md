# AGODA Change Password API

## Getting Started

In this changePasswordAPI is created using java spring-boot and tested with MockMvc and TestNG framework.

### Prerequisites

You need to have docker installed on the system.

### Installing

1. Using docker: 
you can spin up a docker image by running below command

```
docker run -p 9090:9090 nitish18/passwordapi:agoda-1
```

2. Using Github:

Clone the repo.. and run below commands

```
$ cd D/sample
$ mvn clean install
$ docker build sample .
$ docker run -p 9090:9090 -t sample
```

## Running the tests

Includes 30 unit tests by using MockMvc to make mock requests. To run the tests using maven:

```
mvn clean test
```

## Cases handled

* Password match case is handled using Lavenshtein distance.
       
* Regex is used to match if the password meets criteria like atleast "At least 1 Upper case, 1 lower case ,least 1 numeric, 1 special character"

## Curl Request

Change Password API:

```
curl -X POST \
  http://localhost:9090/user/changePassword \
  -H 'Accept: application/json' \
  -H 'Content-Type: application/json' \
  -H 'Postman-Token: de02720e-6a38-4e19-af34-e26d59dd154a' \
  -H 'cache-control: no-cache' \
  -d '{
	"emailId":"nitishbector.it@gmail.com",
	"oldPassword":"aB@#rtttridndjsijdijscijsAfnDFFKDX432",
	"newPassword":"aB@#rtttridndjsijdijsDFFKDX4321123456754"
}'
```

User API:

```
curl -X POST \
  http://localhost:9090/user/addUser \
  -H 'Content-Type: application/json' \
  -H 'Postman-Token: f0ee319e-d6f1-48b7-8213-1f0f35f1aa06' \
  -H 'cache-control: no-cache' \
  -d '{
	"email":"nitishbector@gmail.com",
	"password": "aB@#rtttridndjsijdijscijsAfnDFFKDX432"
}'
```
