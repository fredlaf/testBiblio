package biblioServlet;

import java.util.List;
import java.util.LinkedList;
import java.sql.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import biblio.BiblioException;
import biblio.GestionBibliotheque;

/**
 * Classe traitant la requete provenant de la page listePretMembre.jsp
 */

public class ListePretMembre extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Integer etat = (Integer) request.getSession().getAttribute("etat");
		if (etat == null) {
			RequestDispatcher dispatcher = request
					.getRequestDispatcher("/login.jsp");
			dispatcher.forward(request, response);
		} else if (etat.intValue() != BiblioConstantes.MEMBRE_SELECTIONNE) {
			RequestDispatcher dispatcher = request
					.getRequestDispatcher("/WEB-INF/selectionMembre.jsp");
			dispatcher.forward(request, response);
		} else if (request.getParameter("retourner") != null)
			traiterRetourner(request, response);
		else if (request.getParameter("renouveler") != null)
			traiterRenouveler(request, response);
		else if (request.getParameter("emprunter") != null)
			traiterEmprunter(request, response);
		else if (request.getParameter("selectionMembre") != null)
			traiterSelectionMembre(request, response);
		else {
			List listeMessageErreur = new LinkedList();
			listeMessageErreur.add("Choix non reconnu");
			request.setAttribute("listeMessageErreur", listeMessageErreur);
			RequestDispatcher dispatcher = request
					.getRequestDispatcher("/WEB-INF/listePretMembre.jsp");
			dispatcher.forward(request, response);
		}
	}

	public void traiterRetourner(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			if (request.getParameter("pretSelectionne") == null)
				throw new BiblioException("Aucun pret selectionne");
			int idLivre = Integer.parseInt(request
					.getParameter("pretSelectionne"));
			String dateRetour = (new Date(System.currentTimeMillis()))
					.toString();
			GestionBibliotheque biblio = (GestionBibliotheque) request
					.getSession().getAttribute("biblio");
			// executer la maj en utilisant synchronized pour s'assurer
			// que le thread du servlet est le seul e executer une transaction
			// sur biblio
			synchronized (biblio) {
				biblio.gestionPret.retourner(idLivre, dateRetour);
			}
				RequestDispatcher dispatcher = request
						.getRequestDispatcher("/WEB-INF/listePretMembre.jsp");
				dispatcher.forward(request, response);
		} catch (BiblioException e) {
			List listeMessageErreur = new LinkedList();
			listeMessageErreur.add(e.toString());
			request.setAttribute("listeMessageErreur", listeMessageErreur);
			RequestDispatcher dispatcher = request
					.getRequestDispatcher("/WEB-INF/listePretMembre.jsp");
			dispatcher.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e
					.toString());
		}
	}

	public void traiterRenouveler(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			if (request.getParameter("pretSelectionne") == null)
				throw new BiblioException("Aucun pret selectionne");
			int idLivre = Integer.parseInt(request
					.getParameter("pretSelectionne"));
			String datePret = (new Date(System.currentTimeMillis())).toString();
			GestionBibliotheque biblio = (GestionBibliotheque) request
					.getSession().getAttribute("biblio");
			synchronized (biblio) {
				biblio.gestionPret.renouveler(idLivre, datePret);
			}
				RequestDispatcher dispatcher = request
						.getRequestDispatcher("/WEB-INF/listePretMembre.jsp");
				dispatcher.forward(request, response);
		} catch (BiblioException e) {
			List listeMessageErreur = new LinkedList();
			listeMessageErreur.add(e.toString());
			request.setAttribute("listeMessageErreur", listeMessageErreur);
			RequestDispatcher dispatcher = request
					.getRequestDispatcher("/WEB-INF/listePretMembre.jsp");
			dispatcher.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e
					.toString());
		}
	}

	public void traiterEmprunter(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher = request
				.getRequestDispatcher("/WEB-INF/emprunt.jsp");
		dispatcher.forward(request, response);
	}

	public void traiterSelectionMembre(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher = request
				.getRequestDispatcher("/WEB-INF/selectionMembre.jsp");
		dispatcher.forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
