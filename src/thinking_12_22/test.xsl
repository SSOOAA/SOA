<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
<xsl:template match="/">
    <xsl:apply-templates select="applet"/>
</xsl:template>
<xsl:template match="applet">
    <applet code="{code/@class}" codebase="{codebase}/java" code1="{code}"  test="{test/@class}" test1="{test}" test2="{applet}"/>

</xsl:template>

</xsl:stylesheet>