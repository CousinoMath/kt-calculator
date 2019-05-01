package website.cousinomath

class Parser(val tokens: List<Token>) {
  val length: Int = tokens.size
  var current: Int = 0
  
  fun parse(): Result<ASTNode, String> {
    return assignment()
  }

  private fun peek(n: Int): Token {
    if (current + n < length) {
      return tokens[current + n]
    }
    return tokens[length - 1]
  }

  private fun advance() {
    if (current < length) {
      current += 1
    }
  }

  private fun assignment(): Result<ASTNode, String> {
    val curToken: Token = peek(0)
    if (curToken.type == TokenType.VARIABLE) {
      if (peek(1).type == TokenType.EQUALS) {
        val exprResult: Result<ASTNode, String> = expression();
        return exprResult.map { expr -> ASTAssign(curToken.lexeme, expr) }
      }
    }
    return expression();
  }

  private fun expression(): Result<ASTNode, String> {
    var factorResult: Result<ASTNode, String> = factor()
    if (factorResult.isErr) {
      return factorResult
    }
    val factors: ArrayList<ASTNode> = ArrayList()
    factors.add(factorResult.ok!!)
    var loop: Boolean = true
    while (loop && current < length) {
      val curToken: Token = peek(0)
      when (curToken.type) {
        TokenType.EOI, TokenType.RPAREN -> { loop = false }
        TokenType.PLUS -> {
          advance()
          factorResult = factor()
          if (factorResult.isErr) {
            return factorResult
          }
          factors.add(factorResult.ok!!)
        }
        TokenType.DASH -> {
          advance()
          factorResult = factor()
          if (factorResult.isErr) {
            return factorResult
          }
          factors.add(
            ASTTimes(listOf(ASTNumber(-1.0), factorResult.ok!!)))
        }
        else -> {
          advance()
          return Err("Expected a '+' or '-' but not ${curToken.lexeme}")
        }
      }
    }
    when (factors.size) {
      0 -> return Ok(ASTNumber(0.0))
      1 -> return Ok(factors.get(0))
      else -> return Ok(ASTPlus(factors))
    }
  }

  private fun factor(): Result<ASTNode, String> {
    var exponResult: Result<ASTNode, String> = exponential()
    val expons: ArrayList<ASTNode> = ArrayList()
    if (exponResult.isErr) {
      return exponResult
    }
    expons.add(exponResult.ok!!)
    var loop = true
    while (loop && current < length) {
      val curToken = peek(0)
      when (curToken.type) {
        TokenType.PLUS, TokenType.DASH, TokenType.RPAREN, TokenType.EOI -> {
          loop = false
        }
        TokenType.STAR -> {
          advance()
          exponResult = exponential()
          if (exponResult.isErr) {
            return exponResult
          }
          expons.add(exponResult.ok!!)
        }
        TokenType.SLASH -> {
          advance()
          exponResult = exponential()
          if (exponResult.isErr) {
            return exponResult
          }
          expons.add(ASTPower(exponResult.ok!!, ASTNumber(-1.0)))
        }
        else -> return Err("Expected '*' or '/' instead of ${curToken.lexeme}")
      }
    }
    when (expons.size) {
      0 -> return Ok(ASTNumber(1.0))
      1 -> return Ok(expons.get(0))
      else -> return Ok(ASTTimes(expons))
    }
  }

  private fun exponential(): Result<ASTNode, String> {
    var curToken: Token = peek(0)
    var negate: Boolean = false
    if (curToken.type == TokenType.DASH) {
      negate = true
    }
    var atomResult: Result<ASTNode, String> = atom()
    curToken = peek(0)
    if (curToken.type == TokenType.CARET) {
      val exponResult = exponential()
      atomResult = atomResult.flatMap { 
        atom -> exponResult.map { exp -> ASTPower(atom, exp) as ASTNode }
      }
    }
    if (negate) {
      atomResult = atomResult.map {
        atom -> ASTTimes(listOf(ASTNumber(-1.0), atom))
      }
    }
    return atomResult
  }

  private fun atom(): Result<ASTNode, String> {
    val curToken: Token = peek(0)
    when (curToken.type) {
      TokenType.NUMBER -> return Ok(ASTNumber(curToken.token!!))
      TokenType.VARIABLE -> return Ok(ASTVariable(curToken.lexeme))
      TokenType.FUNCTION -> {
        return atom().map { arg -> ASTFunction(curToken.lexeme, arg) }
      }
      TokenType.LPAREN -> {
        val exprResult = expression()
        if (peek(0).type == TokenType.RPAREN) {
          return Err("Unbalanced parentheses")
        }
        return exprResult
      }
      else -> {
        return Err("Expected a number, variable, or function instead of ${curToken.lexeme}")
      }
    }
  }
}