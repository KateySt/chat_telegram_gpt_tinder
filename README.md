# Telegram Bot with ChatGPT and Dating Features

This project is a Java-based Telegram bot that integrates ChatGPT for conversational AI and includes features tailored for dating and profile generation. The bot provides interactive user experiences, including generating profiles for dating apps like Tinder, starting conversations with tailored openers, and engaging in dynamic dialogue.

## Features

- **Telegram Integration:**
  - Utilizes the Telegram Bot API to handle messages, send text responses, and images.
  - Allows users to interact with the bot using simple commands.

- **ChatGPT Integration:**
  - Connects to the OpenAI API to generate AI-powered responses, making the conversation natural and contextually relevant.
  - Handles casual conversations, advice, and personalized replies.

- **Profile Generation (`/profile`):**
  - The bot interacts with the user by asking a series of questions to generate a formatted profile for dating apps.
  - After collecting user data (e.g., name, age, hobbies), it outputs a complete profile suitable for Tinder.

- **Opener Generation (`/opener`):**
  - The bot creates personalized first messages to help users start conversations with new people.
  - The user can request the bot to generate icebreakers for dating or social interactions.

- **Dating Mode (`/date`):**
  - Switches the bot into a mode focused on dating-related interactions.
  - ChatGPT adapts its responses to assist with dating conversations, relationship advice, and more.

- **Command-based Menu:**
  - A simple, easy-to-use menu helps users navigate between various bot features such as profile generation, dating mode, and opener suggestions.

## Technologies Used

- **Java:** Core programming language used to implement the bot.
- **Telegram Bot API:** Used to handle all interactions with Telegram, including sending and receiving messages.
- **OpenAI GPT API (ChatGPT):** Provides AI-driven conversations, dynamic responses, and personalized messaging.
- **IntelliJ IDEA:** Integrated Development Environment (IDE) for developing and testing the project.
