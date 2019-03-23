package core.modules.tracer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Arthur Kupriyanov
 */
public class CLIInterpretator {
    public static String interpret(String original){

        if (original.matches(".*@[0-9a-fA-F]{3}->.*;.*")) {
            String[] strings = original.split("@");
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (String sample: strings) {
                if (sample.trim().equals("")){
                    first = false;
                    continue;
                }
                boolean added = false;
                Pattern p = Pattern.compile("[0-9a-fA-F]{3}->.*;");
                Matcher m = p.matcher(sample);

                while (m.find()) {
                    sb.append(replaceSub(sample, compileToValuesInserting(m.group()),m.start(), m.end()));
                    added = true;
                }
                if (!added){
                    if (first) sb.append(sample);
                    else sb.append("@").append(sample);
                }
                first = false;
            }
            original = sb.toString();
        }
        System.out.println("origin " + original);
        if (original.matches(".*@c[*][0-9]{1,3}.*")){
            String[] strings = original.split("@");
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (String sample: strings) {
                if (sample.trim().equals("")){
                    first = false;
                    continue;
                }
                boolean added = false;
                Pattern p = Pattern.compile("c[*][0-9]{1,3}");
                Matcher m = p.matcher(sample);

                while (m.find()) {
                    added = true;
                    System.out.println(m.start() + " " + m.end());
                    sb.append(replaceSub(sample, compileToContinueCmd(m.group()),m.start(), m.end()));
                }

                if (!added){
                    if (first) sb.append(sample);
                    else sb.append("@").append(sample);
                }
                first = false;
            }
            original = sb.toString();
        }
        return original;
    }

    /**
     * "144->1111 ,2222, 3333, 4444   ;" ->
     * 144 a 1111 w 2222 w 3333 w 4444 w
     * @param specCommand addr-> addr1, addr2;
     * @return addr a addr1 w addr2 w
     */
    private static String compileToValuesInserting(String specCommand){
        String cmd = "";
        specCommand = specCommand.substring(0, specCommand.length() -1);
        String[] commandParts = specCommand.split("->");
        String startMemoryAddr = commandParts[0];
        String[] values = commandParts[1].split(",");

        cmd += startMemoryAddr + " a ";
        for(String addr : values){
            cmd += addr.trim() + " w ";
        }

        return cmd.trim();
    }
    private static String compileToContinueCmd(String specCommand){
        String cmd = "";
        String[] commandParts = specCommand.split("[*]");
        int values = Integer.valueOf(commandParts[1]);
        for (int i = 0; i < values; i++){
            cmd += " c";
        }
        return cmd.trim();
    }

    private static String replaceSub(String str, String newSub, int startPos, int endPos){
        String firstPart = str.substring(0, startPos);
        String lastPart = str.substring(endPos);
        return firstPart+newSub+lastPart;
    }
    public static void main(String[] args) {
        System.out.println(interpret("@412->FF22,F045,E423; a s @c*12"));
//        String s = "144->1111 ,2222, 3333, 4444   ;";
//        System.out.println(compileToValuesInserting(s.substring(0, s.length()-1)));
    }
}
