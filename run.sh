#!/usr/bin/env sh
# Compila e executa o TicketFlow Console (Linux / macOS / Git Bash)
set -e
javac -encoding UTF-8 -d target/classes $(find src/main/java -name "*.java")
java -cp target/classes Main
