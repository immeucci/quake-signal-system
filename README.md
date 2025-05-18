# Quake Signal System 

A client-server Java application that simulates a seismic network. Each client represents a sensor installed in a city, capable of reporting earthquake events (magnitude and time) to a central server.

---

## Features

1. Java **Swing GUI** for the client
2. **Automatic timestamp** generation at the moment of report
3. **Multi-threaded server** capable of handling multiple concurrent clients
4. **Socket-based communication** (TCP)
5. Each city has its own `.txt` file containing event logs
6. Real-time response and confirmation from the server

---

## Project Structure

quake-signal-system/
├── intellij-project/          # Complete IntelliJ IDEA project
│   ├── .idea/
│   ├── quake_signal_system.iml
│   └── src/
│       ├── client/
│       │   ├── QuakeClient.java
│       │   ├── ClientController.java
│       │   └── ReportGUI.java
│       └── server/
│           ├── QuakeServer.java
│           └── ClientHandler.java
│
├── source-only/               # Only pure Java source files
│   ├── client/
│   │   ├── QuakeClient.java
│   │   ├── ClientController.java
│   │   └── ReportGUI.java
│   └── server/
│       ├── QuakeServer.java
│       └── ClientHandler.java
│
├── README.md
└── LICENSE (optional)

---

## How to run

### Server

- **To start the server:**
'''bash
cd source-only/server
javac *.java
java QuakeServer
'''

### client

- **To start each client:**
'''bash
cd source-only/client
javac *.java
java QuakeClient
'''

### using IntelliJ

- Alternatively, open the **intellij-project/** in **IntelliJ IDEA** and run the classes directly from the IDE.

---

## Example Output

- When a client from **Rome** sends a report of a 4.5 magnitude earthquake at 14:32:10, a file **rome.txt** will be created (or updated) containing:
'''txt
Earthquake reported at 14:32:10 with magnitude 4.5
'''
- Each **client** (representing a different city) will log to its respective file. The **server** manages all client connections in separate **threads**.

---

## Requirements

- **Java 8** (or higher)
- No external libraries required

---

## License

Distributed under the **MIT** license. See the **LICENSE** file for more details.
