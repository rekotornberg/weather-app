# WeatherAppWeek5

Tämä sovellus hakee säätiedot OpenWeatherMap palvelusta ja näyttää ne Android sovelluksessa Jetpack Composen avulla.

## Mitä Retrofit tekee?

Retrofit hoitaa yhteyden API:in.  
Se lähettää HTTP-pyynnön OpenWeather-palveluun ja tuo vastauksen takaisin sovellukseen Kotlin-oliona.

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

API-avain on tallennettu local.properties-tiedostoon.  
Gradle siirtää sen BuildConfigiin, josta se otetaan käyttöön API kutsussa.  
Näin avain ei ole kovakoodattuna sovellukseen.
