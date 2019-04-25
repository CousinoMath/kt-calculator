enum class TokenType {
    EQUALS,
    PLUS,
    MINUS,
    STAR,
    SLASH,
    CARET,
    NUMBER,
    FUNCTION,
    VARIABLE,
    CONSTANT,
}

class Token(type: TokenType, token: String) {
    
}