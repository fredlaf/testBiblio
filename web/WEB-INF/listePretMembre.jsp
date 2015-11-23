<%@ page import="java.util.*,java.text.*,biblio.*" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE>SGB : Liste des prêts d'un membre</TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=ISO-8859-1">
<META NAME="author" CONTENT="Marc Frappier">
<META NAME="description"
CONTENT="Menu d'un membre">
</HEAD>
<BODY>
<CENTER>
<H1>Système de gestion de bibliothèque</H1>
<H2>Liste des prêts</H2>
idMembre : <%= session.getAttribute("idMembre") %>
<BR>
<BR>
<FORM ACTION="ListePretMembre" METHOD="POST">
<%
  // calcul de la liste de prêts du membre
  int idMembre = Integer.parseInt((String) session.getAttribute("idMembre"));
  GestionBibliotheque biblio = (GestionBibliotheque) session.getAttribute("biblio");
  List listePret = biblio.livre.calculerListePret(idMembre);
  // affichage de la liste des prêts du membre
  if ( !listePret.isEmpty() )
    {
%>
    <table
    style="width: 50%; text-align: left; margin-left: auto; margin-right: auto;"
    border="1" cellspacing="2" cellpadding="2">
      <tbody>
    <%-- titre des colonnes --%>
        <tr>
        <td style="vertical-align: top;">Sélection<br></td>
        <td style="vertical-align: top;">Titre<br></td>
        <td style="vertical-align: top;">Auteur<br></td>
        <td style="vertical-align: top;">Date Prêt<br></td>
        </tr>
<%
    ListIterator it = listePret.listIterator();
    while (it.hasNext())
      {
      TupleLivre tuplelivre = (TupleLivre) it.next();
%>
        <tr>
        <td style="vertical-align: top;"><INPUT TYPE="RADIO"
          NAME="pretSelectionne" VALUE="<%= tuplelivre.idLivre %>"><br></td>
        <td style="vertical-align: top;"><%= ServletUtilities.filter(tuplelivre.titre) %><br></td>
        <td style="vertical-align: top;"><%= ServletUtilities.filter(tuplelivre.auteur) %><br></td>
        <td style="vertical-align: top;"><%= tuplelivre.datePret %><br></td>
        </tr>
<%
      }
%>
      </tbody>
    </table>
    <BR>
    <INPUT TYPE="SUBMIT" NAME="retourner" VALUE="Retourner">
    <INPUT TYPE="SUBMIT" NAME="renouveler" VALUE="Renouveler">
    <BR>
<%
    }
  else
    {
%>
    aucun prêt en cours <BR>
<%
    }
%>
<BR>
<INPUT TYPE="SUBMIT" NAME="emprunter" VALUE="Emprunter">
<INPUT TYPE="SUBMIT" NAME="selectionMembre" VALUE="sélection d'un membre">
</FORM>
</CENTER>
<%-- inclusion d'une autre page pour l'affichage des messages d'erreur--%>
<jsp:include page="/WEB-INF/messageErreur.jsp" />
<BR>
<a href="Logout">Sortir</a>
<BR>
Date et heure : <%= DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, Locale.CANADA_FRENCH).format(new java.util.Date()) %>
</BODY>
</HTML>
