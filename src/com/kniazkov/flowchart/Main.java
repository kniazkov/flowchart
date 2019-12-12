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

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Not enough arguments; syntax: java -jar flowchart.jar <source.txt> <destination.txt>");
            return;
        }

        String source = IO.readFileToString(args[0]);
        if (source == null)
            return;
        Queue<String> lines = new LinkedList<String>(Arrays.asList(source.split("\n")));
        try {
            Sequence result = Parser.parse(lines);
            if (result == null) {
                System.err.println("No one statement.");
                return;
            }
            StringBuilder builder = new StringBuilder();
            builder.append("digraph flowchart {\n  splines=polyline;\n");
            result.head.buildNodeDeclaration(builder, new HashSet<>());
            result.head.buildEdges(builder, new HashSet<>());
            builder.append("}");
            String graph = builder.toString();
            //System.out.println(graph);
            IO.writeStringToFile(args[1], graph);
        } catch (ParserError parserError) {
            parserError.printStackTrace();
        }

        System.out.println("done.");
    }
}
