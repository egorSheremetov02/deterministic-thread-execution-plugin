package com.plugin.dte.appearance.highlighting;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import com.plugin.dte.psi.DteTypes;
import com.plugin.dte.util.DteCompleteThreadUsageValidityChecker;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class DteAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        var supportedElements = List.of(
                DteTypes.SYNCHRONIZATION_ACTION
        );

        if (!supportedElements.contains(element.getNode().getElementType())) {
            return;
        }

        if (element.getNode().getElementType() == DteTypes.SYNCHRONIZATION_ACTION) {
            // analyzing only thread_complete synchronization actions
            if (element.getNode().findChildByType(DteTypes.THREAD_COMPLETE) != null) {
                var threadCompleteElement = element.getNode().findChildByType(DteTypes.THREAD_COMPLETE).getPsi();
                var isMain = element.getNode().findChildByType(DteTypes.MAIN_TID) != null;
                // analyze attempt to wait main thread
                if (isMain && DteCompleteThreadUsageValidityChecker.tryingToCompleteMainThread(threadCompleteElement)) {
                    holder
                            .newAnnotation(
                                    HighlightSeverity.ERROR,
                                    "Can't wait for main thread termination"
                            )
                            .range(Objects.requireNonNull(DteCompleteThreadUsageValidityChecker.getMainByAction(element)))
                            .highlightType(ProblemHighlightType.ERROR)
                            // TODO: add quick fix for this error
                            .create();
                    return;
                }
                // analyze attempt to wait thread that already was completed
                if (!isMain && DteCompleteThreadUsageValidityChecker.tryingToCompleteThreadThatAlreadyWasCompleted(threadCompleteElement)) {
                    String threadName = DteCompleteThreadUsageValidityChecker.getTIDByAction(element).getText();
                    holder
                            .newAnnotation(
                                    HighlightSeverity.ERROR,
                                    String.format("Thread %s was already terminated in your code", threadName)
                            )
                            .range(Objects.requireNonNull(DteCompleteThreadUsageValidityChecker.getTIDByAction(element)))
                            .highlightType(ProblemHighlightType.ERROR)
                            // TODO: add quick fix for this error
                            .create();
                    return;
                }
                // analyze attempt to wait for thread that was not created yet
                if (!isMain && DteCompleteThreadUsageValidityChecker.tryingToWaitForNonexistentThread(element)) {
                    String threadName = DteCompleteThreadUsageValidityChecker.getTIDByAction(element).getText();
                    holder
                            .newAnnotation(
                                    HighlightSeverity.ERROR,
                                    String.format("Thread %s was not declared yet", threadName)
                            )
                            .range(Objects.requireNonNull(DteCompleteThreadUsageValidityChecker.getTIDByAction(element)))
                            .highlightType(ProblemHighlightType.ERROR)
                            // TODO: add quick fix for this error
                            .create();
                    return;
                }
            }
        }

    }
}
