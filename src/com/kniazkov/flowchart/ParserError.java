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

public class ParserError extends Exception {
    String line;
    String message;

    public ParserError(String message) {
        this.line = null;
        this.message = message;
    }

    public ParserError(String line, String message) {
        this.line = line;
        this.message = message;
    }

    public String toString() {
        if (line != null)
            return line + "\n" + message;
        return message;
    }
}
