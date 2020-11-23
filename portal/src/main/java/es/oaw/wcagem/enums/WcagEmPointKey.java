package es.oaw.wcagem.enums;

/**
 * The Enum WcagEmPointKey.
 */
public enum WcagEmPointKey {
	/** The wcag 131. */
	WCAG_1_1_1("1.1.1", "WCAG2:non-text-content"),
	/** The wcag 1 3 1. */
	WCAG_1_3_1("1.3.1", "WCAG2:info-and-relationships"),
	/** The wcag 1 3 4. */
	WCAG_1_3_4("1.3.4", "WCAG2:orientation"),
	/** The wcag 1 3 5. */
	WCAG_1_3_5("1.3.5", "WCAG2:identify-input-purpose"),
	/** The wcag 1 4 3. */
	WCAG_1_4_3("1.4.3", "WCAG2:contrast-minimum"),
	/** The wcag 1 4 10. */
	WCAG_1_4_10("1.4.10", "WCAG2:reflow"),
	/** The wcag 1 4 12. */
	WCAG_1_4_12("1.4.12", "WCAG2:text-spacing"),
	/** The wcag 2 1 1. */
	WCAG_2_1_1("2.1.1", "WCAG2:keyboard"),
	/** The wcag 2 2 1. */
	WCAG_2_2_1("2.2.1", "WCAG2:timing-adjustable"),
	/** The wcag 2 2 2. */
	WCAG_2_2_2("2.2.2", "WCAG2:pause-stop-hide"),
	/** The wcag 2 3 1. */
	WCAG_2_3_1("2.3.1", "WCAG2:three-flashes-or-below-threshold"),
	/** The wcag 2 4 1. */
	WCAG_2_4_1("2.4.1", "WCAG2:bypass-blocks"),
	/** The wcag 2 4 2. */
	WCAG_2_4_2("2.4.2", "WCAG2:page-titled"),
	/** The wcag 2 4 3. */
	WCAG_2_4_3("2.4.3", "WCAG2:focus-order"),
	/** The wcag 2 4 4. */
	WCAG_2_4_4("2.4.4", "WCAG2:link-purpose-in-context"),
	/** The wcag 2 4 5. */
	WCAG_2_4_5("2.4.5", "WCAG2:multiple-ways"),
	/** The wcag 2 4 5. */
	WCAG_2_4_7("2.4.7", "WCAG2:focus-visible"),
	/** The wcag 2 5 3. */
	WCAG_2_5_3("2.5.3", "WCAG2:label-in-name"),
	/** The wcag 3 1 1. */
	WCAG_3_1_1("3.1.1", "WCAG2:language-of-page"),
	/** The wcag 3 1 2. */
	WCAG_3_1_2("3.1.2", "WCAG2:language-of-parts"),
	/** The wcag 3 2 1. */
	WCAG_3_2_1("3.2.1", "WCAG2:on-focus"),
	/** The wcag 3 2 2. */
	WCAG_3_2_2("3.2.2", "WCAG2:on-input"),
	/** The wcag 3 2 3. */
	WCAG_3_2_3("3.2.3", "WCAG2:consistent-navigation"),
	/** The wcag 3 3 2. */
	WCAG_3_3_2("3.3.1", "WCAG2:labels-or-instructions"),
	/** The wcag 4 1 2. */
	WCAG_4_1_2("4.1.2", "WCAG2:name-role-value"),
	/** The wcag 4 1 1. */
	WCAG_4_1_1("4.1.1", "WCAG2:parsing");

	/** The wcag point. */
	private final String wcagPoint;
	/** The wcag em id. */
	private final String wcagEmId;

	/**
	 * Instantiates a new wcag em point key.
	 *
	 * @param wcagPoint the wcag point
	 * @param wcagEmId  the wcag em id
	 */
	WcagEmPointKey(String wcagPoint, String wcagEmId) {
		this.wcagPoint = wcagPoint;
		this.wcagEmId = wcagEmId;
	}

	/**
	 * Gets the wcag point.
	 *
	 * @return the wcag point
	 */
	public String getWcagPoint() {
		return wcagPoint;
	}

	/**
	 * Gets the wcag em id.
	 *
	 * @return the wcag em id
	 */
	public String getWcagEmId() {
		return wcagEmId;
	}
}
