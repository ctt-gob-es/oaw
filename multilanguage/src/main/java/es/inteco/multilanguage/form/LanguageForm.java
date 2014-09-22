package es.inteco.multilanguage.form;

import es.inteco.multilanguage.converter.GenericConverter;
import es.inteco.multilanguage.persistence.Language;
import org.apache.commons.beanutils.ConvertUtils;


public class LanguageForm {

    static {
        ConvertUtils.register(new GenericConverter(), Language.class);
    }

    private String id;
    private String name;
    private String text;
    private String code;
    private Boolean toAnalyze;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getToAnalyze() {
        return toAnalyze;
    }

    public void setToAnalyze(Boolean toAnalyze) {
        this.toAnalyze = toAnalyze;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LanguageForm) {
            LanguageForm language = (LanguageForm) obj;
            if (this.code.equals(language.getCode()) && this.id.equals(language.getId()) &&
                    this.text.equals(language.getText()) && this.name.equals(language.getName())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


}
