[![Java CI with Maven in Linux](https://github.com/UNIFI-RESILIENCE/HotelProject/actions/workflows/maven.yaml/badge.svg)](https://github.com/UNIFI-RESILIENCE/HotelProject/actions/workflows/maven.yaml) 
&nbsp; 
[![Coverage Status](https://coveralls.io/repos/github/UNIFI-RESILIENCE/HotelProject/badge.svg?branch=main)](https://coveralls.io/github/UNIFI-RESILIENCE/HotelProject?branch=ci-coveralls) 
&nbsp;
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.hotels.app%3Arooms&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=com.hotels.app%3Arooms) 
&nbsp;
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=com.hotels.app%3Arooms&metric=bugs)](https://sonarcloud.io/summary/new_code?id=com.hotels.app%3Arooms) 
&nbsp;
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=com.hotels.app%3Arooms&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=com.hotels.app%3Arooms) 
&nbsp;
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=com.hotels.app%3Arooms&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=com.hotels.app%3Arooms) 
&nbsp;
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=com.hotels.app%3Arooms&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=com.hotels.app%3Arooms)


# Project Setup

This guide provides instructions on setting up and running the project using Docker Compose.

## Prerequisites

- Ensure you have [Docker](https://www.docker.com/) and [Docker Compose](https://docs.docker.com/compose/) installed on your system.
- Clone the repository and navigate to the project directory.

## Setting Up Environment Variables

1. Create a file named `testenv.sh` in your project directory:

   ```sh
   touch testenv.sh
   ```

2. Open `testenv.sh` and add the following content:

   ```sh
   #!/bin/bash

   export POSTGRES_PASSWORD=<dbpassword>
   export POSTGRES_USER=<dbuser>
   export POSTGRES_DB=<dbname>
   export DB_URL=jdbc:postgresql://db:5432/<dbname>
   export DB_USER=<dbuser>
   export DB_PASSWORD=<dbpassword>

   echo "Environment variables set successfully."
   ```

   Replace `<dbpassword>`, `<dbuser>`, and `<dbname>` with your actual database credentials.

3. Grant execute permission to the script:

   ```sh
   chmod +x testenv.sh
   ```

4. Execute the script using one of the following methods:

   ```sh
   ./testenv.sh
   ```

   **or**

   ```sh
   source testenv.sh
   ```

## Running the Application

Once the environment variables are set, start the application with Docker Compose:

```sh
docker compose up
```

This command will start the necessary containers and initialize the application.

## Stopping the Application

To stop the running containers, use:

```sh
docker compose down
```

## Troubleshooting

- Ensure Docker is running before executing `docker compose up`.
- If permission is denied when executing `testenv.sh`, verify the file permissions:
  ```sh
  ls -l testenv.sh
  ```
  If necessary, run:
  ```sh
  chmod +x testenv.sh
  ```
- If the database fails to connect, double-check the credentials in `testenv.sh` and try running `source testenv.sh` again.

## Conclusion

Following these steps ensures a smooth setup and execution of your application using Docker Compose.