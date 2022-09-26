package com.plugin.dte;

import com.intellij.lang.Language;

public class DteLanguage extends Language {
    public static final DteLanguage LANGUAGE = new DteLanguage();

    private DteLanguage() {
        super("Deterministic Thread Execution Language");
    }
}
