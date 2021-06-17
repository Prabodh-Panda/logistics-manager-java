# Logistics Manager Installation Instructions
The installation process consists of three steps: 
1. Downloading and Extracting files
2. Setting up the database
3. Running the JAR file

## 1. Downloading and Extracting
To download the JAR file and the setup SQL Script click on the link below:

[v0.0.1-alpha Alpha Version of Logistics Manager](https://github.com/Prabodh-Panda/logistics-manager-java/releases/download/v0.0.1-alpha/logistics_manager_v0.0.1-alpha.zip)

Download the ZIP file and extract its contents.


## 2. Setting up the database
The program needs a database in order to run. To set up this database just run the sql script which is included in the download. To run the sql file open the MySQL Command Line Client and run: 

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