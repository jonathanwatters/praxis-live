/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2020 Neil C Smith.
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

import java.util.List;
import java.util.Map;
import javax.swing.DefaultListModel;
import org.praxislive.ide.project.api.ExecutionLevel;
import org.praxislive.ide.project.api.PraxisProject;
import org.openide.awt.HtmlRenderer;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.praxislive.ide.project.api.ExecutionElement;

/**
 *
 */
class WarningsDialogPanel extends javax.swing.JPanel {

    private final PraxisProject project;
    private final ExecutionLevel level;

    /**
     * Creates new form WarningsDialogPanel
     */
    WarningsDialogPanel(PraxisProject project,
            Map<ExecutionElement, List<String>> warnings,
            ExecutionLevel level) {
        this.project = project;
        this.level = level;
        initComponents();
        initListModel(warnings);
        warningsList.setCellRenderer(HtmlRenderer.createRenderer());
        initLabel(warningsList.getModel().getSize());
    }

    private void initListModel(Map<ExecutionElement, List<String>> warnings) {
        DefaultListModel model = new DefaultListModel();
        for (Map.Entry<ExecutionElement, List<String>> entry : warnings.entrySet()) {
            var element = entry.getKey();
            if (element instanceof ExecutionElement.File) {
                var file = ((ExecutionElement.File) element).file();
                var filename = FileUtil.getRelativePath(project.getProjectDirectory(), file);
                if (filename == null) {
                    filename = file.getPath();
                }
                model.addElement("<html><b>" + filename + "</b>");
            } else {
                model.addElement("<html><b>" + project.getProjectDirectory().getName() + "</b>");
            }

            for (String warning : entry.getValue()) {
                model.addElement(warning);
            }
        }
        warningsList.setModel(model);
    }

    private void initLabel(int warningsCount) {
        StringBuilder sb = new StringBuilder();
        sb.append("Project ");
        if (level == ExecutionLevel.BUILD) {
            sb.append("build");
        } else {
            sb.append("run");
        }
        sb.append(" completed with ");
        if (warningsCount == 1) {
            sb.append("1 warning.");
        } else {
            sb.append(warningsCount);
            sb.append(" warnings.");
        }
        label.setText(sb.toString());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        label = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        warningsList = new javax.swing.JList();

        setPreferredSize(new java.awt.Dimension(500, 350));

        label.setText("TEMP TEXT"); // NOI18N

        warningsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        warningsList.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane1.setViewportView(warningsList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
                    .addComponent(label))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel label;
    private javax.swing.JList warningsList;
    // End of variables declaration//GEN-END:variables
}
