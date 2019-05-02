package website.cousinomath

fun main(args: Array<String>) {
  var continueLoop = true
  var memory = HashMap<String, Double>()
  var inputs : List<String> = listOf("1 + 2 * 3","(1 + 2) * 3", "4 * atan 1", "pie = 4 * atan 1", "pi - pie", "pie = 4 * atan 1", "2^2^3", "2^-2^2", "2^2^-2", "2^-2^-2")
  // while (continueLoop) {
  for (input in inputs) {
    println("> $input")
    // print("> ")
    // val input = readLine()
    if (input == null) {
      continueLoop = false
    } else {
      val lexResult: Result<List<Token>, String> = Scanner(input).lex()
      if (lexResult.isErr) {
        println(lexResult.err!!)
        continue
      }
      val tokens = lexResult.ok!!
      tokens.forEach { it -> print("$it") }
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