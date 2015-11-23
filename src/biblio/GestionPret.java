package biblio;
import java.sql.*;

/**
 *<pre>
 *GestionPret.java
 * Permet d'effectuer les transactions de base
 * sur un pret.
 *</pre>
 */

public class GestionPret {

  private Livre livre;
  private Membre membre;
  private Reservation reservation;
  private Connexion cx;

  /**
   * Creation d'une instance.
   * La connection de l'instance de livre et de membre doit etre la meme que cx,
   * afin d'assurer l'integrite des transactions.
   */
  public GestionPret(Livre livre, Membre membre, Reservation reservation)
     throws BiblioException
  {

    if (livre.getConnexion() != membre.getConnexion() ||
        reservation.getConnexion() != membre.getConnexion())
        throw new BiblioException
            ("Les instances de livre, de membre et de reservation n'utilisent pas la meme connexion au serveur");
    this.cx = livre.getConnexion();
    this.livre = livre;
    this.membre = membre;
    this.reservation = reservation;
  }

  /**
   * Pret d'un livre e un membre.
   * Le livre ne doit pas etre prete.
   * Le membre ne doit pas avoir depasse sa limite de pret.
   */
  public void preter(int idLivre, int idMembre, String datePret)
    throws SQLException, BiblioException, Exception
  {
    try {
    	cx.commit();
    	cx.setSerializable();
        /* Verfier si le livre est disponible */
        TupleLivre tupleLivre = livre.getLivre(idLivre);
        if (tupleLivre == null)
            throw new BiblioException("Livre inexistant: " + idLivre);
        if (tupleLivre.idMembre != 0)
            throw new BiblioException
                ("Livre " + idLivre + " deja prete a " + tupleLivre.idMembre);

        /* Verifie si le membre existe et sa limite de pret */
        TupleMembre tupleMembre = membre.getMembre(idMembre);
        if (tupleMembre == null)
            throw new BiblioException("Membre inexistant: " + idMembre);
        if (tupleMembre.nbPret >= tupleMembre.limitePret)
            throw new BiblioException
                ("Limite de pret du membre " + idMembre + " atteinte");

        /* Verifie s'il existe une reservation pour le livre */
        TupleReservation tupleReservation = reservation.getReservationLivre(idLivre);
        if (tupleReservation != null)
            throw new BiblioException("Livre reserve par : " + tupleReservation.idMembre +
                " idReservation : " + tupleReservation.idReservation);

        /* Enregistrement du pret. */
        int nb1 = livre.preter(idLivre,idMembre,datePret);
        if (nb1 == 0)
            throw new BiblioException
                ("Livre supprime par une autre transaction");
        int nb2 = membre.preter(idMembre);
        if (nb2 == 0)
            throw new BiblioException
                ("Membre supprime par une autre transaction");
        cx.commit();
        }
    catch (Exception e)
        {
        cx.rollback();
        throw e;
        }
    finally
    {
    	cx.setReadCommitted();
    }
  }

  /**
   * Renouvellement d'un pret.
   * Le livre doit etre prete.
   * Le livre ne doit pas etre reserve.
   */
  public void renouveler(int idLivre, String datePret)
    throws SQLException, BiblioException, Exception
  {
    try {
        /* Verifier si le livre est prete */
        TupleLivre tupleLivre = livre.getLivre(idLivre);
        if (tupleLivre == null)
            throw new BiblioException("Livre inexistant: " + idLivre);
        if (tupleLivre.idMembre == 0)
            throw new BiblioException
                ("Livre " + idLivre + " n'est pas prete");

        /* Verifier si date renouvellement >= datePret */
        if (Date.valueOf(datePret).before(tupleLivre.datePret))
            throw new BiblioException
                ("Date de renouvellement inferieure e la date de pret");

        /* Verifie s'il existe une reservation pour le livre */
        TupleReservation tupleReservation = reservation.getReservationLivre(idLivre);
        if (tupleReservation != null)
            throw new BiblioException("Livre reserve par : " + tupleReservation.idMembre +
                " idReservation : " + tupleReservation.idReservation);

        /* Enregistrement du pret. */
        int nb1 = livre.preter(idLivre,tupleLivre.idMembre,datePret);
        if (nb1 == 0)
            throw new BiblioException
                ("Livre supprime par une autre transaction");
        cx.commit();
        }
    catch (Exception e)
        {
        cx.rollback();
        throw e;
        }
  }

  /**
   * Retourner un livre prete
   * Le livre doit etre prete.
   */
  public void retourner(int idLivre, String dateRetour)
    throws SQLException, BiblioException, Exception
  {
    try {
    	cx.commit();//terminer la transaction courante
    	cx.setSerializable();
        /* Verifier si le livre est prete */
        TupleLivre tupleLivre = livre.getLivre(idLivre);
        if (tupleLivre == null)
            throw new BiblioException("Livre inexistant: " + idLivre);
        if (tupleLivre.idMembre == 0)
            throw new BiblioException
                ("Livre " + idLivre + " n'est pas prete ");

        /* Verifier si date retour >= datePret */
        if (Date.valueOf(dateRetour).before(tupleLivre.datePret))
            throw new BiblioException
                ("Date de retour inferieure e la date de pret");

        /* Retour du pret. */
        int nb1 = livre.retourner(idLivre);
        if (nb1 == 0)
            throw new BiblioException
                ("Livre supprime par une autre transaction");

        int nb2 = membre.retourner(tupleLivre.idMembre);
        if (nb2 == 0)
            throw new BiblioException
                ("Livre supprime par une autre transaction");
        cx.commit();
        }
    catch (Exception e)
        {
        cx.rollback();
        throw e;
        }
    finally
    {
    	cx.setReadCommitted();
    }
  }
}
