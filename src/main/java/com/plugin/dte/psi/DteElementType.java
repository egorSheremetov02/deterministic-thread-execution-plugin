package com.plugin.dte.psi;

import com.intellij.psi.tree.IElementType;
import com.plugin.dte.DteLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class DteElementType extends IElementType {
    public DteElementType(@NotNull @NonNls String debugName) {
        super(debugName, DteLanguage.LANGUAGE);
    }
}
