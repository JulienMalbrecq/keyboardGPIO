package be.malbrecq.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CommandLauncher {
    public static List<String> Command(String commandString) {
        return CommandLauncher.Command(commandString, true);
    }

    public static List<String> Command(String command, Boolean mustWait) {
        List<String> output = new ArrayList<String>();
        Process p;

        try {

            p = Runtime.getRuntime().exec(command);
            if (mustWait) {
                p.waitFor();

                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = reader.readLine())!= null) {
                    output.add(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return output;
    }
}
