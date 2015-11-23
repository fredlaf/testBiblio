package biblio;
import java.sql.*;

/**
 *<pre>
 * GestionInterrogation.java
 * Permet d'effectuer diverses interrogations pour la bibliotheque
 *</pre>
 */

public class GestionInterrogation {

  private PreparedStatement stmtLivresRetard;
  private PreparedStatement stmtLivresTitreMot;
  private Connexion cx;

  /**
   * Creation d'une instance
   */
  public GestionInterrogation(Connexion cx) throws SQLException {

    this.cx = cx;
    stmtLivresRetard = cx.getConnection().prepareStatement
      ("select t1.idlivre, t1.titre, t1.datePret, " +
         "to_date(?,'YYYY-MM-DD') - t1.datePret - 14 , t1.idMembre, " +
         "t2.nom, count(t3.idReservation) " +
       "from livre t1, membre t2, reservation t3 " +
       "where  t1.idmembre = t2.idmembre and " +
         "t1.idlivre = t3.idlivre (+) and " +
         "to_date(?,'YYYY-MM-DD') - t1.datePret > 14 " +
       "group by t1.idlivre, t1.titre, t1.datePret, t1.idMembre, t2.nom");

    stmtLivresTitreMot = cx.getConnection().prepareStatement
      ("select t1.idLivre, t1.titre, t1.auteur, t1.idmembre, t1.datePret + 14 " +
       "from livre t1 " +
       "where lower(titre) like ?");
  }

  /**
   * Affiche les livres pretes depuis plus de 14 jours.
   */
  public void listerLivresRetard(String date) throws SQLException {

    stmtLivresRetard.setString(1,date);
    stmtLivresRetard.setString(2,date);
    ResultSet rset = stmtLivresRetard.executeQuery();

    System.out.println("idlivre titre datePret retard idmembre nom nb_res");
    while (rset.next()) {
      System.out.println(
        rset.getInt(1) + " " +
        rset.getString(2) + " " +
        rset.getDate(3) + " " +
        rset.getInt(4) + " " +
        rset.getInt(5) + " " +
        rset.getString(6) + " " +
        rset.getInt(7));
      }
  }

  /**
   * Affiche les livres contenu un mot dans le titre
   */
  public void listerLivresTitre(String mot) throws SQLException {

    stmtLivresTitreMot.setString(1,"%" + mot + "%");
    ResultSet rset = stmtLivresTitreMot.executeQuery();

    int idMembre;
    System.out.println("idLivre titre auteur idMembre dateRetour");
    while (rset.next()) {
      System.out.print(
        rset.getInt(1) + " " +
        rset.getString(2) + " " +
        rset.getString(3));
      idMembre = rset.getInt(4);
      if (!rset.wasNull())
        {
        System.out.print(" " + idMembre + " " + rset.getDate(5));
        }
      System.out.println();
      }
  }
}
