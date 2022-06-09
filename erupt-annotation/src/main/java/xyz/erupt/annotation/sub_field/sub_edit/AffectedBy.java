package xyz.erupt.annotation.sub_field.sub_edit;

import xyz.erupt.annotation.config.Comment;

//联动注解
public @interface AffectedBy {

    @Comment("js表达式")
    String expression();

    @Comment("受哪个字段影响")
    String byField();

}
