<%@ page import="
	org.opencms.jsp.*,
	org.opencms.workplace.*,
	org.opencms.workplace.editors.*,
	org.opencms.workplace.explorer.*,
	org.opencms.jsp.*,
	java.util.*"
	buffer="none"
	session="false"
%><%
	
CmsJspActionElement cms = new CmsJspActionElement(pageContext, request, response);
CmsXmlContentEditor wp = new CmsXmlContentEditor(cms);
CmsEditorDisplayOptions options = wp.getEditorDisplayOptions();
Properties displayOptions = options.getDisplayOptions(cms);

int buttonStyle = wp.getSettings().getUserSettings().getEditorButtonStyle();

%><html>
<head>
<meta http-equiv="content-type" content="text/html; charset=<%= wp.getEncoding() %>">
<title>Button bar</title>
<link rel=stylesheet type="text/css" href="<%= wp.getStyleUri(wp.getJsp(),"workplace.css") %>">

<script type="text/javascript">
<!--

var formFrame = top.edit.editform;

function buttonAction(actionValue) {
	formFrame.buttonAction(actionValue);
}

function changeElementLanguage() {
	formFrame.document.forms["EDITOR"].elements["<%= wp.PARAM_ELEMENTLANGUAGE %>"].value = document.forms["buttons"].elements["<%= wp.PARAM_ELEMENTLANGUAGE %>"].value;
	buttonAction(4);
	formFrame.focus();
}

function confirmExit() {
	formFrame.confirmExit();
}
//-->
</script>

</head>
<body class="buttons-head" unselectable="on">
<form name="buttons" action="" method="post">
<table cellspacing="0" cellpadding="0" border="0" style="width: 100%;">	
<tr>
	<td>

<%= wp.buttonBar(wp.HTML_START) %>
<%= wp.buttonBarStartTab(0, 5) %>
<%
if (options.showElement("button.customized", displayOptions)) {%>
	<td><%= wp.buttonActionDirectEdit("buttonAction(8);", buttonStyle) %></td><%
}
%>
<%= wp.button("javascript:buttonAction(2);", null, "save_exit", "button.saveclose", buttonStyle) %>
<%= wp.button("javascript:buttonAction(3);", null, "save", "button.save", buttonStyle) %>


<%

if (options.showElement("option.element.language", displayOptions) && wp.showElementLanguageSelector()) {
	out.println(wp.buttonBarSeparator(5, 5));
	out.println(wp.buttonBarLabel("input.lang"));
	out.println("<td>" + wp.buildSelectElementLanguage("name=\"" + wp.PARAM_ELEMENTLANGUAGE + "\" width=\"150\" onchange=\"changeElementLanguage();\"") + "</td>");
}

%>
	<td class="maxwidth">&nbsp;</td>
<%

if (wp.isPreviewEnabled()) {
	// show preview button if enabled
	out.println(wp.button("javascript:buttonAction(7);", null, "preview", "button.preview", buttonStyle));
	out.println(wp.buttonBarSeparator(5, 5));
}

%>
		
<%= wp.button("javascript:confirmExit();", null, "exit", "button.close", buttonStyle) %>
<%= wp.buttonBarSpacer(5) %>
<%= wp.buttonBar(wp.HTML_END) %>

	</td>
</tr>
</table>
</form>
</body>
</html>