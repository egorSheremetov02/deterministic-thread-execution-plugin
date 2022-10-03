package com.plugin.dte.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.plugin.dte.DteLanguageFileType;
import com.plugin.dte.DteLanguage;
import org.jetbrains.annotations.NotNull;

public class DteFile extends PsiFileBase {
    public DteFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, DteLanguage.LANGUAGE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return DteLanguageFileType.FILE_TYPE;
    }

    @Override
    public String toString() {
        return "Dte File";
    }
}
