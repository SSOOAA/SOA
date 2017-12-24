<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
xmlns="http://jw.nju.edu.cn/schema">
<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>

<xsl:key name="courseId" match="学生列表/学生/课程列表//课程编号" use="."/>
<xsl:key name="type" match="//成绩性质" use="."/>
<xsl:variable name="count" select="count(学生列表/学生)"/>
<xsl:variable name="courseList"/>

<xsl:template match="/">
	<xsl:variable name="IdList">
      <xsl:for-each select="学生列表/学生/课程列表//课程编号[generate-id()=generate-id(key('courseId', .))]">
        <xsl:value-of select="."/>
        <xsl:if test="position()!=last()">|</xsl:if>
      </xsl:for-each>
    </xsl:variable>
    
    <xsl:variable name="typeList">
    	<xsl:for-each select="//成绩性质[generate-id()=generate-id(key('type', .))]">
    		<xsl:value-of select="."/>
    		<xsl:if test="position()!=last()">|</xsl:if>
    	</xsl:for-each>
    </xsl:variable>
	<课程成绩列表>
	
	<xsl:for-each select="学生列表/学生/课程列表//课程编号[generate-id()=generate-id(key('courseId', .))]">
      	<xsl:variable name="curId">
        <xsl:call-template name="output-tokens">
          <xsl:with-param name="list" select="$IdList" />
          <xsl:with-param name="separator">|</xsl:with-param>
          <xsl:with-param name="pos" select="position()"/>
        </xsl:call-template>
      	</xsl:variable>
      	
      <xsl:for-each select="../../学生成绩列表//成绩性质[generate-id()=generate-id(key('type', .))]">
      	<xsl:variable name="curType">
      	 <xsl:call-template name="output-tokens">
          <xsl:with-param name="list" select="$typeList" />
          <xsl:with-param name="separator">|</xsl:with-param>
          <xsl:with-param name="pos" select="position()"/>
        </xsl:call-template>
      	</xsl:variable>
      	
      	<课程成绩>
   		<xsl:for-each select="../../../../学生/学生成绩列表//学生成绩[课程编号=$curId]">
   		<xsl:sort select="得分" order="descending"/>
   			<xsl:if test="成绩性质=$curType">
   			<xsl:variable name="curStu" select="../../个人基本信息/个人编号"/>
			<成绩>
				<学号><xsl:value-of select="$curStu"/></学号>
				<得分><xsl:value-of select="得分"/></得分>
			</成绩>
			</xsl:if>
		</xsl:for-each>
		<课程编号><xsl:value-of select="$curId"/></课程编号>
		<成绩性质><xsl:value-of select="$curType"/></成绩性质>
	
	</课程成绩>
 </xsl:for-each>
 </xsl:for-each>
 </课程成绩列表>
 </xsl:template>
 
 <xsl:template name="output-tokens">
    <xsl:param name="list" />
    <xsl:param name="separator" />
    <xsl:param name="pos" />
    <xsl:variable name="newlist" select="concat(normalize-space($list), $separator)" />
    <xsl:variable name="first" select="substring-before($newlist, $separator)" />
    <xsl:variable name="remaining" select="substring-after($newlist, $separator)" />

    <xsl:choose>
      <xsl:when test="$pos = 1">
        <xsl:value-of select="$first"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:call-template name="output-tokens">
          <xsl:with-param name="list" select="$remaining" />
          <xsl:with-param name="separator" select="$separator" />
          <xsl:with-param name="pos" select="$pos - 1"/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
 </xsl:template>
</xsl:stylesheet>