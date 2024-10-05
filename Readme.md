# **Project 1: E-commerce User Behavior Analysis Using Hadoop MapReduce and `mrjob`**

## **Overview**

In this project, you will use **Hadoop MapReduce** (both Java and Hadoop Streaming) and **Python's `mrjob` framework** to analyze e-commerce user behavior data. The goal is to implement multiple tasks that focus on various aspects of user activity and product performance, such as identifying the most engaged users, analyzing purchase conversion rates, and calculating product profitability.

---

## **Dataset**

The dataset for this project is divided into three CSV files:

### **1. E-commerce Transactions (`transactions.csv`)**:
This file contains details of user purchases on the platform.

**Columns**:
- **TransactionID**: Unique identifier for each transaction.
- **UserID**: Unique identifier for each user.
- **ProductCategory**: The category of the product.
- **ProductID**: The unique identifier of the product.
- **QuantitySold**: The number of units sold in the transaction.
- **RevenueGenerated**: The total revenue generated from the transaction.
- **TransactionTimestamp**: Timestamp of the transaction.

### **2. User Activity Logs (`user_activity.csv`)**:
This file contains logs of user activities on the e-commerce platform.

**Columns**:
- **LogID**: Unique identifier for each user activity.
- **UserID**: Unique identifier for each user.
- **ActivityType**: Type of activity (Browse, AddToCart, Purchase).
- **ProductID**: The product involved in the activity.
- **ActivityTimestamp**: Timestamp of the activity.

### **3. Product Information (`products.csv`)**:
This file contains product details.

**Columns**:
- **ProductID**: Unique identifier of the product.
- **ProductName**: Name of the product.
- **ProductCategory**: Category to which the product belongs.
- **Price**: Price of the product.

---

## **Tasks**

### **Task 1: Identifying the Most Engaged Users (Java MapReduce)**

- **Objective**: Identify the top 10 most engaged users based on the number of interactions (browsing, adding to cart, purchasing).
- **Input**: `user_activity.csv`
- **Mapper**: Emit `UserID` for each interaction.
- **Reducer**: Count the total number of interactions per user and identify the top 10 most active users.

### **Task 2: Product Purchase Conversion Rate (Hadoop Streaming with Java)**

- **Objective**: Calculate the purchase conversion rate (ratio of purchases to interactions) for each product category.
- **Input**: `user_activity.csv`, `transactions.csv`
- **Mapper**: Emit `ProductCategory` for each interaction and each purchase.
- **Reducer**: Calculate the conversion rate for each product category.

### **Task 3: User Purchasing Behavior Based on Time of Day (Java MapReduce)**

- **Objective**: Analyze user purchasing behavior by identifying peak purchasing hours for each product category.
- **Input**: `transactions.csv`
- **Mapper**: Extract the hour from the `TransactionTimestamp` and emit the hour and `ProductCategory`.
- **Reducer**: Count the number of purchases made in each hour for each product category.

### **Task 4: Multi-Step Revenue and Profitability Analysis (Python `mrjob`)**

- **Objective**: Calculate total and average revenue per product and identify the top 3 most profitable products in each category.
- **Input**: `transactions.csv`, `products.csv`
- **Mapper**: Emit the revenue for each product.
- **Reducer 1**: Aggregate the total revenue per product.
- **Reducer 2**: Calculate the average revenue per product and identify the top 3 most profitable products in each category.

---

## **Setup and Execution**

### **1. Clone the Repository**

Fork the project repository from GitHub Classroom and clone it to your local machine or use GitHub Codespaces to work directly in the cloud.

### **2. Docker Compose and Project Setup**

Make sure you have Docker installed on your system. This project contains a `docker-compose.yml` file that you will use to set up the Hadoop environment. Additionally, the Java dependencies are managed through the `pom.xml` file.

**To start the Hadoop cluster**:
```bash
docker-compose up -d
```

### **3. Open the Project in an IDE**

Open this project either in **IntelliJ IDEA** or **Eclipse** to work with the Java-based MapReduce tasks. Ensure that the dependencies in `pom.xml` are resolved by Maven.

---

## **Running the Tasks**

### **Task 1: Identifying the Most Engaged Users**

Run this task using Java MapReduce. The input file for this task is `user_activity.csv`.

### **Task 2: Product Purchase Conversion Rate**

Use Hadoop Streaming in Java to run this task. The input files are `user_activity.csv` and `transactions.csv`.

### **Task 3: User Purchasing Behavior Based on Time of Day**

Run this task using Java MapReduce. The input file is `transactions.csv`.

### **Task 4: Multi-Step Revenue and Profitability Analysis**

This task should be run using Python's `mrjob` framework. The input files are `transactions.csv` and `products.csv`.

Ensure you have installed `mrjob` using the following command:
```bash
pip install mrjob
```

---

## **Submitting Your Work**

1. **Code**: Ensure that all your mappers, reducers, and job scripts are properly commented and organized. Push the code to your GitHub repository.
2. **Results**: Include the output files generated by each task and ensure they are committed to the repository.
3. **Report**: Write a brief report summarizing each task, your approach, and the insights derived from the analysis. Submit the GitHub repository link in GitHub Classroom.

---

## **Grading Criteria**

1. **Correctness (40%)**: Each task must produce accurate results based on the input data.
2. **Efficiency (20%)**: The MapReduce jobs should be optimized for performance, including proper use of combiners and reducers.
3. **Complexity (20%)**: The tasks should handle non-trivial data challenges, such as multiple input files and large datasets.
4. **Documentation (20%)**: Ensure that your code is well-commented and that your report provides a clear summary of your approach and findings.

---

For any questions or clarifications, please feel free to reach out during office hours or post on the course discussion forum.

---

Good luck, and happy coding!
```