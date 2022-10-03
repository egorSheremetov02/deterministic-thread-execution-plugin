// This is a generated file. Not intended for manual editing.
package com.plugin.dte.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.plugin.dte.psi.DteTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class DteParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, null);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return dteFile(b, l + 1);
  }

  /* ********************************************************** */
  // <<dteSequenceDescription MAIN_TID>> ( <<dteSequenceDescription (ID | MAIN_TID)>> ) * <<eof>>
  static boolean dteFile(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dteFile")) return false;
    if (!nextTokenIs(b, MAIN_TID)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = dteSequenceDescription(b, l + 1, MAIN_TID_parser_);
    r = r && dteFile_1(b, l + 1);
    r = r && eof(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( <<dteSequenceDescription (ID | MAIN_TID)>> ) *
  private static boolean dteFile_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dteFile_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!dteFile_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "dteFile_1", c)) break;
    }
    return true;
  }

  // <<dteSequenceDescription (ID | MAIN_TID)>>
  private static boolean dteFile_1_0(PsiBuilder b, int l) {
    return dteSequenceDescription(b, l + 1, DteParser::dteFile_1_0_0_0);
  }

  // ID | MAIN_TID
  private static boolean dteFile_1_0_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dteFile_1_0_0_0")) return false;
    boolean r;
    r = consumeToken(b, ID);
    if (!r) r = consumeToken(b, MAIN_TID);
    return r;
  }

  /* ********************************************************** */
  // <<param>> ':' <<list synchronization_action>>
  public static boolean dteSequenceDescription(PsiBuilder b, int l, Parser _param) {
    if (!recursion_guard_(b, l, "dteSequenceDescription")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = _param.parse(b, l);
    r = r && consumeToken(b, ":");
    r = r && list(b, l + 1, DteParser::synchronization_action);
    exit_section_(b, m, DTE_SEQUENCE_DESCRIPTION, r);
    return r;
  }

  /* ********************************************************** */
  // <<param>> ( <<param>> ) *
  public static boolean list(PsiBuilder b, int l, Parser _param) {
    if (!recursion_guard_(b, l, "list")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = _param.parse(b, l);
    r = r && list_1(b, l + 1, _param);
    exit_section_(b, m, LIST, r);
    return r;
  }

  // ( <<param>> ) *
  private static boolean list_1(PsiBuilder b, int l, Parser _param) {
    if (!recursion_guard_(b, l, "list_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!list_1_0(b, l + 1, _param)) break;
      if (!empty_element_parsed_guard_(b, "list_1", c)) break;
    }
    return true;
  }

  // <<param>>
  private static boolean list_1_0(PsiBuilder b, int l, Parser _param) {
    return _param.parse(b, l);
  }

  /* ********************************************************** */
  // (THREAD_COMPLETE LP ID RP) | (THREAD_CREATE LP ID RP) | MTX_LOCK | MTX_UNLOCK
  public static boolean synchronization_action(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "synchronization_action")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SYNCHRONIZATION_ACTION, "<synchronization action>");
    r = synchronization_action_0(b, l + 1);
    if (!r) r = synchronization_action_1(b, l + 1);
    if (!r) r = consumeToken(b, MTX_LOCK);
    if (!r) r = consumeToken(b, MTX_UNLOCK);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // THREAD_COMPLETE LP ID RP
  private static boolean synchronization_action_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "synchronization_action_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, THREAD_COMPLETE, LP, ID, RP);
    exit_section_(b, m, null, r);
    return r;
  }

  // THREAD_CREATE LP ID RP
  private static boolean synchronization_action_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "synchronization_action_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, THREAD_CREATE, LP, ID, RP);
    exit_section_(b, m, null, r);
    return r;
  }

  static final Parser MAIN_TID_parser_ = (b, l) -> consumeToken(b, MAIN_TID);
}
