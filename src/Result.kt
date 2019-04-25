abstract class Result<R, S> {
    abstract val isOk: Boolean;
    abstract val isErr: Boolean;
    abstract val ok: R?;
    abstract val err: S?;
    abstract fun expected(msg: String): R;
    abstract fun <U> map(f: (x: R) -> U): Result<U, S>;
    abstract fun <U> mapOrElse(f: (x: R) -> U, g: (y: S) -> U): U;
    abstract fun okOr(default: R): R;
    abstract fun okOrElse(g: (y: S) -> R): R;
}

class Ok<R, S>(val okValue: R): Result<R, S>() {
    override val err(): S? = null
    override val ok(): R? = okValue
    override val isOk() = true
    override val isErr() = false
    override fun expected(msg: String) = okValue
    override fun <U> map(f: (R) -> U): Result<U, S> = Ok(f(okValue))
    override fun <U> mapOrElse(f: (R) -> U, g: (S) -> U): U = f(okValue)
    override fun okOr(default: R) = okValue
    override fun okOrElse(g: (S) -> R) = okValue
}

class Err<R, S>(val errValue: S): Result<R, S>() {
    override val err: S? = errValue
    override val ok: R? = null
    override val isOk = false
    override val isErr = true
    override fun expected(msg: String): R {
        throw UnsupportedOperationException("$msg ${errValue.toString()}")
    }
    override fun <U> map(f: (R) -> U): Result<U, S> = Err(errValue)
    override fun <U> mapOrElse(f: (R) -> U, g: (S) -> U): U = g(errValue)
    override fun okOr(default: R) = default
    override fun okOrElse(g: (S) -> R) = g(errValue)
}