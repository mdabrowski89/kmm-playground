package pl.mobite.playground.utils

inline fun <A1, A2> A1.with(
    a2: A2,
    expression: A2.() -> A1.() -> Unit
) {
    expression(a2)(this)
}