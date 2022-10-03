package com.plugin.dte;

import com.intellij.lexer.FlexAdapter;

public class DteLexerAdapter extends FlexAdapter {

    public DteLexerAdapter() {
        super(new DteLexer());
    }

}
