# crypto-trading-sim 📈

A web application that simulates a cryptocurrency trading platform, allowing users to:
- View real-time prices of the top 20 cryptocurrencies using the Kraken WebSocket API.
- Maintain a virtual account balance for buying and selling crypto with transaction history.
- Reset their account balance to a starting value.  


### **1. Prerequisites**
Ensure you have installed:
- **Backend:** Java 21, Maven, PostgreSQL
- **Frontend:** Node.js (>= 18), npm/yarn


### **2. Backend Setup (Spring Boot)**
```
git clone https://github.com/YOUR_GITHUB_USERNAME/crypto-trading-sim.git
cd crypto-trading-sim/backend
mvn clean install
mvn spring-boot:run
```
* How to run some of the unit tests:
```
cd crypto-trading-sim/backend
mvn test
```
* The backend API will be available at: 
🔗 http://localhost:8080


### **3. Frontend Setup (React)**
```
cd crypto-trading-sim/frontend
npm install
npm start
```
* The frontend will be available at:
  🔗 http://localhost:3000
* For any problems starting the frontend check the full explanation in [README.md](frontend/README.md).


### **4. Project Dependencies**
* See the full list in [requirements.txt](requirements.txt).


### **5. Future Improvements**
### ***Scalability Considerations*** 

To ensure that the Crypto Trading Simulation scales effectively as the number of users and transactions increases, we can implement the following strategies:
1. Backend Scalability:

    - ***Load Balancing:*** Deploy multiple instances of the backend and use a load balancer (e.g., NGINX) to distribute traffic evenly.
    - ***Database Optimization:*** Implement caching (e.g., Redis) to store frequently accessed data like crypto prices.
    - ***WebSocket Scaling:*** Use message brokers like Kafka or RabbitMQ to distribute real-time crypto price updates across multiple backend instances.

2. Frontend Scalability:

    - ***Client-Side Caching:*** Utilize React Context API or Redux to cache crypto prices and user balance, reducing unnecessary API calls.
    - ***Static Site Generation (SSG) & Server-Side Rendering (SSR):*** Use Next.js for better performance.
    - ***Content Delivery Network (CDN):*** Deploy the frontend on Vercel, or Cloudflare Pages to distribute static assets globally.

3. Deployment Strategy:

    - ***Containerization (Docker + Kubernetes):*** Package both backend and frontend as Docker containers and deploy them using Kubernetes for high availability.
    - ***Database Replication & Read Replicas:*** Implement database replication to handle high read requests efficiently.