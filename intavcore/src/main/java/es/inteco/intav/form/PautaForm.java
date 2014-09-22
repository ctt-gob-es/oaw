package es.inteco.intav.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PautaForm implements Serializable {
    private String name;
    private String pautaId;
    private List<ProblemForm> problems;

    public PautaForm() {
        problems = new ArrayList<ProblemForm>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProblemForm> getProblems() {
        return problems;
    }

    public void setProblems(List<ProblemForm> problems) {
        this.problems = problems;
    }

    public String getPautaId() {
        return pautaId;
    }

    public void setPautaId(String pautaId) {
        this.pautaId = pautaId;
    }
}
