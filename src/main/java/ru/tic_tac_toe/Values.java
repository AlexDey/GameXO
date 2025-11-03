package ru.tic_tac_toe;

enum Values {
    CROSS("\u274C", "\u001B[31m X \u001B[0m"),
    CIRCLE("\uD83D\uDFE2", "\u001B[32m O \u001B[0m"),
    ONE("\u0031\uFE0F\u20E3", " 1 "),
    TWO("\u0032\uFE0F\u20E3", " 2 "),
    THREE("\u0033\uFE0F\u20E3", " 3 "),
    FOUR("\u0034\uFE0F\u20E3", " 4 "),
    FIVE("\u0035\uFE0F\u20E3", " 5 "),
    SIX("\u0036\uFE0F\u20E3", " 6 "),
    SEVEN("\u0037\uFE0F\u20E3", " 7 "),
    EIGHT("\u0038\uFE0F\u20E3", " 8 "),
    NINE("\u0039\uFE0F\u20E3", " 9 "),
    LINE("-----------", "-------------");

    private final String symbol;
    private final String symbolServer;

    Values(String symbol,String symbolServer) {
        this.symbol = symbol;
        this.symbolServer = symbolServer;
    }

    public String getString(boolean server) {
        return server ? symbolServer: symbol;
    }
}