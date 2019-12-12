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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class IO {
    public static String readFileToString(String path) {
        File file = new File(path);
        if (file.exists()) {
            try {
                byte[] data = Files.readAllBytes(file.toPath());
                return new String(data);
            } catch (IOException e) {
                System.err.println("Can't read '" + path + '\'');
            }
        } else {
            System.err.println("Cannot find file specified: '" + path + "'\n");
        }
        return null;
    }

    public static boolean writeStringToFile(String path, String data) {
        try {
            FileWriter writer = new FileWriter(path);
            writer.write(data);
            writer.close();
            return true;
        } catch (IOException e) {
            System.err.println("Can't write '" + path + '\'');
        }
        return false;
    }
}
