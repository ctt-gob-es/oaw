package es.inteco.rastreador2.intav.utils;

import es.inteco.intav.form.PautaForm;

import java.util.ArrayList;
import java.util.List;

public class ISPDFPautaForm {

    public PautaForm pauta;
    public List<ISPDFProblemForm> problemList;

    public ISPDFPautaForm() {
        pauta = new PautaForm();
        problemList = new ArrayList<>();
    }

    public PautaForm getPauta() {
        return pauta;
    }

    public void setPauta(PautaForm pauta) {
        this.pauta = pauta;
    }

    public List<ISPDFProblemForm> getProblemList() {
        return problemList;
    }

    public void setProblemList(List<ISPDFProblemForm> problemList) {
        this.problemList = problemList;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ISPDFPautaForm) {
            ISPDFPautaForm form = (ISPDFPautaForm) obj;
            if (form.getPauta().getName().equals(pauta.getName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return pauta != null ? pauta.getName().hashCode() : 0;
    }
}
