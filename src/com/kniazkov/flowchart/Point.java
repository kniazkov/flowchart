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

public class Point extends Statement {
    public String name;
    public Statement successor;

    public Point(String name) {
        this.name = name;
    }

    @Override
    public void setSuccessor(Statement statement) {
        this.successor = statement;
        statement.predecessors.add(this);
    }

    @Override
    public Statement[] getSuccessors() {
        return successor != null ? new Statement[]{ successor } : new Statement[0];
    }

    @Override
    public String getShape() {
        return "box";
    }

    @Override
    public String getStyle() {
        return "rounded";
    }

    @Override
    public String getLabel() {
        return name;
    }

    @Override
    public void buildEdges(StringBuilder builder) {
        if (successor != null)
            buildEdge(builder, successor);
    }
}
