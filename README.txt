Cosmin-Viorel Lovin, 325CD - Sistem Energetic: Etapa 2 POO

Jocul este realizat cu ajutorul clasei Simulate, care tine cont de luna 
in care ne aflam si simuleaza jocul incepand cu luna initiala. Apoi,
sunt simulate lunile urmatoare rand pe rand, pana jocul se incheie.
Actiunile sunt simulate cu ajutorul clasei Action care implementeaza
metode pentru fiecare pas ce trebuie realizat in joc. Prima actiune 
simulata este:
@chooseProducers.
Cand distribuitorii isi aleg producatorii sunt luate in considerare
doua cazuri: cazul in care acesta are deja distribuitori, dar trebuie 
sa faca o verificare avand in vedere ca unul dintre ei si-au schimbat
informatiile, iar cel de-al doilea caz cand distribuitorul pur si 
simplu nu are niciun producator. Fiecare distribuitor va apela metoda
@chooseProducer. Aceasta metoda genereaza un obiect de tipul 
EnergyChoice in functie de preferinta distribuitorului in legatura
cu modalitatea de generare a energiei de catre producatori. 
@EnergyChoice este o clasa ce implementeaza un obiect de tip Strategy.
Practic, pentru generarea listelor de producatori care respecta un
anumit energyChoice, s-a folosit un design pattern de tip Strategy.
Clasele Green, Price si Quantity implementeaza interfata Strategy si
intorc o lista de producatori SORTATA care respecta energyChoice-ul 
impus. Sortarea se realizeaza tot prin intermediul unor clase care
implementeaza interfata SortStrategy. Clasele SortGreen, SortPrice si
SortQuantity implementeaza toate interfata SortStrategy si scopul lor
este sa sorteze lista de producatori in functie de strategia pe care
o doreste distribuitorul.
Deci obiectul EnergyChoice din metoda @chooseProducer, va fi folosit
pentru a intoarce o lista sortata de producatori ce respecta strategia 
distribuitorului. Aceasta lista va fi parcursa si din ea vor fi extrase
id-urile producatorilor care respecta dorinta distribuitorilor, pana ce
va fi atinsa energia dorita de distribuitori.
Avand lista de id-uril ale producatorilor, ne intoarcem in metoda 
@chooseProducer, iar aici vom realiza conexiunile intre distribuitori
si producatori, populand campurile ce marcheaza acest lucru(lista de
currentMonthStats -> producers, hasProducers, producersID -> distribuitor).

Urmeaza simularea actiunii @distributorsCalculateContracts.
Dupa finalizarea acestueia urmeaza @consumersChooseDistributors.
Pentru realizarea acestei actiuni, se va sorta lista de distribuitori
in functie de cel mai mic contract, pentru a fi mai usor sa realizam
potrivirile consumer<->distributor. Sortarea se realizeaza cu ajutorul
clasei SortDistributors. Conexiunea dintre cele doua obiecte duce la 
popularea campurilor legate de distribuitor pentru consumator si la 
adaugarea in lista de consumatori a distribuitorului.

Urmeaza primirea salariilor de catre toti consumatorii si platirea 
taxelor(@consumersGetSalaries , @consumersPayTaxes). Pentru platirea
taxelor, se tine cont de doua lucruri. Consumatorul poate avea o datorie
caz in care se apeleaza metoda specifica clasei de Consumatori, 
payDebts. Totodata, prin intermediul acestei metode(@consumersPayTaxes),
distribuitorii cu care consumatorii au contract vor primi si ei 
taxele contractuale. Daca consumatorul nu are datorie se apeleaza metoda
payTaxes. Tot in cadrul acestei metode se stabileste daca un consumator
devine bankrupt sau nu.

Dupa ce se primesc salariile si consumatorii platesc taxele, e randul
distribuitorilor sa plateasca taxele, asa ca se apeleaza metoda
@distributorsPayTaxes, care apeleaza metoda specifica clasei
Distributor, @payTaxes.

Pentru simularea unei luni normal din joc, se procedeaza aproximativ 
la fel, cu exceptia faptului ca la inceputul lunii se updateaza intai
entitatile Consumer si Distributor cu ajutorul metodei @updateEntities.
Apoi, distribuitorii isi calculeaza contractele, si urmeaza ca acestia
sa ii elimine din lista de contracte pe consumatorii care sunt Bankrupt
sau care si-au incheiat contractul, @removeConsumers. Apoi toti consu-
matorii care nu au distribuitor, isi vor gasi unul, se platesc salariile,
consumatorii platesc taxele, apoi urmeaza ceva nou, se updateaza produ-
catorii, @updateProducers. Updatarea informatiilor producatorilor va 
duce la apelarea metodei @notifyDistributors, care va notifica toti
distribuitorii producatorului care a primit modificari ca valorile
acestuia s-au schimbat si ca este necesar ca distribuitorul sa isi
revizuiasca contractele cu producatorii. La randul ei, metoda 
@notifyDistributors, face exact ceea ce am prezentat mai sus
si apeleaza metoda specifica clasei Distributor @reconsiderProductionCost.
Aceasta metoda va modifica campul distributorului respectiv, 
@producerToReconsiderID si @reconsiderProductionCost.
Modificarea acestor campuri va fi folosita la apelarea metodei
@distributorsChooseProducers, cand si distribuitorii instiintati vor 
putea sa-ti caute noi producatori.

In final luna se incheie cu metoda @updateProducerStats. Aceasta
realizeaza statistica lunara cu distribuitori pentru fiecare producator.
