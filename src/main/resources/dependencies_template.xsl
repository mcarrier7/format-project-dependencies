<?xml version="1.0"?>
<xslt:stylesheet xmlns:date="http://exslt.org/dates-and-times"
	xmlns:str="http://exslt.org/strings" xmlns:xslt="http://www.w3.org/1999/XSL/Transform"
	xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xf="http://www.ecrion.com/xf/1.0"
	xmlns:xc="http://www.ecrion.com/xc" xmlns:xfd="http://www.ecrion.com/xfd/1.0"
	xmlns:svg="http://www.w3.org/2000/svg" xmlns:msxsl="urn:schemas-microsoft-com:xslt"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
	extension-element-prefixes="date str">
	<xslt:output indent="yes" />
	
	<xsl:variable name="font-family">ColaborateThinRegular,Arial,Helvetica,sans-serif</xsl:variable>
	<xsl:variable name="document-header-font-size">9pt</xsl:variable>
	<xsl:variable name="document-body-font-size">8pt</xsl:variable>
	<xsl:variable name="document-summary-font-size">8pt</xsl:variable>
	<xsl:variable name="headers-font-size">12pt</xsl:variable>
	<xsl:variable name="table-row-padding-top">1mm</xsl:variable>
	
	<xsl:template match="/">
		<fo:root>
			<fo:layout-master-set>
				<fo:simple-page-master master-name="first" page-height="11.0in" page-width="8.5in" 
					margin-top="0.7cm" margin-bottom="0.7cm" margin-left="1.0cm" margin-right="1.0cm">
					<fo:region-body padding="0pt" region-name="xsl-region-body" margin-top="1.25cm" margin-bottom="1.5cm" />
					<fo:region-before padding="0pt" region-name="header-first" display-align="after" margin-top="2.0cm" extent="1.075cm" />
					<fo:region-after padding="0pt" region-name="footer" display-align="before" margin-top="2.0cm" extent="1.075cm"/>
				</fo:simple-page-master>
				<fo:simple-page-master master-name="rest" page-height="11.0in" page-width="8.5in" 
					margin-top="0.7cm" margin-bottom="0.7cm" margin-left="1.0cm" margin-right="1.0cm">
					<fo:region-body padding="0pt" region-name="xsl-region-body" margin-top="1.25cm" margin-bottom="1.5cm" />
					<fo:region-before padding="0pt" region-name="header-rest" display-align="after" margin-top="1.0cm" extent="1.075cm" />
					<fo:region-after padding="0pt" region-name="footer" display-align="before" margin-top="2.0cm" extent="1.075cm"/>
				</fo:simple-page-master>
				<fo:page-sequence-master master-name="document">
					<fo:repeatable-page-master-alternatives>
						<fo:conditional-page-master-reference page-position="first" master-reference="first" />
						<fo:conditional-page-master-reference page-position="rest" master-reference="rest" />
					</fo:repeatable-page-master-alternatives>
				</fo:page-sequence-master>
			</fo:layout-master-set>
			
			<fo:page-sequence master-reference="document">
				<!-- First Page Header -->
				<fo:static-content flow-name="header-first" font-size="10pt" font-family="{$font-family}" text-align="center">
					<fo:block/>
				</fo:static-content>
				<!-- Rest of Pages Header -->
				<fo:static-content flow-name="header-rest" font-size="10pt" font-family="{$font-family}" text-align="center">
					<fo:block/>
				</fo:static-content>
				
				<!-- Page Footer -->
				<fo:static-content flow-name="footer" font-size="10pt" font-family="{$font-family}" text-align="center">
					<fo:block text-align="right" font-size="8pt" margin-bottom="1mm">
						Page: <fo:page-number/> of <fo:page-number-citation ref-id="last-page"/>
					</fo:block>
				</fo:static-content>
				
				<fo:flow flow-name="xsl-region-body">
					<xsl:if test="dependencies/internalDependencies/e">
						<fo:block linefeed-treatment="preserve">
							<!-- Internal Dependencies title value -->
							<fo:block font-size="14px" padding-top="3mm" padding-bottom="2mm" color="rgb(195, 195, 195)" font-family="{$font-family}">
								<fo:inline>Internal Dependencies</fo:inline>
							</fo:block>
						</fo:block>
					
						<fo:block linefeed-treatment="preserve" padding-top="5mm" padding-left="0mm" padding-bottom="0mm" width="100%">
							<fo:table table-layout="fixed" width="100%" font-size="{$document-header-font-size}" font-family="{$font-family}" padding-top="1mm" border-collapse="collapse">
								<fo:table-column column-number="1" column-width="proportional-column-width(1)"/>
								<fo:table-column column-number="2" column-width="proportional-column-width(1)"/>
								<fo:table-column column-number="3" column-width="proportional-column-width(1)"/>
								<fo:table-column column-number="4" column-width="proportional-column-width(1)"/>
								
								<fo:table-header>
									<fo:table-row>
										<fo:table-cell text-align="center" border-left="1pt solid" border-right="1pt solid" border-top="1pt solid" border-bottom="1pt solid">
											<fo:block font-weight="bold"><xsl:text>GroupId</xsl:text></fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="center" border-left="1pt solid" border-right="1pt solid" border-top="1pt solid" border-bottom="1pt solid">
											<fo:block font-weight="bold"><xsl:text>ArtifactId</xsl:text></fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="center" border-left="1pt solid" border-right="1pt solid" border-top="1pt solid" border-bottom="1pt solid">
											<fo:block font-weight="bold"><xsl:text>Version</xsl:text></fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="center" border-left="1pt solid" border-right="1pt solid" border-top="1pt solid" border-bottom="1pt solid">
											<fo:block font-weight="bold"><xsl:text>Scope</xsl:text></fo:block>
										</fo:table-cell>
									</fo:table-row>
								</fo:table-header>
								
								<fo:table-body>
									<xsl:choose>
										<xsl:when test="dependencies/internalDependencies/e">
											<xsl:for-each select="dependencies/internalDependencies/e">
												<fo:table-row>
													<fo:table-cell text-align="center" border-left="1pt solid" border-right="1pt solid" border-top="1pt solid" border-bottom="1pt solid">
														<fo:block><xsl:value-of select="groupId"/></fo:block>
													</fo:table-cell>
													<fo:table-cell text-align="center" border-left="1pt solid" border-right="1pt solid" border-top="1pt solid" border-bottom="1pt solid">
														<fo:block><xsl:value-of select="artifactId"/></fo:block>
													</fo:table-cell>
													<fo:table-cell text-align="center" border-left="1pt solid" border-right="1pt solid" border-top="1pt solid" border-bottom="1pt solid">
														<fo:block><xsl:value-of select="version"/></fo:block>
													</fo:table-cell>
													<fo:table-cell text-align="center" border-left="1pt solid" border-right="1pt solid" border-top="1pt solid" border-bottom="1pt solid">
														<fo:block><xsl:value-of select="scope"/></fo:block>
													</fo:table-cell>
												</fo:table-row>
											</xsl:for-each>
										</xsl:when>
										<xsl:otherwise>
											<fo:table-row>
												<fo:table-cell text-align="center" border-left="1pt solid" border-right="1pt solid" border-top="1pt solid" border-bottom="1pt solid"><fo:block/></fo:table-cell>
												<fo:table-cell text-align="center" border-left="1pt solid" border-right="1pt solid" border-top="1pt solid" border-bottom="1pt solid"><fo:block/></fo:table-cell>
												<fo:table-cell text-align="center" border-left="1pt solid" border-right="1pt solid" border-top="1pt solid" border-bottom="1pt solid"><fo:block/></fo:table-cell>
												<fo:table-cell text-align="center" border-left="1pt solid" border-right="1pt solid" border-top="1pt solid" border-bottom="1pt solid"><fo:block/></fo:table-cell>
											</fo:table-row>
										</xsl:otherwise>
									</xsl:choose>
								</fo:table-body>
							</fo:table>
						</fo:block>
					</xsl:if>
					
					<xsl:if test="dependencies/externalDependencies/e">
						<fo:block linefeed-treatment="preserve" padding-top="5mm" padding-bottom="0mm" width="100%">
							<!-- External Dependencies title value -->
							<fo:block font-size="14px" padding-top="3mm" padding-bottom="2mm" color="rgb(195, 195, 195)" font-family="{$font-family}">
								<fo:inline>External Dependencies</fo:inline>
							</fo:block>
						</fo:block>
					
						<fo:block linefeed-treatment="preserve" padding-top="5mm" padding-left="0mm" padding-bottom="0mm" width="100%">
							<fo:table table-layout="fixed" width="100%" font-size="{$document-header-font-size}" font-family="{$font-family}" padding-top="1mm" border-collapse="collapse">
								<fo:table-column column-number="1" column-width="proportional-column-width(1)"/>
								<fo:table-column column-number="2" column-width="proportional-column-width(1)"/>
								<fo:table-column column-number="3" column-width="proportional-column-width(1)"/>
								<fo:table-column column-number="4" column-width="proportional-column-width(1)"/>
								
								<fo:table-header>
									<fo:table-row>
										<fo:table-cell text-align="center" border-left="1pt solid" border-right="1pt solid" border-top="1pt solid" border-bottom="1pt solid">
											<fo:block font-weight="bold"><xsl:text>GroupId</xsl:text></fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="center" border-left="1pt solid" border-right="1pt solid" border-top="1pt solid" border-bottom="1pt solid">
											<fo:block font-weight="bold"><xsl:text>ArtifactId</xsl:text></fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="center" border-left="1pt solid" border-right="1pt solid" border-top="1pt solid" border-bottom="1pt solid">
											<fo:block font-weight="bold"><xsl:text>Version</xsl:text></fo:block>
										</fo:table-cell>
										<fo:table-cell text-align="center" border-left="1pt solid" border-right="1pt solid" border-top="1pt solid" border-bottom="1pt solid">
											<fo:block font-weight="bold"><xsl:text>Scope</xsl:text></fo:block>
										</fo:table-cell>
									</fo:table-row>
								</fo:table-header>
								
								<fo:table-body>
									<xsl:choose>
										<xsl:when test="dependencies/externalDependencies/e">
											<xsl:for-each select="dependencies/externalDependencies/e">
												<fo:table-row>
													<fo:table-cell text-align="center" border-left="1pt solid" border-right="1pt solid" border-top="1pt solid" border-bottom="1pt solid">
														<fo:block><xsl:value-of select="groupId"/></fo:block>
													</fo:table-cell>
													<fo:table-cell text-align="center" border-left="1pt solid" border-right="1pt solid" border-top="1pt solid" border-bottom="1pt solid">
														<fo:block><xsl:value-of select="artifactId"/></fo:block>
													</fo:table-cell>
													<fo:table-cell text-align="center" border-left="1pt solid" border-right="1pt solid" border-top="1pt solid" border-bottom="1pt solid">
														<fo:block><xsl:value-of select="version"/></fo:block>
													</fo:table-cell>
													<fo:table-cell text-align="center" border-left="1pt solid" border-right="1pt solid" border-top="1pt solid" border-bottom="1pt solid">
														<fo:block><xsl:value-of select="scope"/></fo:block>
													</fo:table-cell>
												</fo:table-row>
											</xsl:for-each>
										</xsl:when>
										<xsl:otherwise>
											<fo:table-row>
												<fo:table-cell text-align="center" border-left="1pt solid" border-right="1pt solid" border-top="1pt solid" border-bottom="1pt solid"><fo:block/></fo:table-cell>
												<fo:table-cell text-align="center" border-left="1pt solid" border-right="1pt solid" border-top="1pt solid" border-bottom="1pt solid"><fo:block/></fo:table-cell>
												<fo:table-cell text-align="center" border-left="1pt solid" border-right="1pt solid" border-top="1pt solid" border-bottom="1pt solid"><fo:block/></fo:table-cell>
												<!-- <fo:table-cell text-align="center" border-left="1pt solid" border-right="1pt solid" border-top="1pt solid" border-bottom="1pt solid"><fo:block/></fo:table-cell> -->
											</fo:table-row>
										</xsl:otherwise>
									</xsl:choose>
								</fo:table-body>
							</fo:table>
						</fo:block>
					</xsl:if>
					<fo:block id="last-page"/>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
</xslt:stylesheet>