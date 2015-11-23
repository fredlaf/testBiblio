package biblio;

import java.sql.*;

/**
 * Gestionnaire d'une connexion avec une BD relationnelle via JDBC.
 * 
 * 
 * Ce programme ouvrir une connexion avec une BD via JDBC.
 * La methode serveursSupportes() indique les serveurs support�s.
 * 
 * Pre-condition
 *   le driver JDBC approprie doit etre accessible.
 * 
 * Post-condition
 *   la connexion est ouverte en mode autocommit false et s�rialisable, 
 *   (s'il est support� par le serveur).
 */
public class Connexion {

	private Connection conn;

	private boolean serializable;

	/**
	 * Ouverture d'une connexion en mode autocommit false et s�rialisable (si
	 * support�)
	 * 

	 * @bd nom de la base de donn�es
	 * @user userid sur le serveur SQL
	 * @pass mot de passe sur le serveur SQL
	 */
	public Connexion(String adresseIP, String bd, String user, String pass)
			throws SQLException {

		Driver d;
		try {
				d = (Driver) Class.forName("org.postgresql.Driver")
						.newInstance();
				DriverManager.registerDriver(d);
				conn = DriverManager.getConnection("jdbc:postgresql://" + adresseIP + "/" + bd,
						user, pass);

			// mettre en mode de commit manuel
			conn.setAutoCommit(false);

			// mettre en mode serialisable si possible
			// (plus haut niveau d'integrit� l'acc�s concurrent aux donn�es)
			DatabaseMetaData dbmd = conn.getMetaData();
			if (dbmd
					.supportsTransactionIsolationLevel(Connection.TRANSACTION_SERIALIZABLE))
				serializable = true;
			else
				serializable = false;
			System.out.println("Ouverture de la connexion : "
					+ System.currentTimeMillis() + " " + conn);
		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new SQLException("JDBC Driver non instanci�");
		}
	}

	public void setSerializable() throws SQLException {
		if (serializable)
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
	}

	public void setReadCommitted() throws SQLException {
		conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
	}

	public void displayCurrentIsolationLevel() throws SQLException {
		int level = conn.getTransactionIsolation();
		if (level == Connection.TRANSACTION_SERIALIZABLE)
			System.out.println("Transaction en mode s�rialisable.");
		else if (level == Connection.TRANSACTION_READ_COMMITTED)
			System.out.println("Transaction en mode read commited.");
		else
			System.out.println("Transaction en mode inconnu.");
	}

	/**
	 * fermeture d'une connexion
	 */
	public void fermer() throws SQLException {
		conn.rollback();
		conn.close();
		System.out.println("Connexion ferm�e" + " " + conn);
	}

	/**
	 * commit
	 */
	public void commit() throws SQLException {
		conn.commit();
	}

	/**
	 * rollback
	 */
	public void rollback() throws SQLException {
		conn.rollback();
	}

	/**
	 * retourne la Connection jdbc
	 */
	public Connection getConnection() {
		return conn;
	}

	/**
	 * Retourne la liste des serveurs support�s par ce gestionnaire de
	 * connexions
	 */
	public static String serveursSupportes() {
		return "local : Oracle install� localement 127.0.0.1\n"
				+ "sti   : Oracle install� au Service des technologies de l'information\n"
				+ "postgres : Postgres install� localement\n"
				+ "access : Microsoft Access, install� localement et inscrit dans ODBC";
	}
}// Classe Connexion
