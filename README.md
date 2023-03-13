# paid-parking-api

The project implements the way that the paid parking is working.

It is based on Java 17 with Spring boot framework and it is secured with Spring security. The build tool is Maven. Paid-parking-api uses h2 database through JPA.
The project is based on Controller-Service-Repository pattern.
It has some unit and integration test examples managed with JUnit and Mockito.


Setup the project:

Before starting the project you have to build it with command: "mvn clean install".
Paid-parking-api can be started from PaidParkingApiApplication.class.
There is a basic authentication, where you should add headers with Username = "username" and Password = "password"
These are the sample credentials. They can be configured from application.properties.

Sample requests:
The positive scenario of the request is given as an example, of course there could be some negative cases caused mainly by wrong data.
Big part of the corner cases are handled. You can try to test the app with bad data. 

1.Create parking
First of all you should create Parking with the following request:

POST http://localhost:8080/api/parking/create
requestBody:
{
    "carsCurrentCount": 0,
    "busesCurrentCount": 0
}

response:
200 OK
{
    "parkingId": 3252,
    "carsCurrentCount": 0,
    "busesCurrentCount": 0,
    "vehicles": null
}



2.Enter parking
You can use the parkingId from previous response to enter this parking. In the requestBody you can chose what is the vehicle type - "CAR" or "BUS".

POST http://localhost:8080/api/parking/enter/3252
requestBody: 
{
    "type": "CAR"
}

response:
200 OK
Vehicle with id 14903 entered successfully!



3.Exit parking
Now you can exit the parking with the parkingId and the vehicleId from the previous response.

POST http://localhost:8080/api/parking/exit/3252/14903

response:
200 OK
Vehicle with id 14903 exit successfully!



4.Report of sales by date range 
You can check the report for the sales between two dates. It shows the revenue for CAR/BUS separately.

POST http://localhost:8080/api/sale/range
requestBody:
{
    "parkingId": 3252,
    "from": "2023-03-09T03:14:41",
    "to": "2023-03-14T04:18:41"
}

response:
200 OK
{BUS=0, CAR=1.00}



5.Report with revenue for each date of a month by given year and month.
You can check the revenue report for sales for each day by given year and month.
Of course each month has different count of days. Notice that the 13th day has revenue amount of 1.00.

POST http://localhost:8080/api/sale/revenue
requestBody:
{
    "parkingId": 3252,
    "year": "2023",
    "month": "03"
}

response:
200 OK
{1=0, 2=0, 3=0, 4=0, 5=0, 6=0, 7=0, 8=0, 9=0, 10=0, 11=0, 12=0, 13=1.00, 14=0, 15=0, 16=0, 17=0, 18=0, 19=0, 20=0, 21=0, 22=0, 23=0, 24=0, 25=0, 26=0, 27=0, 28=0, 29=0, 30=0, 31=0}



There are few more auxiliary request:


6.Get all sales

GET http://localhost:8080/api/sale/all

response: 
[
    {
        "saleId": 1,
        "amount": 5.00,
        "saleDate": "2023-03-09T21:40:12.846985",
        "vehicleType": "BUS",
        "parkingId": 1
    },
    {
        "saleId": 12852,
        "amount": 1.00,
        "saleDate": "2023-03-10T17:56:02.099703",
        "vehicleType": "CAR",
        "parkingId": 2
    },
    {
        "saleId": 12902,
        "amount": 1.00,
        "saleDate": "2023-03-13T01:16:06.545335",
        "vehicleType": "CAR",
        "parkingId": 3252
    },
    {
        "saleId": 12952,
        "amount": 1.00,
        "saleDate": "2023-03-13T01:22:31.084125",
        "vehicleType": "CAR",
        "parkingId": 3252
    }
]




7. Get all vehicles(in parkings)

GET http://localhost:8080/api/vehicle/all

response:
[
    {
        "vehicleId": 1552,
        "type": "BUS",
        "enterDateTime": "2023-03-09T21:57:22.81041"
    },
    {
        "vehicleId": 14752,
        "type": "CAR",
        "enterDateTime": "2023-03-10T17:19:42.728442"
    },
    {
        "vehicleId": 14802,
        "type": "CAR",
        "enterDateTime": "2023-03-10T17:22:06.433727"
    },
    {
        "vehicleId": 14902,
        "type": "CAR",
        "enterDateTime": "2023-03-13T01:09:12.014563"
    }
]




8. Get all vehicles in specific parking.

GET http://localhost:8080/api/parking/3252

response:
{
    "parkingId": 3252,
    "carsCurrentCount": 1,
    "busesCurrentCount": 0,
    "vehicles": [
        {
            "vehicleId": 14902,
            "type": "CAR",
            "enterDateTime": "2023-03-13T01:09:12.014563"
        }
    ]
}



9. Get all sales

GET http://localhost:8080/api/sale/all

response:
[
    {
        "saleId": 11253,
        "amount": 1.00,
        "saleDate": "2023-03-10T02:15:13.23783",
        "vehicleType": "CAR",
        "parkingId": 2
    },
    {
        "saleId": 12852,
        "amount": 1.00,
        "saleDate": "2023-03-10T17:56:02.099703",
        "vehicleType": "CAR",
        "parkingId": 2
    },
    {
        "saleId": 12902,
        "amount": 1.00,
        "saleDate": "2023-03-13T01:16:06.545335",
        "vehicleType": "CAR",
        "parkingId": 3252
    },
    {
        "saleId": 12952,
        "amount": 1.00,
        "saleDate": "2023-03-13T01:22:31.084125",
        "vehicleType": "CAR",
        "parkingId": 3252
    }
]
