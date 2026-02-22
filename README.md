# WeatherAppWeek6 Room tietokannan käyttö

Tähän viikko 6 harjoitukseen on lisätty sääsovellukseen Room tietokanta, mihin tallennetaan säätietoja välimuistiin.

---

## Mitä Room tekee (Entity – DAO – Database – Repository – ViewModel – UI)

**Entity**
WeatherCacheEntity määrittää tietokantataulun rakenteen.

**DAO**
WeatherDao määrittää kyselyt tietokantaan. Sen kautta haetaan tietyn kaupungin säätieto, tallennetaan välimuistiin, luetaan data UI:lle.

**Database**
AppDatabase luo Room tietokannan, ja antaa sen DAO:n käyttöön.

**Repository**
WeatherRepository yhdistää Retrofit API kutsut, Room tallennuksen ja hoitaa välimuistilogiikan, joka on nyt 30 min.

**ViewModel**
WeatherViewModel kuuntelee Room Flow dataa, kutsuu repositoryä, kun kaupunkia haetaan ja hoitaa UI-tilaa.

---

## Miten datavirta kulkee
1. Käyttäjä syöttää kaupungin ja painaa “Hae sää”.
2. ViewModel kutsuu Repositoryä.
3. Repository tarkistaa Roomista löytyykö kyseisen kaupungin data.
4. Jos tarvitaan, tehdään API kutsu Retrofitilla.
5. Data tallennetaan Roomiin.
6. Roomin Flow päivittyy.
7. ViewModel saa uuden datan.
8. Compose UI päivittyy.

---

## Miten välimuistilogiikka toimii?

Kun käyttäjä hakee säätä:

1. Repository tarkistaa onko haettua kaupungin säätietoa tallennettu Roomiin.
2. Jos data on alle 30 minuuttia vanha, API kutsua ei tehdä.
3. Jos data puuttuu tai on yli 30 minuuttia vanha, tehdään uusi API kutsu.
4. Uusi data tallennetaan Roomiin.
5. UI päivittyy Flown kautta.

# WeatherAppWeek5

Tämä sovellus hakee säätiedot OpenWeatherMap palvelusta ja näyttää ne Android sovelluksessa Jetpack Composen avulla.

## Mitä Retrofit tekee?

Retrofit hoitaa yhteyden API:in.  
Se lähettää HTTP pyynnön OpenWeather palveluun ja tuo vastauksen takaisin sovellukseen Kotlin oliona.

## Miten JSON muutetaan dataluokiksi?

API palauttaa tiedot JSON-muodossa.  
Retrofit käyttää Gsonia, joka muuntaa JSON-vastauksen automaattisesti Kotlin dataluokiksi.  
Ei tarvitse käsitellä JSON:ia käsin.

## Miten coroutines toimii tässä?

API-kutsu tehdään taustalla coroutineilla.    
Kun data saadaan, UI päivittyy automaattisesti.

## Miten UI-tila toimii?

ViewModel säilyttää sovelluksen tilan, haettu -> kaupunki -> lataus -> virhe -> säädata.  
Compose tarkkailee tätä tilaa.  
Kun tila muuttuu, näkymä päivittyy automaattisesti.

## Miten API-key on tallennettu?

API avain on tallennettu local.properties-tiedostoon.  
Gradle siirtää sen BuildConfigiin, josta se otetaan käyttöön API kutsussa.  
Näin avain ei ole kovakoodattuna sovellukseen.
