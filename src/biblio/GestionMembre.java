package biblio;
import java.sql.*;

/**
 *<pre>
 *GestionMembre.java
 * Permet d'effectuer les transactions sur un membre
 * - ajouter
 * - supprimer
 *</pre>
 */

public class GestionMembre {

  private Connexion cx;
  private Membre membre;

  /**
   * Creation d'une instance
   */
  public GestionMembre(Membre membre) {

    this.cx = membre.getConnexion();
    this.membre = membre;
  }

  /**
   * Ajout d'un nouveau membre dans la base de donnees.
   * S'il existe deja, une exception est levee.
   */
  public void inscrire(int idMembre, String nom, long telephone, int limitePret)
    throws SQLException, BiblioException, Exception
  {
    try {
        /* Verifie si le membre existe deja */
        if (membre.existe(idMembre))
            throw new BiblioException("Membre existe deja: " + idMembre);

        /* Ajout du membre. */
        membre.inscrire(idMembre, nom, telephone, limitePret);
        cx.commit();
        }
    catch (Exception e)
        {
        cx.rollback();
        throw e;
        }
  }

  /**
   * Suppression d'un membre de la base de donnees.
   */
  public void desinscrire(int idMembre)
    throws SQLException, BiblioException, Exception
  {
    try {
        /* Verifie si le membre existe et son nombre de pret en cours */
        TupleMembre tupleMembre = membre.getMembre(idMembre);
        if (tupleMembre == null)
            throw new BiblioException("Membre inexistant: " + idMembre);
        if (tupleMembre.nbPret > 0)
            throw new BiblioException
                ("Le membre " + idMembre + " a encore des prets.");

        /* Suppression du membre */
        int nb = membre.desinscrire(idMembre);
        if (nb == 0)
            throw new BiblioException
            ("Membre " + idMembre + " inexistant");
        cx.commit();
        }
    catch (Exception e)
        {
        cx.rollback();
        throw e;
        }
  }
}
