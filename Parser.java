//> Parsing Expressions parser
package Golang;

//> Statements and State parser-imports
import java.util.ArrayList;
//< Statements and State parser-imports
//> Control Flow import-arrays
import java.util.Arrays;
//< Control Flow import-arrays
import java.util.List;

import static Golang.TokenType.*;

class Parser {
    //> parse-error
    private static class ParseError extends RuntimeException {}

    //< parse-error
    private final List<Token> tokens;
    public static int formatstatus=0;
    public static int funcmainstatus=0;
    public static int positionmain=0;
    private int current = 0;
//    private final PackageName;

    Parser(List<Token> tokens) {
        this.tokens = tokens;
        if(this.tokens.size()>=1 && this.tokens.get(0).type==IMPORT)
        {
            if(this.tokens.size()>=2 &&  this.tokens.get(1).type==STRING  && this.tokens.get(1).literal.equals("fmt"))
            {
                formatstatus=1;
                tokens.remove(0);
                tokens.remove(0);
            }
            else
            {
                System.err.println("Invalid Library");
                tokens.remove(0);
                tokens.remove(0);
            }

        }
        for(int i=0;i<tokens.size();i++)
        {
            if(this.tokens.get(i).type==FUN)
            {
                if(i+1<tokens.size())
                {
                    // System.out.println("aa"+" "+this.tokens.get(i+1).type);
                    if(this.tokens.get(i+1).type==MAIN)
                    {
                        if(i+2<tokens.size() && this.tokens.get(i+2).type==LEFT_BRACE)
                        {
                            ///System.out.println("ab"+" "+this.tokens.get(tokens.size()-2).type);
                            if(tokens.get(tokens.size()-2).type==RIGHT_BRACE)
                            {
                                tokens.remove(tokens.size()-2);
                                tokens.remove(i+2);
                                tokens.remove(i+1);
                                tokens.remove(i);
                                funcmainstatus=1;
                                positionmain=i;
                            }
                            else
                            {
                             //   System.err.println("in1");
                                tokens.remove(tokens.size()-2);
                                tokens.remove(i+2);
                                tokens.remove(i+1);
                                tokens.remove(i);
                                break;
                            }
                        }
                        else
                        {
                          //  System.err.println("in2");
                            tokens.remove(i+2);
                            tokens.remove(i+1);
                            tokens.remove(i);
                            break;
                        }
                    }
                    else
                    {
                       // System.err.println("in3");
                        tokens.remove(i+1);
                        tokens.remove(i);
                        break;
                    }
                }
                else
                {
                    tokens.remove(i);
                   // System.err.println("in4");
                    break;
                }
            }
        }
    }
    /* Parsing Expressions parse < Statements and State parse
      Expr parse() {
        try {
          return expression();
        } catch (ParseError error) {
          return null;
        }
      }
    */
//> Statements and State parse
    List<Stmt> parse() {
        List<Stmt> statements = new ArrayList<>();
        while (!isAtEnd()) {
/* Statements and State parse < Statements and State parse-declaration
      statements.add(statement());
*/
//> parse-declaration
            statements.add(declaration());
//< parse-declaration
        }

        return statements; // [parse-error-handling]
    }





    //< Statements and State parse
//> expression
    private Expr expression() {
/* Parsing Expressions expression < Statements and State expression
    return equality();
*/
//> Statements and State expression
        return assignment();
//< Statements and State expression
    }
    private Expr expression(int k) {
/* Parsing Expressions expression < Statements and State expression
    return equality();
*/
//> Statements and State expression
        return assignment(k);
//< Statements and State expression
    }
    //< expression
//> Statements and State declaration
    private Stmt declaration() {
        try {
//> Classes match-class
//            if (match(CLASS)) return classDeclaration();
//< Classes match-class
//> Functions match-fun
//< Functions match-fun
            if (match(IDENTIFIER)){current--; return varDeclaration();}
            if(match(VAR)){ return varDeclarationDataType();}
           // System.out.println(current);
            if(funcmainstatus==1 && positionmain<=current)
            {
                return statement();
            }
            else
            {
                error(peek(),"Code outside main function" );
                current=tokens.size()-1;
                return null;
            }
//            return statement();
        } catch (ParseError error) {
            synchronize();
            return null;
        }
    }
    //< Statements and State declaration
//> Classes parse-class-declaration

    //< Classes parse-class-declaration
//> Statements and State parse-statement

    private Stmt statement() {
//> Control Flow match-for

        if (match(FOR)) return forStatement();
//< Control Flow match-for
//> Control Flow match-if
        if (match(IF)) return ifStatement();
//< Control Flow match-if
        if(match(FMT)) {

            consume(DOT, "Expect '.' after loop condition.");
            if(formatstatus==1)
            {
                if (match(PRINTLN) ) return new Stmt.Block2(printlnStatement());
                else if (match(PRINT)) return new Stmt.Block2(printStatement());
                else{
                    error(peek(), "Expected Expression");
                }
            }
            else
            {
                error(peek(),"fmt not imported" );
                current++;
                return null;
                // System.out.println("Error Print about import");
            }
        }


//> Functions match-return
//< Functions match-return
//> Control Flow match-while
//        if (match(WHILE)) return whileStatement();
//< Control Flow match-while
//> parse-block
        if (match(LEFT_BRACE)) return new Stmt.Block(block());
//< parse-block

        return expressionStatement();
    }
    //< Statements and State parse-statement
//> Control Flow for-statement
    private Stmt forStatement() {
//        consume(LEFT_PAREN, "Expect '(' after 'for'.");

/* Control Flow for-statement < Control Flow for-initializer
    // More here...
*/
//> for-initializer
        Stmt initializer;
        if (match(SEMICOLON)) {
            initializer = null;
        //    System.out.println("a0");
        } else if (match(IDENTIFIER)) {
         //   System.out.println("a1");
            current--;
            initializer = varDeclaration();
            consume(SEMICOLON, "Expected semicolon after initialization");
        } else if(match(VAR)){
            initializer = varDeclarationDataType();
            consume(SEMICOLON, "Expected Semicolon after initialization");
        }
        else {
           // System.out.println("a2");
            initializer = expressionStatement();
        }
//< for-initializer
//> for-condition

        Expr condition = null;
        if (!check(SEMICOLON)) {
            condition = expression();
        }

        consume(SEMICOLON, "Expect ';' after loop condition.");
//< for-condition
//> for-increment

        Expr increment = null;
        if (!check(LEFT_BRACE)) {
            increment = expression();
        }
//        consume(LEFT_BRACE, "Expect '{' after for clauses.");
//< for-increment
//> for-body
       // System.out.println("a1111");
        Stmt body = statement();

//> for-desugar-increment
        if (increment != null) {
            body = new Stmt.Block2(
                    Arrays.asList(
                            body,
                            new Stmt.Expression(increment)));
        }
      //  System.out.println("b1");
//< for-desugar-increment
//> for-desugar-condition

        if (condition == null) condition = new Expr.Literal(true);
        body = new Stmt.While(condition, body);

//< for-desugar-condition
//> for-desugar-initializer
     //   System.out.println(initializer+"<==Initializer");
        if (initializer != null) {
            body = new Stmt.Block2(Arrays.asList(initializer, body));
        }
      //  System.out.println("b1");
//< for-desugar-initializer
        return body;
//< for-body
    }
    //< Control Flow for-statement
//> Control Flow if-statement
    private Stmt ifStatement() {
        Expr condition = expression();
//        consume(RIGHT_PAREN, "Expect ')' after if condition."); // [parens]

        Stmt thenBranch = statement();
        Stmt elseBranch = null;
        if (match(ELSE)) {
            elseBranch = statement();
        }

        return new Stmt.If(condition, thenBranch, elseBranch);
    }
    //< Control Flow if-statement
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
//> Statements and State parse-print-statement
    private List<Stmt> printStatement() {
        consume(LEFT_PAREN, "Expect '(' after value.");
        List<Stmt> statements = new ArrayList<>();
        Expr value;
        List<Expr> values = new ArrayList<>();
        if (!check(RIGHT_PAREN)) {
            do {
//> check-max-arity
                if (values.size() >= 255) {
                    error(peek(), "Can't have more than 255 arguments.");
                }
//< check-max-arity
                values.add(expression());
            } while (match(COMMA));
        }
        consume(RIGHT_PAREN, "Expect ')' after value.");

        for(Expr v:values){
            statements.add(new Stmt.Print(v));
        }

      //  System.out.println("Parse print");
        return statements;
    }
    private List<Stmt> printlnStatement() {
        consume(LEFT_PAREN, "Expect '(' after value.");
        List<Stmt> statements = new ArrayList<>();
        Expr value;
        List<Expr> values = new ArrayList<>();
        if (!check(RIGHT_PAREN)) {
            do {
//> check-max-arity
                if (values.size() >= 255) {
                    error(peek(), "Can't have more than 255 arguments.");
                }
//< check-max-arity
                values.add(expression());
            } while (match(COMMA));
        }
        consume(RIGHT_PAREN, "Expect ')' after value.");

        for(Expr v:values){
            statements.add(new Stmt.Println(v));
        }

        //System.out.println("Parse print");
        return statements;
    }
    //< Statements and State parse-print-statement
//> Functions parse-return-statement

    //< Functions parse-return-statement
//> Statements and State parse-var-declaration
    private Stmt varDeclaration() {
        Token name = consume(IDENTIFIER, "Expect variable name.");

        Expr initializer = null;
        if (match(COLON_EQUAl)) {
            initializer = expression();
        }else {
            current--;
            return expressionStatement();
//            consume(COLON_EQUAl, "Expect ':=' after variable declaration.");
        }
//        match(ENDL);
//        match(SEMICOLON);

//        consume(ENDL, "Expect end of line after variable declaration.");
        //System.out.println(stringify(Main.interpreter.evaluate(initializer)));
        return new Stmt.Var(name, initializer);
    }
    private Stmt varDeclarationDataType()
    {
        Expr initializer=null;
        Token name=consume(IDENTIFIER,"Expected variable name");
        if(match(EQUAL))
        {
            initializer = expression();
           // System.out.println(stringify(Main.interpreter.evaluate(initializer)));
            return new Stmt.Var(name, initializer);
        }
        else if(match(INT))
        {
//            name.literal=0;

//           System.out.println("size of hash map is "+values.size());
            //name.literal=0;
            if(!match(EQUAL))
                return new Stmt.VarInt(name, new Expr.Literal(0));
            else
            {
                initializer = expression();
                return new Stmt.VarInt(name, initializer);
            }
        }
        else if(match(FLOAT))
        {
//            values.put(name.lexeme,0.0);
//            name.literal=0.0;
            if(!match(EQUAL))
                return new Stmt.VarDouble(name, new Expr.Literal(0.0));
            else
            {
                initializer = expression();
                return new Stmt.VarDouble(name, initializer);
            }
        }
        else if(match(BOOL))
        {
//            values.put(name.lexeme,false);
//            name.literal=false;
            if(!match(EQUAL))
                return new Stmt.VarBool(name,new Expr.Literal(false));
            else
            {
                initializer = expression();
                return new Stmt.VarBool(name, initializer);
            }
        }
        else if(match(STR))
        {
//            values.put(name.lexeme,"");
//            name.literal="";
            if(!match(EQUAL))

            return new Stmt.VarString(name, new Expr.Literal(""));
            else
            {
                initializer = expression();
                return new Stmt.VarString(name, initializer);
            }
        }
        consume(INT, "Invalid declaration");
        return null;
    }
    //< Statements and State parse-var-declaration
//> Control Flow while-statement
//    private Stmt whileStatement() {
////        consume(LEFT_PAREN, "Expect '(' after 'while'.");
//        Expr condition = expression();
////        consume(RIGHT_PAREN, "Expect ')' after condition.");
//        Stmt body = statement();
//
//        return new Stmt.While(condition, body);
//    }
    //< Control Flow while-statement
//> Statements and State parse-expression-statement
    private Stmt expressionStatement() {
        Expr expr = expression();
//        consume(ENDL, "Expect ';' after expression.");
//        if(!(match(ENDL)||match(SEMICOLON))){
////            consume(ENDL, "Expect ';' after expression.");
//        }
        return new Stmt.Expression(expr);
    }
    //< Statements and State parse-expression-statement
//> Functions parse-function

    //< Functions parse-function
//> Statements and State block
    private List<Stmt> block() {
        List<Stmt> statements = new ArrayList<>();

        while (!check(RIGHT_BRACE) && !isAtEnd()) {
            statements.add(declaration());
        }

        consume(RIGHT_BRACE, "Expect '}' after block.");
        return statements;
    }
    //< Statements and State block
//> Statements and State parse-assignment
    private Expr assignment() {
/* Statements and State parse-assignment < Control Flow or-in-assignment
    Expr expr = equality();
*/
//> Control Flow or-in-assignment
        Expr expr = or();
//< Control Flow or-in-assignment
        if (match(EQUAL)) {
            //System.out.println("Inside");
            Token equals = previous();
            //System.out.println(equals+"  <-equals");
            Expr value = assignment();
           // System.out.println(value+"  <-value");
          //  System.out.println(expr+"  <-Expr");
            if (expr instanceof Expr.Variable) {
                Token name = ((Expr.Variable)expr).name;
              //  System.out.println(name+"  <-Name");
                return new Expr.Assign(name, value);
//> Classes assign-set
            } else if (expr instanceof Expr.Get) {
                Expr.Get get = (Expr.Get)expr;
              //  System.out.println(get+"  <-get");
                return new Expr.Set(get.object, get.name, value);
//< Classes assign-set
            }

            error(equals, "Invalid assignment target."); // [no-throw]
        }

        return expr;
    }
    private Expr assignment(int k) {
/* Statements and State parse-assignment < Control Flow or-in-assignment
    Expr expr = equality();
*/
//> Control Flow or-in-assignment
        Expr value;
        String s = " ";
       // System.out.println("CAlled");
        if(match(COMMA)){
            value=expression();

            s+=" "+stringify(value);
            value = new Expr.Literal(s);
           // System.out.println(s);
          //  System.out.println("INSIDE");
        }else
            value = or();
//< Control Flow or-in-assignment


        return value;
    }
    //< Statements and State parse-assignment
//> Control Flow or
    private Expr or() {
        Expr expr = and();

        while (match(OR)) {
            Token operator = previous();
            Expr right = and();
            expr = new Expr.Logical(expr, operator, right);
        }

        return expr;
    }
    //< Control Flow or
//> Control Flow and
    private Expr and() {
        Expr expr = equality();

        while (match(AND)) {
            Token operator = previous();
            Expr right = equality();
            expr = new Expr.Logical(expr, operator, right);
        }

        return expr;
    }
    //< Control Flow and
//> equality
    private Expr equality() {
        Expr expr = comparison();

        while (match(BANG_EQUAL, EQUAL_EQUAL)) {
            Token operator = previous();
            Expr right = comparison();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }
    //< equality
//> comparison
    private Expr comparison() {
        Expr expr = term();

        while (match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)) {
            Token operator = previous();
            Expr right = term();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }
    //< comparison
//> term
    private Expr term() {
        Expr expr = factor();

        while (match(MINUS, PLUS)) {
            Token operator = previous();
            Expr right = factor();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }
    //< term
//> factor
    private Expr factor() {
        Expr expr = unary();

        while (match(SLASH, STAR)) {
            Token operator = previous();
            Expr right = unary();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }
    //< factor
//> unary
    private Expr unary() {
        if (match(BANG, MINUS)) {
            Token operator = previous();
            Expr right = unary();
            return new Expr.Unary(operator, right);
        }

/* Parsing Expressions unary < Functions unary-call
    return primary();
*/
//> Functions unary-call
        return call();
//< Functions unary-call
    }
    //< unary
//> Functions finish-call

    //< Functions finish-call
//> Functions call

    //< Functions call
//> primary
    private Expr primary() {
        if (match(FALSE)) return new Expr.Literal(false);
        if (match(TRUE)) return new Expr.Literal(true);
        if (match(NIL)) return new Expr.Literal(null);

        if (match(NUMBER, STRING)) {
            return new Expr.Literal(previous().literal);
        }
//> Inheritance parse-super


//< Inheritance parse-super
//> Classes parse-this

//< Classes parse-this
//> Statements and State parse-identifier

        if (match(IDENTIFIER)) {
//            if(check(EQUAL)){
//                return null;
//            }
            return new Expr.Variable(previous());
        }
//< Statements and State parse-identifier

        if (match(LEFT_PAREN)) {
            Expr expr = expression();
            consume(RIGHT_PAREN, "  --Expect ')' after expression.");
            return new Expr.Grouping(expr);
        }
//        if(match(ENDL)){
//            return null;
//        }
        //System.out.println(tokens.get(current) +" --------");
//> primary-error
        throw error(peek(), "Expect expression.");
//< primary-error
    }
    //< primary
//> match
    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
           //     System.out.println("Match"+type);
                advance();
                return true;
            }
        }

        return false;
    }
    //< match

//> consume
    private Expr call() {
        Expr expr = primary();

        while (true) { // [while-true]
            if (match(LEFT_PAREN)) {
                expr = finishCall(expr);
//> Classes parse-property
            } else if (match(DOT)) {
                Token name = consume(IDENTIFIER,
                        "Expect property name after '.'.");
                expr = new Expr.Get(expr, name);
//< Classes parse-property
            } else {
                break;
            }
        }

        return expr;
    }
    private Expr finishCall(Expr callee) {
        List<Expr> arguments = new ArrayList<>();
        if (!check(RIGHT_PAREN)) {
            do {
//> check-max-arity
                if (arguments.size() >= 255) {
                    error(peek(), "Can't have more than 255 arguments.");
                }
//< check-max-arity
                arguments.add(expression());
            } while (match(COMMA));
        }

        Token paren = consume(RIGHT_PAREN,
                "Expect ')' after arguments.");

        return new Expr.Call(callee, paren, arguments);
    }
    private Token consume(TokenType type, String message) {
        if (check(type)) {
            //System.out.println("consume"+type);
            return advance();
        }

        throw error(peek(), message);
    }
    //< consume
//> check
    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }
    //< check
//> advance
    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }
    //< advance
//> utils
    private boolean isAtEnd() {
        return peek().type == EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }
    //< utils
//> error
    private ParseError error(Token token, String message) {
        Main.error(token, message);
        return new ParseError();
    }
    //< error
//> synchronize
    private void synchronize() {
        advance();

        while (!isAtEnd()) {
            if (previous().type == ENDL) return;

            switch (peek().type) {
                case CLASS:
                case FUN:
                case VAR:
                case FOR:
                case IF:
                case WHILE:
                case FMT:
                case PRINT:
                case RETURN:
                case IDENTIFIER:
                    return;
            }

            advance();
        }
    }
//< synchronize
}
