package website.cousinomath

class Scanner(val source: String) {
  val length = this.source.length
  var tokens: MutableList<Token> = ArrayList<Token>()
  var start = 0
  var current = 0
  val constants = setOf("e", "pi")
  val functions = setOf("acos", "asin", "atan", "cos", "exp", "log", "sin", "tan")

  fun lex(): List<Token> {
    if (current == 0) {
      while (current < length) {
        skipWhitespace()
        start = current
        advance()
        tokens.add(lexToken())
      }
      tokens.add(Token(TokenType.EOI, "♠"))
    }
    return tokens
  }

  private fun advance() {
    if (current < length) {
      current += 1
    }
  }

  private fun skipWhitespace() {
    while (current < length && source[current] == ' ') {
      advance()
    }
  }

  private fun substring(): String = source.substring(start, current)

  private fun lexToken(): Token {
    return when (source[start]) {
      '+' -> Token(TokenType.PLUS, substring())
      '-' -> Token(TokenType.DASH, substring())
      '*' -> Token(TokenType.STAR, substring())
      '/' -> Token(TokenType.SLASH, substring())
      '=' -> Token(TokenType.EQUALS, substring())
      in '0'..'9', '.' -> lexNumber()
      in 'a'..'z', in 'A'..'Z' -> lexIdentifier()
      else -> throw UnsupportedOperationException("Unrecognized token ${source[start]}.")
    }
  }

  private fun lexNumber(): Token {
    while (current < length && (source[current] in '0'..'9' || source[current] == '.')) {
      advance()
    }
    val substr = substring()
    return Token(TokenType.NUMBER, substr, java.lang.Double.parseDouble(substr))
  }

  private fun lexIdentifier(): Token {
    while (current < length && (source[current] in 'a'..'z' || source[current] in 'A'..'Z')) {
      advance()
    }
    val substr = substring()
    return when (substr) {
      in constants -> Token(TokenType.CONSTANT, substr)
      in functions -> Token(TokenType.FUNCTION, substr)
      else -> Token(TokenType.VARIABLE, substr)
    }
  }
}