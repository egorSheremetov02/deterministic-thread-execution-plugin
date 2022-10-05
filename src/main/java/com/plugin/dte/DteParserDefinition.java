package com.plugin.dte;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;

import com.plugin.dte.parser.DteParser;
import com.plugin.dte.psi.DteFile;
import com.plugin.dte.psi.DteTypes;

import org.jetbrains.annotations.NotNull;

public class DteParserDefinition implements ParserDefinition {
    public static final IFileElementType FILE = new IFileElementType(DteLanguage.LANGUAGE);

    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        return new DteLexerAdapter();
    }

    @NotNull
    @Override
    public TokenSet getCommentTokens() {
        return TokenSet.EMPTY;
    }

    @NotNull
    @Override
    public TokenSet getStringLiteralElements() {
        return TokenSet.EMPTY;
    }

    @NotNull
    @Override
    public PsiParser createParser(final Project project) {
        return new DteParser();
    }

    @NotNull
    @Override
    public IFileElementType getFileNodeType() {
        return FILE;
    }

    @NotNull
    @Override
    public PsiFile createFile(@NotNull FileViewProvider viewProvider) {
        return new DteFile(viewProvider);
    }

    @NotNull
    @Override
    public PsiElement createElement(ASTNode node) {
        return DteTypes.Factory.createElement(node);
    }
}
