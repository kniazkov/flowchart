# Flowchart generator

The flowchart generator parses algorithm description (BASIC-like syntax) and generates output in the DOT format
(it can be transformed into an image using the Graphviz tool).

Example:
~~~
BEGIN
INPUT read m
INPUT read n
IF n > 0 or m > 0  ' check correctness
    :label
    IF m = n
        OUTPUT write m
        END
    ELSE
        IF m > n
            m = m - n
        ELSE
            n = n - m
        END_IF
        GOTO label
    END_IF
ELSE
    ERROR incorrect values
END_IF
~~~
How to use:
~~~
java -jar flowchart.jar <source.txt> <destination.txt>
~~~