# Compila e executa o TicketFlow Console (Windows / PowerShell)
$ErrorActionPreference = "Stop"
$sources = Get-ChildItem -Path "src/main/java" -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -d target/classes $sources
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
java -cp target/classes Main
