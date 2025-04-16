# ✈️ System rezerwacji biletów

Aplikacja webowa do rezerwacji lotów stworzona z użyciem **Spring Boot**, **Angulara** oraz bazy danych **H2**. Projekt umożliwia przeglądanie dostępnych lotów, rezerwację miejsc i zarządzanie pasażerami.

## Jak skorzystać z aplikacji?

### Wersja zdeploywana
- Frontend jest dostępny na GitHub Pages: https://wilinskiw.github.io/bilet-system/
- Backend jest dostępny na Renderze: https://bilet-system.onrender.com/swagger-ui/index.html
> [!IMPORTANT]
> Render na darmowym planie (free tier) usypia aplikację, jeśli nie jest używana. Dlatego dodałem UpTimeRobot, który regularnie pinguję serwer.
> Może się jednak zdarzyć, że serwery Rendera przestaną działać. W takiej sytuacji, po wejściu na adres URL serwera, trzeba będzie chwilę poczekać, aż serwer się uruchomi.

### Uruchamianie lokalnie
#### Start serwera (Spring boot)
1. Sklonuj repozytorium
   
   ```
   git clone https://github.com/WilinskiW/bilet-system.git
   ```
3. Wejdź do katalogu projektu i wpisz do konsoli:
   
   ```
   ./mvnw spring-boot:run
   ```
4. Domyślnie serwer startuje na `http://localhost:8080`
#### Start frontendu (Angular)
1. Przejdź do katalogu `bilet-system-client/`
2. Zainstaluj zależności i uruchom aplikację
   
   ```
   npm install
   ng serve
   ```
3. Frontend dostępny będzie pod `http://localhost:4200`

###

## 🔧 Stack technologiczny

- **Backend:** Spring Boot, JPA (Hibernate), Spring Web, Spring Data JPA
- **Frontend:** Angular
- **Baza danych:** H2 (Użyłem jej, ponieważ jest łatwa w uruchamianiu)
- **Testy:** JUnit 5, Mockito
- **Deployment:** Git, Docker

---

## ✅ Wykonane założenia
- Spełniłem wszystkie wymagania + dodatkowe.
- Aplikacja ma testy jednostkowe.
- Warstwę frontendową.
- Dokumentacje API za pomocą Swaggera.
