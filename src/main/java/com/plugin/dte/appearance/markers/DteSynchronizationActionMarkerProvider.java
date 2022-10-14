package com.plugin.dte.appearance.markers;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;

import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.FileBasedIndex;
import com.plugin.dte.appearance.icons.DteIcons;
import com.plugin.dte.psi.DteTypes;
import org.jetbrains.annotations.NotNull;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class DteSynchronizationActionMarkerProvider implements LineMarkerProvider {

    public static List<VirtualFile> findFileByRelativePath(@NotNull Project project, @NotNull String fileRelativePath) {
        String relativePath = fileRelativePath.startsWith("/") ? fileRelativePath : "/" + fileRelativePath;
        Set<FileType> fileTypes = Collections.singleton(FileTypeManager.getInstance().getFileTypeByFileName(relativePath));
        final List<VirtualFile> fileList = new ArrayList<>();
        FileBasedIndex.getInstance().processFilesContainingAllKeys(FileTypeIndex.NAME, fileTypes, GlobalSearchScope.projectScope(project), null, virtualFile -> {
            if (virtualFile.getPath().endsWith(relativePath)) {
                fileList.add(virtualFile);
            }
            return true;
        });
        return fileList;
    }

    @Override
    public LineMarkerInfo<?> getLineMarkerInfo (@NotNull PsiElement element) {
        var supportedElementTypes = List.of(
                DteTypes.THREAD_CREATE,
                DteTypes.THREAD_COMPLETE,
                DteTypes.MTX_UNLOCK,
                DteTypes.MTX_LOCK
        );

        if (!supportedElementTypes.contains(element.getNode().getElementType())) {
            return null;
        }

        return new LineMarkerInfo<>(
                element,
                element.getTextRange(),
                DteIcons.syncAction,
                (PsiElement elt) -> "Go to corresponding C++ code",
                (MouseEvent mouseEvent, PsiElement elt) -> {
                    String dteFilenameWithExtension = elt.getContainingFile().getName();
                    String dteFilename = dteFilenameWithExtension.substring(0, dteFilenameWithExtension.length() - 4);
                    String jsonFileWithSnapshots = dteFilename + "_stack_snapshots.json";
                    // TODO: extract needed file from json file with stack snapshots instead of hardcoding it
                    // TODO: add several levels of stack, add popup menu for greater effect
                    var file  = findFileByRelativePath(element.getProject(), "src/main.cpp").get(0);
                    var descriptor = new OpenFileDescriptor(element.getProject(), file, 1, 0);
                    descriptor.navigate(true);
                },
                GutterIconRenderer.Alignment.LEFT,
                () -> "Go to corresponding C++ code"
        );
    }
}

