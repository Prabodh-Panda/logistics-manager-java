# Logistics Manager Installation Instructions
The installation process consists of two steps: 
1. Download files
2. Setting up the database
3. Running the JAR file

## 1. Download files

## 2. Setting up the database
The program needs a database in order to run. To set up this database just run the sql script which is included in the download. To run the sql file open the MySQL Command Line Client and run the downloaded script.

```
sql> source PATH_TO_SQL_SCRIPT
```

Suppose your sql script is in downloads then (FOR MY PC): 
```
sql> source C:/Users/lipun/Downloads/logistics_manager_setup.sql
```

This will create the database and all of the required tables.

## 3. Running the JAR File
When you first open the JAR file it asks you to enter the username and password for your MySQL Server. You will need to enter these once and these will be saved in a config file for future use.

Now you can register a company and its branches and use the application.