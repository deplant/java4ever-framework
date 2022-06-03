package tech.deplant.java4ever.framework.expr;

sealed interface Expr permits Expr.Add, Expr.Sub, Expr.Mul, Expr.Div, Expr.Const {

//TODO JDK19

//    static BigDecimal eval(Expr e) {
//        return switch (e) {
//            case Add(Expr l, Expr r) -> eval(l).add(eval(r));
//            case Const(String value) -> new BigDecimal(value);
//        }
//    }

    record Add(Expr left, Expr right) implements Expr {
    }

    record Sub(Expr left, Expr right) implements Expr {
    }

    record Mul(Expr left, Expr right) implements Expr {
    }

    record Div(Expr left, Expr right) implements Expr {
    }

    record Const(String value) implements Expr {
    }
}
