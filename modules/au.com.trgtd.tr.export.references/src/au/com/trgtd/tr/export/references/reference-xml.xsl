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
