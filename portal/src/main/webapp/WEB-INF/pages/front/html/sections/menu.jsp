<!--
Copyright (C) 2012 INTECO, Instituto Nacional de Tecnologías de la Comunicación, 
This program is licensed and may be used, modified and redistributed under the terms
of the European Public License (EUPL), either version 1.2 or (at your option) any later 
version as soon as they are approved by the European Commission.
Unless required by applicable law or agreed to in writing, software distributed under the 
License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
ANY KIND, either express or implied. See the License for the specific language governing 
permissions and more details.
You should have received a copy of the EUPL1.2 license along with this program; if not, 
you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
Modificaciones: MINHAFP (Ministerio de Hacienda y Función Pública) 
Email: observ.accesibilidad@correo.gob.es
-->
<%@ include file="/common/taglibs.jsp"%>
<%@page import="es.inteco.common.Constants"%>
<html:xhtml />
<bean:define id="rolAdmin">
	<inteco:properties key="role.administrator.id" file="crawler.properties" />
</bean:define>
<bean:define id="rolConfig">
	<inteco:properties key="role.configurator.id" file="crawler.properties" />
</bean:define>
<bean:define id="rolVisor">
	<inteco:properties key="role.visualizer.id" file="crawler.properties" />
</bean:define>
<bean:define id="rolCustResp">
	<inteco:properties key="role.customer.responsible.id" file="crawler.properties" />
</bean:define>
<bean:define id="rolCustUser">
	<inteco:properties key="role.customer.user.id" file="crawler.properties" />
</bean:define>
<bean:define id="rolObservatory">
	<inteco:properties key="role.observatory.id" file="crawler.properties" />
</bean:define>
<ul role="presentation" class="nav nav-pills nav-stacked">
	<inteco:menu roles="<%=rolAdmin + \";\" + rolConfig + \";\" + rolObservatory%>">
		<logic:equal name="<%=Constants.MENU%>" value="<%=Constants.MENU_INTECO_OBS%>">
			<li class="active">
				<a>
					<bean:message key="menuadmin.observatorio" />
				</a>
			</li>
		</logic:equal>
		<logic:notEqual name="<%=Constants.MENU%>" value="<%=Constants.MENU_INTECO_OBS%>">
			<li>
				<html:link forward="observatoryMenu" titleKey="menuadmin.observatorio.title">
					<bean:message key="menuadmin.observatorio" />
				</html:link>
			</li>
		</logic:notEqual>
		<li>
			<ul role="presentation" class="nav nav-pills nav-stacked">
				<inteco:menu roles="<%=rolAdmin + \";\" + rolConfig%>">
					<logic:equal name="<%=Constants.SUBMENU%>" value="<%=Constants.SUBMENU_OBSERVATORIO%>">
						<li class="active">
							<a>
								<bean:message key="menuadmin.observatorio" />
							</a>
						</li>
					</logic:equal>
					<logic:notEqual name="<%=Constants.SUBMENU%>" value="<%=Constants.SUBMENU_OBSERVATORIO%>">
						<li>
							<html:link forward="observatoryMenu" titleKey="menuadmin.observatorio.title">
								<bean:message key="menuadmin.observatorio" />
							</html:link>
						</li>
					</logic:notEqual>
					<logic:equal name="<%=Constants.SUBMENU%>" value="<%=Constants.SUBMENU_CATEGORIES%>">
						<li class="active">
							<html:link forward="getSeedCategories" styleId="subselected"
								titleKey="menuconfig.semillas.listado.categorias.title">
								<bean:message key="menuconfig.semillas.listado.categorias" />
							</html:link>
					</logic:equal>
					<logic:notEqual name="<%=Constants.SUBMENU%>" value="<%=Constants.SUBMENU_CATEGORIES%>">
						<li>
							<html:link forward="getSeedCategories" titleKey="menuconfig.semillas.listado.categorias.title">
								<bean:message key="menuconfig.semillas.listado.categorias" />
							</html:link>
					</logic:notEqual>
		</li>
		<logic:equal name="<%=Constants.SUBMENU%>" value="<%=Constants.SUBMENU_OBS_SEMILLA%>">
			<li class="active">
				<html:link styleId="subselected" forward="observatorySeed" titleKey="menu.config.observatory.seed.title">
					<bean:message key="menu.config.observatory.seed" />
				</html:link>
		</logic:equal>
		<logic:notEqual name="<%=Constants.SUBMENU%>" value="<%=Constants.SUBMENU_OBS_SEMILLA%>">
			<li>
				<html:link forward="observatorySeed" titleKey="menu.config.observatory.seed.title">
					<bean:message key="menu.config.observatory.seed" />
				</html:link>
		</logic:notEqual>
		</li>
		<logic:equal name="<%=Constants.SUBMENU%>" value="<%=Constants.SUBMENU_OBS_DEPENDENCIAS%>">
			<li class="active">
				<html:link styleId="subselected" forward="observatoryDependencias" titleKey="menu.config.dependencias.title">
					<bean:message key="menu.config.dependencias" />
				</html:link>
		</logic:equal>
		<logic:notEqual name="<%=Constants.SUBMENU%>" value="<%=Constants.SUBMENU_OBS_DEPENDENCIAS%>">
			<li>
				<html:link forward="observatoryDependencias" titleKey="menu.config.dependencias.title">
					<bean:message key="menu.config.dependencias" />
				</html:link>
		</logic:notEqual>
		</li>
		<logic:equal name="<%=Constants.SUBMENU%>" value="<%=Constants.SUBMENU_OBS_COMPLEJIDADES%>">
			<li class="active">
				<html:link styleId="subselected" forward="observatoryComplejidades" titleKey="menu.config.complejidades.title">
					<bean:message key="menu.config.complejidades" />
				</html:link>
		</logic:equal>
		<logic:notEqual name="<%=Constants.SUBMENU%>" value="<%=Constants.SUBMENU_OBS_COMPLEJIDADES%>">
			<li>
				<html:link forward="observatoryComplejidades" titleKey="menu.config.complejidades.title">
					<bean:message key="menu.config.complejidades" />
				</html:link>
		</logic:notEqual>
		</li>
		<logic:equal name="<%=Constants.SUBMENU%>" value="<%=Constants.SUBMENU_OBS_ETIQUETAS%>">
			<li class="active">
				<html:link styleId="subselected" forward="observatoryEtiquetas" titleKey="menu.config.etiquetas.title">
					<bean:message key="menu.config.etiquetas" />
				</html:link>
		</logic:equal>
		<logic:notEqual name="<%=Constants.SUBMENU%>" value="<%=Constants.SUBMENU_OBS_ETIQUETAS%>">
			<li>
				<html:link forward="observatoryEtiquetas" titleKey="menu.config.etiquetas.title">
					<bean:message key="menu.config.etiquetas" />
				</html:link>
		</logic:notEqual>
		</li>
		<logic:equal name="<%=Constants.SUBMENU%>" value="<%=Constants.SUBMENU_OBS_PLANTILLAS%>">
			<li class="active">
				<html:link styleId="subselected" forward="observatoryPlantillas" titleKey="menu.config.plantillas.title">
					<bean:message key="menu.config.plantillas" />
				</html:link>
		</logic:equal>
		<logic:notEqual name="<%=Constants.SUBMENU%>" value="<%=Constants.SUBMENU_OBS_PLANTILLAS%>">
			<li>
				<html:link forward="observatoryPlantillas" titleKey="menu.config.etiquetas.title">
					<bean:message key="menu.config.plantillas" />
				</html:link>
		</logic:notEqual>
		<logic:equal name="<%=Constants.SUBMENU%>" value="<%=Constants.SUBMENU_OBS_RANGES%>">
			<li class="active">
				<html:link styleId="subselected" forward="observatoryRanges" titleKey="menu.config.ranges.title">
					<bean:message key="menu.config.ranges.title" />
				</html:link>
		</logic:equal>
		<logic:notEqual name="<%=Constants.SUBMENU%>" value="<%=Constants.SUBMENU_OBS_RANGES%>">
			<li>
				<html:link forward="observatoryRanges" titleKey="menu.config.ranges.title">
					<bean:message key="menu.config.ranges.title" />
				</html:link>
		</logic:notEqual>
		</li>
		</li>
	</inteco:menu>
</ul>
</li>
</inteco:menu>
<inteco:menu roles="<%=rolAdmin%>">
	<logic:equal name="<%=Constants.MENU%>" value="<%=Constants.MENU_SERVICIO_DIAGNOSTICO%>">
		<li class="active">
	</logic:equal>
	<logic:notEqual name="<%=Constants.MENU%>" value="<%=Constants.MENU_SERVICIO_DIAGNOSTICO%>">
		<li>
	</logic:notEqual>
	<a>
		<bean:message key="menuadmin.servicio_diagnostico" />
	</a>
	</li>
	<li> 
		<ul role="presentation" class="nav nav-pills nav-stacked">
			<li>
				<a href="/oaw/diagnostico.html">
					<bean:message key="menuadmin.servicio_diagnostico" />
				</a>
			</li>
			<logic:equal name="<%=Constants.MENU%>" value="<%=Constants.MENU_SERVICIO_DIAGNOSTICO%>">
				<li class="active">
					<html:link forward="servicioDiagnostico" titleKey="menuadmin.exportar_servicio_diagnostico.title">
						<bean:message key="menuadmin.exportar_servicio_diagnostico" />
					</html:link>
			</logic:equal>
			<logic:notEqual name="<%=Constants.MENU%>" value="<%=Constants.MENU_SERVICIO_DIAGNOSTICO%>">
				<li>
					<html:link forward="servicioDiagnostico" titleKey="menuadmin.exportar_servicio_diagnostico.title">
						<bean:message key="menuadmin.exportar_servicio_diagnostico" />
					</html:link>
			</logic:notEqual>
		</ul>
	</li>
</inteco:menu>
<logic:equal name="<%=Constants.MENU%>" value="<%=Constants.MENU_OTHER_OPTIONS%>">
	<li role="presentation" class="dropdown active">
</logic:equal>
<logic:notEqual name="<%=Constants.MENU%>" value="<%=Constants.MENU_OTHER_OPTIONS%>">
	<li role="presentation" class="dropdown">
</logic:notEqual>
<a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">
	<bean:message key="other.options" />
	<span style="margin-top: 8px;" class="caret pull-right"></span>
</a>
<ul class="dropdown-menu nav nav-stacked nav-pills">
	<li>
		<logic:equal name="<%=Constants.MENU%>" value="<%=Constants.MENU_PASSWORD%>">
			<html:link forward="changePassMenu" styleId="selected" titleKey="menuvisor.cambiar.pass.title">
				<bean:message key="menuvisor.cambiar.pass" />
			</html:link>
		</logic:equal>
		<logic:notEqual name="<%=Constants.MENU%>" value="<%=Constants.MENU_PASSWORD%>">
			<html:link forward="changePassMenu" titleKey="menuvisor.cambiar.pass.title">
				<bean:message key="menuvisor.cambiar.pass" />
			</html:link>
		</logic:notEqual>
	</li>
	<inteco:menu roles="<%=rolAdmin%>">
		<li>
			<logic:equal name="<%=Constants.MENU%>" value="<%=Constants.MENU_USERS%>">
				<html:link forward="usersMenu" styleId="selected" titleKey="menuadmin.usuarios.title">
					<bean:message key="menuadmin.usuarios" />
				</html:link>
			</logic:equal>
			<logic:notEqual name="<%=Constants.MENU%>" value="<%=Constants.MENU_USERS%>">
				<html:link forward="usersMenu" titleKey="menuadmin.usuarios.title">
					<bean:message key="menuadmin.usuarios" />
				</html:link>
			</logic:notEqual>
		</li>
	</inteco:menu>
	<inteco:menu roles="<%=rolAdmin + \";\" + rolConfig%>">
		<li>
			<html:link forward="clientAccountsMenu" styleId="selected" titleKey="menuadmin.cuenta.usuarios.title">
				<bean:message key="menuadmin.cuenta.usuarios" />
			</html:link>
		</li>
	</inteco:menu>
	<inteco:menu roles="<%=rolAdmin + \";\" + rolConfig + \";\" + rolVisor%>">
		<li>
			<html:link styleId="selected" forward="crawlingsMenu" titleKey="menuadmin.rastreos.title">
				<bean:message key="menuadmin.rastreos" />
			</html:link>
		</li>
	</inteco:menu>		
	<inteco:menu roles="<%=rolAdmin + \";\" + rolConfig%>">
		<li>
			<a>
			 	<bean:message key="menuadmin.backup.entidades" />
			</a>
			<ul class="nav nav-stacked nav-pills">
				<li>
					<html:link forward="importarEntidadesMenu" styleId="subselected" titleKey="menuadmin.importar.title">
						<bean:message key="menuadmin.importar.entidades" /> 
					</html:link>
				</li>
				<li>
					<html:link forward="exportarEntidadesMenu" styleId="subselected" titleKey="menuadmin.exportar.title">
						<bean:message key="menuadmin.exportar.entidades" /> 
					</html:link>
				</li>
			</ul>
		</li>
	</inteco:menu>
	<inteco:menu roles="<%=rolAdmin + \";\" + rolConfig%>">
		<li>
			<html:link forward="listSeedsMenu" styleId="subselected" titleKey="menuadmin.semillas.listado.semillas.title">
				<bean:message key="menuadmin.semillas" />
			</html:link>
			<ul class="nav nav-stacked nav-pills">
				<li>
					<html:link forward="listSeedsMenu" styleId="subselected" titleKey="menuadmin.semillas.listado.semillas.title">
						<bean:message key="menuadmin.semillas.listado.semillas" />
					</html:link>
				</li>
				<li>
					<html:link forward="ipSeedsMenu" styleId="subselected" titleKey="menuadmin.semillas.rangoIP.title">
						<bean:message key="menuadmin.semillas.rangoIP" />
					</html:link>
				</li>
				<li>
					<html:link forward="googleSeedsMenu" styleId="subselected" titleKey="menuadmin.semillas.resultados.google.title">
						<bean:message key="menuadmin.semillas.resultados.google" />
					</html:link>
				</li>
				<li>
					<html:link forward="conectividadMenu" styleId="subselected" titleKey="menuadmin.conectividad.title">
						<bean:message key="menuadmin.conectividad" />
					</html:link>
				</li>
			</ul>
		</li>
	</inteco:menu>
</ul>
</li>
</ul>