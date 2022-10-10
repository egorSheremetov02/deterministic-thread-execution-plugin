package com.plugin.dte.util;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.plugin.dte.psi.DteTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DteCompleteThreadUsageValidityChecker {
    public static boolean isValid(final PsiElement completeThreadElement) {
        return
                !tryingToCompleteMainThread(completeThreadElement)
                        &&
                        !tryingToCompleteThreadThatAlreadyWasCompleted(completeThreadElement);
    }

    // expecting completeThreadElement.getNode().getElementType() == DteTypes.THREAD_COMPLETE
    public static boolean tryingToCompleteMainThread(final PsiElement completeThreadElement) {
        var completeThreadActionElement = completeThreadElement.getParent();
        // otherwise we are trying to wait finish of main thread although all threads that were created by main should
        // finish first
        return completeThreadActionElement.getNode().findChildByType(DteTypes.MAIN_TID) != null;
    }

    public static boolean tryingToCompleteThreadThatAlreadyWasCompleted(final PsiElement completeThreadElement) {
        var completeThreadActionElement = completeThreadElement.getParent();
        var targetThreadID = getTIDByAction(completeThreadActionElement);
        var actions = getActionsByTID(completeThreadElement.getContainingFile(), targetThreadID, DteTypes.THREAD_COMPLETE);
        // means there exists THREAD_COMPLETE action with same TID before current THREAD_COMPLETE action
        return actions.get(0) != completeThreadActionElement;
    }

    public static PsiElement getTIDByAction(final PsiElement actionElement) {
        var tid = actionElement.getNode().findChildByType(DteTypes.ID);
        return tid == null ? null : tid.getPsi();
    }

    public static PsiElement getMainByAction(final PsiElement actionElement) {
        var tid = actionElement.getNode().findChildByType(DteTypes.MAIN_TID);
        return tid == null ? null : tid.getPsi();
    }

    public static String getStringTIDByAction(final PsiElement actionElement) {
        var tid = getTIDByAction(actionElement);
        return tid == null ? null : tid.getText();
    }

    public static List<PsiElement> getActionsByTID(final PsiElement dteProgram, final PsiElement threadID, IElementType actionType) {
        ArrayList<PsiElement> actions = new ArrayList<>();
        for (var dteSeqDescription : dteProgram.getChildren()) {

//            System.out.println(dteSeqDescription.getNode().findChildByType(DteTypes.LIST));
//            Arrays.stream(dteSeqDescription.getChildren()).forEach(e -> System.err.println(e.getNode().getElementType().toString() + e.getNode().getElementType().equals(DteTypes.LIST)));

            var listElement = dteSeqDescription.getNode().findChildByType(DteTypes.LIST);
            if (listElement == null) {
                continue;
            }

            actions.addAll(Arrays.stream(listElement
                    .getChildren(TokenSet.create(DteTypes.SYNCHRONIZATION_ACTION)))
                    .filter(node ->
                            node.findChildByType(actionType) != null
                                    &&
                            Objects.equals(getStringTIDByAction(node.getPsi()), threadID.getText())
                    )
                    .map(ASTNode::getPsi)
                    .collect(Collectors.toList()));
        }
        return actions;
    }

    public static PsiElement getTIDByThreadCompleteElement(PsiElement threadCompleteElement) {
        return getTIDByAction(threadCompleteElement.getParent());
    }
}
