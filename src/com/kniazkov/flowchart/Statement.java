/*

Copyright (C) 19 Ivan Kniazkov

This file is part of flowchart generator.

Flowchart generator is free software: you can redistribute it and/or
modify it under the terms of the GNU General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Flowchart generator is distributed in the hope that it will be
useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License along
with Flowchart generator.  If not, see <http://www.gnu.org/licenses/>.

*/

package com.kniazkov.flowchart;

import java.util.ArrayList;
import java.util.Set;

public abstract class Statement {
    private static int uid = 0;
    private int id;
    public ArrayList<Statement> predecessors;
    public Comment comment;

    public Statement() {
        id = uid++;
        predecessors = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getNodeName() {
        return "node_" + id;
    }

    protected static String escapeHtmlEntities(String str) {
        StringBuilder result = new StringBuilder();
        for (int i = 0, len = str.length(); i < len; i++) {
            char ch = str.charAt(i);
            switch(ch) {
                case '<':
                    result.append("&lt;");
                    break;
                case '>':
                    result.append("&gt;");
                    break;
                case '\'':
                    result.append("&apos;");
                    break;
                case '\"':
                    result.append("&quot;");
                    break;
                case '&':
                    result.append("&amp;");
                    break;
                default:
                    result.append(ch);
            }
        }
        return result.toString();
    }

    public void buildNodeDeclaration(StringBuilder builder, Set<Statement> processed) {
        if (processed.contains(this))
            return;
        processed.add(this);
        builder.append("  ")
                .append(getNodeName())
                .append(" [label=<")
                .append(escapeHtmlEntities(getLabel()))
                .append("> shape=")
                .append(getShape())
                .append(" style=")
                .append(getStyle())
                .append("];\n");
        for (Statement successor : getSuccessors()) {
            successor.buildNodeDeclaration(builder, processed);
        }
        if (comment != null) {
            comment.buildNodeDeclaration(builder, processed);
        }
    }

    public void buildEdges(StringBuilder builder, Set<Statement> processed) {
        if (processed.contains(this))
            return;
        processed.add(this);
        buildEdges(builder);
        for (Statement successor : getSuccessors()) {
            successor.buildEdges(builder, processed);
        }
        if (comment != null) {
            builder.append("  ")
                    .append(comment.getNodeName())
                    .append(" -> ")
                    .append(getNodeName())
                    .append(" [style=\"dashed\"];\n");
            builder.append("  { rank=same; ")
                    .append(comment.getNodeName())
                    .append("; ")
                    .append(getNodeName())
                    .append(" };\n");
        }
    }

    protected void buildEdge(StringBuilder builder, Statement successor) {
        if (successor instanceof Goto) {
            Goto statement = (Goto)successor;
            if (statement.point != null)
                successor = statement.point;
        }
        builder.append("  ")
                .append(getNodeName())
                .append(" -> ")
                .append(successor.getNodeName())
                .append("\n");
    }

    protected void buildEdge(StringBuilder builder, Statement successor, String label) {
        if (successor instanceof Goto) {
            Goto statement = (Goto)successor;
            if (statement.point != null)
                successor = statement.point;
        }
        builder.append("  ")
                .append(getNodeName())
                .append(" -> ")
                .append(successor.getNodeName())
                .append(" [label=< ")
                .append(escapeHtmlEntities(label))
                .append(">]\n");
    }

    public String getStyle()
    {
        return "solid";
    }

    public abstract void setSuccessor(Statement statement);
    public abstract Statement[] getSuccessors();
    public abstract String getShape();
    public abstract String getLabel();
    public abstract void buildEdges(StringBuilder builder);
}
