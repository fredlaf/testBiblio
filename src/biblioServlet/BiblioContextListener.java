package biblioServlet;

import javax.servlet.*;
import java.util.*;

/**
 * Classe pour gestion des sessions
 * <P>
 */

public class BiblioContextListener implements ServletContextListener {
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("Contexte demarre:"
				+ sce.getServletContext().getServletContextName());
		System.out
				.println("Voici les parametres du contexte tels que definis dans web.xml");
		Enumeration initParams = sce.getServletContext()
				.getInitParameterNames();
		while (initParams.hasMoreElements()) {
			String name = (String) initParams.nextElement();
			System.out.println(name + ":"
					+ sce.getServletContext().getInitParameter(name));
		}
	}

	public void contextDestroyed(ServletContextEvent sce) {
		System.out
				.println("Le contexte de l'application GestionBibliotheque vient d'etre detruit.");
	}
}
