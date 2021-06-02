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
package es.inteco.rastreador2.dao.export.database;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * The Class AspectScore.
 */
@Entity
@Table(name = "export_aspect_score")
public class AspectScore {

    /** The id. */
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    /** The aspect. */
    @Column(name = "aspect", nullable = false)
    private String aspect;

    /** The score. */
    @Column(name = "score", nullable = false)
    private BigDecimal score;

    /** The observatory. */
    @ManyToOne
    @JoinColumn(name = "idExecution", referencedColumnName = "idExecution")
    private Observatory observatory;

    /** The category. */
    @ManyToOne
    @JoinColumn(name = "idCategory", referencedColumnName = "id")
    private Category category;

    /** The site. */
    @ManyToOne
    @JoinColumn(name = "idSite", referencedColumnName = "id")
    private Site site;

    /** The page. */
    @ManyToOne
    @JoinColumn(name = "idPage")
    private Page page;

    /**
	 * Gets the id.
	 *
	 * @return the id
	 */
    public Long getId() {
        return id;
    }

    /**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
    public void setId(Long id) {
        this.id = id;
    }

    /**
	 * Gets the aspect.
	 *
	 * @return the aspect
	 */
    public String getAspect() {
        return aspect;
    }

    /**
	 * Sets the aspect.
	 *
	 * @param aspect the new aspect
	 */
    public void setAspect(String aspect) {
        this.aspect = aspect;
    }

    /**
	 * Gets the score.
	 *
	 * @return the score
	 */
    public BigDecimal getScore() {
        return score;
    }

    /**
	 * Sets the score.
	 *
	 * @param score the new score
	 */
    public void setScore(BigDecimal score) {
        this.score = score;
    }

    /**
	 * Gets the observatory.
	 *
	 * @return the observatory
	 */
    public Observatory getObservatory() {
        return observatory;
    }

    /**
	 * Sets the observatory.
	 *
	 * @param observatory the new observatory
	 */
    public void setObservatory(Observatory observatory) {
        this.observatory = observatory;
    }

    /**
	 * Gets the category.
	 *
	 * @return the category
	 */
    public Category getCategory() {
        return category;
    }

    /**
	 * Sets the category.
	 *
	 * @param category the new category
	 */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
	 * Gets the site.
	 *
	 * @return the site
	 */
    public Site getSite() {
        return site;
    }

    /**
	 * Sets the site.
	 *
	 * @param site the new site
	 */
    public void setSite(Site site) {
        this.site = site;
    }

    /**
	 * Gets the page.
	 *
	 * @return the page
	 */
    public Page getPage() {
        return page;
    }

    /**
	 * Sets the page.
	 *
	 * @param page the new page
	 */
    public void setPage(Page page) {
        this.page = page;
    }
}
