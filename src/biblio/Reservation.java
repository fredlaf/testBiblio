package biblio;
import java.sql.*;

/**
 *<pre>
 * Reservation.java
 * Permet d'effectuer les acces a la table reservation
 *</pre>
 */

public class Reservation {

  private PreparedStatement stmtExiste;
  private PreparedStatement stmtExisteLivre;
  private PreparedStatement stmtInsert;
  private PreparedStatement stmtDelete;
  private Connexion cx;

  /**
   * Creation d'une instance.
   */
  public Reservation(Connexion cx) throws SQLException {

    this.cx = cx;
    stmtExiste = cx.getConnection().prepareStatement
        ("select idReservation, idLivre, idMembre, dateReservation " +
         "from reservation where idReservation = ?");
    stmtExisteLivre = cx.getConnection().prepareStatement
        ("select idReservation, idLivre, idMembre, dateReservation " +
         "from reservation where idLivre = ? " +
         "order by dateReservation");
    stmtInsert = cx.getConnection().prepareStatement
        ("insert into reservation (idReservation, idlivre, idMembre, dateReservation) " +
         "values (?,?,?,to_date(?,'YYYY-MM-DD'))");
    stmtDelete = cx.getConnection().prepareStatement
        ("delete from reservation where idReservation = ?");
  }

  /**
   * Retourner la connexion associee.
   */
  public Connexion getConnexion() {

    return cx;
  }

  /**
   * Verifie si une reservation existe.
   */
  public boolean existe(int idReservation) throws SQLException {

    stmtExiste.setInt(1,idReservation);
    ResultSet rset = stmtExiste.executeQuery();
    return rset.next();
  }

  /**
   * Lecture d'une reservation.
   */
  public TupleReservation getReservation(int idReservation) throws SQLException {

    stmtExiste.setInt(1,idReservation);
    ResultSet rset = stmtExiste.executeQuery();
    if (rset.next())
        {
        TupleReservation tupleReservation = new TupleReservation();
        tupleReservation.idReservation = rset.getInt(1);
        tupleReservation.idLivre = rset.getInt(2);;
        tupleReservation.idMembre = rset.getInt(3);
        tupleReservation.dateReservation = rset.getDate(4);
        return tupleReservation;
        }
    else
        return null;
  }

  /**
   * Lecture de la premiere reservation d'un livre.
   */
  public TupleReservation getReservationLivre(int idLivre) throws SQLException {

    stmtExisteLivre.setInt(1,idLivre);
    ResultSet rset = stmtExisteLivre.executeQuery();
    if (rset.next())
        {
        TupleReservation tupleReservation = new TupleReservation();
        tupleReservation.idReservation = rset.getInt(1);
        tupleReservation.idLivre = rset.getInt(2);;
        tupleReservation.idMembre = rset.getInt(3);
        tupleReservation.dateReservation = rset.getDate(4);
        return tupleReservation;
        }
    else
        return null;
  }

  /**
   * Reservation d'un livre.
   */
  public void reserver(int idReservation, int idLivre, int idMembre,  String dateReservation)
    throws SQLException
  {
    /* Ajout de la reservation. */
    stmtInsert.setInt(1,idReservation);
    stmtInsert.setInt(2,idLivre);
    stmtInsert.setInt(3,idMembre);
    stmtInsert.setString(4,dateReservation);
    stmtInsert.executeUpdate();
  }

  /**
   * Suppression d'une reservation.
   */
  public int annulerRes(int idReservation)
    throws SQLException
  {
    /* Suppression de la reservation. */
    stmtDelete.setInt(1,idReservation);
    return stmtDelete.executeUpdate();
  }
}
