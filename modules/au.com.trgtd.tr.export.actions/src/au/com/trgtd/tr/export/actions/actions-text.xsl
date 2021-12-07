<?xml version="1.0" encoding="UTF-8"?>
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
<xsl:stylesheet version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
    <!--xsl:output method="text" encoding="UTF-8"/-->
    <!--xsl:output method="text" version="1.0"/-->
    
    <!--=====================================================================-->
    <!-- Parameters                                                          -->	
    <!--=====================================================================-->
    <xsl:param name="context"/>
    <xsl:param name="topic"/>
    <xsl:param name="upto"/>
    <xsl:param name="include-done"/>
    <xsl:param name="include-inactive"/>
    <xsl:param name="include-doasap"/>
    <xsl:param name="include-scheduled"/>
    <xsl:param name="include-delegated"/>
    <xsl:param name="separator"/>
    <xsl:param name="date-format"/>
    <xsl:param name="field-1"/>
    <xsl:param name="field-2"/>
    <xsl:param name="field-3"/>
    <xsl:param name="field-4"/>
    <xsl:param name="field-5"/>
    <xsl:param name="field-6"/>
    <xsl:param name="field-7"/>
    <xsl:param name="field-8"/>
    <xsl:param name="field-9"/>
    <xsl:param name="field-10"/>
    <xsl:param name="field-11"/>
    <xsl:param name="field-12"/>
    <xsl:param name="field-13"/>
    <xsl:param name="field-14"/>
    <xsl:param name="field-15"/>
    <xsl:param name="field-16"/>
    <xsl:param name="field-17"/>
    <xsl:param name="field-18"/>
    <xsl:param name="field-19"/>
    <xsl:param name="field-20"/>
    <xsl:param name="output-headings"/>
    <xsl:param name="h-field-1"/>
    <xsl:param name="h-field-2"/>
    <xsl:param name="h-field-3"/>
    <xsl:param name="h-field-4"/>
    <xsl:param name="h-field-5"/>
    <xsl:param name="h-field-6"/>
    <xsl:param name="h-field-7"/>
    <xsl:param name="h-field-8"/>
    <xsl:param name="h-field-9"/>
    <xsl:param name="h-field-10"/>
    <xsl:param name="h-field-11"/>
    <xsl:param name="h-field-12"/>
    <xsl:param name="h-field-13"/>
    <xsl:param name="h-field-14"/>
    <xsl:param name="h-field-15"/>
    <xsl:param name="h-field-16"/>
    <xsl:param name="h-field-17"/>
    <xsl:param name="h-field-18"/>
    <xsl:param name="h-field-19"/>
    <xsl:param name="h-field-20"/>    
    
    <!--=====================================================================-->
    <!-- Variables                                                           -->	
    <!--=====================================================================-->
    <xsl:variable name="upto-date">
        <xsl:choose>
            <xsl:when test="$upto='today'">
                <xsl:value-of select="substring(/data/date,1,8)"/>
            </xsl:when>
            <xsl:when test="$upto='tomorrow'">
                <xsl:value-of select="substring(/data/tomorrow,1,8)"/>
            </xsl:when>
            <xsl:when test="$upto='one-week'">
                <xsl:value-of select="substring(/data/week,1,8)"/>
            </xsl:when>
            <xsl:when test="$upto='two-weeks'">
                <xsl:value-of select="substring(/data/two-weeks,1,8)"/>
            </xsl:when>
            <xsl:when test="$upto='three-weeks'">
                <xsl:value-of select="substring(/data/three-weeks,1,8)"/>
            </xsl:when>
            <xsl:when test="$upto='four-weeks'">
                <xsl:value-of select="substring(/data/four-weeks,1,8)"/>
            </xsl:when>
            <xsl:when test="$upto='latest'">
                <xsl:value-of select="99999999"/>
            </xsl:when>
        </xsl:choose>
    </xsl:variable>
    <xsl:variable name="context-key">
        <xsl:choose>
            <xsl:when test="$context='all'">
                <xsl:value-of select="$context"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="/data/contexts/context[name=$context]/@key"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:variable>
    <xsl:variable name="topic-key">
        <xsl:choose>
            <xsl:when test="$topic='all'">
                <xsl:value-of select="$topic"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="/data/topics/topic[name=$topic]/@key"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:variable>
    
    <!--=====================================================================-->
    <!-- Root element                                                        -->
    <!--=====================================================================-->
    <xsl:template match="data">

<!--        
        <xsl:call-template name="write"><xsl:with-param name="string" select="$context"/></xsl:call-template>		
        &newline;						        
        <xsl:call-template name="write"><xsl:with-param name="string" select="$context-key"/></xsl:call-template>
        &newline;						        
        <xsl:call-template name="write"><xsl:with-param name="string" select="$topic"/></xsl:call-template>							
        &newline;						        
        <xsl:call-template name="write"><xsl:with-param name="string" select="$topic-key"/></xsl:call-template>
        &newline;						
-->
        <xsl:choose>
            <xsl:when test="$output-headings='true'">
                <xsl:call-template name="headings"/>
            </xsl:when>
        </xsl:choose>
        <xsl:apply-templates select="actions/action"/>
        <xsl:apply-templates select="single_actions/action"/>
    </xsl:template>
    
    <!--=====================================================================-->
    <!-- Action filter                                                       -->	
    <!--=====================================================================-->
    <xsl:template match="action">
        <xsl:variable name="type" select="state/@type"/>
        <xsl:variable name="date" select="substring(state/date,1,8)"/>
        <xsl:choose>
            <xsl:when test="$include-done='false' and done='true'"/>
            <xsl:when test="$include-doasap='false' and $type='ASAP'"/>
            <xsl:when test="$include-inactive='false' and $type='INACTIVE'"/>
            <xsl:when test="$include-scheduled='false' and $type='SCHEDULED'"/>
            <xsl:when test="$include-delegated='false' and $type='DELEGATED'"/>
            <xsl:when test="$type='SCHEDULED' and $date and number($date) &gt; number($upto-date)"/>
            <xsl:when test="$type='DELEGATED' and $date and number($date) &gt; number($upto-date)"/>
            <xsl:when test="$context!='all' and context-key!=$context-key"/>
            <xsl:when test="$topic!='all' and topic-key!=$topic-key"/>
            <xsl:otherwise>
                <xsl:call-template name="export-action">
                    <xsl:with-param name="action" select="."/>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>	
    
    <!--=====================================================================-->
    <!-- Headings export                                                        -->	
    <!--=====================================================================-->
    <xsl:template name="headings">
        <xsl:if test="$h-field-1 != 'none'">
            <xsl:call-template name="write">
                <xsl:with-param name="string" select="$h-field-1"/>
            </xsl:call-template>
        </xsl:if>
        <xsl:if test="$h-field-2 != 'none'">
            <xsl:call-template name="write-separator"/>            
            <xsl:call-template name="write">
                <xsl:with-param name="string" select="$h-field-2"/>
            </xsl:call-template>
        </xsl:if>
        <xsl:if test="$h-field-3 != 'none'">
            <xsl:call-template name="write-separator"/>            
            <xsl:call-template name="write">
                <xsl:with-param name="string" select="$h-field-3"/>
            </xsl:call-template>
        </xsl:if>
        <xsl:if test="$h-field-4 != 'none'">
            <xsl:call-template name="write-separator"/>            
            <xsl:call-template name="write">
                <xsl:with-param name="string" select="$h-field-4"/>
            </xsl:call-template>
        </xsl:if>
        <xsl:if test="$h-field-5 != 'none'">
            <xsl:call-template name="write-separator"/>            
            <xsl:call-template name="write">
                <xsl:with-param name="string" select="$h-field-5"/>
            </xsl:call-template>
        </xsl:if>
        <xsl:if test="$h-field-6 != 'none'">
            <xsl:call-template name="write-separator"/>            
            <xsl:call-template name="write">
                <xsl:with-param name="string" select="$h-field-6"/>
            </xsl:call-template>
        </xsl:if>
        <xsl:if test="$h-field-7 != 'none'">
            <xsl:call-template name="write-separator"/>            
            <xsl:call-template name="write">
                <xsl:with-param name="string" select="$h-field-7"/>
            </xsl:call-template>
        </xsl:if>
        <xsl:if test="$h-field-8 != 'none'">
            <xsl:call-template name="write-separator"/>            
            <xsl:call-template name="write">
                <xsl:with-param name="string" select="$h-field-8"/>
            </xsl:call-template>
        </xsl:if>
        <xsl:if test="$h-field-9 != 'none'">
            <xsl:call-template name="write-separator"/>            
            <xsl:call-template name="write">
                <xsl:with-param name="string" select="$h-field-9"/>
            </xsl:call-template>
        </xsl:if>
        <xsl:if test="$h-field-10 != 'none'">
            <xsl:call-template name="write-separator"/>            
            <xsl:call-template name="write">
                <xsl:with-param name="string" select="$h-field-10"/>
            </xsl:call-template>
        </xsl:if>
        <xsl:if test="$h-field-11 != 'none'">
            <xsl:call-template name="write-separator"/>            
            <xsl:call-template name="write">
                <xsl:with-param name="string" select="$h-field-11"/>
            </xsl:call-template>
        </xsl:if>
        <xsl:if test="$h-field-12 != 'none'">
            <xsl:call-template name="write-separator"/>            
            <xsl:call-template name="write">
                <xsl:with-param name="string" select="$h-field-12"/>
            </xsl:call-template>
        </xsl:if>
        <xsl:if test="$h-field-13 != 'none'">
            <xsl:call-template name="write-separator"/>            
            <xsl:call-template name="write">
                <xsl:with-param name="string" select="$h-field-13"/>
            </xsl:call-template>
        </xsl:if>
        <xsl:if test="$h-field-14 != 'none'">
            <xsl:call-template name="write-separator"/>            
            <xsl:call-template name="write">
                <xsl:with-param name="string" select="$h-field-14"/>
            </xsl:call-template>
        </xsl:if>
        <xsl:if test="$h-field-15 != 'none'">
            <xsl:call-template name="write-separator"/>            
            <xsl:call-template name="write">
                <xsl:with-param name="string" select="$h-field-15"/>
            </xsl:call-template>
        </xsl:if>
        <xsl:if test="$h-field-16 != 'none'">
            <xsl:call-template name="write-separator"/>            
            <xsl:call-template name="write">
                <xsl:with-param name="string" select="$h-field-16"/>
            </xsl:call-template>
        </xsl:if>
        <xsl:if test="$h-field-17 != 'none'">
            <xsl:call-template name="write-separator"/>            
            <xsl:call-template name="write">
                <xsl:with-param name="string" select="$h-field-17"/>
            </xsl:call-template>
        </xsl:if>
        <xsl:if test="$h-field-18 != 'none'">
            <xsl:call-template name="write-separator"/>            
            <xsl:call-template name="write">
                <xsl:with-param name="string" select="$h-field-18"/>
            </xsl:call-template>
        </xsl:if>
        <xsl:if test="$h-field-19 != 'none'">
            <xsl:call-template name="write-separator"/>            
            <xsl:call-template name="write">
                <xsl:with-param name="string" select="$h-field-19"/>
            </xsl:call-template>
        </xsl:if>
        <xsl:if test="$h-field-20 != 'none'">
            <xsl:call-template name="write-separator"/>            
            <xsl:call-template name="write">
                <xsl:with-param name="string" select="$h-field-20"/>
            </xsl:call-template>
        </xsl:if>    
        &newline;
    </xsl:template>	
    
    <!--=====================================================================-->
    <!-- Action export                                                       -->	
    <!--=====================================================================-->
    <xsl:template name="export-action">
        <xsl:param name="action"/>
        <xsl:call-template name="export-field">
            <xsl:with-param name="field" select="$field-1"/>
            <xsl:with-param name="action" select="$action"/>
            <xsl:with-param name="first">true</xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="export-field">
            <xsl:with-param name="field" select="$field-2"/>
            <xsl:with-param name="action" select="$action"/>
            <xsl:with-param name="first"></xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="export-field">
            <xsl:with-param name="field" select="$field-3"/>
            <xsl:with-param name="action" select="$action"/>
            <xsl:with-param name="first"></xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="export-field">
            <xsl:with-param name="field" select="$field-4"/>
            <xsl:with-param name="action" select="$action"/>
            <xsl:with-param name="first"></xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="export-field">
            <xsl:with-param name="field" select="$field-5"/>
            <xsl:with-param name="action" select="$action"/>
            <xsl:with-param name="first"></xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="export-field">
            <xsl:with-param name="field" select="$field-6"/>
            <xsl:with-param name="action" select="$action"/>
            <xsl:with-param name="first"></xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="export-field">
            <xsl:with-param name="field" select="$field-7"/>
            <xsl:with-param name="action" select="$action"/>
            <xsl:with-param name="first"></xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="export-field">
            <xsl:with-param name="field" select="$field-8"/>
            <xsl:with-param name="action" select="$action"/>
            <xsl:with-param name="first"></xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="export-field">
            <xsl:with-param name="field" select="$field-9"/>
            <xsl:with-param name="action" select="$action"/>
            <xsl:with-param name="first"></xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="export-field">
            <xsl:with-param name="field" select="$field-10"/>
            <xsl:with-param name="action" select="$action"/>
            <xsl:with-param name="first"></xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="export-field">
            <xsl:with-param name="field" select="$field-11"/>
            <xsl:with-param name="action" select="$action"/>
            <xsl:with-param name="first"></xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="export-field">
            <xsl:with-param name="field" select="$field-12"/>
            <xsl:with-param name="action" select="$action"/>
            <xsl:with-param name="first"></xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="export-field">
            <xsl:with-param name="field" select="$field-13"/>
            <xsl:with-param name="action" select="$action"/>
            <xsl:with-param name="first"></xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="export-field">
            <xsl:with-param name="field" select="$field-14"/>
            <xsl:with-param name="action" select="$action"/>
            <xsl:with-param name="first"></xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="export-field">
            <xsl:with-param name="field" select="$field-15"/>
            <xsl:with-param name="action" select="$action"/>
            <xsl:with-param name="first"></xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="export-field">
            <xsl:with-param name="field" select="$field-16"/>
            <xsl:with-param name="action" select="$action"/>
            <xsl:with-param name="first"></xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="export-field">
            <xsl:with-param name="field" select="$field-17"/>
            <xsl:with-param name="action" select="$action"/>
            <xsl:with-param name="first"></xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="export-field">
            <xsl:with-param name="field" select="$field-18"/>
            <xsl:with-param name="action" select="$action"/>
            <xsl:with-param name="first"></xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="export-field">
            <xsl:with-param name="field" select="$field-19"/>
            <xsl:with-param name="action" select="$action"/>
            <xsl:with-param name="first"></xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="export-field">
            <xsl:with-param name="field" select="$field-20"/>
            <xsl:with-param name="action" select="$action"/>
            <xsl:with-param name="first"></xsl:with-param>
        </xsl:call-template>
        
        &newline;
    </xsl:template>

    <!--=====================================================================-->
    <!-- Field export                                                        -->	
    <!--=====================================================================-->
    <xsl:template name="export-field">
        <xsl:param name="field"/>
        <xsl:param name="action"/>
        <xsl:param name="first"/>
        <xsl:if test="not($first = 'true') and not($field ='none')">
            <xsl:call-template name="write-separator"/>
        </xsl:if>
        <xsl:choose>
            <xsl:when test="$field='none'"/>
            <xsl:when test="$field='field-key'">
                <xsl:call-template name="write">
                    <xsl:with-param name="string" select="$action/@key"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$field='field-desc'">
                <xsl:call-template name="write">
                    <xsl:with-param name="string" select="$action/desc"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$field='field-notes'">
                <xsl:call-template name="write">
                    <xsl:with-param name="string" select="$action/notes"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$field='field-created'">
                <xsl:call-template name="write-date">
                    <xsl:with-param name="date" select="$action/created"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$field='field-done'">
                <xsl:call-template name="write">
                    <xsl:with-param name="string" select="$action/done"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$field='field-done-date'">
                <xsl:call-template name="write-date">
                    <xsl:with-param name="date" select="$action/done_date"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$field='field-thought-key'">
                <xsl:call-template name="write">
                    <xsl:with-param name="string" select="$action/thought-key"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$field='field-thought-desc'">
                <xsl:call-template name="write">
                    <xsl:with-param name="string" select="//thoughts/thought[@key=$action/thought-key]/desc"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$field='field-parent-key'">
                <xsl:call-template name="write">
                    <xsl:with-param name="string" select="$action/parent-key"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$field='field-parent-desc'">
                <xsl:call-template name="write">
                    <xsl:with-param name="string" select="//projects/project[@key=$action/parent-key]/desc"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$field='field-topic-key'">
                <xsl:call-template name="write">
                    <xsl:with-param name="string" select="$action/topic-key"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$field='field-topic-desc'">
                <xsl:call-template name="write">
                    <xsl:with-param name="string" select="//topics/topic[@key=$action/topic-key]/name"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$field='field-context-key'">
                <xsl:call-template name="write">
                    <xsl:with-param name="string" select="$action/context-key"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$field='field-context-desc'">
                <xsl:call-template name="write">
                    <xsl:with-param name="string" select="//contexts/context[@key=$action/context-key]/name"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$field='field-state'">
                <xsl:call-template name="write">
                    <xsl:with-param name="string" select="$action/state/@type"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$field='field-project-path'">
                <xsl:call-template name="write">
                    <xsl:with-param name="string" select="//projects/project[@key=$action/parent-key]/path"/>
                </xsl:call-template>
            </xsl:when>            
            <!--
            <xsl:when test="$field='field-action-date'">
                <xsl:choose>
                    <xsl:when test="$action/state/@type='SCHEDULED'">		
                        <xsl:call-template name="write-date"><xsl:with-param name="date" select="$action/state/date"/></xsl:call-template>							
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:call-template name="write-date"><xsl:with-param name="date" select="$action/state/date"/></xsl:call-template>							
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            -->
            <xsl:when test="$field='field-action-date'">
                <xsl:call-template name="write-date">
                    <xsl:with-param name="date" select="$action/action-date"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$field='field-scheduled-datetime'">
                <xsl:choose>
                    <xsl:when test="$action/state/@type='SCHEDULED'">
                        <xsl:call-template name="write-date">
                            <xsl:with-param name="date" select="$action/state/date"/>
                        </xsl:call-template>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:call-template name="write">
                            <xsl:with-param name="string" select=""/>
                        </xsl:call-template>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:when test="$field='field-scheduled-duration'">
                <xsl:choose>
                    <xsl:when test="$action/state/@type='SCHEDULED'">
                        <xsl:variable name="duration">
                            <xsl:value-of select="$action/state/duration-hrs"/>
                            <xsl:text>:</xsl:text>
                            <xsl:value-of select="$action/state/duration-mns"/>
                        </xsl:variable>
                        <xsl:call-template name="write">
                            <xsl:with-param name="string" select="$duration"/>
                        </xsl:call-template>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:call-template name="write">
                            <xsl:with-param name="string" select=""/>
                        </xsl:call-template>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:when test="$field='field-delegated-to'">
                <xsl:call-template name="write">
                    <xsl:with-param name="string" select="$action/state/to"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$field='field-start-date'">
                <start-date>
                    <xsl:call-template name="write-date">
                        <xsl:with-param name="date" select="$action/start-date"/>
                    </xsl:call-template>
                </start-date>
            </xsl:when>
            <xsl:when test="$field='field-due-date'">
                <due-date>
                    <xsl:call-template name="write-date">
                        <xsl:with-param name="date" select="$action/due-date"/>
                    </xsl:call-template>
                </due-date>
            </xsl:when>
            <xsl:when test="$field='field-success'">
                <success>
                    <xsl:call-template name="write">
                        <xsl:with-param name="string" select="$action/success"/>
                    </xsl:call-template>
                </success>
            </xsl:when>
            <xsl:when test="$field='field-time'">
                <time>
                    <xsl:call-template name="write">
                        <xsl:with-param name="string" select="$action/time"/>
                    </xsl:call-template>
                </time>
            </xsl:when>
            <xsl:when test="$field='field-energy'">
                <energy>
                    <xsl:call-template name="write">
                        <xsl:with-param name="string" select="$action/energy"/>
                    </xsl:call-template>
                </energy>
            </xsl:when>
            <xsl:when test="$field='field-priority'">
                <priority>
                    <xsl:call-template name="write">
                        <xsl:with-param name="string" select="$action/priority"/>
                    </xsl:call-template>
                </priority>
            </xsl:when>
        </xsl:choose>
    </xsl:template>	
    
    <!--=====================================================================-->
    <!-- Write a string after removing seperator characters                  -->	
    <!--=====================================================================-->
    <xsl:template name="write">
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
    <!-- Write the seperator character                                       -->	
    <!--=====================================================================-->
    <xsl:template name="write-separator">
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
