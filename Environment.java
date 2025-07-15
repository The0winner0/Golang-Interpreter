//> Statements and State environment-class
package Golang;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class Environment {

    final Environment enclosing;

    private final Map<String, Object> values = new HashMap<>();
    private final Map<String, Integer> datatype = new HashMap<>();//0 null, 2 integer, 1 double, 3 string 4 bool

    Environment() {
        enclosing = null;
    }

    Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }


    Object get(Token name) {
//        System.out.println(name);
//        System.out.println(values);
//        System.out.println("values in get");
        if (values.containsKey(name.lexeme)) {
         //   System.out.println("Get1");
            return values.get(name.lexeme);
        }


        if (enclosing != null){
            //System.out.println("Get2");
            return enclosing.get(name);
        }


        throw new RuntimeError(name,
                "Undefined variable 1'" + name.lexeme + "'.");
    }
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
    private int check(Object object) {
        if (object == null) return 0;

        if (object instanceof Double) {
            String text = object.toString();
            if (text.endsWith(".0")) {
                return 1;
            }
            return 2;
        }
        if(object instanceof Boolean){
            return 4;
        }


        return 3;
    }
    //< environment-get
//> environment-assign
    void assign(Token name, Object value) {
        if (values.containsKey(name.lexeme)) {
            if(datatype.containsKey(name.lexeme)){
                if(datatype.get(name.lexeme)==check(value)){
                    values.put(name.lexeme, value);
                }
                else{
                    throw new RuntimeError(name,
                            "Unexpected dataType '" + name.lexeme + "'.");
                }
            }else
                values.put(name.lexeme, value);
            return;
        }

//> environment-assign-enclosing
        if (enclosing != null) {
            enclosing.assign(name, value);
            return;
        }

//< environment-assign-enclosing
        throw new RuntimeError(name,
                "Undefined variable '" + name.lexeme + "'.");
    }
    //< environment-assign
//> environment-define
    void define(Token name, Object value) {
        if(values.containsKey(name.lexeme)){
            throw new RuntimeError(name,
                    "Variable Already Defined '" + name.lexeme + "'.");
        }
        values.put(name.lexeme, value);
        datatype.put(name.lexeme, check(value));

    }
    void define(Token name, Object value, int k) {
        if(values.containsKey(name.lexeme)){
            throw new RuntimeError(name,
                    "Variable Already Defined '" + name.lexeme + "'.");
        }
        values.put(name.lexeme, value);
        datatype.put(name.lexeme, k);
//        System.out.println(values);
//        System.out.println("values");
    }
    //< environment-define
//> Resolving and Binding ancestor
    Environment ancestor(int distance) {
        Environment environment = this;
        for (int i = 0; i < distance; i++) {
            environment = environment.enclosing; // [coupled]
        }

        return environment;
    }
    //< Resolving and Binding ancestor
//> Resolving and Binding get-at
    Object getAt(int distance, String name) {
        System.out.println(distance+"  -  "+name);
        System.out.println(ancestor(distance).values.get(name));
        System.out.println(ancestor(distance));
        return ancestor(distance).values.get(name);
    }
    //< Resolving and Binding get-at
//> Resolving and Binding assign-at
    void assignAt(int distance, Token name, Object value) {
        Environment e = ancestor(distance);
        if (e.values.containsKey(name.lexeme)) {
            if(e.datatype.containsKey(name.lexeme)){
                if(e.datatype.get(name.lexeme)==check(value)){
                    e.values.put(name.lexeme, value);
                }
                else{
                    throw new RuntimeError(name,
                            "Unexpected dataType '" + name.lexeme + "'.");
                }
            }else
                values.put(name.lexeme, value);
            return;
        }
//        e.values.put(name.lexeme, value);
    }
    //< Resolving and Binding assign-at
//> omit
    @Override
    public String toString() {
        String result = values.toString();
        if (enclosing != null) {
            result += " -> " + enclosing.toString();
        }

        return result;
    }
//< omit
}
