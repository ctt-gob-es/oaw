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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "export_site")
public class Site {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "score")
    private BigDecimal score;

    @Column(name = "scoreLevel1")
    private BigDecimal scoreLevel1;

    @Column(name = "scoreLevel2")
    private BigDecimal scoreLevel2;

    @Column(name = "level", nullable = false)
    private String level;

    @Column(name = "numAA", nullable = false)
    private int numAA;

    @Column(name = "numA", nullable = false)
    private int numA;

    @Column(name = "numNV", nullable = false)
    private int numNV;

    @ManyToOne
    @JoinColumn(name = "idCategory", nullable = false, referencedColumnName = "id")
    private Category category;

    @OneToMany(targetEntity = Page.class, mappedBy = "site", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Page> pageList;

    @OneToMany(targetEntity = AspectScore.class, mappedBy = "site", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<AspectScore> aspectScoreList;

    @OneToMany(targetEntity = VerificationScore.class, mappedBy = "site", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<VerificationScore> verificationScoreList;

    @OneToMany(targetEntity = VerificationModality.class, mappedBy = "site", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<VerificationModality> verificationModalityList;

    @Column(name = "idCrawlerSeed")
    private Long idCrawlerSeed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public List<Page> getPageList() {
        if (pageList == null) {
            pageList = new ArrayList<>();
        }
        return pageList;
    }

    public void setPageList(List<Page> pageList) {
        this.pageList = pageList;
    }

    public BigDecimal getScoreLevel1() {
        return scoreLevel1;
    }

    public void setScoreLevel1(BigDecimal scoreLevel1) {
        this.scoreLevel1 = scoreLevel1;
    }

    public BigDecimal getScoreLevel2() {
        return scoreLevel2;
    }

    public void setScoreLevel2(BigDecimal scoreLevel2) {
        this.scoreLevel2 = scoreLevel2;
    }

    public int getNumAA() {
        return numAA;
    }

    public void setNumAA(int numAA) {
        this.numAA = numAA;
    }

    public int getNumA() {
        return numA;
    }

    public void setNumA(int numA) {
        this.numA = numA;
    }

    public int getNumNV() {
        return numNV;
    }

    public void setNumNV(int numNV) {
        this.numNV = numNV;
    }

    public List<AspectScore> getAspectScoreList() {
        if (aspectScoreList == null) {
            aspectScoreList = new ArrayList<>();
        }
        return aspectScoreList;
    }

    public void setAspectScoreList(List<AspectScore> aspectScoreList) {
        this.aspectScoreList = aspectScoreList;
    }

    public List<VerificationScore> getVerificationScoreList() {
        if (verificationScoreList == null) {
            verificationScoreList = new ArrayList<>();
        }
        return verificationScoreList;
    }

    public void setVerificationScoreList(
            List<VerificationScore> verificationScoreList) {
        this.verificationScoreList = verificationScoreList;
    }

    public List<VerificationModality> getVerificationModalityList() {
        if (verificationModalityList == null) {
            verificationModalityList = new ArrayList<>();
        }
        return verificationModalityList;
    }

    public void setVerificationModalityList(
            List<VerificationModality> verificationModalityList) {
        this.verificationModalityList = verificationModalityList;
    }

    public Long getIdCrawlerSeed() {
        return idCrawlerSeed;
    }

    public void setIdCrawlerSeed(Long idCrawlerSeed) {
        this.idCrawlerSeed = idCrawlerSeed;
    }
}
