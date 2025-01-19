# Monster Trading Card Game (MTCG)

## Inhaltsverzeichnis
- [App-Design](#app-design)
- [Lessons learned](#lessons-learned)
- [Unit-Testing](#unit-tests)
- [Unique Feature](#unique-feature)
- [Erfasste Zeit](#erfasste-zeit)
- [GitHub-Repository](#github-repository)

---

## App-Design

### **Design-Entscheidungen**
- Die Anwendung folgt einer **mehrschichtigen Architektur**, die sich in folgende Ebenen unterteilt:
    - **Controller:** Verarbeitet HTTP-Anfragen und leitet sie an die entsprechenden Servicemethoden weiter.
    - **Service:** Enthält die Geschäftslogik und verwaltet die Interaktionen mit den Repositories.
    - **Repository:** Kommuniziert mit der PostgreSQL-Datenbank zur Durchführung von CRUD-Operationen.

### **Strukturübersicht**
```
src/
│-- application/
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── entity/
│   ├── dto/
│   ├── exception/
│   ├── config/
│-- resources/
│-- test/
│-- README.md
```
---

## Lessons learned

- **Datenbankdesign:**  
  Man sollte am Anfang sorgfältig die Datenbank entwerfen, damit im späteren Verlauf keine Probleme auftreten. Mit einem klarem Schema ist es auch einfacher die restliche Logik zu schreiben.

- **Handhabung von Konkurrenz:**  
  Verwaltung des gleichzeitigen Zugriffs auf die Battle-Queue und Synchronisation zur Vermeidung von Race Conditions.

- **Testgetriebene Entwicklung (TDD):**  
  Das Schreiben von Tests vor der Implementierung half, das Design zu verfeinern und potenzielle Fehler frühzeitig zu erkennen.

- **Fehlerbehandlung:**  
  Implementierung aussagekräftiger Fehlermeldungen und Behandlung von Grenzfälle. Oft hatte ich am Anfang undeutliche Fehlermeldungen und musste lange den Code debuggen. Das kann vermieden werden, indem von Anfang an sinnvolle Fehlermeldungen zurückgegeben werden.

---

## Unit Tests

- **Mockito für Dependency Mocking:**  
  Abhängigkeiten wie Repositories und Services wurden zur Isolierung von Unit-Tests gemockt.

- **JUnit 5 Framework:**  
  Wird verwendet, um Tests und Assertions strukturiert zu organisieren.

- **Testabdeckungsschwerpunkte:**  
  Tests wurden hauptsächlich geschrieben für:
    - Servicemethoden zur Sicherstellung der Geschäftslogik.
    - Repository-Methoden zur Validierung der Datenbankinteraktionen.
    - Grenzfälle wie nicht existierende Benutzer und unzureichendes Guthaben.

Beispiel-Testfall für den Kauf eines Pakets:

---

## Unique Feature

Als einzigartigest Feature hab ich mir überlegt, dass User sich coins verdienen können. Für jedes gewonnene Battle bekommt der User 2 coins. Allerdings kann er auch coins bei einem lose verlieren. Bei einem lose verliert der User 1 coin. Dadurch gewinnt die Userbase im Schnitt an coins und es können stärkere Karten erworben werden.

---

## Erfasste Zeit

| Aufgabe                     | Aufgewendete Zeit (Stunden) |
|----------------------------|-----------------------------|
| Initiales Design & Setup    | 5                           |
| Datenbankschema-Design      | 10                          |
| Implementierung (Controller, Services) | 25                          |
| Unit-Testing                | 6                           |
| Fehlerbehebungen & Optimierungen | 3                           |
| Dokumentation | 2                           |
| **Gesamt**                   | **49**                      |

---

## GitHub-Repository

Der Quellcode des Projekts ist auf GitHub verfügbar:

[GitHub Repository Link](https://github.com/tomiella/SWEN1-MonsterTradingCardsGame)

---

