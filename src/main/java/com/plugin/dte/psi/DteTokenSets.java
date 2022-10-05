package com.plugin.dte.psi;

import com.intellij.psi.tree.TokenSet;

public class DteTokenSets {

    TokenSet IDENTIFIERS = TokenSet.create(
            DteTypes.MAIN_TID,
            DteTypes.ID
    );

    TokenSet KEYWORDS = TokenSet.create(
            DteTypes.SYNCHRONIZATION_ACTION
    );

    TokenSet BRACKETS = TokenSet.create(
            DteTypes.LP,
            DteTypes.RP
    );
}
