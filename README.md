# ComputerShop
FRONT-END: https://github.com/mmpodkanski/computer-shop-front

AWS: http://mmpod-computershop.s3-website.us-east-2.amazonaws.com/

Simple ecommerce shop, whereby you are able to buy some computer components.

This project uses:
- **CQRS**, **Facade** design patterns
- **JPA Projections**,
- Stripe framework to handle payments

Now I'm working on:
- possibilty to collect invoice pdf file(OpenPDF framework)

## Screenshots

1.
![image](https://user-images.githubusercontent.com/75319903/122103778-43ae5400-ce17-11eb-8e2e-ed392c778065.png)

2.
![image](https://user-images.githubusercontent.com/75319903/122103948-75bfb600-ce17-11eb-8617-2028a85a460c.png)


3.
![image](https://user-images.githubusercontent.com/75319903/122103815-4dd05280-ce17-11eb-99a3-2de28cfa0352.png)


4.
![image](https://user-images.githubusercontent.com/75319903/122103833-545eca00-ce17-11eb-9025-5f36b941cc82.png)


5.
![image](https://user-images.githubusercontent.com/75319903/122103861-5b85d800-ce17-11eb-9c43-7f57dec4cbf0.png)


6.
![image](https://user-images.githubusercontent.com/75319903/122103924-6d677b00-ce17-11eb-973e-a09427508a76.png)

7.
![image](https://user-images.githubusercontent.com/75319903/122104036-85d79580-ce17-11eb-820c-fd69f1b73286.png)


## Technologies and tools
* Java 11
* Spring
* Maven
* Hibernate
* Stripe
* JUnit
* MySQL
* IntelliJ IDEA

## Endpoints (examples)
| Value | Endpoint | Access |
| :---         |     :---:      |          ---: |
| GET   | /products?page/    | not required  |
| GET   | /products?category/    | not required  |
| GET   | /products?code/  | not required  |
| GET   | /products/:id/   | not required  |
| POST   | /products/    | admin  |
| PATCH   | /products/:id?increase/   | admin  |
| PATCH   | /products/:id?increase/    | admin  |
| DELETE   | /products/:id/    | admin  |
||||||
| GET, POST   | /cart/    | customer  |
| UPDATE   | /cart/:id/    | customer  |
| DELETE   | /cart    | customer  |
||||||
| GET, POST   | /orders/    | customer  |
| GET   | /orders/:id/    | customer  |
| POST   | /orders/checkout=:id/   | customer  |
| DELETE   | /orders/:id/    | customer  |
||||||
| GET, POST   | /customer/    | customer  |
| PUT   | /customer?details    | customer  |
| PUT   | /customer?login    | customer  |
| PUT   | /customer?register    | customer  |


## Installation

1. Clone the Project using link https://github.com/mmpodkanski/computer-shop-back.git or Download the zip
2. Open project in IntelliJ:
- File->New->Project from Version Control then past clone link
- File->Open then find and open downloaded zip
3. Run java application

OR

You can run application with maven wrapper:
```
- mvnw clean install
- mvnw spring-boot:run
```

## Contact
Created by [@mmpodkanski](https://github.com/mmpodkanski/)


