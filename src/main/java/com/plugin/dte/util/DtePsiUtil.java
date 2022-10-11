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

import static java.util.Objects.requireNonNull;

public class DtePsiUtil {
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

    public static PsiElement getActionThread(final PsiElement action) {
        var curIdElement = action.getNode().findChildByType(DteTypes.ID);
        if (curIdElement == null) {
            curIdElement = action.getNode().findChildByType(DteTypes.MAIN_TID);
        }
        return requireNonNull(curIdElement).getPsi();
    }

    public static List<PsiElement> getActionsByTID(final PsiElement dteProgram, final PsiElement threadID, IElementType ...actionTypes) {
        ArrayList<PsiElement> actions = new ArrayList<>();
        for (var dteSeqDescription : dteProgram.getChildren()) {
            var listElement = dteSeqDescription.getNode().findChildByType(DteTypes.LIST);
            if (listElement == null) {
                continue;
            }
            actions.addAll(Arrays.stream(listElement
                    .getChildren(TokenSet.create(DteTypes.SYNCHRONIZATION_ACTION)))
                    .filter(node ->
                            node.getChildren(TokenSet.create(actionTypes)).length > 0
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

    public static ASTNode getDteSequenceThread(PsiElement dteSeqDescription) {
        var curIdElement = dteSeqDescription.getNode().findChildByType(DteTypes.ID);
        if (curIdElement == null) {
            curIdElement = dteSeqDescription.getNode().findChildByType(DteTypes.MAIN_TID);
        }
        return curIdElement;
    }

    public static IElementType getActionType(PsiElement action) {
        var actionAST = action.getNode();
        var actionTypes = actionAST.getChildren(TokenSet.create(DteTypes.THREAD_CREATE, DteTypes.THREAD_COMPLETE, DteTypes.MTX_UNLOCK, DteTypes.MTX_LOCK));
        assert actionTypes.length == 1;
        return actionTypes[0].getElementType();
    }

    public static boolean tryingToWaitForUncreatedThread(PsiElement completeAction) {
        var actions = getActionsByTID(
                completeAction.getContainingFile(),
                getActionThread(completeAction),
                DteTypes.THREAD_COMPLETE, DteTypes.THREAD_CREATE);
        for (var action : actions) {
            if (action == completeAction) {
                break;
            }
            var actionType = getActionType(action);
            if (actionType == DteTypes.THREAD_CREATE) {
                return false;
            }
        }
        return true;
    }

    public static boolean tryingToCreateThreadWithDuplicateName(PsiElement createAction) {
        System.err.println("in tryingToCreateThreadWithDuplicateName");
        var actions = getActionsByTID(
                createAction.getContainingFile(),
                getTIDByAction(createAction),
                DteTypes.THREAD_CREATE);
        System.err.println("actions size is " + actions.size() + " for thread " + getTIDByAction(createAction).getText());
        for (var action : actions) {
            if (action == createAction) {
                break;
            }
            if (
                    Objects.equals(
                        requireNonNull(getTIDByAction(action)).getText(),
                        requireNonNull(getTIDByAction(createAction)).getText()
                    )
            ) {
                return true;
            }
        }
        return false;
    }

    public static boolean tryingToUseUndefinedTID(PsiElement idElement) {
        for (var dteSeqDescription : idElement.getContainingFile().getChildren()) {
            var listElementAST = dteSeqDescription.getNode().findChildByType(DteTypes.LIST);
            if (listElementAST == null) {
                continue;
            }
            var curIdElementAST = getDteSequenceThread(dteSeqDescription);
            if (curIdElementAST == null) {
                continue;
            }
            var curIdElement = curIdElementAST.getPsi();
            var listElement = listElementAST.getPsi();
            if (idElement == curIdElement) {
                break;
            }
            var optionalAction = Arrays
                    .stream(listElement.getNode().getChildren(TokenSet.create(DteTypes.SYNCHRONIZATION_ACTION)))
                    .map(ASTNode::getPsi)
                    .filter(action ->
                            getActionType(action) == DteTypes.THREAD_CREATE
                                    &&
                            Objects.equals(requireNonNull(getTIDByAction(action)).getText(), idElement.getText()))
                    .findAny();
            if (optionalAction.isPresent()) {
                return false;
            }
        }
        return true;
    }
}
