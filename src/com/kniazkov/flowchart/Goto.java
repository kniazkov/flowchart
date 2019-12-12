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

public class Goto extends Statement {
    public Point point;

    public Goto(Point point) {
        this.point = point;
    }

    @Override
    public void setSuccessor(Statement statement) {
    }

    @Override
    public Statement[] getSuccessors() {
        return point != null ? new Statement[]{ point } : new Statement[0];
    }

    @Override
    public String getShape() {
        return point != null ? "none" : "point";
    }

    @Override
    public String getLabel() {
        return "";
    }

    @Override
    public void buildEdges(StringBuilder builder) {
    }
}
