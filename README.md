# Aplikacja_zaliczenie

## 1. OPIS APLIKACJI
1. Projekt został stworzony jako zaliczeniowy w ramach przedmiotu „Programowanie aplikacji WWW w technologii Java”. Głównym celem aplikacji jest zarządzanie prostym katalogiem ofert nieruchomości.  
2. Użytkownik może się zarejestrować i zalogować, przeglądać listę dostępnych nieruchomości (domów, mieszkań i działek), a także – po zalogowaniu – dodawać nowe ogłoszenia oraz edytować lub usuwać wcześniej dodane oferty.  
3. Konto z uprawnieniami administratora umożliwia dodawanie ofert, edytowanie wszystkich ogłoszeń oraz ich usuwanie – niezależnie od autora. Administrator ma także możliwość zarządzania użytkownikami poprzez specjalny panel administracyjny (np. usuwanie kont).  
4. Interfejs użytkownika oparto na prostych szablonach HTML z użyciem silnika szablonów Thymeleaf. Logikę aplikacji oraz warstwę danych obsługuje framework Spring Boot. Dane przechowywane są lokalnie w bazie H2 (plik baza.mv.db w katalogu data).  

## 2. WYMAGANIA SYSTEMOWE
1. Java JDK 17 (lub nowsze): Wymagana do uruchomienia aplikacji.  
2. Maven: Projekt używa Mavena do budowy i zarządzania zależnościami.  
3. Brak dodatkowych serwerów: dane przechowywane lokalnie dzięki H2.  
4. IDE (opcjonalnie): Wygodniejsze zarządzanie projektem, np. IntelliJ IDEA z wsparciem Maven.  

## 3. INSTALACJA I URUCHOMIENIE
1. Pobranie projektu
- Pobierz lub sklonuj projekt z GitHuba:  
  `https://github.com/Slawek189/Aplikacja_zaliczenie`  

2. Uruchomienie w IntelliJ IDEA (zalecane)
- Otwórz projekt w IntelliJ IDEA.  
- Poczekaj, aż Maven pobierze zależności.  
- Uruchom klasę `AplikacjaZaliczenieApplication.java` (prawym przyciskiem → Run).  
- Aplikacja uruchomi się lokalnie na porcie `http://localhost:8080`.  

3. Alternatywnie: budowanie pliku JAR
- Jeśli chcesz uruchomić aplikację z konsoli:  
- Otwórz terminal w katalogu projektu.  
- Wpisz:  
  mvn clean package
- Po zbudowaniu uruchom plik:
  java -jar target/aplikacja-zaliczenie-0.0.1-SNAPSHOT.jar

4. Dostęp do aplikacji
- Otwórz przeglądarkę i wpisz:  
`http://localhost:8080`  
- Zobaczysz stronę główną aplikacji.  

## 4. FUNKCJONALNOŚCI
1. Rejestracja i logowanie: Umożliwia utworzenie konta użytkownika z hasłem szyfrowanym przy użyciu BCrypt. System blokuje rejestrację, jeśli podany login już istnieje. Logowanie odbywa się w sposób bezpieczny z wykorzystaniem Spring Security.  
2. Przeglądanie nieruchomości: Na stronie z ogłoszeniami wyświetlana jest lista dostępnych ofert nieruchomości. Użytkownik może kliknąć ogłoszenie, aby zobaczyć jego szczegóły (w tym zdjęcie i numer telefonu po kliknięciu w przycisk).  
3. Dodawanie i edytowanie ogłoszeń: Zalogowany użytkownik może dodawać nowe oferty nieruchomości, a także edytować i usuwać tylko własne ogłoszenia.  
4. Panel administratora: Konto administratora umożliwia dodawanie ogłoszeń, edytowanie oraz usuwanie wszystkich ofert (niezależnie od właściciela). Dodatkowo administrator może zarządzać użytkownikami – np. usuwać konta z poziomu panelu admina.  
 - Dane testowego konta administratora:  
   - Login: `admin`  
   - Hasło: `admin123`  
5. Obsługa błędów: Aplikacja posiada własny kontroler błędów (CustomErrorController), który przechwytuje błędy (np. 404) i wyświetla użytkownikowi przyjazny komunikat.  

## 5. ARCHITEKTURA I TECHNOLOGIA
1. Aplikacja wykorzystuje standardową architekturę wielowarstwową Spring Boot (MVC). Kontrolery (klasy w pakiecie `controller`) przyjmują żądania HTTP i delegują zadania do warstwy usług (`service`). Logika biznesowa znajduje się w warstwie serwisów (np. `UserService`), która z kolei używa repozytoriów (`PropertyRepository`, `UserRepository`) do wykonywania operacji CRUD w bazie danych. Model danych stanowią encje (`Property`, `User`) mapowane na tabele bazy przez JPA (pliki `schema.sql`, `baza.mv.db` w katalogu data).  
2. Użyte technologie:  
 - Spring Boot (Java) – framework ułatwiający konfigurację i uruchomienie aplikacji webowej (wbudowany serwer Tomcat).  
 - Spring Security – zabezpiecza logowanie i autoryzację użytkowników (`WebSecurityConfig`).  
 - Spring Data JPA – do interakcji z bazą danych (repozytoria).  
 - Thymeleaf – silnik szablonów HTML dla widoków (.html w `resources/templates`).  
 - H2 Database – wbudowana baza danych (pliki `baza.mv.db` w katalogu data).  

Dodatkowo:  
- Formularze chronione są przed atakami CSRF (Cross-Site Request Forgery) dzięki domyślnej integracji z Spring Security – do każdego formularza automatycznie dołączany jest token zabezpieczający.  
- Walidacja danych wejściowych odbywa się po stronie klienta (atrybuty HTML5 `required`, `pattern`) oraz w kontrolerach aplikacji (np. sprawdzanie unikalności loginu).  
- Hasła użytkowników są bezpiecznie hashowane przy użyciu algorytmu BCrypt przed zapisaniem w bazie danych.  

## 6. PODSUMOWANIE
Dokumentacja ta przedstawia kluczowe informacje o aplikacji `Aplikacja_zaliczenie` przygotowanej na potrzeby zaliczenia przedmiotu „Programowanie aplikacji WWW w technologii Java”. Zawiera opis funkcji aplikacji, wymagania systemowe, instrukcje instalacji i uruchomienia, a także szczegóły architektury i użytych technologii. W razie potrzeby można ją rozwijać o dodatkowe informacje i zrzuty ekranu, aby jeszcze lepiej zobrazować działanie projektu.
