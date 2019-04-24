package website.cousinomath;

abstract class Either<R, S> {
  abstract fun isLeft(): Boolean;
  abstract fun isRight(): Boolean;
  abstract fun getLeft(): R;
  abstract fun getRight(): S;
  abstract fun <U> mapLeft(f: (x: R) -> U): Either<U, S>;
  abstract fun <V> mapRight(g: (y: S) -> V): Either<R, V>;
  abstract fun <U> flatMapLeft(f: (x: R) -> Either<U, S>): Either<U, S>;
  abstract fun <V> flatMapRight(g: (y: S) -> Either<R, V>): Either<R, V>;
}

class Left<R, S>(private val lvalue: R): Either<R, S>() {
  override fun isLeft() = true
  override fun isRight() = false
  override fun getLeft() = this.lvalue
  override fun getRight() = throw UnsupportedOperationException("Cannot get a right value out of Left.")
  override fun <U> mapLeft(f: (x: R) -> U): Either<U, S> = Left(f(this.lvalue))
  override fun <V> mapRight(g: (y: S) -> V): Either<R, V> = Left(this.lvalue)
  override fun <U> flatMapLeft(f: (x: R) -> Either<U, S>): Either<U, S> = f(this.lvalue)
  override fun <V> flatMapRight(g: (y: S) -> Either<R, V>): Either<R, V> = Left(this.lvalue)
}

class Right<R, S>(private val rvalue: S): Either<R, S>() {
  override fun isLeft() = false
  override fun isRight() = true
  override fun getLeft() = throw UnsupportedOperationException("Cannot get a left value out of Right.")
  override fun getRight() = this.rvalue
  override fun <U> mapLeft(f: (x: R) -> U): Either<U, S> = Right(this.rvalue)
  override fun <V> mapRight(g: (y: S) -> V): Either<R, V> = Right(g(this.rvalue))
  override fun <U> flatMapLeft(f: (x: R) -> Either<U, S>): Either<U, S> = Right(this.rvalue)
  override fun <V> flatMapRight(g: (y: S) -> Either<R, V>): Either<R, V> = g(this.rvalue)
}

fun <R, S, T> either(xe: Either<R, S>, f: (x: R) -> T, g: (y: S) -> T): T =
  if (xe.isLeft()) f(xe.getLeft()) else g(xe.getRight())
