<%@ page import="java.util.*,java.text.*" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE>IFT287 - Système de gestion de bibliothèque</TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=ISO-8859-1">
<META NAME="author" CONTENT="Marc Frappier">
<META NAME="description"
CONTENT="page d'accueil système de gestion de bilbiothèque.">
</HEAD>
<BODY>
<CENTER>
<H1>Système de gestion de bibliothèque</H1>
<FORM ACTION="Login" METHOD="POST">
    <BR>
    <BR>
    User Id : <INPUT TYPE="TEXT" NAME="userIdBD" VALUE="user">
    <BR>
    <BR>
    Mot de passe : <INPUT TYPE="TEXT" NAME="motDePasseBD" VALUE="bingo">
    <BR>
    <BR>
    <BR>
    <BR>
    adresseIP : <INPUT TYPE="TEXT" NAME="adresseIP"  VALUE="localhost">

    <BR>
    <BR>
    BD : <INPUT TYPE="TEXT" NAME="bd"  VALUE="biblio">
<BR>
<BR>
<INPUT TYPE="SUBMIT" VALUE="Soumettre">
</FORM>
</CENTER>
<BR>
<%-- inclusion d'une autre page pour l'affichage des messages d'erreur--%>
<jsp:include page="/WEB-INF/messageErreur.jsp" />
<BR>
<%-- affichage de la date et heure; --%>
<%-- utile pour débogger et verifier si la page a été --%>
<%-- par le fureteur --%>
Date et heure normale de l'est: <%= DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, Locale.CANADA_FRENCH).format(new java.util.Date()) %>
</BODY>
</HTML>
