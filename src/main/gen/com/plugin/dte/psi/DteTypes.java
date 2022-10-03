// This is a generated file. Not intended for manual editing.
package com.plugin.dte.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.plugin.dte.psi.impl.*;

public interface DteTypes {

  IElementType DTE_SEQUENCE_DESCRIPTION = new DteElementType("DTE_SEQUENCE_DESCRIPTION");
  IElementType LIST = new DteElementType("LIST");
  IElementType SYNCHRONIZATION_ACTION = new DteElementType("SYNCHRONIZATION_ACTION");

  IElementType ID = new DteTokenType("ID");
  IElementType LP = new DteTokenType("(");
  IElementType MAIN_TID = new DteTokenType("main");
  IElementType MTX_LOCK = new DteTokenType("mutex_lock");
  IElementType MTX_UNLOCK = new DteTokenType("mutex_unlock");
  IElementType RP = new DteTokenType(")");
  IElementType THREAD_COMPLETE = new DteTokenType("complete_thread");
  IElementType THREAD_CREATE = new DteTokenType("create_thread");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == DTE_SEQUENCE_DESCRIPTION) {
        return new DteDteSequenceDescriptionImpl(node);
      }
      else if (type == LIST) {
        return new DteListImpl(node);
      }
      else if (type == SYNCHRONIZATION_ACTION) {
        return new DteSynchronizationActionImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
