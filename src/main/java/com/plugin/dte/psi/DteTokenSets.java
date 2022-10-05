package com.plugin.dte.psi;

import com.intellij.psi.tree.TokenSet;

public class DteTokenSets {

    public static final TokenSet IDENTIFIERS = TokenSet.create(
            DteTypes.MAIN_TID,
            DteTypes.ID
    );

    public static final TokenSet KEYWORDS = TokenSet.create(
            DteTypes.MTX_UNLOCK,
            DteTypes.MTX_LOCK,
            DteTypes.THREAD_CREATE,
            DteTypes.THREAD_COMPLETE
    );

    public static final TokenSet PARENTHESES = TokenSet.create(
            DteTypes.LP,
            DteTypes.RP
    );
}
