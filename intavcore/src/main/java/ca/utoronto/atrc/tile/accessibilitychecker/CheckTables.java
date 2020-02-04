/*******************************************************************************
* Copyright (C) 2012 INTECO, Instituto Nacional de Tecnologías de la Comunicación, 
* This program is licensed and may be used, modified and redistributed under the terms
* of the European Public License (EUPL), either version 1.2 or (at your option) any later 
* version as soon as they are approved by the European Commission.
* Unless required by applicable law or agreed to in writing, software distributed under the 
* License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
* ANY KIND, either express or implied. See the License for the specific language governing 
* permissions and more details.
* You should have received a copy of the EUPL1.2 license along with this program; if not, 
* you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
* Modificaciones: MINHAFP (Ministerio de Hacienda y Función Pública) 
* Email: observ.accesibilidad@correo.gob.es
******************************************************************************/
package ca.utoronto.atrc.tile.accessibilitychecker;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.inteco.common.logging.Logger;
import es.inteco.common.utils.StringUtils;

/**
 * The Class CheckTables.
 */
public final class CheckTables {
	/**
	 * Instantiates a new check tables.
	 */
	private CheckTables() {
	}

	/**
	 * Function row count.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	protected static boolean functionRowCount(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		int number;
		int rows;
		try {
			number = Integer.parseInt(checkCode.getFunctionNumber());
			rows = (Integer) elementGiven.getUserData("rows");
		} catch (Exception e) { // number could not be parsed
			return false;
		}
		final String position = checkCode.getFunctionPosition().isEmpty() ? "not-equals" : checkCode.getFunctionPosition();
		if ("equals".equalsIgnoreCase(position)) {
			return rows == number;
		} else if ("greater".equalsIgnoreCase(position)) {
			return rows > number;
		} else if ("lesser".equalsIgnoreCase(position)) {
			return rows < number;
		} else {
			// Incluye tanto not-equals como cualquier otro valor
			return rows != number;
		}
	}

	/**
	 * Function column count.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	protected static boolean functionColumnCount(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		int number;
		int cols;
		try {
			number = Integer.parseInt(checkCode.getFunctionNumber());
			cols = (Integer) elementGiven.getUserData("cols");
		} catch (Exception e) { // number could not be parsed
			return false;
		}
		final String position = checkCode.getFunctionPosition().isEmpty() ? "not-equals" : checkCode.getFunctionPosition();
		if ("equals".equalsIgnoreCase(position)) {
			return cols == number;
		} else if ("greater".equalsIgnoreCase(position)) {
			return cols > number;
		} else if ("lesser".equalsIgnoreCase(position)) {
			return cols < number;
		} else {
			// Incluye tanto not-equals como cualquier otro valor
			return cols != number;
		}
	}

	// Checks if the given table is the given type.
	/**
	 * Function table type.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	// Returns true if the given table is the given type.
	protected static boolean functionTableType(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		String tableShouldBe = checkCode.getFunctionValue();
		tableShouldBe = tableShouldBe.toLowerCase();
		if (!tableShouldBe.equalsIgnoreCase("data") && !tableShouldBe.equalsIgnoreCase("layout")) {
			Logger.putLog("Warning: invalid table type in functionTableType: " + tableShouldBe, Check.class, Logger.LOG_LEVEL_WARNING);
			return false;
		}
		if (isDataTable(elementGiven)) {
			return tableShouldBe.equalsIgnoreCase("data");
		}
		return tableShouldBe.equalsIgnoreCase("layout");
	}

	// Makes a guess about the table type.
	/**
	 * Checks if is data table.
	 *
	 * @param elementGiven the element given
	 * @return true, if is data table
	 */
	// Returns true if this should be a data table, false if layout table.
	protected static boolean isDataTable(Element elementGiven) {
		// check for TH elements
		NodeList listTh = elementGiven.getElementsByTagName("th");
		if (listTh.getLength() > 0) {
			return true;
		}
		// no TH elements
		// check for caption element
		NodeList listCaption = elementGiven.getElementsByTagName("caption");
		return listCaption.getLength() > 0;
	}

	/**
	 * Comprueba si una tabla es compleja. Se considera como ese tipo de tabla aquellas que tienen encabezados de filas O columnas multinivel (puede presentar encabezados en solo uno de los ejes)
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	protected static boolean functionTableComplex(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		// Creamos la matriz que representa la tabla
		NodeList trList = elementGiven.getElementsByTagName("tr");
		int x = 0;
		if (trList.item(0) != null && trList.item(0).getChildNodes() != null) {
			x = countLength(trList.item(0).getChildNodes());
		}
		int y = trList.getLength();
		TableNode[][] table = createTable(trList, x, y);
		// Comprobamos si tiene mas de una fila de encabezados, entonces también será
		// compleja
		boolean complex = true;
		int cont = 0;
		for (int i = 0; i < y && complex; i++) {
			if ((i == 0 && !functionHeaderWithOnlyCell(checkCode, nodeNode, elementGiven)) || (i > 0)) {
				if (isCorrectHorizontalHeading(table, i, x, false)) {
					cont++;
					if (cont > 1) {
						return true;
					}
				} else {
					complex = false;
				}
			}
		}
		// Comprobamos si tiene mas de una columna de encabezados, entonces también será
		// compleja
		complex = true;
		cont = 0;
		for (int i = 0; i < x && complex; i++) {
			if (isCorrectVerticalHeading(table, y, i, false)) {
				cont++;
				if (cont > 1) {
					return true;
				}
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * Comprueba si una tabla tiene encabezados de complejidad superior. Se considera como ese tipo de tabla aquellas que teniendo encabezados de filas Y columnas al menos uno de ellos es además
	 * multinivel
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	protected static boolean functionTableHeadingComplex(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		// Creamos la matriz que representa la tabla
		NodeList trList = elementGiven.getElementsByTagName("tr");
		int x = 0;
		if (trList.item(0) != null && trList.item(0).getChildNodes() != null) {
			x = countLength(trList.item(0).getChildNodes());
		}
		int y = trList.getLength();
		TableNode[][] table = createTable(trList, x, y);
		// Si la tabla es bidireccional, es compleja
		if (!isBidirectionalHeading(table, x, y)) {
			return false;
		}
		// Comprobamos si tiene mas de una fila de encabezados, entonces también será
		// compleja
		boolean complex = true;
		int cont = 0;
		for (int i = 0; i < y && complex; i++) {
			// if ((i == 0 && !functionHeaderWithOnlyCell(checkCode, nodeNode,
			// elementGiven)) || (i > 0)) {
			if (isCorrectHorizontalHeading(table, i, x, false)) {
				cont++;
				if (cont > 1) {
					return true;
				}
			} else {
				complex = false;
			}
			// }
		}
		// Comprobamos si tiene mas de una columna de encabezados, entonces también será
		// compleja
		complex = true;
		cont = 0;
		for (int i = 0; i < x && complex; i++) {
			if (isCorrectVerticalHeading(table, y, i, false)) {
				cont++;
				if (cont > 1) {
					return true;
				}
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * Function table heading blank.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	protected static boolean functionTableHeadingBlank(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		// Creamos la matriz que representa la tabla
		NodeList trList = elementGiven.getElementsByTagName("tr");
		int maxCols = 0;
		if (trList.item(0) != null && trList.item(0).getChildNodes() != null) {
			maxCols = countLength(trList.item(0).getChildNodes());
		}
		int maxRows = trList.getLength();
		final TableNode[][] table = createTable(trList, maxCols, maxRows);
		final boolean isBidirectionalCheck = isBidirectionalHeading(table, maxCols, maxRows);
		final int initialCounter = isBidirectionalCheck ? 1 : 0;
		for (int col = 0; col < maxCols; col++) {
			for (int row = initialCounter; row < maxRows; row++) {
				if (table[row][col].isHeaderCell() && table[row][col].getNode() != null && StringUtils.isEmpty(StringUtils.normalizeWhiteSpaces(table[row][col].getNode().getTextContent()))) {
					return true;
				}
			}
		}
		for (int row = initialCounter; row < maxRows; row++) {
			try {
				if (table[row][0].isHeaderCell() && StringUtils.isEmpty(StringUtils.normalizeWhiteSpaces(table[row][0].getNode().getTextContent()))) {
					return true;
				}
			} catch (Exception e) {
				// Solución provisional, nos hemos encontrado con que hay tablas que dan un
				// OutOfRange porque la primera
				// fila es vacía y el resto no... en este caso suponemos que la tabla no es
				// vertical ya que el primer
				// encabezado vertical no existe.
				return false;
			}
		}
		return false;
	}

	/**
	 * Function missing id headers.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	protected static boolean functionMissingIdHeaders(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final NodeList listRows = elementGiven.getElementsByTagName("tr");
		for (int i = 0; i < listRows.getLength(); i++) {
			final Element elementRow = (Element) listRows.item(i);
			final NodeList listTh = elementRow.getElementsByTagName("th");
			final NodeList listTd = elementRow.getElementsByTagName("td");
			for (int j = 0; j < listTh.getLength(); j++) {
				final Element th = (Element) listTh.item(j);
				final String content = StringUtils.normalizeWhiteSpaces(th.getTextContent());
				final boolean onlyWhiteChars = content.trim().isEmpty();
				if (!onlyWhiteChars && !th.hasAttribute("id")) {
					return true;
				}
			}
			for (int j = 0; j < listTd.getLength(); j++) {
				final Element td = (Element) listTd.item(j);
				final String content = StringUtils.normalizeWhiteSpaces(td.getTextContent());
				final boolean onlyWhiteChars = content.trim().isEmpty();
				if (!onlyWhiteChars && (!td.hasAttribute("headers") || (StringUtils.isEmpty(td.getAttribute("headers"))))) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Consideraremos que una tabla tiene encabezamientos en un sentido sí: 1.- Todas las celdas de la primera fila o columna son encabezados (encabezamiento correcto o solo una celda en esa fila o
	 * columna) 2.- Tiene más de una celda de encabezamiento en esa fila o columna
	 *
	 * @param table the table
	 * @param x     the x
	 * @param y     the y
	 * @return true, if is bidirectional heading
	 */
	protected static boolean isBidirectionalHeading(TableNode[][] table, int x, int y) {
		return isCorrectHorizontalHeading(table, 0, x, true) && isCorrectVerticalHeading(table, y, 0, true) && !isIrregularTable(table, x, y);
	}

	/**
	 * Function correct heading.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	protected static boolean functionCorrectHeading(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		if (elementGiven.getElementsByTagName("th") != null && ((NodeList) elementGiven.getElementsByTagName("th")).getLength() != 0) {
			NodeList trList = elementGiven.getElementsByTagName("tr");
			// Calculamos el número de celdas en la horizontal y en la vertical de la tabla
			// x será el número de elementos que tenga el primer tr de la tabla (primera
			// fila) teniendo en cuanenta los colspan
			// y será el número de tr de la tabla (número de filas)
			if (trList != null) {
				int x = 0;
				if (trList.item(0) != null && trList.item(0).getChildNodes() != null) {
					x = countLength(trList.item(0).getChildNodes());
				}
				int y = trList.getLength();
				TableNode[][] table = createTable(trList, x, y);
				// Comprobamos que no todos los elementos de la tabla sean encabezados, si es
				// asi ERROR
				if (isFullHeaderTable(table, x, y)) {
					return false;
				}
				// Si la tabla tiene datos y encabezados, entonces comprobamos que estos sean
				// correctos
				if (isBidirectionalHeading(table, x, y)) {
					// Miramos si la primera celda es un encabezado correcto
					if (isFirstHeaderCell(table[0][0].getNode())) {
						table[0][0].setHeaderCell(true);
						return isCorrectBidirectionalTable(table, x, y);
					} else {
						return false;
					}
				} else {
					// Cmprobamos si la tabla es horizontal o vertical y si está bien formada
					if (isIrregularTable(table, x, y)) {
						// Si es irregular no evaluamos nada y delegramos esto en otras validaciones
						return true;
					}
					if (isCorrectHorizontalHeading(table, 0, x, false)) {
						return isCorrectHorizontalTable(table, x, y);
					} else if (isCorrectVerticalHeading(table, y, 0, false)) {
						return isCorrectVerticalTable(table, x, y);
					} else {
						// Si no es bidireccional ni horizontal ni vertical... ¿que es? --> ERROR
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Checks if is full header table.
	 *
	 * @param table the table
	 * @param x     the x
	 * @param y     the y
	 * @return true, if is full header table
	 */
	// Comprueba si la tabla tiene algun elemento que no sea un encabezado
	private static boolean isFullHeaderTable(TableNode[][] table, int x, int y) {
		for (int i = 0; i < y; i++) {
			for (int j = 0; j < x; j++) {
				// La celda primera la ignoramos, si el resto son encabezados falla, esta ya se
				// verificará mas adelante
				if ((j != 0) || (i != 0)) {
					// Comprobamos que no sea un encabezado ni un th, porque, en el casod e que
					// fuese un th
					// vacio no sería un encabezado correcto y no estaría marcado como tal.
					if (!table[i][j].isHeaderCell() && !table[i][j].isSpanCell() && !"th".equalsIgnoreCase(table[i][j].getNode().getNodeName())) {
						return false;
					}
				}
			}
		}
		return true;
	}

	// Le pasamos u nodo y nos dice si la primera celda es o no encabezado
	// Consideramos esta y solo esta (la 0,0) encabezado cuando es th(con o sin
	/**
	 * Checks if is first header cell.
	 *
	 * @param node the node
	 * @return true, if is first header cell
	 */
	// texto) o td vacío
	private static boolean isFirstHeaderCell(Node node) {
		final String content = StringUtils.normalizeWhiteSpaces(node.getTextContent());
		final boolean onlyWhiteChars = content.trim().isEmpty();
		return "th".equalsIgnoreCase(node.getNodeName()) || onlyWhiteChars;
	}

	// Recorremos la tabla, si encontramos una celda encabezado, comprobamos que o
	// la fila o la columna o ambas,
	/**
	 * Checks if is correct bidirectional table.
	 *
	 * @param table the table
	 * @param x     the x
	 * @param y     the y
	 * @return true, if is correct bidirectional table
	 */
	// a la que pertenece es de encabezados, en caso contrario ERROR
	private static boolean isCorrectBidirectionalTable(TableNode[][] table, int x, int y) {
		boolean correctHeader;
		for (int i = 1; i < y; i++) {
			for (int j = 1; j < x; j++) {
				if (table[i][j].isHeaderCell()) {
					correctHeader = checkRowColumnFromCell(table, x, y, i, j);
					if (!correctHeader) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Check row column from cell.
	 *
	 * @param table  the table
	 * @param x      the x
	 * @param y      the y
	 * @param file   the file
	 * @param column the column
	 * @return true, if successful
	 */
	/*
	 * Comprobamos si la fila o columna (o ambas) a la que pertenece una celda es una fila o columna de encabezados Como se ha dado por bueno un encabezado si es o un th con texto o un td vacío, y en
	 * el caso de tablas bidireccionales se permite que la primera celda (y solo esta) pueda ser un th vacío, hay que hacer una comprobación más.
	 */
	private static boolean checkRowColumnFromCell(TableNode[][] table, int x, int y, int file, int column) {
		// Comprobamos si la fila es de encabezados si no lo es, entonces para que la
		// tabla sea correcta
		// las columna de la celda ha de ser de encabezados, si es correcta la damos por
		// buena.
		boolean isCorrect = true;
		for (int i = 1; i < x; i++) {
			if (!table[file][i].isHeaderCell()) {
				isCorrect = false;
			}
		}
		if (isCorrect) {
			return true;
		}
		// Comprobamos si la columna es de encabezados, si hemos llegado a este puntoes
		// porque la fila de la celda
		// no es de encabezados, entonces, en cuanto encontremos una celda en la columna
		// que no sea encabezado,
		// ya no tenemos una tabla válida
		for (int i = 1; i < y; i++) {
			if (!table[i][column].isHeaderCell()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if is correct horizontal table.
	 *
	 * @param table the table
	 * @param x     the x
	 * @param y     the y
	 * @return true, if is correct horizontal table
	 */
	// Comprueba si una tabla con encabezados en la horizontal está bien formada
	private static boolean isCorrectHorizontalTable(TableNode[][] table, int x, int y) {
		// Se comprueba que todas las filas sean o encabezados o datos, (no híbridos)
		// Si se encuentra una sola que sea híbrida, la tabla no es correcta
		for (int i = 0; i < y; i++) {
			List<TableNode> nodeList = new ArrayList<>();
			for (int j = 0; j < x; j++) {
				nodeList.add(table[i][j]);
			}
			if (isHybridList(nodeList)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if is correct vertical table.
	 *
	 * @param table the table
	 * @param x     the x
	 * @param y     the y
	 * @return true, if is correct vertical table
	 */
	// Comprueba si una tabla con encabezados en la vertical está bien formada
	private static boolean isCorrectVerticalTable(TableNode[][] table, int x, int y) {
		// Se comprueba que todas las columnas sean o encabezados o datos, (no híbridos)
		// Si se encuentra una sola que sea híbrida, la tabla no es correcta
		for (int j = 0; j < x; j++) {
			List<TableNode> nodeList = new ArrayList<>();
			for (int i = 0; i < y; i++) {
				nodeList.add(table[i][j]);
			}
			if (isHybridList(nodeList)) {
				return false;
			}
		}
		return true;
	}

	// Crea una matriz representativa de la tabla nxn (si nos entcontramos rowSpan o
	// colSpan incluimos en las celdas
	/**
	 * Creates the table.
	 *
	 * @param trList the tr list
	 * @param x      the x
	 * @param y      the y
	 * @return the table node[][]
	 */
	// correspondientes un elemento tableNode con la propiedad isSpanCell = true)
	private static TableNode[][] createTable(NodeList trList, int x, int y) {
		TableNode[][] table = new TableNode[y][x];
		// Inicializamos la matriz
		inizializeTable(table, y, x);
		// Recorremos las filas de tabla
		for (int i = 0; i < y; i++) {
			NodeList rowList = trList.item(i).getChildNodes();
			List<Node> nodeElementList = createNodeElementList(rowList);
			int j = 0;
			int element = 0;
			// Vamos recorriendo los elementos de cada fila e incluyendo en la tabla los
			// elementos
			while (j < x && element < nodeElementList.size()) {
				boolean isSpanCell = true;
				// Buscamos la primera celda que no sea de span
				while (isSpanCell && j < x) {
					if (table[i][j].isSpanCell()) {
						j++;
					} else {
						isSpanCell = false;
					}
				}
				// Si hemos encontrado una celda vacía en la fila incluimos el elemento y si el
				// elemento tiene
				// el atributo rowSpan, se marcan (rowSpan - 1) celdas en la vertical como
				// celdas de Span,
				// lo mismo si tiene colSpan pero las celdas que se marcan son en la horizontal
				if (j < x && element < nodeElementList.size()) {
					TableNode tableNode = new TableNode(nodeElementList.get(element));
					// Si es un encabezado lo marcamos como tal
					if (isHeaderCell(nodeElementList.get(element))) {
						// Es un th con texto, estamos seguros de que es encabezado
						tableNode.setHeaderCell(true);
					}
					table[i][j] = tableNode;
					element++;
					// Las celdas de span se consideran tambien celdas de encabezados
					if (table[i][j].getRowSpan() != 0) {
						for (int z = 1; z < table[i][j].getRowSpan(); z++) {
							TableNode node = new TableNode(true);
							if (isHeaderCell(nodeElementList.get(element - 1))) {
								// Es un th con texto, estamos seguros de que es encabezado
								tableNode.setHeaderCell(true);
								node.setHeaderCell(true);// Marcar como cabeceras las celdas de span si la que no
															// es span lo es
							}
							if (i + z < y) {
								table[i + z][j] = node;
							}
						}
					}
					if (table[i][j].getColSpan() != 0) {
						for (int z = 1; z < table[i][j].getColSpan(); z++) {
							TableNode node = new TableNode(true);
							if (isHeaderCell(nodeElementList.get(element - 1))) {
								// Es un th con texto, estamos seguros de que es encabezado
								tableNode.setHeaderCell(true);
								node.setHeaderCell(true);// Marcar como cabeceras las celdas de span si la que no
															// es span lo es
							}
							if (j + z < x) {
								table[i][j + z] = node;
							}
						}
					}
					j++;
				}
			}
		}
		return table;
	}

	/**
	 * Creates the node element list.
	 *
	 * @param nodeList the node list
	 * @return the list
	 */
	private static List<Node> createNodeElementList(NodeList nodeList) {
		List<Node> elementListNode = new ArrayList<>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
				elementListNode.add(nodeList.item(i));
			}
		}
		return elementListNode;
	}

	/**
	 * Count length.
	 *
	 * @param nodeList the node list
	 * @return the int
	 */
	private static int countLength(NodeList nodeList) {
		int counter = 0;
		for (int i = 0; i < nodeList.getLength(); i++) {
			if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
				final Element element = (Element) nodeList.item(i);
				if (element.getAttribute("colspan") != null && StringUtils.isNotEmpty(element.getAttribute("colspan"))) {
					counter = counter + Integer.parseInt(element.getAttribute("colspan"));
				} else {
					counter++;
				}
			}
		}
		return counter;
	}

	/**
	 * Inizialize table.
	 *
	 * @param table the table
	 * @param x     the x
	 * @param y     the y
	 */
	private static void inizializeTable(TableNode[][] table, int x, int y) {
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				// Incluimos en cada celda un elemento tableNode con los valores inicializados
				TableNode tableNode = new TableNode();
				table[i][j] = tableNode;
			}
		}
	}

	// Comprueba si la lista de nodos que se le pasan corresponden a elementos de
	/**
	 * Checks if is hybrid list.
	 *
	 * @param nodeList the node list
	 * @return true, if is hybrid list
	 */
	// encabezados
	private static boolean isHybridList(List<TableNode> nodeList) {
		boolean isHeader = nodeList.get(0).isHeaderCell();
		for (int i = 1; i < nodeList.size(); i++) {
			if (nodeList.get(i).getNode() != null && isHeader != nodeList.get(i).isHeaderCell()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if is header cell.
	 *
	 * @param node the node
	 * @return true, if is header cell
	 */
	// Comprueba si una celda corresponde o no a un encabezado
	private static boolean isHeaderCell(Node node) {
		return node.getNodeName().equalsIgnoreCase("th") && StringUtils.isNotEmpty(node.getTextContent());
	}

	// Comprueba si la columna "colum" de una tabla es una columna de encabezados
	/**
	 * Checks if is correct vertical heading.
	 *
	 * @param table                the table
	 * @param x                    the x
	 * @param colum                the colum
	 * @param isBidirectionalCheck the is bidirectional check
	 * @return true, if is correct vertical heading
	 */
	// Ignoramos la primera celda que es especial
	private static boolean isCorrectVerticalHeading(TableNode[][] table, int x, int colum, boolean isBidirectionalCheck) {
		int initialCounter = 0;
		if (isBidirectionalCheck) {
			initialCounter = 1;
		}
		try {
			for (int i = initialCounter; i < x; i++) {
				if (!table[i][colum].isHeaderCell() && !table[i][colum].isSpanCell()) {
					return false;
				}
			}
		} catch (Exception e) {
			// Solución provisional, nos hemos encontrado con que hay tablas que dan un
			// OutOfRange porque la primera
			// fila es vacía y el resto no... en este caso suponemos que la tabla no es
			// vertical ya que el primer
			// encabezado vertical no existe.
			return false;
		}
		return true;
	}

	// Comprueba si la fila "row" de una tabla es una fila de encabezados
	/**
	 * Checks if is correct horizontal heading.
	 *
	 * @param table                the table
	 * @param row                  the row
	 * @param y                    the y
	 * @param isBidirectionalCheck the is bidirectional check
	 * @return true, if is correct horizontal heading
	 */
	// Ignoramos la primera celda que es especial
	private static boolean isCorrectHorizontalHeading(TableNode[][] table, int row, int y, boolean isBidirectionalCheck) {
		int initialCounter = 0;
		if (isBidirectionalCheck) {
			initialCounter = 1;
		}
		for (int j = initialCounter; j < y; j++) {
			if (!table[row][j].isHeaderCell() && !table[row][j].isSpanCell()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Function header with only cell.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	protected static boolean functionHeaderWithOnlyCell(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		NodeList trList = elementGiven.getElementsByTagName("tr");
		if (trList.item(0) != null) {
			int numTds = ((Element) trList.item(0)).getElementsByTagName("td").getLength();
			int numThs = ((Element) trList.item(0)).getElementsByTagName("th").getLength();
			return numTds + numThs == 1;
		}
		return false;
	}

	/**
	 * Checks if is irregular table.
	 *
	 * @param table the table
	 * @param x     the x
	 * @param y     the y
	 * @return true, if is irregular table
	 */
	protected static boolean isIrregularTable(TableNode[][] table, int x, int y) {
		boolean isIrregular = false;
		for (int i = 1; i < y; i++) {
			for (int j = 1; j < x; j++) {
				if (table[i][j].isHeaderCell()) {
					isIrregular = true;
				}
			}
		}
		return isIrregular;
	}
}
