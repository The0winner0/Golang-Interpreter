//> Classes lox-instance
package Golang;

import java.util.HashMap;
import java.util.Map;

class GolangInstance {
    //> lox-instance-fields
    private final Map<String, Object> fields = new HashMap<>();
//< lox-instance-fields


    //> lox-instance-get-property
    Object get(Token name) {
        if (fields.containsKey(name.lexeme)) {
            return fields.get(name.lexeme);
        }

//> lox-instance-get-method
/* Classes lox-instance-get-method < Classes lox-instance-bind-method
    if (method != null) return method;
*/
//> lox-instance-bind-method
//< lox-instance-bind-method

//< lox-instance-get-method
        throw new RuntimeError(name, // [hidden]
                "Undefined property '" + name.lexeme + "'.");
    }
    //< lox-instance-get-property
//> lox-instance-set-property
    void set(Token name, Object value) {
        fields.put(name.lexeme, value);
    }
    //< lox-instance-set-property

}
