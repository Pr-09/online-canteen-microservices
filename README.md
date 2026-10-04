# 🍽️ Online Canteen Ordering System

A full-stack **Online Canteen Ordering System** designed to reduce crowding in college canteens and make food ordering faster, easier, and more organized.

Students and teachers can scan the **QR code available on their table**, browse the menu, place orders, make online payments, and track their order status without standing in a queue.

The system is built using a **Microservices Architecture** with Spring Boot, Angular, Eureka Service Discovery, API Gateway, MySQL, and cloud services.

---

## 🚀 Live Application

### Frontend
https://online-canteen-frontend.onrender.com

### API Gateway
https://online-canteen-apigateway.onrender.com

---

# ✨ Features

## 👨‍🎓 Customer / Student

- 🔐 User Registration & Login
- 🔑 JWT-based Authentication
- 🔵 Google OAuth Login
- 📱 QR-based table ordering
- 🍔 Browse available menu items
- 🛒 Add items to cart
- 📦 Place orders
- 💳 Online payment using Razorpay
- 💰 Udhar/Credit payment option
- 📋 View previous orders
- 🔎 Track order status
- 📧 Email notifications
- 🪑 Table-based ordering

---

## 👨‍🍳 Kitchen / Staff

- View incoming orders
- Update order status
- Manage preparation status
- Track estimated preparation time
- Mark orders as ready
- Manage order workflow

### Order Status Flow

```text
PAYMENT_PENDING
       ↓
    PENDING
       ↓
   PREPARING
       ↓
     READY
       ↓
   DELIVERED
