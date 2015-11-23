package biblio;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 *<pre>
 * Livre.java
 * Permet d'effectuer les acces e la table livre
 *</pre>
 */

public class Livre {

  private PreparedStatement stmtExiste;
  private PreparedStatement stmtInsert;
  private PreparedStatement stmtUpdate;
  private PreparedStatement stmtDelete;
  private PreparedStatement stmtListePret;
  private Connexion cx;

  /**
   * Creation d'une instance. Des enonces SQL pour chaque requete sont precompiles.
   */
  public Livre(Connexion cx) throws SQLException {

    this.cx = cx;
    stmtExiste = cx.getConnection().prepareStatement
        ("select idlivre, titre, auteur, dateAcquisition,  idMembre, datePret from livre where idlivre = ?");
    stmtInsert = cx.getConnection().prepareStatement
        ("insert into livre (idLivre, titre, auteur, dateAcquisition) " +
         "values (?,?,?,to_date(?,'YYYY-MM-DD'))");
    stmtUpdate = cx.getConnection().prepareStatement
        ("update livre set idMembre = ?, datePret = to_date(?,'YYYY-MM-DD') " +
         "where idLivre = ?");
    stmtDelete = cx.getConnection().prepareStatement
        ("delete from livre where idlivre = ?");
    stmtListePret = cx.getConnection().prepareStatement
    ("select idLivre, titre, auteur, dateAcquisition, idMembre, datePret " +
     "from livre where idmembre = ?");
    
  }

  /**
   * Retourner la connexion associee.
   */
  public Connexion getConnexion() {

    return cx;
  }

  /**
   * Verifie si un livre existe.
   */
  public boolean existe(int idLivre) throws SQLException {

    stmtExiste.setInt(1,idLivre);
    ResultSet rset = stmtExiste.executeQuery();
    return rset.next();
  }

  /**
   * Lecture d'un livre.
   */
  public TupleLivre getLivre(int idLivre) throws SQLException {

    stmtExiste.setInt(1,idLivre);
    ResultSet rset = stmtExiste.executeQuery();
    if (rset.next())
        {
        TupleLivre tupleLivre = new TupleLivre();
        tupleLivre.idLivre = idLivre;
        tupleLivre.titre = rset.getString(2);
        tupleLivre.auteur = rset.getString(3);
        tupleLivre.dateAcquisition = rset.getDate(4);
        tupleLivre.idMembre = rset.getInt(5);
        tupleLivre.datePret = rset.getDate(6);
        return tupleLivre;
        }
    else
        return null;
  }

  /**
   * Ajout d'un nouveau livre dans la base de donnees.
   */
  public void acquerir(int idLivre, String titre, String auteur, String dateAcquisition)
    throws SQLException
  {
    /* Ajout du livre. */
    stmtInsert.setInt(1,idLivre);
    stmtInsert.setString(2,titre);
    stmtInsert.setString(3,auteur);
    stmtInsert.setString(4,dateAcquisition);
    stmtInsert.executeUpdate();
  }

  /**
   * Pret d'un livre e un membre.
   */
  public int preter(int idLivre, int idMembre, String datePret)
    throws SQLException
  {
    /* Enregistrement du pret. */
    stmtUpdate.setInt(1,idMembre);
    stmtUpdate.setString(2,datePret);
    stmtUpdate.setInt(3,idLivre);
    return stmtUpdate.executeUpdate();
  }

  /**
   * Retour d'un pret
   */
  public int retourner(int idLivre)
    throws SQLException
  {
    /* Enregistrement du pret. */
    stmtUpdate.setNull(1,Types.INTEGER);
    stmtUpdate.setNull(2,Types.DATE);
    stmtUpdate.setInt(3,idLivre);
    return stmtUpdate.executeUpdate();
  }

  /**
   * Suppression d'un livre de la base de donnees.
   */
  public int vendre(int idLivre)
    throws SQLException
  {
    /* Suppression du livre. */
    stmtDelete.setInt(1,idLivre);
    return stmtDelete.executeUpdate();
  }

  public List calculerListePret(int idMembre)
    throws SQLException
  {
  stmtListePret.setInt(1,idMembre);
  ResultSet rset = stmtListePret.executeQuery();
  List listePret = new LinkedList();
  while (rset.next())
    {
    listePret.add(
      new TupleLivre(
        rset.getInt("idLivre"),
        rset.getString("titre"),
        rset.getString("auteur"),
        rset.getDate("dateAcquisition"),
        rset.getInt("idMembre"),
        rset.getDate("datePret")
        )
      );
    }
  rset.close();
  return listePret;
  }
}
