package biblio;
import java.sql.*;

/**
 *<pre>
 *GestionReservation.java
 * Permet d'effectuer les transactions de base
 * sur une reservation.
 *</pre>
 */

public class GestionReservation {

  private Livre livre;
  private Membre membre;
  private Reservation reservation;
  private Connexion cx;

  /**
   * Creation d'une instance.
   * La connection de l'instance de livre et de membre doit etre la meme que cx,
   * afin d'assurer l'integrite des transactions.
   */
  public GestionReservation(Livre livre, Membre membre, Reservation reservation)
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
   * Reservation d'un livre par un membre.
   * Le livre doit etre prete.
   */
  public void reserver(int idReservation, int idLivre, int idMembre, String dateReservation)
    throws SQLException, BiblioException, Exception
  {
    try {
        /* Verifier que le livre est prete */
        TupleLivre tupleLivre = livre.getLivre(idLivre);
        if (tupleLivre == null)
            throw new BiblioException("Livre inexistant: " + idLivre);
        if (tupleLivre.idMembre == 0)
            throw new BiblioException
                ("Livre " + idLivre + " n'est pas prete");
        if (tupleLivre.idMembre == idMembre)
            throw new BiblioException
                ("Livre " + idLivre + " deja prete a ce membre");

        /* Verifier que le membre existe */
        TupleMembre tupleMembre = membre.getMembre(idMembre);
        if (tupleMembre == null)
            throw new BiblioException("Membre inexistant: " + idMembre);

        /* Verifier si date reservation >= datePret */
        if (Date.valueOf(dateReservation).before(tupleLivre.datePret))
            throw new BiblioException
                ("Date de reservation inferieure e la date de pret");

        /* Verifier que la reservation n'existe pas */
        if (reservation.existe(idReservation))
            throw new BiblioException("Reservation " + idReservation + " existe deja");

        /* Creation de la reservation */
        reservation.reserver(idReservation,idLivre,idMembre,dateReservation);
        cx.commit();
        }
    catch (Exception e)
        {
        cx.rollback();
        throw e;
        }
  }

  /**
   * Prise d'une reservation.
   * Le livre ne doit pas etre prete.
   * Le membre ne doit pas avoir depasse sa limite de pret.
   * La reservation doit la etre la premiere en liste.
   */
  public void prendreRes(int idReservation, String datePret)
    throws SQLException, BiblioException, Exception
  {
    try {
    	cx.commit();
    	cx.setSerializable();
        /* Verifie s'il existe une reservation pour le livre */
        TupleReservation tupleReservation = reservation.getReservation(idReservation);
        if (tupleReservation == null)
            throw new BiblioException("Reservation inexistante : " + idReservation);

        /* Verifie que c'est la premiere reservation pour le livre */
        TupleReservation tupleReservationPremiere =
            reservation.getReservationLivre(tupleReservation.idLivre);
        if (tupleReservation.idReservation != tupleReservationPremiere.idReservation)
            throw new BiblioException("La reservation n'est pas la premiere de la liste " +
                "pour ce livre; la premiere est " + tupleReservationPremiere.idReservation);

        /* Verifier si le livre est disponible */
        TupleLivre tupleLivre = livre.getLivre(tupleReservation.idLivre);
        if (tupleLivre == null)
            throw new BiblioException("Livre inexistant: " + tupleReservation.idLivre);
        if (tupleLivre.idMembre != 0)
            throw new BiblioException
                ("Livre " + tupleLivre.idLivre + " deja prete a " + tupleLivre.idMembre);

        /* Verifie si le membre existe et sa limite de pret */
        TupleMembre tupleMembre = membre.getMembre(tupleReservation.idMembre);
        if (tupleMembre == null)
            throw new BiblioException("Membre inexistant: " + tupleReservation.idMembre);
        if (tupleMembre.nbPret >= tupleMembre.limitePret)
            throw new BiblioException
                ("Limite de pret du membre " + tupleReservation.idMembre + " atteinte");

        /* Verifier si datePret >= tupleReservation.dateReservation */
        if (Date.valueOf(datePret).before(tupleReservation.dateReservation))
            throw new BiblioException
                ("Date de pret inferieure e la date de reservation");

        /* Enregistrement du pret. */
        if (livre.preter(tupleReservation.idLivre,tupleReservation.idMembre,datePret) == 0)
            throw new BiblioException
                ("Livre supprime par une autre transaction");
        if (membre.preter(tupleReservation.idMembre) == 0)
            throw new BiblioException
                ("Membre supprime par une autre transaction");
        /* Eliminer la reservation */
        reservation.annulerRes(idReservation);
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
   * Annulation d'une reservation.
   * La reservation doit exister.
   */
  public void annulerRes(int idReservation)
    throws SQLException, BiblioException, Exception
  {
    try {

        /* Verifier que la reservation existe */
        if (reservation.annulerRes(idReservation) == 0)
            throw new BiblioException("Reservation " + idReservation + " n'existe pas");

        cx.commit();
        }
    catch (Exception e)
        {
        cx.rollback();
        throw e;
        }
  }
}
