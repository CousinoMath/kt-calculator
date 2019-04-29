package website.cousinomath

sealed class ASTNode {
    abstract fun evaluate(memory: MutableMap<String, Double>): Double
}

data class ASTNumber(val value: Double): ASTNode() {
    override fun evaluate(memory: MutableMap<String, Double>) = value
}

data class ASTVariable(val name: String): ASTNode() {
    override fun evaluate(memory: MutableMap<String, Double>) =
        memory.get(name) ?: Double.NaN
}

data class ASTConstant(val name: String): ASTNode() {
    override fun evaluate(memory: MutableMap<String, Double>) =
        when (name) {
            "pi" -> Math.PI
            "e" -> Math.E
            else -> Double.NaN
        }
}

data class ASTFunction(val name: String, val argument: ASTNode): ASTNode() {
    override fun evaluate(memory: MutableMap<String, Double>): Double {
        val argEvaled = argument.evaluate(memory)
        return when (name) {
            "sin" -> Math.sin(argEvaled)
            "cos" -> Math.cos(argEvaled)
            "tan" -> Math.tan(argEvaled)
            "exp" -> Math.exp(argEvaled)
            "asin" -> Math.asin(argEvaled)
            "acos" -> Math.acos(argEvaled)
            "atan" -> Math.atan(argEvaled)
            "log" -> Math.log(argEvaled)
            else -> Double.NaN
        }
    }
}

data class ASTPlus(val arguments: List<ASTNode>): ASTNode() {
    override fun evaluate(memory: MutableMap<String, Double>) =
        arguments.map { it.evaluate(memory) }.sum()
}

data class ASTTimes(val arguments: List<ASTNode>): ASTNode() {
    override fun evaluate(memory: MutableMap<String, Double>) =
        arguments.map { it.evaluate(memory) }.fold(1.0, {
            product, factor -> product * factor
        })
}

data class ASTPower(val base: ASTNode, val exp: ASTNode): ASTNode() {
    override fun evaluate(memory: MutableMap<String, Double>) =
        Math.pow(base.evaluate(memory), exp.evaluate(memory))
}

data class ASTAssign(val name: String, val expr: ASTNode): ASTNode() {
    override fun evaluate(memory: MutableMap<String, Double>): Double {
        val value = expr.evaluate(memory)
        memory.set(name, value)
        return value
    }
}