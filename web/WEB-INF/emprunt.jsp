<%@ page import="java.util.*,java.text.*" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE>Syst�me de gestion de biblioth�que</TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=ISO-8859-1">
<META NAME="author" CONTENT="Marc Frappier">
<META NAME="description"
CONTENT="page d'accueil syst�me de gestion de bilbioth�que.">
</HEAD>
<BODY>
<CENTER>
<H1>Syst�me de gestion de biblioth�que</H1>
No de membre : <%= session.getAttribute("idMembre") %>
<BR>
<FORM ACTION="Emprunt" METHOD="POST">
No de livre : <INPUT TYPE="TEXT" NAME="idLivre"
  VALUE="<%= (request.getAttribute("idLivre") != null) ?
                  request.getAttribute("idLivre")
                : "" %>">
<BR>
<BR>
<INPUT TYPE="SUBMIT" NAME="preter"VALUE="Soumettre">
<BR>
<BR>
<INPUT TYPE="SUBMIT" NAME="selectionMembre" VALUE="s�lection d'un membre">
</FORM>
</CENTER>
<%-- inclusion d'une autre page pour l'affichage des messages d'erreur--%>
<jsp:include page="/WEB-INF/messageErreur.jsp" />
<BR>
<%-- Appel du servlet Logout pour revenir au menu login--%>
<a href="Logout">Sortir</a>
<BR>
Date et heure : <%= DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, Locale.CANADA_FRENCH).format(new java.util.Date()) %>
</BODY>
</HTML>
