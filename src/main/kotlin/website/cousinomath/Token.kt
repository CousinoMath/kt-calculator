package website.cousinomath

enum class TokenType {
    EQUALS,
    PLUS,
    DASH,
    STAR,
    SLASH,
    CARET,
    NUMBER,
    FUNCTION,
    VARIABLE,
    CONSTANT,
    EOI,
}

data class Token(val type: TokenType, val lexeme: String, val token: Any? = null)