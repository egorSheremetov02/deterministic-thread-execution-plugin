// This is a generated file. Not intended for manual editing.
package com.plugin.dte.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.plugin.dte.psi.DteTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.plugin.dte.psi.*;

public class DteSynchronizationActionImpl extends ASTWrapperPsiElement implements DteSynchronizationAction {

  public DteSynchronizationActionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull DteVisitor visitor) {
    visitor.visitSynchronizationAction(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DteVisitor) accept((DteVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiElement getId() {
    return findChildByType(ID);
  }

}
