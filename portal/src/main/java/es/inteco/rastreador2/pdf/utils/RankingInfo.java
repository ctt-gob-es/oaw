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
package es.inteco.rastreador2.pdf.utils;

import es.inteco.rastreador2.actionform.semillas.AmbitoForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.actionform.semillas.ComplejidadForm;

import java.math.BigDecimal;

/**
 * The Class RankingInfo.
 */
public class RankingInfo {

    /** The score. */
    private BigDecimal score;
    
    /** The global rank. */
    private int globalRank;
    
    /** The global seeds number. */
    private int globalSeedsNumber;
    
    /** The category rank. */
    private int categoryRank;
    
    /** The complexity rank. */
    private int complexityRank;
    
    /** The category seeds number. */
    private int categorySeedsNumber;
    
    /** The categoria. */
    private CategoriaForm categoria;
    
    private ComplejidadForm complejidad;
    
    /** The ambito. */
    private AmbitoForm ambito;
    
    /** The date. */
    private String date;
    
    /** The compliance. */
    private String compliance;
    
    
    /**
	 * @return the complejidad
	 */
	public ComplejidadForm getComplejidad() {
		return complejidad;
	}

	/**
	 * @param complejidad the complejidad to set
	 */
	public void setComplejidad(ComplejidadForm complejidad) {
		this.complejidad = complejidad;
	}

	/**
	 * Gets the compliance.
	 *
	 * @return the compliance
	 */
	public String getCompliance() {
		return compliance;
	}

	/**
	 * Sets the compliance.
	 *
	 * @param compliance the compliance to set
	 */
	public void setCompliance(String compliance) {
		this.compliance = compliance;
	}

	/**
	 * Gets the complexity rank.
	 *
	 * @return the complexity rank
	 */
    public int getComplexityRank() {
		return complexityRank;
	}

	/**
	 * Sets the complexity rank.
	 *
	 * @param complexityRank the new complexity rank
	 */
	public void setComplexityRank(int complexityRank) {
		this.complexityRank = complexityRank;
	}

	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	public String getDate() {
        return date;
    }

    /**
	 * Sets the date.
	 *
	 * @param date the new date
	 */
    public void setDate(String date) {
        this.date = date;
    }

    

    /**
	 * Gets the global rank.
	 *
	 * @return the global rank
	 */
    public int getGlobalRank() {
        return globalRank;
    }

    /**
	 * Sets the global rank.
	 *
	 * @param globalRank the new global rank
	 */
    public void setGlobalRank(int globalRank) {
        this.globalRank = globalRank;
    }

    /**
	 * Increment global rank.
	 */
    public void incrementGlobalRank() {
        this.globalRank++;
    }

    /**
	 * Gets the global seeds number.
	 *
	 * @return the global seeds number
	 */
    public int getGlobalSeedsNumber() {
        return globalSeedsNumber;
    }

    /**
	 * Sets the global seeds number.
	 *
	 * @param globalSeedsNumber the new global seeds number
	 */
    public void setGlobalSeedsNumber(int globalSeedsNumber) {
        this.globalSeedsNumber = globalSeedsNumber;
    }

    /**
	 * Gets the category rank.
	 *
	 * @return the category rank
	 */
    public int getCategoryRank() {
        return categoryRank;
    }

    /**
	 * Sets the category rank.
	 *
	 * @param categoryRank the new category rank
	 */
    public void setCategoryRank(int categoryRank) {
        this.categoryRank = categoryRank;
    }

    /**
	 * Increment category rank.
	 */
    public void incrementCategoryRank() {
        this.categoryRank++;
    }
    
    /**
	 * Increment complexity rank.
	 */
    public void incrementComplexityRank() {
        this.complexityRank++;
    }


    /**
	 * Gets the category seeds number.
	 *
	 * @return the category seeds number
	 */
    public int getCategorySeedsNumber() {
        return categorySeedsNumber;
    }

    /**
	 * Sets the category seeds number.
	 *
	 * @param categorySeedsNumber the new category seeds number
	 */
    public void setCategorySeedsNumber(int categorySeedsNumber) {
        this.categorySeedsNumber = categorySeedsNumber;
    }

    /**
	 * Gets the categoria.
	 *
	 * @return the categoria
	 */
    public CategoriaForm getCategoria() {
        return categoria;
    }

    /**
	 * Sets the categoria.
	 *
	 * @param categoria the new categoria
	 */
    public void setCategoria(CategoriaForm categoria) {
        this.categoria = categoria;
    }

    /**
	 * Gets the ambito.
	 *
	 * @return the ambito
	 */
    public AmbitoForm getAmbito() {
        return ambito;
    }

    /**
	 * Sets the ambito.
	 *
	 * @param ambito the new ambito
	 */
    public void setAmbito(AmbitoForm ambito) {
        this.ambito = ambito;
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
}
