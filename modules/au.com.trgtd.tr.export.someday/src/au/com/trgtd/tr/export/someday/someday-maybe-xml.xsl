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
    <xsl:param name="include-tickle"/>
    <xsl:param name="date-format"/>    
    
    <!--=====================================================================-->
    <!-- Root element                                                        -->	
    <!--=====================================================================-->
    <xsl:template match="data">	
        <xsl:apply-templates select="futures" />
    </xsl:template>
    
    <!--=====================================================================-->
    <!-- Future items                                                        -->	
    <!--=====================================================================-->
    <xsl:template match="futures">	
        <future-items>	
            <xsl:apply-templates select="future" />	
        </future-items>
    </xsl:template>
    
    <!--=====================================================================-->
    <!-- Future item                                                         -->	
    <!--=====================================================================-->
    <xsl:template match="future">
        <item>
            <desc>
                <xsl:value-of select="desc"/>
            </desc>
            <xsl:if test="$include-topic = 'true'">
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
            <xsl:if test="$include-tickle='true'">
                <tickle>
                    <xsl:call-template name="write-date">
                        <xsl:with-param name="date" select="tickle"/>
                    </xsl:call-template>							
                </tickle>				
            </xsl:if>                        
        </item>
    </xsl:template>
    
    <!--=====================================================================-->
    <!-- Write a date                                                        -->	
    <!--=====================================================================-->
    <xsl:template name="write-date">
        <xsl:param name="date"/>		
        <xsl:choose>		
            <xsl:when test="$date-format='f1'">
                <xsl:value-of select="substring($date,1,14)"/>
            </xsl:when>		
            <xsl:when test="$date-format='f2'">
                <xsl:value-of select="substring($date,15,string-length($date))"/>
            </xsl:when>		
        </xsl:choose>				
    </xsl:template>
    
    <!--=====================================================================-->
</xsl:stylesheet>
