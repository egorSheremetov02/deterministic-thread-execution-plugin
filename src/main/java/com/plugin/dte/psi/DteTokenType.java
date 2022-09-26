package com.plugin.dte.psi;

import com.intellij.psi.tree.IElementType;
import com.plugin.dte.DteLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;


public class DteTokenType extends IElementType {
    public DteTokenType(@NotNull @NonNls String debugName) {
        super(debugName, DteLanguage.LANGUAGE);
    }

    @Override
    public String toString() {
        return "DteTokenType." + super.toString();
    }
}
