package com.plugin.dte;


import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class DteLanguageFileType extends LanguageFileType {

    public static final DteLanguageFileType FILE_TYPE = new DteLanguageFileType();

    private DteLanguageFileType() {
        super(DteLanguage.LANGUAGE);
    }

    @Override
    public @NonNls @NotNull String getName() {
        return "DTE File";
    }

    @Override
    public @NotNull String getDescription() {
        return "This file defines order of threads execution, thus makes your multithreaded code more predictable. Useful for debugging purposes";
    }

    @Override
    public @NotNull String getDefaultExtension() {
        return "dte";
    }

    @Override
    public @Nullable Icon getIcon() {
        return DteIcons.fileIcon;
    }
}
