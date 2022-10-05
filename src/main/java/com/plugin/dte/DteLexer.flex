package com.plugin.dte;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.plugin.dte.psi.DteTypes.*;

%%

%{
  public DteLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class DteLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL=\R
WHITE_SPACE=\s+

SPACE=[ \t\n\x0B\f\r]+
ID=[a-z][a-zA-Z0-9]*

%%
<YYINITIAL> {
  {WHITE_SPACE}          { return WHITE_SPACE; }

  "("                    { return LP; }
  ")"                    { return RP; }
  ":"                    { return SEMICOLON; }
  "complete_thread"      { return THREAD_COMPLETE; }
  "create_thread"        { return THREAD_CREATE; }
  "main"                 { return MAIN_TID; }
  "mutex_lock"           { return MTX_LOCK; }
  "mutex_unlock"         { return MTX_UNLOCK; }

  {SPACE}                { return SPACE; }
  {ID}                   { return ID; }

}

[^] { return BAD_CHARACTER; }
