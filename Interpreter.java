//> Evaluating Expressions interpreter-class
package Golang;
//> Statements and State import-list

//> Functions import-array-list
//< Functions import-array-list
//> Resolving and Binding import-hash-map
import java.util.HashMap;
//< Resolving and Binding import-hash-map
import java.util.List;
//< Statements and State import-list
//> Resolving and Binding import-map
import java.util.Map;
//< Resolving and Binding import-map

/* Evaluating Expressions interpreter-class < Statements and State interpreter
class Interpreter implements Expr.Visitor<Object> {
*/
//> Statements and State interpreter
class Interpreter implements Expr.Visitor<Object>,
        Stmt.Visitor<Void> {
    //< Statements and State interpreter
/* Statements and State environment-field < Functions global-environment
  private Environment environment = new Environment();
*/
//> Functions global-environment
    final Environment globals = new Environment();
    private Environment environment = globals;
    //< Functions global-environment
//> Resolving and Binding locals-field
    private final Map<Expr, Integer> locals = new HashMap<>();
//< Resolving and Binding locals-field
//> Statements and State environment-field

    //< Statements and State environment-field
//> Functions interpreter-constructor
    Interpreter() {
        
    }

    //< Functions interpreter-constructor
/* Evaluating Expressions interpret < Statements and State interpret
  void interpret(Expr expression) { // [void]
    try {
      Object value = evaluate(expression);
      System.out.println(stringify(value));
    } catch (RuntimeError error) {
      GolangruntimeError(error);
    }
  }
*/
//> Statements and State interpret
    void interpret(List<Stmt> statements) {
        try {
            for (Stmt statement : statements) {
                execute(statement);
            }
        } catch (RuntimeError error) {
            Main.runtimeError(error);
        }
    }
    //< Statements and State interpret
//> evaluate
    public Object evaluate(Expr expr) {
        return expr.accept(this);
    }
    //< evaluate
//> Statements and State execute
    private void execute(Stmt stmt) {
        stmt.accept(this);
    }
    //< Statements and State execute
//> Resolving and Binding resolve
    void resolve(Expr expr, int depth) {

        locals.put(expr, depth);
        //System.out.println(locals);System.out.println("   locals   ");
    }
    //< Resolving and Binding resolve
//> Statements and State execute-block
    void executeBlock(List<Stmt> statements,
                      Environment environment) {
        Environment previous = this.environment;
        try {
            this.environment = environment;

            for (Stmt statement : statements) {
                execute(statement);
            }
        } finally {
            this.environment = previous;
        }
    }
    void executeBlock2(List<Stmt> statements,
                      Environment environment) {
        Environment previous = this.environment;
        try {
//            this.environment = environment;

            for (Stmt statement : statements) {
                execute(statement);
            }
        } finally {
            this.environment = previous;
        }
    }
    //< Statements and State execute-block
//> Statements and State visit-block
    @Override
    public Void visitBlockStmt(Stmt.Block stmt) {
        executeBlock(stmt.statements, new Environment(environment));
        return null;
    }
    public Void visitBlockStmt2(Stmt.Block2 stmt) {
        executeBlock2(stmt.statements, new Environment(environment));
        return null;
    }
    //< Statements and State visit-block
//> Classes interpreter-visit-class
    @Override
    public Void visitClassStmt(Stmt.Class stmt) {

        return null;
    }
    //< Classes interpreter-visit-class
//> Statements and State visit-expression-stmt
    @Override
    public Void visitExpressionStmt(Stmt.Expression stmt) {
        evaluate(stmt.expression);
        return null;
    }
    //< Statements and State visit-expression-stmt
//> Functions visit-function
    @Override
    public Void visitFunctionStmt(Stmt.Function stmt) {
        return null;
    }
    //< Functions visit-function
//> Control Flow visit-if
    @Override
    public Void visitIfStmt(Stmt.If stmt) {
        if (isTruthy(evaluate(stmt.condition))) {
            execute(stmt.thenBranch);
        } else if (stmt.elseBranch != null) {
            execute(stmt.elseBranch);
        }
        return null;
    }
    //< Control Flow visit-if
//> Statements and State visit-print
    @Override
    public Void visitPrintStmt(Stmt.Print stmt) {
        Object value = evaluate(stmt.expression);
      //
        //  System.out.println("Printing");
        System.out.print(stringify(value));
        return null;
    }
    @Override
    public Void visitPrintlnStmt(Stmt.Println stmt) {

        Object value = evaluate(stmt.expression);
//        System.out.println("Printing");
        System.out.println(stringify(value));
        return null;
    }
    //< Statements and State visit-print
//> Functions visit-return
    @Override
    public Void visitReturnStmt(Stmt.Return stmt) {
        return null;
    }
    //< Functions visit-return
//> Statements and State visit-var
    @Override
    public Void visitVarStmt(Stmt.Var stmt) {
        Object value = null;
        if (stmt.initializer != null) {
            value = evaluate(stmt.initializer);
        }
      //  System.out.println(stringify(value));
        environment.define(stmt.name, value);
        return null;
    }
    //In the following functions each, 1 corresponds to integer value, 2 for float, 3 for string and 4 for bool
    @Override
    public Void visitVarIntStmt(Stmt.VarInt stmt) {
        Object value = null;
        if (stmt.initializer != null) {
            value = evaluate(stmt.initializer);
        }
        //System.out.println(stringify(value));
        environment.define(stmt.name, value, 1);
        return null;
    }
    @Override
    public Void visitVarDoubleStmt(Stmt.VarDouble stmt) {
        Object value = null;
        if (stmt.initializer != null) {
            value = evaluate(stmt.initializer);
        }
        //System.out.println(stringify(value));
        environment.define(stmt.name, value, 2);
        return null;
    }
    @Override
    public Void visitVarStringStmt(Stmt.VarString stmt) {
        Object value = null;
        if (stmt.initializer != null) {
            value = evaluate(stmt.initializer);
        }
        //System.out.println(stringify(value));
        environment.define(stmt.name, value, 3);
        return null;
    }
    @Override
    public Void visitVarBoolStmt(Stmt.VarBool stmt) {
        Object value = null;
        if (stmt.initializer != null) {
            value = evaluate(stmt.initializer);
        }
      //  System.out.println(stringify(value));
        environment.define(stmt.name, value, 4);
        return null;
    }
    //< Statements and State visit-var
//> Control Flow visit-while
    @Override
    public Void visitWhileStmt(Stmt.While stmt) {
        while (isTruthy(evaluate(stmt.condition))) {
            execute(stmt.body);
        }
        return null;
    }
    //< Control Flow visit-while
//> Statements and State visit-assign
    @Override
    public Object visitAssignExpr(Expr.Assign expr) {
        Object value = evaluate(expr.value);
/* Statements and State visit-assign < Resolving and Binding resolved-assign
    environment.assign(expr.name, value);
*/
//> Resolving and Binding resolved-assign
        //System.out.println(locals);

        Integer distance = locals.get(expr);
        if (distance != null) {
            environment.assignAt(distance, expr.name, value);
        } else {
            globals.assign(expr.name, value);
        }

//< Resolving and Binding resolved-assign
        return value;
    }
    //< Statements and State visit-assign
//> visit-binary
    @Override
    public Object visitBinaryExpr(Expr.Binary expr) {
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right); // [left]

        switch (expr.operator.type) {
//> binary-equality
            case BANG_EQUAL: return !isEqual(left, right);
            case EQUAL_EQUAL: return isEqual(left, right);
//< binary-equality
//> binary-comparison
            case GREATER:
//> check-greater-operand
                checkNumberOperands(expr.operator, left, right);
//< check-greater-operand
                return (double)left > (double)right;
            case GREATER_EQUAL:
//> check-greater-equal-operand
                checkNumberOperands(expr.operator, left, right);
//< check-greater-equal-operand
                return (double)left >= (double)right;
            case LESS:
//> check-less-operand
                checkNumberOperands(expr.operator, left, right);
//< check-less-operand
                return (double)left < (double)right;
            case LESS_EQUAL:
//> check-less-equal-operand
                checkNumberOperands(expr.operator, left, right);
//< check-less-equal-operand
                return (double)left <= (double)right;
//< binary-comparison
            case MINUS:
//> check-minus-operand
                checkNumberOperands(expr.operator, left, right);
//< check-minus-operand
                return (double)left - (double)right;
//> binary-plus
            case PLUS:
                if (left instanceof Double && right instanceof Double) {
                    return (double)left + (double)right;
                } // [plus]

                if (left instanceof String && right instanceof String) {
                    return (String)left + (String)right;
                }

/* Evaluating Expressions binary-plus < Evaluating Expressions string-wrong-type
        break;
*/
//> string-wrong-type
                throw new RuntimeError(expr.operator,
                        "Operands must be two numbers or two strings.");
//< string-wrong-type
//< binary-plus
            case SLASH:
//> check-slash-operand
                checkNumberOperands(expr.operator, left, right);
//< check-slash-operand
                return (double)left / (double)right;
            case STAR:
//> check-star-operand
                checkNumberOperands(expr.operator, left, right);
//< check-star-operand
                return (double)left * (double)right;
        }

        // Unreachable.
        return null;
    }
    //< visit-binary
//> Functions visit-call
    @Override
    public Object visitCallExpr(Expr.Call expr) {
        return null;
    }
    //< Functions visit-call
//> Classes interpreter-visit-get
    @Override
    public Object visitGetExpr(Expr.Get expr) {
        Object object = evaluate(expr.object);
        if (object instanceof GolangInstance) {
            return ((GolangInstance) object).get(expr.name);
        }

        throw new RuntimeError(expr.name,
                "Only instances have properties.");
    }
    //< Classes interpreter-visit-get
//> visit-grouping
    @Override
    public Object visitGroupingExpr(Expr.Grouping expr) {
        return evaluate(expr.expression);
    }
    //< visit-grouping
//> visit-literal
    @Override
    public Object visitLiteralExpr(Expr.Literal expr) {
        return expr.value;
    }
    //< visit-literal
//> Control Flow visit-logical
    @Override
    public Object visitLogicalExpr(Expr.Logical expr) {
        Object left = evaluate(expr.left);

        if (expr.operator.type == TokenType.OR) {
            if (isTruthy(left)) return left;
        } else {
            if (!isTruthy(left)) return left;
        }

        return evaluate(expr.right);
    }
    //< Control Flow visit-logical
//> Classes interpreter-visit-set
    @Override
    public Object visitSetExpr(Expr.Set expr) {
        Object object = evaluate(expr.object);

        if (!(object instanceof GolangInstance)) { // [order]
            throw new RuntimeError(expr.name,
                    "Only instances have fields.");
        }

        Object value = evaluate(expr.value);
      //  System.out.println(expr.name + "  Value =======    "+stringify(value));
        ((GolangInstance)object).set(expr.name, value);
        return value;
    }

//> visit-unary
    @Override
    public Object visitUnaryExpr(Expr.Unary expr) {
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
//> unary-bang
            case BANG:
                return !isTruthy(right);
//< unary-bang
            case MINUS:
//> check-unary-operand
                checkNumberOperand(expr.operator, right);
//< check-unary-operand
                return -(double)right;
        }

        // Unreachable.
        return null;
    }
    //< visit-unary
//> Statements and State visit-variable
    @Override
    public Object visitVariableExpr(Expr.Variable expr) {
/* Statements and State visit-variable < Resolving and Binding call-look-up-variable
    return environment.get(expr.name);
*/
//> Resolving and Binding call-look-up-variable

        return lookUpVariable(expr.name, expr);
//< Resolving and Binding call-look-up-variable
    }
    //> Resolving and Binding look-up-variable
    private Object lookUpVariable(Token name, Expr expr) {
//       // System.out.println(locals);
//       // System.out.println("-------------locals-----------");
        Integer distance = locals.get(expr);
        if (distance != null) {
            return environment.getAt(distance, name.lexeme);
        } else {
//            return environment.getAt(distance, name.lexeme);
            return globals.get(name);
        }
    }
    //< Resolving and Binding look-up-variable
//< Statements and State visit-variable
//> check-operand
    private void checkNumberOperand(Token operator, Object operand) {
        if (operand instanceof Double) return;
        throw new RuntimeError(operator, "Operand must be a number.");
    }
    //< check-operand
//> check-operands
    private void checkNumberOperands(Token operator,
                                     Object left, Object right) {
        if (left instanceof Double && right instanceof Double) return;
        // [operand]
        throw new RuntimeError(operator, "Operands must be numbers.");
    }

    private void checkIntOperands(Token operator,
                                     Object left, Object right) {
        if (left instanceof Integer && right instanceof Integer) return;
        // [operand]
        throw new RuntimeError(operator, "Operands must be integers");
    }
    //< check-operands
//> is-truthy
    private boolean isTruthy(Object object) {
        if (object == null) return false;
        if (object instanceof Boolean) return (boolean)object;
        return true;
    }
    //< is-truthy
//> is-equal
    private boolean isEqual(Object a, Object b) {
        if (a == null && b == null) return true;
        if (a == null) return false;

        return a.equals(b);
    }
    //< is-equal
//> stringify
    private String stringify(Object object) {
        if (object == null) return "nil";

        if (object instanceof Double) {
            String text = object.toString();
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
            return text;
        }


        return object.toString();
    }
//< stringify
}
