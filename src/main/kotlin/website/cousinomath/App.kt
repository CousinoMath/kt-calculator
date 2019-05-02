package website.cousinomath

fun main(args: Array<String>) {
  var continueLoop = true
  var memory = HashMap<String, Double>()
  while (continueLoop) {
    print("> ")
    val input = readLine()
    if (input == null) {
      continueLoop = false
    } else {
      val lexResult: Result<List<Token>, String> = Scanner(input).lex()
      if (lexResult.isErr) {
        println(lexResult.err!!)
        continue
      }
      val tokens = result.ok!!
      tokens.forEach { print("$it") }
      println("")
      val parserResult: Result<ASTNode, String> = Parser(tokens).parse()
      if (parserResult.isErr) {
        println(parserResult.err!!)
        continue
      }
      val node = parserResult.ok!!
      println("$node")
      val value = node.evaluate(memory)
      println("$value")
    }
  }
}