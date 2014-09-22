package es.inteco.rastreador2.imp.xml.result;

import es.inteco.common.Constants;
import es.inteco.rastreador2.pdf.utils.SpecialChunk;

import java.util.Map;

public class ParagraphForm {

    private String paragraph;
    private Map<Integer, SpecialChunk> specialChunks;
    private String type;

    public ParagraphForm() {
        type = Constants.OBJECT_TYPE_PARAGRAPH;
    }

    public Map<Integer, SpecialChunk> getSpecialChunks() {
        return specialChunks;
    }

    public void setSpecialChunks(Map<Integer, SpecialChunk> specialChunks) {
        this.specialChunks = specialChunks;
    }

    public String getParagraph() {
        return paragraph;
    }

    public void setParagraph(String paragraph) {
        this.paragraph = paragraph;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
