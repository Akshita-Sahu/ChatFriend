<div align="center">
  <img src="https://ui-avatars.com/api/?name=Chat+Friend&background=6C3DE0&color=fff&size=200" height="150" alt="ChatFriend Logo"/>
  <h1>ChatFriend</h1>
  <p>A powerful, high-performance real-time AI Chat Platform tailored for enterprise and social needs.</p>
</div>

## ✨ Features
* **Multi AI Provider Support** (OpenAI, Claude, Gemini, DeepSeek, Ollama, Azure)
* **Real-time Chat** via WebSockets with typing indicators and online statuses
* **Rich Interactions** including Emoji reactions, Message read receipts, and File/Image sharing
* **Enterprise Knowledge Base** integration
* **Robust Security** equipped with HTTPS, XSS sanitization, Rate Limiting, and JWT tokens
* **Beautiful Modern UI** featuring deep purple to blue gradients, dark mode, and mobile responsiveness

## 🚀 Setup Instructions
1. **Database (MySQL):** Create a database named `chatfriend` and execute `docs/chatfriend.sql`.
2. **Cache (Redis):** Install and run Redis on default port `6379`.
3. **Backend Setup:**
   * Ensure Java 17 and Maven are installed.
   * Run `mvn clean install`
   * Start `ChatFriendApp.java`
4. **Frontend Setup:**
   * Navigate to the `chatfriend-ui` directory.
   * Run `npm install` or `pnpm install`
   * Run `npm run dev` to compile the Vue 3 application.

## 📚 API Documentation
All backend endpoints are structurally managed under `/api/v1/`. For integrated testing, navigate to `/doc.html` or the Swagger UI provided by the Spring Boot interface.

## 👩‍💻 Credits
**Developed by Akshita Sahu**  
© 2025 ChatFriend. All Rights Reserved.
