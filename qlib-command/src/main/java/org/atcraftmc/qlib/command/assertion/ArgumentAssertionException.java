package org.atcraftmc.qlib.command.assertion;

import org.atcraftmc.qlib.command.execute.CommandErrorType;

public final class ArgumentAssertionException extends RuntimeException {
    private final int position;
    private final CommandErrorType code;
    private final Object[] info;

    public ArgumentAssertionException(CommandErrorType code, int position, Object... info) {
        this.position = position;
        this.code = code;
        this.info = info;
    }

    public Object[] getInfo() {
        return info;
    }

    public CommandErrorType getCode() {
        return code;
    }

    public int getPosition() {
        return position;
    }
}
