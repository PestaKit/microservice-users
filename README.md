# microservice-users
login and user session management

# Deploy

## Database
We use CockroachDB as database with a cluster of three nodes to take advantage of CockroachDB's automatic replication, rebalancing, and fault tolerance capabilities.

Simply run `docker-compose up -d` in the `topology` folder to start the database cluster.

To stop the database run `docker-compose down`.

Once deployed the database is available on port `26257` and the Cockroach console on port `9090`.

# Security library for SpringBoot [ ![Download](https://api.bintray.com/packages/pestakit/microservice-users/users-security/images/download.svg) ](https://bintray.com/pestakit/microservice-users/users-security/_latestVersion)

We provide a library to automatically configure SpringBoot's WebSecurity module.

## Install
To install the library add the following repository into your project `pom.xml`:
````
<repository>
    <snapshots>
        <enabled>false</enabled>
    </snapshots>
    <id>bintray-pestakit-microservice-users</id>
    <name>bintray</name>
    <url>https://dl.bintray.com/pestakit/microservice-users</url>
</repository>
````

and then the following dependency:
```
<dependency>
    <groupId>io.pestakit</groupId>
    <artifactId>users-security</artifactId>
    <version>0.1.0</version>
</dependency>
```

## Configuration

Configure SprintBoot to scan our library package. To do this add the package `io.pestakit.users.security` in the `@ComponentScan` annotation in your main class.

Example:
```
@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackages = { "io.pestakit.users.security", "your.package" }
```

Finally specify our api URL into the `application.properties` file.
```
users.security.api.url=http://localhost:8080/api
```

## Usage

To secure an endpoint add one of the following annotations on top of your controller:

- `@PreAuthorize("hasRole('USER')")`
- `@PreAuthorize("hasPermission(#id, 'OWNER')")`

The first one check if the user has a valid token and the second one check if the controller parameter `id` and the userID inside the token are equals.

Our token contain the following user information:

- userID
- username

To get these values inside a controller annotated with `@PreAuthorize` use the following code:
```
UserProfile profile = (UserProfile)SecurityContextHolder.getContext().getAuthentication().getDetails();
//profile.getUserID();
//profile.getUsername();
```
