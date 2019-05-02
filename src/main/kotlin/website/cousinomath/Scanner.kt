package website.cousinomath

class Scanner(val source: String) {
  val length = this.source.length
  var tokens: MutableList<Token> = ArrayList<Token>()
  var start = 0
  var current = 0
  val constants = setOf("e", "pi")
  val functions = setOf("acos", "asin", "atan", "cos", "exp", "log", "sin", "tan")

  fun lex(): Result<List<Token>, String> {
    if (current == 0) {
      while (current < length) {
        skipWhitespace()
        if (current >= length) { break; }
        start = current
        advance()
        val result = lexToken()
        if (result.isErr) {
          return Err(result.err!!);
        }
        tokens.add(result.ok!!)
      }
      tokens.add(Token(TokenType.EOI, "♠"))
    }
    return Ok<List<Token>, String>(tokens)
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

  private fun lexToken(): Result<Token, String> {
    return when (source[start]) {
      '+' -> Ok<Token, String>(Token(TokenType.PLUS, substring()))
      '-' -> Ok<Token, String>(Token(TokenType.DASH, substring()))
      '*' -> Ok<Token, String>(Token(TokenType.STAR, substring()))
      '/' -> Ok<Token, String>(Token(TokenType.SLASH, substring()))
      '=' -> Ok<Token, String>(Token(TokenType.EQUALS, substring()))
      '(' -> Ok(Token(TokenType.LPAREN, substring()))
      ')' -> Ok(Token(TokenType.RPAREN, substring()))
      in '0'..'9', '.' -> lexNumber()
      in 'a'..'z', in 'A'..'Z' -> lexIdentifier()
      else -> Err<Token, String>("Unrecognized token ${source[start]}.")
    }
  }

  private fun lexNumber(): Result<Token, String> {
    while (current < length && (source[current] in '0'..'9' || source[current] == '.')) {
      advance()
    }
    val substr = substring()
    try {
      return Ok(Token(TokenType.NUMBER, substr, java.lang.Double.parseDouble(substr)))
    } catch(nfe: NumberFormatException) {
      return Err(nfe.getLocalizedMessage())
    }
  }

  private fun lexIdentifier(): Result<Token, String> {
    while (current < length && (source[current] in 'a'..'z' || source[current] in 'A'..'Z')) {
      advance()
    }
    val substr = substring()
    return when (substr) {
      in constants -> Ok<Token, String>(Token(TokenType.CONSTANT, substr))
      in functions -> Ok<Token, String>(Token(TokenType.FUNCTION, substr))
      else -> Ok<Token, String>(Token(TokenType.VARIABLE, substr))
    }
  }
}