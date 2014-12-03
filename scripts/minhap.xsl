<?xml version="1.0" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="tr">
	<semilla>
		<nombre><xsl:value-of select="./td[1]"/></nombre>
		<activa>true</activa>
	        <url><xsl:value-of select="./td[2]"/></url>
        	<acronimo></acronimo>
	        <depende_de><xsl:value-of select="(preceding-sibling::tr/th)[last()]"/></depende_de>
	        <en_directorio>false</en_directorio>
	</semilla>
	</xsl:template>
	
	<xsl:template match="/">
<lista>
	<xsl:apply-templates />	 
</lista>
	</xsl:template>

</xsl:stylesheet>
