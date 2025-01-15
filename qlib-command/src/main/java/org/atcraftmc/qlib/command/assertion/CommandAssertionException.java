package org.atcraftmc.qlib.command.assertion;

import org.atcraftmc.qlib.command.execute.CommandErrorType;

public final class CommandAssertionException extends RuntimeException {
    private final CommandErrorType code;
    private final Object[] info;

    public CommandAssertionException(CommandErrorType code, Object... info) {
        this.code = code;
        this.info = info;
    }

    public CommandAssertionException(CommandErrorType code) {
        this.info = new Object[0];
        this.code = code;
    }

    public Object[] getInfo() {
        return info;
    }

    public CommandErrorType getCode() {
        return code;
    }
}
