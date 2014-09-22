package es.inteco.rastreador2.intav.utils;

import es.inteco.intav.form.ProblemForm;

import java.util.ArrayList;
import java.util.List;

public class ISPDFProblemForm {

    public ProblemForm problem;
    public List<String> urls;

    public ISPDFProblemForm() {
        problem = new ProblemForm();
        urls = new ArrayList<String>();
    }

    public ProblemForm getProblem() {
        return problem;
    }

    public void setProblem(ProblemForm problem) {
        this.problem = problem;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    @Override
    public int hashCode() {
        return problem != null ? problem.getCheck().hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ISPDFProblemForm) {
            final ISPDFProblemForm form = (ISPDFProblemForm) obj;
            if (form.problem.getCheck().equals(problem.getCheck())) {
                return true;
            }
        }
        return false;
    }
}
