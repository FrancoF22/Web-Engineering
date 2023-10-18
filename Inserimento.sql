Delete from utente;
/*Utente*/
Insert into utente(id, nome, cognome, email, password, ruolo)
values (1,"Mario","Rossi","mr@gmail.com","numer1","responsabile");
Insert into utente(id, nome, cognome, email, password, ruolo)
values (2,"Luigi","Verdi","lv@gmail.com","luigitime","admin");
Insert into utente(id, nome, cognome, email, password, ruolo)
values (3,"Wario","Giallo","wg@gmail.com","money","studente");
Delete from aula;
/*Aula
Insert into aula(id, nome, capienza, prese_elettriche, prese_rete, attrezzatura, nota, luogo, edificio, piano, id_responsabile)
Values ('1', 'Aula Magna', '45', '6', '4', ('proiettore,schermo motorizzato,schermo manuale,impianto audio,pc fisso,microfono a filo'), 'Coppito', 'Blocco 2', 'Primo', '1');*/
Delete from calendario;
/*Calendario*/
Delete from corso;
/*Corso*/
Insert into corso(id, nome, descrizione)
values (1,"Algoritmi strutture dati", "Corso di algoritmi");
Insert into corso(id, nome, descrizione)
values (2,"Sistemi Operativi", "Corso di sistemi operativi");
Insert into corso(id, nome, descrizione)
values (3,"Ingegneria del web", "Progettazione java web application");
Delete from evento;
/*Evento*/
Insert into evento(id, nome, descrizione, tipologia, id_responsabile, id_corso)
value (1, "Algoritmi strutture dati", "esame straordinario di novembre", "esame", 1, 1);
Delete from gruppo;
/*Gruppo*/
Insert into gruppo(id, nome, descrizione, tipologia)
value (1, "Ingegneria Informatica", "Polo triennale di ingengeria informatica", "polo");
Delete from gruppo_aula;
/*Gruppo_Aula*/
Delete from aula;
/*Aula*/