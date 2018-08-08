<?xml version="1.0"?> 
<!-- 
/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can get a copy of the License at http://www.thinkingrock.com.au/cddl.html
 * or http://www.thinkingrock.com.au/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.thinkingrock.com.au/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * The Original Software is ThinkingRock. The Initial Developer of the Original
 * Software is Avente Pty Ltd, Australia.
 *
 * Portions Copyright 2006-2007 Avente Pty Ltd. All Rights Reserved.
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
    <xsl:param name="output-headings"/>    
    <xsl:param name="heading-descr"/>
    <xsl:param name="heading-topic"/>
    <xsl:param name="heading-notes"/>
    
    <!--=====================================================================-->
    <!-- Root element                                                        -->	
    <!--=====================================================================-->
    <xsl:template match="data">	
        <xsl:if test="$output-headings = 'true'">
            <xsl:call-template name="headings"/>
        </xsl:if>
        <xsl:apply-templates select="infos" />
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
        
        <!-- End Of Line -->				
        &newline;        
        
    </xsl:template>
    
    <!--=====================================================================-->
    <!-- Information items                                                   -->	
    <!--=====================================================================-->
    <xsl:template match="infos">
        <xsl:apply-templates select="info" />
    </xsl:template>
    
    <!--=====================================================================-->
    <!-- Information item                                                    -->	
    <!--=====================================================================-->
    <xsl:template match="info">
        
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
        
        <!-- End Of Line -->				
        &newline;
        
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
    
</xsl:stylesheet>
