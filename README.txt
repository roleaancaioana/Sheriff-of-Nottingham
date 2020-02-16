			 |        |
			 | Player |
			 |________| 	
                              |
			 _____|______    
			|            |
			| BasicPlayer|
			|____________|
			      |
		   ___________|___________
                  |                       |
             _____|________      _________|_______
            |              |    |                 |
            | GreedyPlayer |    |  BribedPlayer   |
            |______________|    |_________________|


- In realizarea temei m-am folosit mult de conceptul de mostenire. 

- Am observat ca cele 3 tipuri de jucatori (basic, greedy si bribed) au 
lucruri in comun, asa ca am hotarat sa-mi construiesc o clasa Player care 
sa intruneasca toate caracteristicile comune ale acestor tipuri de jucatori.

- De asemenea, jucatorii de tipul greedy sau bribed actioneaza in unele 
cazuri exact ca un basic.

- Prin urmare, clasele care ilustreaza modul cum actioneaza jucatorii de 
tipul greedy sau bribed vor fi derivate ale clasei basic.

- La fiecare clasa ce ilustreaza modul cum se comporta un jucator, am suprascris 
metodele "merchant" si "sheriff" pentru a putea implementa algoritmul de joc 
corespunzator fiecarui tip de jucator.

- In metoda "merchant" din clasa BasicPlayer prin intermediul variabilei "ok"
decid daca comerciantul are sau nu bunuri legale in mana.

- In metoda "merchant" din clasa BribedPlayer prin intermediul variabilei 
"numberOfIllegalGoodsInBag" retin numarul de bunuri ilegale pe care le va 
adauga in sacul sau un comerciant bribed. In acest fel vom putea decide
care este valoarea mitei pe care intentioneaza sa o dea comerciantul respectiv. 
De asemenea, tin sa mentionez faptul ca in arraylist-ul illegalGoodsInHand voi 
retine id-urile bunurilor ilegale pe care le are in mana comerciantul. In 
legalGoodsInHand voi retine id-urile bunurilor legale pe care le are in mana 
comerciantul.

- In metoda "sheriff" din clasa BribedPlayer am tinut cont de cazul exceptat 
cand in joc sunt doar 2 jucatori si nu va exista si un jucator
in partea dreapta pe care sa-l verifice seriful bribed. 

- Metoda "verifyPenalty" din clasa BribedPlayer ma va ajuta la completarea 
sacului fiecarui comerciant tinand cont de penalty-ul care s-ar acumula, dar si 
la determinarea numarului de bunuri ilegale din sac.

- In metoda "merchant" din clasa GreedyPlayer am tinut cont de urmatorul caz 
special: daca un comerciant greedy are doar bunuri ilegale in mana, el va alege
cartea ilegala cu profitul cel mai mare, iar apoi, daca se afla intr-o runda 
para, va mai baga in sac un bun ilegal, dar trebuie sa tinem cont ca bunul 
ilegal cu profitul cel mai mare a fost deja bagat in sacul sau. Prin urmare, 
va introduce in sac al doilea bun din punct de vedere al marimii profitului.

- In clasa Player, mentionez faptul ca in arraylist-ul allowedGoods voi 
retine bunurile care ajung in Nottingham pe taraba fiecarui jucator.

- Factory-ul PlayerFactory va ajuta la crearea fiecarui tip de jucator.

- Prin intermediul clasei PlayGame, am putut aplica efectiv regulile jocului,
tinand cont de input-ul avut. Variabila "counter" din metoda "playGame"
ma va ajuta le fiecare subrunda la extragerea corecta a cartilor 
de catre fiecare comerciant. Aceasta variabila nu se va incrementa
daca jucatorul ar fi serif, deoarece seriful nu trage carti.
Variabila "countPlayers" tot din aceasta metoda ma va ajuta la determinarea
numarului rundei in care se afla un jucator.

- In metoda "computeFinalScores" din clasa PlayGame ii adaug fiecarui jucator 
pe taraba bunurile bonus pe care le va primi atunci cand reuseste sa 
introduca un bun ilegal in Nottingham. De asemenea, mi-am construit o 
matrice care va contine frecventa de aparitie a fiecarui bun legal pe taraba 
fiecarui jucator. Aceasta matrice ma ajuta sa determin ce jucator va lua 
"King's Bonus" si "Queen's Bonus" pentru un anumit bun legal. Am calculat apoi
profitul scos din vanzarea bunurilor legale si ilegale, iar apoi am calculat
scorul final al fiecarui jucator.