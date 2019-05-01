package website.cousinomath

fun main(args: Array<String>) {
  var continueLoop = true
  while (continueLoop) {
    print("> ")
    val input = readLine()
    if (input == null) {
      continueLoop = false
    } else {
      val result: Result<List<Token>, String> = Scanner(input).lex()
      if (result.isErr) {
        println(result.err!!)
        continue
      }
      val tokens = result.ok!!
      tokens.forEach { print("$it") }
      println("")
    }
  }
}