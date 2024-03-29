package biblio;

import java.sql.*;
import java.util.*;

/**
 *<pre>
 *Membre.java
 * Permet d'effectuer les acces e la table membre.
 *</pre>
 */

public class Membre {

	private PreparedStatement stmtExiste;

	private PreparedStatement stmtInsert;

	private PreparedStatement stmtUpdateIncrNbPret;

	private PreparedStatement stmtUpdateDecNbPret;

	private PreparedStatement stmtDelete;

	private Connexion cx;

	/**
	 * Creation d'une instance. Precompilation d'enonces SQL.
	 */
	public Membre(Connexion cx) throws SQLException {
		this.cx = cx;
		stmtExiste = cx
				.getConnection()
				.prepareStatement(
						"select idMembre, nom, telephone, limitePret, nbpret from membre where idmembre = ?");
		stmtInsert = cx.getConnection().prepareStatement(
				"insert into membre (idmembre, nom, telephone, limitepret, nbpret) "
						+ "values (?,?,?,?,0)");
		stmtUpdateIncrNbPret = cx.getConnection().prepareStatement(
				"update membre set nbpret = nbPret + 1 where idMembre = ?");
		stmtUpdateDecNbPret = cx.getConnection().prepareStatement(
				"update membre set nbpret = nbPret - 1 where idMembre = ?");
		stmtDelete = cx.getConnection().prepareStatement(
				"delete from membre where idmembre = ?");
	}

	/**
	 * Retourner la connexion associee.
	 */
	public Connexion getConnexion() {

		return cx;
	}

	/**
	 * Verifie si un membre existe.
	 */
	public boolean existe(int idMembre) throws SQLException {
		stmtExiste.setInt(1, idMembre);
		ResultSet rset = stmtExiste.executeQuery();
		return rset.next();
	}

	/**
	 * Lecture d'un membre.
	 */
	public TupleMembre getMembre(int idMembre) throws SQLException {
		stmtExiste.setInt(1, idMembre);
		ResultSet rset = stmtExiste.executeQuery();
		if (rset.next()) {
			TupleMembre tupleMembre = new TupleMembre();
			tupleMembre.idMembre = idMembre;
			tupleMembre.nom = rset.getString(2);
			tupleMembre.telephone = rset.getLong(3);
			tupleMembre.limitePret = rset.getInt(4);
			tupleMembre.nbPret = rset.getInt(5);
			return tupleMembre;
		} else
			return null;
	}

	/**
	 * Ajout d'un nouveau membre dans la base de donnees.
	 */
	public void inscrire(int idMembre, String nom, long telephone,
			int limitePret) throws SQLException {
		/* Ajout du membre. */
		stmtInsert.setInt(1, idMembre);
		stmtInsert.setString(2, nom);
		stmtInsert.setLong(3, telephone);
		stmtInsert.setInt(4, limitePret);
		stmtInsert.executeUpdate();
	}

	/**
	 * Incrementer le nb de pret d'un membre.
	 */
	public int preter(int idMembre) throws SQLException {
		stmtUpdateIncrNbPret.setInt(1, idMembre);
		return stmtUpdateIncrNbPret.executeUpdate();
	}

	/**
	 * Decrementer le nb de pret d'un membre.
	 */
	public int retourner(int idMembre) throws SQLException {
		stmtUpdateDecNbPret.setInt(1, idMembre);
		return stmtUpdateDecNbPret.executeUpdate();
	}

	/**
	 * Suppression d'un membre de la base de donnees.
	 */
	public int desinscrire(int idMembre) throws SQLException {
		/* Suppression du membre */
		stmtDelete.setInt(1, idMembre);
		return stmtDelete.executeUpdate();
	}
}
