package website.cousinomath

enum class TokenType {
    EQUALS,
    PLUS,
    DASH,
    STAR,
    SLASH,
    CARET,
    LPAREN,
    RPAREN,
    NUMBER,
    FUNCTION,
    VARIABLE,
    CONSTANT,
    EOI,
}

data class Token(val type: TokenType, val lexeme: String, val token: Double? = null)