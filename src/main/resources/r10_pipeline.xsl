<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:r10Ex="http://www.nrf-arts.org/IXRetail/namespace/">
    <xsl:strip-space elements="*"/>
    <xsl:output method="html" indent="yes" version="4.0"/>
    <xsl:include href="r10_pipeline_xml2csv.xsl"/>
    <xsl:include href="r10_pipeline_equalizing.xsl"/>

    <!-- Start here -->     
    <xsl:template match="/">
        <xsl:apply-templates select="//r10Ex:Transaction" mode="s1_user-defined"/>
    </xsl:template>



    <!-- /r10Ex:POSLog/r10Ex:Transaction -->
    <xsl:template match="r10Ex:Transaction" mode="s1_user-defined">

        <!-- STEP-1: s1_flatten -->
        <!-- Automatically flattens XML, attributes and elements, to three levels. "_" and "." are used as delimiters in flattened element names. -->
        <!-- for r10Ex:Transaction -->
        <xsl:variable name="s1_flatheader">
            <xsl:apply-templates select="./*[not(self::r10Ex:ReceiptImage | self::r10Ex:RetailTransaction)]" mode="s1_flatten"/>  
        </xsl:variable>
        
        
        
        <!-- STEP-2: s2_r10_pipeline_flatxml -->
        <!-- for r10Ex:RetailTransaction -->
        <xsl:variable name="s2_joinxml">
            <xsl:element name="lines">
            <xsl:for-each select="r10Ex:RetailTransaction/r10Ex:LineItem">
                <xsl:element name="line">
                    <xsl:copy-of select="$s1_flatheader"></xsl:copy-of>
                    
                    <!-- STEP-3: s3_flatten-->
                    <!-- Automatically flattens XML, attributes and elements, to three levels. "_" and "." are used as delimiters in flattened element names. -->
                    <!-- for r10Ex:Sale -->
                    <xsl:apply-templates select="r10Ex:Sale" mode="s3_flatten"/>  
                    
                </xsl:element>
            </xsl:for-each>
            </xsl:element>
        </xsl:variable>
        
<!--        <xsl:copy-of select="$s2_flatxml"/>-->
        
        
        
        <!-- STEP-4: r10_pipeline_equalizing.xsl -->
        <xsl:variable name="s4_equalizing">
            <xsl:apply-templates mode="s4_equalizing" select="$s2_joinxml"/>
        </xsl:variable>
        
        <xsl:copy-of select="$s4_equalizing"/>
        
        
        
        <!-- STEP-5: r10_pipeline_xml2csv.xsl -->
        <!-- The final transformation of input or modified input xml file to CSV. -->
        <xsl:variable name="s5_xml2csv">
            <xsl:apply-templates mode="s7_xml2csv" select="$s4_equalizing"/>
        </xsl:variable>


<!--        <xsl:result-document href="temp_r10-pipeline.csv" method="text">
            <xsl:copy-of select="$s5_xml2csv"/>
        </xsl:result-document>-->

<!--        <xsl:value-of select="$s5_xml2csv"/>-->
       
    </xsl:template>
       
       
       
    
    
    
    
    <!-- STEP-1: s1_flatten -->
    <xsl:template match="/*" mode="s1_flatten">
        <xsl:copy>
            <!-- Attributes at top-level is ignored. -->
            <xsl:apply-templates select="node()" mode="s1_flatten"/>
        </xsl:copy>
    </xsl:template>
    
    <!-- Elements at item level.  -->
    <xsl:template match="/*/*" mode="s1_flatten">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()" mode="s1_flatten"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="*" mode="s1_flatten">
        <xsl:choose>
            <xsl:when test="*">
                <xsl:apply-templates select="@*|node()" mode="s1_flatten"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:variable name="name"
                    select="if(count(ancestor::*) > 2) then for $a in ancestor::*[count(ancestor::*) > 1] return for $b in $a return local-name($b) else ''"/>
                <xsl:element
                    name="{concat(string-join($name, '_'), if(count(ancestor::*) > 2)then '_' else '',  local-name(.))}">
                    <xsl:value-of select="."/>
                </xsl:element>
                <xsl:apply-templates select="@*" mode="s1_flatten"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <!-- All attributes except first and second level.  --> 
    <xsl:template match="@*" mode="s1_flatten">
        <xsl:variable name="name"
            select="if(count(ancestor::*) > 2) then for $a in ancestor::*[count(ancestor::*) > 1] return for $b in $a return local-name($b) else ''"/>
        <xsl:element
            name="{concat(string-join($name, '_'), if(count(ancestor::*) > 2)then '.' else '',  local-name(.))}">
            <xsl:value-of select="."/>
        </xsl:element>
    </xsl:template>
    
    <!-- Attributes of item (/*/*) need an extra ancestor level to get element name right. -->
    <xsl:template match="/*/*/@*" mode="s1_flatten">
        <xsl:variable name="name"
            select="if(count(ancestor::*) > 1) then for $a in ancestor::*[count(ancestor::*) > 0] return for $b in $a return local-name($b) else ''"/>
        <xsl:element
            name="{concat(string-join($name, '_'), if(count(ancestor::*) > 1)then '.' else '',  local-name(.))}">
            <xsl:value-of select="."/>
        </xsl:element>
    </xsl:template>
    
    
    
    
    
    
    
    
    
    
    
    
    <!-- STEP-2: s2_user-defined -->
    <xsl:template match="r10Ex:RetailTransaction/@*|r10Ex:RetailTransaction/node()" mode="s2_user-defined">
        <xsl:for-each select=".">
            <xsl:apply-templates select="r10Ex:Sale" mode="s3_flatten"/>
        </xsl:for-each>
    </xsl:template>
    








    <!-- STEP-3: s3_flatten-->
    <xsl:template match="/*" mode="s3_flatten">
        <xsl:copy>
            <!-- Attributes at top-level is ignored. -->
            <xsl:apply-templates select="node()" mode="s3_flatten"/>
        </xsl:copy>
    </xsl:template>
    
    <!-- Elements at item level.  -->
    <xsl:template match="/*/*" mode="s3_flatten">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()" mode="s3_flatten"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="*" mode="s3_flatten">
        <xsl:choose>
            <xsl:when test="*">
                <xsl:apply-templates select="@*|node()" mode="s3_flatten"/>
            </xsl:when>
            <xsl:when test="@ID[1]|@TypeCode[1]">
                <!-- Added Step: Customised flatten Attribute Name with @ID or @TypeCode and Value as an Element -->
                <xsl:variable name="name"
                    select="if(count(ancestor::*) > 2) then for $a in ancestor::*[count(ancestor::*) > 1] return for $b in $a return local-name($b) else ''"/>
                <xsl:variable name="id" select="(@*[1])"/>
                <xsl:element
                    name="{concat(string-join($name, '_'), if(count(ancestor::*) > 2)then '_' else '', local-name(.), '.', $id)}">
                    <xsl:value-of select="."/>
                </xsl:element>
                <xsl:apply-templates select="@*" mode="s3_flatten"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:variable name="name"
                    select="if(count(ancestor::*) > 2) then for $a in ancestor::*[count(ancestor::*) > 1] return for $b in $a return local-name($b) else ''"/>
                <xsl:element
                    name="{concat(string-join($name, '_'), if(count(ancestor::*) > 2)then '_' else '',  local-name(.))}">
                    <xsl:value-of select="."/>
                </xsl:element>
                <xsl:apply-templates select="@*" mode="s3_flatten"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <!-- All attributes except first and second level.  --> 
    <xsl:template match="@*" mode="s3_flatten">
        <xsl:variable name="name"
            select="if(count(ancestor::*) > 2) then for $a in ancestor::*[count(ancestor::*) > 1] return for $b in $a return local-name($b) else ''"/>
        <xsl:element
            name="{concat(string-join($name, '_'), if(count(ancestor::*) > 2)then '.' else '',  local-name(.))}">
            <xsl:value-of select="."/>
        </xsl:element>
    </xsl:template>
    
    <!-- Attributes of item (/*/*) need an extra ancestor level to get element name right. -->
    <xsl:template match="/*/*/@*" mode="s3_flatten">
        <xsl:variable name="name"
            select="if(count(ancestor::*) > 1) then for $a in ancestor::*[count(ancestor::*) > 0] return for $b in $a return local-name($b) else ''"/>
        <xsl:element
            name="{concat(string-join($name, '_'), if(count(ancestor::*) > 1)then '.' else '',  local-name(.))}">
            <xsl:value-of select="."/>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="comment()|processing-instruction()" mode="s3_flatten">
        <xsl:copy/>          
    </xsl:template>
    
    
     
    
</xsl:stylesheet>
