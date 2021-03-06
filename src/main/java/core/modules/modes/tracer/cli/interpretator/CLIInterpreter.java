package core.modules.modes.tracer.cli.interpretator;

import core.common.DoubleTuple;

/**
 * @author Arthur Kupriyanov
 */
public class CLIInterpreter {
    public static DoubleTuple<String, String> interpret(String context){
        DoubleTuple<String, String> result = new DoubleTuple<>(context, "");
        for (CLIExpression exp : InterpretManager.getInterpreters()){
            result = exp.interpret(result.first, result);
        }

        return result;
    }
}
