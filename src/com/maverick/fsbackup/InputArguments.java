package com.maverick.fsbackup;

import com.beust.jcommander.Parameter;
import lombok.Getter;

import java.util.List;

@Getter
public class InputArguments {
    @Parameter(names = {"--src", "-s"}, description = "Root path of the source files.")
    private List<String> srcRoot;

    @Parameter(names = {"--dest", "-d"}, description = "Root path of the destinatino files.")
    private List<String> destRoot;

    @Parameter(names = {"--ts", "-t"}, description = "Last run timestamp location.")
    private List<String> tsPath;

    @Parameter(names = {"--connType", "-c"}, description = "Connection type: adb/fs")
    private String connType;

    @Parameter(names = {"--tc"}, description = "Last run timestamp location.")
    private  Integer threadCount = 6;
}
