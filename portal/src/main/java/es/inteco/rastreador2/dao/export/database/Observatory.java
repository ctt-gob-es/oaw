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

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class Observatory.
 */
@Entity
@Table(name = "export_observatory")
public class Observatory {
    
    /** The id execution. */
    @Id
    @Column(name = "idExecution", nullable = false)
    private Long idExecution;

    /** The name. */
    @Column(name = "name", nullable = false)
    private String name;

    /** The date. */
    @Column(name = "date", nullable = false)
    private Timestamp date;

    /** The num AA. */
    @Column(name = "numAA", nullable = false)
    private int numAA;

    /** The num A. */
    @Column(name = "numA", nullable = false)
    private int numA;

    /** The num NV. */
    @Column(name = "numNV", nullable = false)
    private int numNV;

    /** The verification modality list. */
    @OneToMany(targetEntity = VerificationModality.class, mappedBy = "observatory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<VerificationModality> verificationModalityList;

    /** The verification score list. */
    @OneToMany(targetEntity = VerificationScore.class, mappedBy = "observatory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<VerificationScore> verificationScoreList;

    /** The aspect score list. */
    @OneToMany(targetEntity = AspectScore.class, mappedBy = "observatory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<AspectScore> aspectScoreList;

    /** The category list. */
    @OneToMany(targetEntity = Category.class, mappedBy = "observatory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Category> categoryList;

    /**
	 * Gets the id execution.
	 *
	 * @return the id execution
	 */
    public Long getIdExecution() {
        return idExecution;
    }

    /**
	 * Sets the id execution.
	 *
	 * @param idExecution the new id execution
	 */
    public void setIdExecution(Long idExecution) {
        this.idExecution = idExecution;
    }

    /**
	 * Gets the name.
	 *
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * Gets the date.
	 *
	 * @return the date
	 */
    public Timestamp getDate() {
        return date;
    }

    /**
	 * Sets the date.
	 *
	 * @param date the new date
	 */
    public void setDate(Timestamp date) {
        this.date = date;
    }

    /**
	 * Gets the num AA.
	 *
	 * @return the num AA
	 */
    public int getNumAA() {
        return numAA;
    }

    /**
	 * Sets the num AA.
	 *
	 * @param numAA the new num AA
	 */
    public void setNumAA(int numAA) {
        this.numAA = numAA;
    }

    /**
	 * Gets the num A.
	 *
	 * @return the num A
	 */
    public int getNumA() {
        return numA;
    }

    /**
	 * Sets the num A.
	 *
	 * @param numA the new num A
	 */
    public void setNumA(int numA) {
        this.numA = numA;
    }

    /**
	 * Gets the num NV.
	 *
	 * @return the num NV
	 */
    public int getNumNV() {
        return numNV;
    }

    /**
	 * Sets the num NV.
	 *
	 * @param numNV the new num NV
	 */
    public void setNumNV(int numNV) {
        this.numNV = numNV;
    }

    /**
	 * Gets the verification modality list.
	 *
	 * @return the verification modality list
	 */
    public List<VerificationModality> getVerificationModalityList() {
        if (verificationModalityList == null) {
            verificationModalityList = new ArrayList<>();
        }
        return verificationModalityList;
    }

    /**
	 * Sets the verification modality list.
	 *
	 * @param verificationModalityList the new verification modality list
	 */
    public void setVerificationModalityList(
            List<VerificationModality> verificationModalityList) {
        this.verificationModalityList = verificationModalityList;
    }

    /**
	 * Gets the verification score list.
	 *
	 * @return the verification score list
	 */
    public List<VerificationScore> getVerificationScoreList() {
        if (verificationScoreList == null) {
            verificationScoreList = new ArrayList<>();
        }
        return verificationScoreList;
    }

    /**
	 * Sets the verification score list.
	 *
	 * @param verificationScoreList the new verification score list
	 */
    public void setVerificationScoreList(
            List<VerificationScore> verificationScoreList) {
        this.verificationScoreList = verificationScoreList;
    }

    /**
	 * Gets the aspect score list.
	 *
	 * @return the aspect score list
	 */
    public List<AspectScore> getAspectScoreList() {
        if (aspectScoreList == null) {
            aspectScoreList = new ArrayList<>();
        }
        return aspectScoreList;
    }

    /**
	 * Sets the aspect score list.
	 *
	 * @param aspectScoreList the new aspect score list
	 */
    public void setAspectScoreList(List<AspectScore> aspectScoreList) {
        this.aspectScoreList = aspectScoreList;
    }

    /**
	 * Gets the category list.
	 *
	 * @return the category list
	 */
    public List<Category> getCategoryList() {
        if (categoryList == null) {
            categoryList = new ArrayList<>();
        }
        return categoryList;
    }

    /**
	 * Sets the category list.
	 *
	 * @param categoryList the new category list
	 */
    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }
}
