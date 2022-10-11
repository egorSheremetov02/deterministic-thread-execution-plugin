package com.plugin.dte.appearance.highlighting;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import com.plugin.dte.psi.DteTypes;
import com.plugin.dte.util.DtePsiUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

// TODO: create different annotators for different logic
public class DteAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        var supportedElements = List.of(
                DteTypes.SYNCHRONIZATION_ACTION,
                DteTypes.DTE_SEQUENCE_DESCRIPTION
        );

        if (!supportedElements.contains(element.getNode().getElementType())) {
            return;
        }

        if (element.getNode().getElementType() == DteTypes.DTE_SEQUENCE_DESCRIPTION) {
            var idNode = element.getNode().findChildByType(DteTypes.ID);
            if (idNode == null) {
                return;
            }
            // if we reached this, we are not in main thread
            var idElement = idNode.getPsi();
            if (DtePsiUtil.tryingToUseUndefinedTID(idElement)) {
                holder
                        .newAnnotation(
                                HighlightSeverity.ERROR,
                                String.format("None of created thread have name %s", idElement.getText())
                        )
                        .range(idElement)
                        .highlightType(ProblemHighlightType.ERROR)
                        // TODO: add quick fix for this error
                        .create();
            }
        }

        if (element.getNode().getElementType() == DteTypes.SYNCHRONIZATION_ACTION) {
            // analyzing only thread_complete synchronization actions
            var isMain = element.getNode().findChildByType(DteTypes.MAIN_TID) != null;
            if (element.getNode().findChildByType(DteTypes.THREAD_COMPLETE) != null) {
                var threadCompleteElement = Objects.requireNonNull(element.getNode().findChildByType(DteTypes.THREAD_COMPLETE)).getPsi();
                // analyze attempt to wait main thread
                if (isMain && DtePsiUtil.tryingToCompleteMainThread(threadCompleteElement)) {
                    holder
                            .newAnnotation(
                                    HighlightSeverity.ERROR,
                                    "Can't wait for main thread termination"
                            )
                            .range(Objects.requireNonNull(DtePsiUtil.getMainByAction(element)))
                            .highlightType(ProblemHighlightType.ERROR)
                            // TODO: add quick fix for this error
                            .create();
                    return;
                }
                // analyze attempt to wait thread that already was completed
                if (!isMain && DtePsiUtil.tryingToCompleteThreadThatAlreadyWasCompleted(threadCompleteElement)) {
                    String threadName = Objects.requireNonNull(DtePsiUtil.getTIDByAction(element)).getText();
                    holder
                            .newAnnotation(
                                    HighlightSeverity.ERROR,
                                    String.format("Thread %s was already terminated in your code", threadName)
                            )
                            .range(Objects.requireNonNull(DtePsiUtil.getTIDByAction(element)))
                            .highlightType(ProblemHighlightType.ERROR)
                            // TODO: add quick fix for this error
                            .create();
                    return;
                }
                // analyze attempt to wait for thread that was not created yet
                if (!isMain && DtePsiUtil.tryingToWaitForUncreatedThread(element)) {
                    String threadName = Objects.requireNonNull(DtePsiUtil.getTIDByAction(element)).getText();
                    holder
                            .newAnnotation(
                                    HighlightSeverity.ERROR,
                                    String.format("Thread %s was not created yet", threadName)
                            )
                            .range(Objects.requireNonNull(DtePsiUtil.getTIDByAction(element)))
                            .highlightType(ProblemHighlightType.ERROR)
                            // TODO: add quick fix for this error
                            .create();
                    return;
                }
            }
            // analyzing usage of thread_create synchronization action only
            if (!isMain && element.getNode().findChildByType(DteTypes.THREAD_CREATE) != null) {
                if (DtePsiUtil.tryingToCreateThreadWithDuplicateName(element)) {
                    String threadName = Objects.requireNonNull(DtePsiUtil.getTIDByAction(element)).getText();
                    holder
                            .newAnnotation(
                                    HighlightSeverity.ERROR,
                                    String.format("Name %s was already used earlier, think of a different one", threadName)
                            )
                            .range(Objects.requireNonNull(DtePsiUtil.getTIDByAction(element)))
                            .highlightType(ProblemHighlightType.ERROR)
                            // TODO: add quick fix for this error
                            .create();
                }
            }
        }

    }
}
