# Event API

This is a RESTful API for managing data about events, venues, and artists. It provides endpoints for retrieving information about artists and the events they perform at.

## Endpoints

The following endpoints are available:

- `GET /artists/{id}`: Retrieves information about an artist by ID, including all events they will perform at.

**Parameters**
- `artistId` (required) - The ID of the artist to retrieve.

## Error Handling

The API handles three types of exceptions:

- **Invalid Artist ID**: If an invalid artist ID is provided (e.g. an ID that does not exist in the data), the API will return a 404 Not Found response.

- **Web Client Error**: If there is an error while fetching data from the upstream API, the API will return a 500 Internal Server Error response.

- **Artist NotFound Exception**: If an artist ID is provided, not present in the the upstream API,the API will return a 404 NOT FOUND Error response.

## Technologies Used

This API was built using the following technologies:

- Java 17
- Spring Boot
- Reactive programming
- WebClient
- Lombok
- Reactor Test
- JUnit

## Setup

To run the Event API locally, follow these steps:

1. Clone this repository to your local machine.
2. Open the project in your IDE of choice.
3. Build the project using Maven.
4. Set the necessary environment variables:

    - `APP_BASE_URL`: the base URL for the external API (e.g. https://iccp-interview-data.s3-eu-west-1.amazonaws.com/78656681)

## Installation

To build and run the Event API, follow these steps:

1. Clone the git repository: `git clone https://github.com/{YOUR_USERNAME}/event-api.git`
2. Navigate to the project directory: `cd event-api`
3. Build the project with Maven: `./mvnw clean install`
4. Run the project: `./mvnw spring-boot:run`

## Trade-offs and Design Decisions

Some of the trade-offs and design decisions made in the implementation of this API include:

- **Use of Spring Boot**: Spring Boot was used to simplify the implementation of the API, as it provides a lot of functionality out of the box. However, it may not be the best choice for all use cases.

- **Use of S3 as a REST Endpoint**: S3 was used as a REST endpoint for fetching the data, even though it is not a typical use case for S3. This was done to keep things simple, but in a real-world scenario a more appropriate data store or API may be used.

- **Use of WebClient**: WebClient was used for making HTTP requests to the upstream API, as it is a reactive/non-blocking client that integrates well with Spring. However, other HTTP clients may be used depending on the use case.

- **Use of Reactive Programming**: Reactive programming was used to implement the API, as it allows for efficient handling of asynchronous events and reduces the risk of blocking.

- **Use of Lombok, Reactor Test, and JUnit**: Lombok was used to reduce boilerplate code in model classes, while Reactor Test and JUnit were used for testing. 
