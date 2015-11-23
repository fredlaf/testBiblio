package biblio;
import java.sql.*;

/**
 * <pre>
 * TupleReservation.java
 * Permet de representer un tuple de la table des reservations.
 *</pre>
*/

public class TupleReservation {

  public int    idReservation;
  public int    idLivre;
  public int    idMembre;
  public Date   dateReservation;
}
