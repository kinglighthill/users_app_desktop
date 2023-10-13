package com.scholarly.utme.data.model.newDb.contentViewType;

import com.scholarly.utme.data.model.newDb.contentType.ContentViewType;

public class LatexMathViewType extends ContentViewType {
    private String katex;

    public LatexMathViewType(String katex) {
        this.katex = katex;
    }

    public String getKatex() {
        if (katex == null) {
            return null;
        }
        return "$$" + katex + "$$";
    }
}
