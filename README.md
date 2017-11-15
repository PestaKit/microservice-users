# microservice-users
login and user session management

# Deploy

## Database
We use CockroachDB as database with a cluster of three nodes to take advantage of CockroachDB's automatic replication, rebalancing, and fault tolerance capabilities.

Simply run `docker-compose up -d` in the `topology` folder to start the database cluster.

To stop the database run `docker-compose down`.

Once deployed the database is available on port `26257` and the Cockroach console on port `9090`.
