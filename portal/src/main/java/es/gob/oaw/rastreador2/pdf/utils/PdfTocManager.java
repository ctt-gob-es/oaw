package es.gob.oaw.rastreador2.pdf.utils;

import com.itextpdf.text.pdf.events.IndexEvents;

/**
 * Created by mikunis on 1/16/17.
 */
public class PdfTocManager {
	/** The index. */
	private final IndexEvents index;
	/** The num chapter. */
	private int numChapter = 1;
	/** The num section. */
	private int numSection = 1;

	/**
	 * Instantiates a new pdf toc manager.
	 *
	 * @param index the index
	 */
	public PdfTocManager(final IndexEvents index) {
		this.index = index;
	}

	/**
	 * Gets the index.
	 *
	 * @return the index
	 */
	public IndexEvents getIndex() {
		return index;
	}

	/**
	 * Adds the chapter count.
	 */
	public void addChapterCount() {
		numChapter++;
	}

	/**
	 * Adds the section count.
	 */
	public void addSectionCount() {
		numSection++;
	}

	/**
	 * Gets the num section.
	 *
	 * @return the num section
	 */
	public int getNumSection() {
		return numSection;
	}

	/**
	 * Gets the num chapter.
	 *
	 * @return the num chapter
	 */
	public int getNumChapter() {
		return numChapter;
	}

	/**
	 * Adds the section.
	 *
	 * @return the int
	 */
	public int addSection() {
		return numSection++;
	}
}
