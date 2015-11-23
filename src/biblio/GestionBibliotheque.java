package biblio;
/****************************************************************/
/* GestionBibliotheque.java                                     */
/****************************************************************/

import java.sql.*;

/**
 *<pre>
 *
 *Permet d'executer les transactions du systeme de gestion de bibliotheque
 *
 *Parametres:0- site du serveur SQL ("local" ou "sti")
 *           1- user id pour etablir une connexion avec le serveur SQL
 *           2- mot de passe pour le user id
 *</pre>
 */
public class GestionBibliotheque
{
public Connexion cx;
public Livre livre;
public Membre membre;
public Reservation reservation;
public GestionLivre gestionLivre;
public GestionMembre gestionMembre;
public GestionPret gestionPret;
public GestionReservation gestionReservation;
public GestionInterrogation gestionInterrogation;

public GestionBibliotheque(String adresseIP, String bd, String user, String password)
  throws BiblioException, SQLException
{
// allocation des objets pour le traitement des transactions
cx = new Connexion(adresseIP, bd, user, password);
livre = new Livre(cx);
membre = new Membre(cx);
reservation = new Reservation(cx);
gestionLivre = new GestionLivre(livre);
gestionMembre = new GestionMembre(membre);
gestionPret = new GestionPret(livre, membre, reservation);
gestionReservation = new GestionReservation(livre, membre, reservation);
gestionInterrogation = new GestionInterrogation(cx);
}

public void fermer()
  throws SQLException
{
// fermeture de la connexion
cx.fermer();
}
}
