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
<!DOCTYPE stylesheet [
<!ENTITY newline "<xsl:text>
</xsl:text>">
]>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.1">
    
    <!--xsl:output method="text" encoding="UTF-8"/-->
    <xsl:output method="text"/>
    
    <!--=====================================================================-->
    <!-- Parameters                                                          -->	
    <!--=====================================================================-->
    <xsl:param name="separator"/>
    <xsl:param name="include-topic"/>
    <xsl:param name="include-notes"/>
    <xsl:param name="include-tickle"/>
    <xsl:param name="date-format"/>    
    <xsl:param name="output-headings"/>    
    <xsl:param name="heading-descr"/>
    <xsl:param name="heading-topic"/>
    <xsl:param name="heading-notes"/>
    <xsl:param name="heading-tickle"/>
    
    <!--=====================================================================-->
    <!-- Root element                                                        -->	
    <!--=====================================================================-->
    <xsl:template match="data">	
        <xsl:if test="$output-headings = 'true'">
            <xsl:call-template name="headings"/>
        </xsl:if>
        <xsl:apply-templates select="futures" />
    </xsl:template>
    
    <!--=====================================================================-->
    <!-- Headings                                                            -->	
    <!--=====================================================================-->
    <xsl:template name="headings">
        
        <!-- Description -->
        <xsl:call-template name="print">
            <xsl:with-param name="string" select="$heading-descr"/>
        </xsl:call-template>				  	
        
        <!-- Topic if requested -->
        <xsl:if test="$include-topic = 'true'">
            <xsl:call-template name="print-separator"/>
            <xsl:call-template name="print">
                <xsl:with-param name="string" select="$heading-topic"/>
            </xsl:call-template>				  	
        </xsl:if>
        
        <!-- Notes if requested -->
        <xsl:if test="$include-notes = 'true'">
            <xsl:call-template name="print-separator"/>
            <xsl:call-template name="print">
                <xsl:with-param name="string" select="$heading-notes"/>
            </xsl:call-template>				  	
        </xsl:if>        
        
        <!-- Tickle if requested -->
        <xsl:if test="$include-tickle = 'true'">
            <xsl:call-template name="print-separator"/>
            <xsl:call-template name="print">
                <xsl:with-param name="string" select="$heading-tickle"/>
            </xsl:call-template>				  	
        </xsl:if>                
        
        <!-- End Of Line -->				
        &newline;        
        
    </xsl:template>    
    
    <!--=====================================================================-->
    <!-- Future items                                                        -->	
    <!--=====================================================================-->
    <xsl:template match="futures">
        <xsl:apply-templates select="future" />
    </xsl:template>
    
    <!--=====================================================================-->
    <!-- Future item                                                         -->	
    <!--=====================================================================-->
    <xsl:template match="future">
        
        <!-- Description -->
        <xsl:call-template name="print">
            <xsl:with-param name="string" select="desc" />
        </xsl:call-template>				  	
        
        <!-- Topic if requested -->
        <xsl:if test="$include-topic = 'true'">
            <xsl:variable name="topicKey" select="topic-key"/>			
            <xsl:call-template name="print-separator" />
            <xsl:choose>
                <xsl:when test="$topicKey = 'null'">
                    <xsl:text>None</xsl:text>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:call-template name="print">
                        <xsl:with-param name="string" select="//topic[@key=$topicKey]/name" />
                    </xsl:call-template>				  	
                </xsl:otherwise>
            </xsl:choose>	
        </xsl:if>
        
        <!-- Notes if requested -->
        <xsl:if test="$include-notes = 'true'">
            <xsl:call-template name="print-separator" />
            <xsl:call-template name="print">
                <xsl:with-param name="string" select="notes" />
            </xsl:call-template>				  	
        </xsl:if>        
        
        <!-- Tickle date if requested -->
        <xsl:if test="$include-tickle = 'true'">
            <xsl:call-template name="print-separator" />
            <xsl:call-template name="print-date">
                <xsl:with-param name="date" select="tickle"/>
            </xsl:call-template>							
        </xsl:if>        
        
        <!-- End Of Line -->				
        <!-- <xsl:if test="not(position()=last())"> -->
        &newline;
        <!-- </xsl:if> -->
		
    </xsl:template>
    
    <!--=====================================================================-->
    <!-- Print string after removing seperator characters                    -->	
    <!--=====================================================================-->
    <xsl:template name="print">
        <xsl:param name="string"/>				
        <xsl:choose>
            <xsl:when test="$separator='comma'">
                <xsl:value-of select="translate($string, ',', '')"/>
            </xsl:when>
            <xsl:when test="$separator='semicolon'">
                <xsl:value-of select="translate($string, ';', '')"/>
            </xsl:when>
            <xsl:when test="$separator = 'tab'">
                <xsl:value-of select="translate($string, '&#09;', '')"/>
            </xsl:when>
        </xsl:choose>			
    </xsl:template>	
    
    <!--=====================================================================-->
    <!-- Print the seperator character                                       -->	
    <!--=====================================================================-->
    <xsl:template name="print-separator">
        <xsl:choose>
            <xsl:when test="$separator='comma'">
                <xsl:text>,</xsl:text>
            </xsl:when>
            <xsl:when test="$separator='semicolon'">
                <xsl:text>;</xsl:text>
            </xsl:when>
            <xsl:when test="$separator = 'tab'">
                <xsl:text>&#09;</xsl:text>
            </xsl:when>
        </xsl:choose>	
    </xsl:template>	
    
    <!--=====================================================================-->
    
    <!--=====================================================================-->
    <!-- Write a date                                                        -->	
    <!--=====================================================================-->
    <xsl:template name="print-date">
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
