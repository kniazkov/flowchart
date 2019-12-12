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

public class Condition extends Statement {
    public String condition;
    public Sequence sequenceTrue;
    public Sequence sequenceFalse;
    public Statement successor;

    public Condition(String condition) {
        this.condition = condition;
    }

    @Override
    public void setSuccessor(Statement statement) {
        successor = statement;
        sequenceTrue.tail.setSuccessor(statement);
        if (sequenceFalse != null)
            sequenceFalse.tail.setSuccessor(statement);
    }

    @Override
    public Statement[] getSuccessors() {
        if (sequenceFalse != null)
            return new Statement[]{ sequenceTrue.head, sequenceFalse.head };
        if (successor != null)
            return new Statement[]{ sequenceTrue.head, successor };
        return new Statement[]{ sequenceTrue.head };
    }

    @Override
    public String getShape() {
        return "diamond";
    }

    @Override
    public String getLabel() {
        return condition + " ?";
    }

    @Override
    public void buildEdges(StringBuilder builder) {
        buildEdge(builder, sequenceTrue.head, "yes");
        if (sequenceFalse != null)
            buildEdge(builder, sequenceFalse.head, "no");
        else if (successor != null)
            buildEdge(builder, successor, "no");
    }
}
