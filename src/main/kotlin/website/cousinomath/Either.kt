package website.cousinomath

sealed class Either<R, S> {
  abstract val isLeft: Boolean
  abstract val isRight: Boolean
  abstract val left: R?
  abstract val right: S?
  abstract fun <U> mapLeft(f: (R) -> U): Either<U, S>;
  abstract fun <V> mapRight(g: (S) -> V): Either<R, V>;
  abstract fun <U> flatMapLeft(f: (R) -> Either<U, S>): Either<U, S>;
  abstract fun <V> flatMapRight(g: (S) -> Either<R, V>): Either<R, V>;
  abstract fun <T> either(f: (R) -> T, g: (S) -> T): T;
}

data class Left<R, S>(private val lvalue: R): Either<R, S>() {
  override val isLeft = true
  override val isRight = false
  override val left = lvalue
  override val right = null
  override fun <U> mapLeft(f: (R) -> U): Either<U, S> = Left(f(lvalue))
  override fun <V> mapRight(g: (S) -> V): Either<R, V> = Left(lvalue)
  override fun <U> flatMapLeft(f: (R) -> Either<U, S>): Either<U, S> = f(lvalue)
  override fun <V> flatMapRight(g: (S) -> Either<R, V>): Either<R, V> = Left(lvalue)
  override fun <T> either(f: (R) -> T, g: (S) -> T): T = f(lvalue)
}

data class Right<R, S>(private val rvalue: S): Either<R, S>() {
  override val isLeft = false
  override val isRight = true
  override val left = null
  override val right = rvalue
  override fun <U> mapLeft(f: (R) -> U): Either<U, S> = Right(rvalue)
  override fun <V> mapRight(g: (S) -> V): Either<R, V> = Right(g(rvalue))
  override fun <U> flatMapLeft(f: (R) -> Either<U, S>): Either<U, S> = Right(rvalue)
  override fun <V> flatMapRight(g: (S) -> Either<R, V>): Either<R, V> = g(rvalue)
  override fun <T> either(f: (R) -> T, g: (S) -> T): T = g(rvalue)
}

fun <R, S> Collection<Either<R, S>>.lefts(): Collection<R> = this.filter { it.isLeft }.map { it.left!! }
fun <R, S> Collection<Either<R, S>>.rights(): Collection<S> = this.filter { it.isRight }.map { it.right!! }
fun <R, S> Collection<Either<R, S>>.partition(): Pair<Collection<R>, Collection<S>> {
  val (lefts, rights) = this.partition { it.isLeft }
  return Pair(lefts.map { it.left!! }, rights.map { it.right!! })
}
