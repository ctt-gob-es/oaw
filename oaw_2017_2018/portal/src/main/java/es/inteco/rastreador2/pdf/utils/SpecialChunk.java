package es.inteco.rastreador2.pdf.utils;

import com.lowagie.text.Font;

public class SpecialChunk {
    private String text;
    private String anchor;
    private boolean destination;
    private Font font;
    private boolean externalLink;
    private String type;

    public SpecialChunk(String text, String anchor, boolean destination, Font font) {
        this.text = text;
        this.anchor = anchor;
        this.destination = destination;
        this.font = font;
        this.externalLink = false;
    }

    public SpecialChunk(String text, Font font) {
        this.text = text;
        this.font = font;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAnchor() {
        return anchor;
    }

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    public boolean isDestination() {
        return destination;
    }

    public void setDestination(boolean destination) {
        this.destination = destination;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public boolean isExternalLink() {
        return externalLink;
    }

    public void setExternalLink(boolean externalLink) {
        this.externalLink = externalLink;
    }
}
