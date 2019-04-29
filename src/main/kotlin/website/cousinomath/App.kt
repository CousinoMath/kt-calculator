package website.cousinomath

fun main(args: Array<String>) {
  var continueLoop = true
  while (continueLoop) {
    print("> ")
    val input = readLine()
    if (input == null) {
      continueLoop = false
    } else {
      val token: List<Token> = Scanner(input).lex()
      token.forEach { print("$it") }
      println("")
    }
  }
}