package biblioServlet;
import javax.servlet.http.*;

import biblio.GestionBibliotheque;

import java.sql.*;

/** Classe pour gestion des sessions
 */

public class BiblioSessionListener implements HttpSessionListener
{
public void sessionCreated(HttpSessionEvent se)
{
}

public void sessionDestroyed(HttpSessionEvent se)
{
System.out.println("BiblioSessionListener " + se.getSession().getId());
GestionBibliotheque biblio = (GestionBibliotheque) se.getSession().getAttribute("biblio");
if (biblio != null)
  {
  System.out.println("connexion =" + biblio.cx);
  try {biblio.fermer();}
  catch (SQLException e)
    {
    System.out.println("Impossible de fermer la connexion");
    e.printStackTrace();
    }
  }
else
  System.out.println("biblio inaccessible.");
}
}
