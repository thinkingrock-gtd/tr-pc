<?xml version="1.0"?> 
<!-- 
/*
 * ThinkingRock, a project management tool for Personal Computers. 
 * Copyright (C) 2006 Avente Pty Ltd
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.1">
    
    <!--xsl:output method="xml" version="1.0" encoding="UTF-8" omit-xml-declaration="no" indent="yes"/-->
    <xsl:output method="xml" version="1.0" omit-xml-declaration="yes" indent="yes"/>
    
    <!--=====================================================================-->
    <!-- Parameters                                                          -->	
    <!--=====================================================================-->
    <xsl:param name="include-topic"/>
    <xsl:param name="include-notes"/>
    
    <!--=====================================================================-->
    <!-- Root element                                                        -->	
    <!--=====================================================================-->
    <xsl:template match="data">	
        <xsl:apply-templates select="infos" />
    </xsl:template>
    
    <!--=====================================================================-->
    <!-- Information items                                                   -->	
    <!--=====================================================================-->
    <xsl:template match="infos">
        <information-items>
            <xsl:apply-templates select="info" />
        </information-items>
    </xsl:template>
    
    <!--=====================================================================-->
    <!-- Information item                                                    -->	
    <!--=====================================================================-->
    <xsl:template match="info">
        <item>	
            <desc>
                <xsl:value-of select="desc"/>
            </desc>
            <xsl:if test="$include-topic='true'">
                <topic>
                    <xsl:variable name="topic-key" select="topic-key"/>			
                    <xsl:choose>
                        <xsl:when test="$topic-key = 'null'">
                            <xsl:text>None</xsl:text>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="//topic[@key=$topic-key]/name"/>
                        </xsl:otherwise>
                    </xsl:choose>	
                </topic>				
            </xsl:if>
            <xsl:if test="$include-notes='true'">
                <notes>
                    <xsl:value-of select="notes"/>
                </notes>				
            </xsl:if>
        </item>			
    </xsl:template>
    
    <!--=====================================================================-->
    
</xsl:stylesheet>
