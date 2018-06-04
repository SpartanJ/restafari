package com.ensoft.restafari.database.annotations;


import android.support.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention( RetentionPolicy.RUNTIME)
@Target( ElementType.FIELD)
public @interface DbForeignKey
{
	String value();
	
	@Action int onUpdate() default NO_ACTION;
	
	@Action int onDelete() default NO_ACTION;
	
	int NO_ACTION = 1;
	int RESTRICT = 2;
	int SET_NULL = 3;
	int SET_DEFAULT = 4;
	int CASCADE = 5;
	
	@IntDef({NO_ACTION, RESTRICT, SET_NULL, SET_DEFAULT, CASCADE})
	@interface Action {
	}
}
