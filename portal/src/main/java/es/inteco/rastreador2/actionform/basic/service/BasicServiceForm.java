package es.inteco.rastreador2.actionform.basic.service;

import es.inteco.common.Constants;
import es.inteco.common.utils.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.struts.validator.ValidatorForm;

import java.util.Date;

public class BasicServiceForm extends ValidatorForm {
    private long id;
    private String language;
    private String domain;
    private String content;
    private String email;
    private String name;
    private String user;
    private String profundidad;
    private String amplitud;
    private String report;
    private Date schedulingDate;
    private boolean inDirectory;
    // Campos relativos al historico/evolutivo del servicio de diagnóstico
    private boolean registerAnalysis;
    private String analysisToDelete;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getProfundidad() {
        return profundidad;
    }

    public void setProfundidad(String profundidad) {
        this.profundidad = profundidad;
    }

    public String getAmplitud() {
        return amplitud;
    }

    public void setAmplitud(String amplitud) {
        this.amplitud = amplitud;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getSchedulingDate() {
        return new Date(schedulingDate.getTime());
    }

    public void setSchedulingDate(Date schedulingDate) {
        this.schedulingDate = new Date(schedulingDate.getTime());
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isInDirectory() {
        return inDirectory;
    }

    public void setInDirectory(boolean inDirectory) {
        this.inDirectory = inDirectory;
    }

    public boolean isContentAnalysis() {
        return StringUtils.isEmpty(this.domain) && StringUtils.isNotEmpty(this.content);
    }

    public String reportToString() {
        if (Constants.REPORT_OBSERVATORY.equals(report) || Constants.REPORT_OBSERVATORY_FILE.equals(report)) {
            return "Observatorio UNE 2004";
        } else if (Constants.REPORT_OBSERVATORY_2.equals(report)) {
            return "Observatorio UNE 2012";
        } else if ("observatorio-1-nobroken".equals(report)) {
            return "Observatorio UNE 2004 (sin comprobar enlaces rotos)";
        } else if (Constants.REPORT_OBSERVATORY_2_NOBROKEN.equals(report)) {
            return "Observatorio UNE 2012 (sin comprobar enlaces rotos)";
        } else {
            return report;
        }
    }

    public boolean isRegisterAnalysis() {
        return registerAnalysis;
    }

    public void setRegisterAnalysis(boolean registerAnalysis) {
        this.registerAnalysis = registerAnalysis;
    }

    public String getAnalysisToDelete() {
        return analysisToDelete;
    }

    public void setAnalysisToDelete(String analysisToDelete) {
        this.analysisToDelete = analysisToDelete;
    }

    public boolean isDeleteOldAnalysis() {
        return analysisToDelete != null && !analysisToDelete.isEmpty();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("language", language)
                .append("domain", domain)
                .append("content", content)
                .append("email", email)
                .append("name", name)
                .append("user", user)
                .append("profundidad", profundidad)
                .append("amplitud", amplitud)
                .append("report", report)
                .append("schedulingDate", schedulingDate)
                .append("inDirectory", inDirectory)
                .append("registerAnalysis", registerAnalysis)
                .append("analysisToDelete", analysisToDelete)
                .toString();
    }
}
