abstract class Result<R, S> {
    abstract fun err(): S?;
    abstract fun expected(msg: String): R;
    abstract fun ok(): R?;
    abstract fun isOk(): Boolean;
    abstract fun isErr(): Boolean;
    abstract fun <U> map(f: (x: R) -> U): Result<U, S>;
    abstract fun <U> mapOrElse(f: (x: R) -> U, g: (y: S) -> U): U;
    abstract fun unwrap(): R;
    abstract fun unwrapErr(): S;
    abstract fun unwrapOr(default: R): R;
    abstract fun unwrapOrElse(g: (y: S) -> R): R;
}

class Ok<R, S>(val okValue: R): Result<R, S>() {
    override fun err(): S? = null
    override fun expected(msg: String) = okValue
    override fun ok(): R? = okValue
    override fun isOk() = true
    override fun isErr() = false
    override fun <U> map(f: (x: R) -> U): Result<U, S> = Ok(f(okValue))
    override fun <U> mapOrElse(f: (x: R) -> U, g: (y: S) -> U): U = f(okValue)
    override fun unwrap() = okValue
    override fun unwrapErr(): S {
        throw UnsupportedOperationException("Cannot unwrap an Err value from Ok.")
    }
    override fun unwrapOr(default: R) = okValue
    override fun unwrapOrElse(g: (y: S) -> R) = okValue
}

class Err<R, S>(val errValue: S): Result<R, S>() {
    override fun err(): S? = errValue
    override fun expected(msg: String): R {
        throw UnsupportedOperationException("$msg ${errValue.toString()}")
    }
    override fun ok(): R? = null
    override fun isOk() = false
    override fun isErr() = true
    override fun <U> map(f: (x: R) -> U): Result<U, S> = Err(errValue)
    override fun <U> mapOrElse(f: (x: R) -> U, g: (y: S) -> U): U = g(errValue)
    override fun unwrap(): R {
        throw UnsupportedOperationException("Cannot unwrap an Ok value from Err.")
    }
    override fun unwrapErr(): S = errValue
    override fun unwrapOr(default: R) = default
    override fun unwrapOrElse(g: (y: S) -> R) = g(errValue)
}