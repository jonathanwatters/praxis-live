/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2019 Neil C Smith.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details.
 *
 * You should have received a copy of the GNU General Public License version 3
 * along with this work; if not, see http://www.gnu.org/licenses/
 *
 *
 * Please visit http://neilcsmith.net if you need additional information or
 * have any questions.
 */
package org.praxislive.ide.project.ui;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.praxislive.ide.project.DefaultPraxisProject;
import org.praxislive.ide.project.ProjectPropertiesImpl;
import org.praxislive.ide.project.api.ExecutionLevel;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.spi.project.ui.CustomizerProvider;
import org.netbeans.spi.project.ui.support.ProjectCustomizer;
import org.netbeans.spi.project.ui.support.ProjectCustomizer.Category;

/**
 *
 * @author Neil C Smith (http://neilcsmith.net)
 */
public class PraxisCustomizerProvider implements CustomizerProvider,
        ProjectCustomizer.CategoryComponentProvider {

    private final Category build;
    private final Category run;
    private final Category libraries;
    private final Category java;
    private final DefaultPraxisProject project;

    private FilesCustomizer buildFiles;
    private FilesCustomizer runFiles;
    private LibrariesCustomizer librariesCustomizer;
    private JavaCustomizer javaCustomizer;

    public PraxisCustomizerProvider(DefaultPraxisProject project) {
        this.project = project;
        build = Category.create(
                "build",
                "Build Level Files",
                null);
        run = Category.create(
                "run",
                "Run Level Files",
                null);
        libraries = Category.create(
                "libraries",
                "Libraries",
                null);
        java = Category.create(
                "java",
                "Java",
                null);
    }

    @Override
    public void showCustomizer() {
        Category[] categories = new Category[]{build, run, libraries, java};
        if (buildFiles != null) {
            buildFiles.refreshList();
        }
        if (runFiles != null) {
            runFiles.refreshList();
        }
        if (javaCustomizer != null) {
            javaCustomizer.refresh();
        }
        Dialog dialog = ProjectCustomizer.createCustomizerDialog(categories, this,
                null, new OKButtonListener(), null);
        dialog.setTitle(ProjectUtils.getInformation(project).getDisplayName());
        dialog.setVisible(true);
    }

    @Override
    public JComponent create(Category category) {
        if (build.equals(category)) {
            if (buildFiles == null) {
                buildFiles = new FilesCustomizer(project, ExecutionLevel.BUILD);
            } else {
                buildFiles.refreshList();
            }
            return buildFiles;
        } else if (run.equals(category)) {
            if (runFiles == null) {
                runFiles = new FilesCustomizer(project, ExecutionLevel.RUN);
            } else {
                runFiles.refreshList();
            }
            return runFiles;
        } else if (libraries.equals(category)) {
            if (librariesCustomizer == null) {
                librariesCustomizer = new LibrariesCustomizer(project);
            }
            return librariesCustomizer;
        } else if (java.equals(category)) {
            if (javaCustomizer == null) {
                javaCustomizer = new JavaCustomizer(project);
            } else {
                javaCustomizer.refresh();
            }
            return javaCustomizer;
        } else {
            return new JPanel();
        }
    }

    private class OKButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            ProjectPropertiesImpl props = project.getLookup().lookup(ProjectPropertiesImpl.class);
            if (props == null) {
                return;
            }
            if (buildFiles != null) {
                props.setProjectFiles(ExecutionLevel.BUILD, buildFiles.getFiles());
            }
            if (runFiles != null) {
                props.setProjectFiles(ExecutionLevel.RUN, runFiles.getFiles());
            }
            if (javaCustomizer != null) {
                javaCustomizer.updateProject();
            }
//            ProjectState state = project.getLookup().lookup(ProjectState.class);
//            if (state != null) {
//                state.markModified();
//            }
        }

    }

}
