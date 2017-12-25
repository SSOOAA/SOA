<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns="http://jw.nju.edu.cn/schema">
    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>

    <xsl:key name="courseId" match="学生列表/学生/课程列表//课程编号" use="."/>
    <xsl:key name="type" match="//成绩性质" use="."/>

    <xsl:template match="/">

        <xsl:element name="课程成绩列表">

            <xsl:for-each select="学生列表/学生/课程列表//课程编号[generate-id()=generate-id(key('courseId', .))]">
                <xsl:variable name="curId" select="."/>

                <xsl:for-each select="../../学生成绩列表//成绩性质[generate-id()=generate-id(key('type', .))]">
                    <xsl:variable name="curType" select="."/>

                    <xsl:element name="课程成绩">
                        <xsl:attribute name="课程编号">
                            <xsl:value-of select="$curId"/>
                        </xsl:attribute>
                        <xsl:attribute name="成绩性质">
                            <xsl:value-of select="$curType"/>
                        </xsl:attribute>
                        <xsl:for-each select="../../../../学生/学生成绩列表//学生成绩[课程编号=$curId]">
                            <xsl:sort select="得分" order="descending"/>
                            <xsl:if test="成绩性质=$curType">
                                <xsl:variable name="curStu" select="../../个人基本信息/个人编号"/>
                                <xsl:element name="成绩">
                                    <xsl:element name="学号">
                                        <xsl:value-of select="$curStu"/>
                                    </xsl:element>
                                    <xsl:element name="得分">
                                        <xsl:value-of select="得分"/>
                                    </xsl:element>
                                </xsl:element>
                            </xsl:if>
                        </xsl:for-each>
                    </xsl:element>
                </xsl:for-each>
            </xsl:for-each>
        </xsl:element>
    </xsl:template>

</xsl:stylesheet>