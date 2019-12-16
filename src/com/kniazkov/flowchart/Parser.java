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

import java.util.*;

public class Parser {
    protected  enum Terminator
    {
        END_OF_FILE,
        ELSE,
        ELSE_END_IF,
        END_IF
    };

    public static Sequence parse(Queue<String> lines) throws ParserError {
        return parse(lines, null, new HashMap<>(), new HashMap<>());
    }

    protected  static Sequence parse(Queue<String> lines, Pointer<Terminator> terminator,
                                     Map<String, Point> pointByName,
                                     Map<String, List<Goto>> gotoByName) throws ParserError {
        Sequence result = null;
        Statement next = null;

        while (!lines.isEmpty()) {
            String line = lines.poll().trim();
            if (line.length() > 0) {
                String comment = null;
                String color = "black";
                int idx = line.indexOf('\'');
                if (idx >= 0) {
                    comment = line.substring(idx + 1).trim();
                    line = line.substring(0, idx).trim();
                }
                if (line.startsWith("#")) {
                    idx = line.indexOf(' ');
                    if (idx == -1)
                        throw new ParserError("Syntax error");
                    color = line.substring(1, idx);
                    line = line.substring(idx + 1).trim();
                }
                next = null;
                if (line.startsWith(":")) {
                    String name = line.substring(1).trim();
                    Point point = new Point(name);
                    pointByName.put(name, point);
                    if (gotoByName.containsKey(name)) {
                        List<Goto> list = gotoByName.get(name);
                        for (Goto statement : list) {
                            statement.point = point;
                        }
                    }
                    next = point;
                }
                else if (line.startsWith("GOTO ")) {
                    String name = line.substring(5).trim();
                    if (pointByName.containsKey(name)) {
                        next = new Goto(pointByName.get(name));
                    }
                    else {
                        Goto statement = new Goto(null);
                        next = statement;
                        if (gotoByName.containsKey(name)) {
                            gotoByName.get(name).add(statement);
                        }
                        else {
                            List<Goto> list = new ArrayList<>();
                            list.add(statement);
                            gotoByName.put(name, list);
                        }
                    }
                }
                else if (line.equals("BEGIN")) {
                    Begin statement = new Begin();
                    result = new Sequence(statement);
                }
                else if (line.equals("END")) {
                    next = new End();
                }
                else if (line.startsWith("INPUT ")) {
                    next = new InOut(line.substring(6).trim());
                }
                else if (line.startsWith("OUTPUT ")) {
                    next = new InOut(line.substring(7).trim());
                }
                else if (line.startsWith("ERROR ")) {
                    next = new Error(line.substring(6).trim());
                }
                else if (line.startsWith("IF ")) {
                    Condition statement = new Condition(line.substring(3).trim());
                    next = statement;
                    Pointer<Terminator> ifTerminator = new Pointer<>(Terminator.ELSE_END_IF);
                    statement.sequenceTrue = parse(lines, ifTerminator, pointByName, gotoByName);
                    if (ifTerminator.value == Terminator.END_OF_FILE)
                        throw new ParserError("Unexpected end of file");
                    if (ifTerminator.value == Terminator.ELSE)
                    {
                        ifTerminator.value = Terminator.END_IF;
                        statement.sequenceFalse = parse(lines, ifTerminator, pointByName, gotoByName);
                        if (ifTerminator.value == Terminator.END_OF_FILE)
                            throw new ParserError("Unexpected end of file");
                    }
                }
                else if (line.equals("ELSE")) {
                    if (terminator != null) {
                        if (terminator.value == Terminator.ELSE_END_IF) {
                            terminator.value = Terminator.ELSE;
                            return result;
                        }
                    }
                    throw new ParserError(line, "'ELSE' keyword is allowed only in 'IF' statement");
                }
                else if (line.equals("END_IF")) {
                    if (terminator != null) {
                        if (terminator.value == Terminator.ELSE_END_IF || terminator.value == Terminator.END_IF) {
                            terminator.value = Terminator.END_IF;
                            return result;
                        }
                    }
                    throw new ParserError(line, "'END_IF' without 'IF'");
                }
                else {
                    next = new Action(line);
                }
                if (next != null) {
                    if (comment != null && comment.length() > 0)
                        next.comment = new Comment(comment);
                    next.color = color;
                    if (result != null) {
                        result.tail.setSuccessor(next);
                        result.tail = next;
                    } else {
                        result = new Sequence(next);
                    }
                }
            }
        }
        if (terminator != null)
            terminator.value = Terminator.END_OF_FILE;
        return result;
    }
}
