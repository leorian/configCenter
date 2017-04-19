package com.luotian.json;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
//!import java.beans.BeanInfo;
//!import java.beans.Introspector;
//!import java.beans.IntrospectionException;
//!import java.beans.PropertyDescriptor;
import java.lang.reflect.*;

import com.luotian.json.factories.*;

public class ObjectBinder {

    private Stack<Object> objectStack = new Stack<Object>();
    private Stack<Object> jsonStack = new Stack<Object>();
    private Path currentPath = new Path();
    private Map<Class,ObjectFactory> factories = new HashMap<Class,ObjectFactory>();
    private static Map<Class,ObjectFactory> defaultFactories;
    //private Map<Path,ObjectFactory> pathFactories = new HashMap<Path,ObjectFactory>();
   

    static {
    	defaultFactories = new HashMap<Class,ObjectFactory>();
        defaultFactories.put( Object.class, new BeanObjectFactory() );
        defaultFactories.put( Collection.class, new ListObjectFactory() );
        defaultFactories.put( List.class, new ListObjectFactory() );
        defaultFactories.put( Set.class, new SetObjectFactory() );
        defaultFactories.put( SortedSet.class, new SortedSetObjectFactory() );
        defaultFactories.put( Map.class, new MapObjectFactory() );
        defaultFactories.put( Integer.class, new IntegerObjectFactory() );
        defaultFactories.put( int.class, new IntegerObjectFactory() );
        defaultFactories.put( Float.class, new FloatObjectFactory() );
        defaultFactories.put( float.class, new FloatObjectFactory() );
        defaultFactories.put( Double.class, new DoubleObjectFactory() );
        defaultFactories.put( double.class, new DoubleObjectFactory() );
        defaultFactories.put( Short.class, new ShortObjectFactory() );
        defaultFactories.put( short.class, new ShortObjectFactory() );
        defaultFactories.put( Long.class, new LongObjectFactory() );
        defaultFactories.put( long.class, new LongObjectFactory() );
        defaultFactories.put( Byte.class, new ByteObjectFactory() );
        defaultFactories.put( byte.class, new ByteObjectFactory() );
        defaultFactories.put( Boolean.class, new BooleanObjectFactory() );
        defaultFactories.put( boolean.class, new BooleanObjectFactory() );
        defaultFactories.put( Character.class, new CharacterObjectFactory() );
        defaultFactories.put( char.class, new CharacterObjectFactory() );
        defaultFactories.put( Enum.class, new EnumObjectFactory() );
        defaultFactories.put( Date.class, new DateObjectFactory() );
        defaultFactories.put( String.class, new StringObjectFactory() );
        defaultFactories.put( Array.class, new ArrayObjectFactory() );
        defaultFactories.put( BigDecimal.class, new BigDecimalFactory() );
        defaultFactories.put( BigInteger.class, new BigIntegerFactory() );
        
        // Added
        defaultFactories.put( byte[].class, new ByteArrayObjectFactory() );
        defaultFactories.put( Class.class, new ClassObjectFactory() );
        defaultFactories.put(StackTraceElement.class, new StackTraceElementFactory());
        defaultFactories.put(Throwable.class, new ThrowableObjectFactory());
    }
    
	public ObjectBinder() {
		factories = defaultFactories;

	}

//    public ObjectBinder use(Path path, ObjectFactory factory) {
//        pathFactories.put( path, factory );
//        return this;
//    }

    public ObjectBinder use(Class clazz, ObjectFactory factory) {
        factories.put( clazz, factory );
        return this;
    }

    public Path getCurrentPath() {
        return currentPath;
    }

    public Object bind( Object input ) {
        return this.bind( input, null );
    }

    public Object bind( Object source, Object target ) {
        if( target instanceof Map ) {
            bindIntoMap( (Map)source, (Map<Object,Object>)target, null, null );
        } else if( target instanceof Collection ) {
            bindIntoCollection( (Collection)source, (Collection<Object>)target, null );
        } else {
            bindIntoObject( (Map)source, target, target.getClass() );
        }
        return target;
    }

    public Object bind( Object input, Type targetType ) {
        jsonStack.push( input );
        try {
            if( input == null ) return null;
            Class targetClass = findClassName( input, getTargetClass( targetType ) );
            ObjectFactory factory = findFactoryFor(input, targetClass );
            if( factory == null ) throw new JSONException( currentPath + ": + Could not find a suitable ObjectFactory for " + targetClass );
            return factory.instantiate( this, input, targetType, targetClass );
        } finally {
            jsonStack.pop();
        }
    }

    public <T extends Collection<Object>> T bindIntoCollection(Collection value, T target, Type targetType) {
        Type valueType = null;
        if( targetType instanceof ParameterizedType) {
            valueType = ((ParameterizedType)targetType).getActualTypeArguments()[0];
        }
        jsonStack.push( value );
        objectStack.push( target );
        getCurrentPath().enqueue("values");
        for( Object obj : value ) {
            target.add( bind( obj, valueType ) );
        }
        getCurrentPath().pop();
        objectStack.pop();
        jsonStack.pop();
        return target;
    }

    public Object bindIntoMap(Map input, Map<Object, Object> result, Type keyType, Type valueType) {
        jsonStack.push( input );
        objectStack.push( result );
        for( Object inputKey : input.keySet() ) {
            currentPath.enqueue("keys");
            Object key = bind( inputKey, keyType );
            currentPath.pop();
            currentPath.enqueue("values");
            Object value = bind( input.get(inputKey), valueType );
            currentPath.pop();
            result.put( key, value );
        }
        objectStack.pop();
        jsonStack.pop();
        return result;
    }

    public Object bindIntoObject(Map jsonOwner, Object target, Type targetType) {
        try {
            objectStack.push( target );
            BeanInfo info = Introspector.getBeanInfo( target.getClass() );
            for( PropertyDescriptor descriptor : info.getPropertyDescriptors() ) {
                Object value = findFieldInJson( jsonOwner, descriptor );
                if( value != null ) {
                    currentPath.enqueue( descriptor.getName() );
                    Method setMethod = descriptor.getWriteMethod();
                    if( setMethod != null ) {
                        Type[] types = setMethod.getGenericParameterTypes();
                        if( types.length == 1 ) {
                            Type paramType = types[0];
                            
                            // By Helk, special handling to reserve actual type of targetClass into paramType.
                            if (paramType instanceof ParameterizedType && targetType instanceof ParameterizedType) {
                            	
                            	//  paramType should be like this List<T>
                            	// We should change it into List<Question>, the Question may be in
                            	// targetType as Page<Question>, namely, set actual type into paramType.
                            	
                            	
                            	ParameterizedType parameterizedParamType = (ParameterizedType)paramType;
                            	if (parameterizedParamType.getActualTypeArguments()[0] instanceof Class) { // check the first arguments
                            		// true type, do not do substitution.(e.g. java.lang.String)
                            	} else {
                            		// possibly the T, do substitution.
                            		paramType = new JSONParameterizedType((Class)parameterizedParamType.getRawType(), ((ParameterizedType)targetType).getActualTypeArguments());
                            	}
 
                            	
                            }
                            setMethod.invoke( objectStack.peek(), bind( value, resolveParameterizedTypes( paramType, targetType ) ) );
                        } else {
                            throw new JSONException(currentPath + ":  Expected a single parameter for method " + target.getClass().getName() + "." + setMethod.getName() + " but got " + types.length );
                        }
                    } else {
                        try {
                            Field field = target.getClass().getDeclaredField( descriptor.getName() );
                            field.setAccessible( true );
                            if( value instanceof Map ) {
                                field.set( target, bind(value, field.getGenericType() ) );
                            } else {
                                field.set( target, bind( value, field.getGenericType() ) );
                            }
                        } catch (NoSuchFieldException e) {
                            // ignore must not be there.
                        }
                    }
                    currentPath.pop();
                }
            }
            return objectStack.pop();
        } catch (IllegalAccessException e) {
            throw new JSONException(currentPath + ":  Could not access the no-arg constructor for " + target.getClass().getName(), e);
        } catch (InvocationTargetException ex ) {
            throw new JSONException(currentPath + ":  Exception while trying to invoke setter method.", ex );
        } catch (IntrospectionException e) {
            throw new JSONException(currentPath + ":  Could not inspect " + target.getClass().getName(), e );
        }
    }

    public JSONException cannotConvertValueToTargetType(Object value, Class targetType) {
        return new JSONException( String.format("%s:  Can not convert %s into %s", currentPath, value.getClass().getName(), targetType.getClass().getName() ) );
    }

    private Class getTargetClass(Type targetType) {
        if( targetType == null ) {
            return null;
        } else if( targetType instanceof Class ) {
            return (Class)targetType;
        } else if( targetType instanceof ParameterizedType ) {
            return (Class)((ParameterizedType)targetType).getRawType();
        } else if( targetType instanceof GenericArrayType ) {
            return Array.class;
        } else if( targetType instanceof WildcardType ) {
            return null; // nothing you can do about these.  User will have to specify this with use()
        } else if( targetType instanceof TypeVariable ) {
            return null; // nothing you can do about these.  User will have to specify this with use()
        } else {
            throw new JSONException(currentPath + ":  Unknown type " + targetType );
        }
    }

    private Type resolveParameterizedTypes(Type genericType, Type targetType) {
        if( genericType instanceof Class ) {
            return genericType;
        } else if( genericType instanceof ParameterizedType ) {
            return genericType;
        } else if( genericType instanceof TypeVariable ) {
            return targetType;
        } else if( genericType instanceof WildcardType ) {
            return targetType;
        } else if( genericType instanceof GenericArrayType ) {
            return ((GenericArrayType)genericType).getGenericComponentType();
        } else {
            throw new JSONException( currentPath + ":  Unknown generic type " + genericType + ".");
        }
    }


    private Class findClassName( Object map, Class targetType ) throws JSONException {
//        if( !pathFactories.containsKey( currentPath ) ) {
    		Class overrideType = null;
    		// Override class if targetType is java.util.Map
    		if (targetType != null) {
    			Class rawType = getTargetClass(targetType);
    		
    			if (Map.class.isAssignableFrom(rawType)) {
    				overrideType = HashMap.class;		
    			}
    		}
    	
            Class mostSpecificType = useMostSpecific( targetType, map instanceof Map ? findClassInMap( (Map)map, overrideType ) : null );
            if( mostSpecificType == null ) {
                return map.getClass();
            } else {
                return mostSpecificType;
            }
//        } else {
//            return null;
//        }
    }

    protected Class useMostSpecific(Class classFromTarget, Class typeFound) {
        if( classFromTarget != null && typeFound != null ) {
            return typeFound.isAssignableFrom( classFromTarget ) ? classFromTarget : typeFound;
        } else if( typeFound != null ) {
            return typeFound;
        } else if( classFromTarget != null ) {
            return classFromTarget;
        } else {
            return null;
        }
    }

    protected Class findClassInMap( Map map, Class override ) {
        if( override == null ) {
            String classname = (String)map.remove("class");
            try {
                if( classname != null ) {
                    return Class.forName( classname );
                } else {
                    return null;
                }
            } catch( ClassNotFoundException e ) {
                throw new JSONException( String.format( "%s:  Could not load %s", currentPath, classname ), e );
            }
        } else {
            return override;
        }
    }

	private ObjectFactory findFactoryFor(Object input, Class targetType) {
		// ObjectFactory factory = pathFactories.get(currentPath);
		// if (factory == null) {

		if (targetType != null) {
			// Special handling for byte[].class
			if (targetType == byte[].class) {
				return findFactoryByTargetClass(targetType);
			}

			if (targetType.isArray()) {
				return factories.get(Array.class);
			}

		}
		if (targetType == Object.class && input != null) {
			return findFactoryByTargetClass(input.getClass());
		}
		// }
		return findFactoryByTargetClass(targetType);
		// return factory;
	}

    private ObjectFactory findFactoryByTargetClass(Class targetType) {
        ObjectFactory factory;
        factory = factories.get( targetType );
        if( factory == null && targetType != null && targetType.getSuperclass() != null ) {
            for( Class intf : targetType.getInterfaces() ) {
                factory = findFactoryByTargetClass( intf );
                if( factory != null ) return factory;
            }
            return findFactoryByTargetClass( targetType.getSuperclass() );
        } else {
            return factory;
        }
    }

    protected Object instantiate( Class clazz ) {
        try {
            Constructor constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible( true );
            return constructor.newInstance();
        } catch (InstantiationException e) {
            throw new JSONException(currentPath + ":There was an exception trying to instantiate an instance of " + clazz.getName(), e );
        } catch (IllegalAccessException e) {
            throw new JSONException(currentPath + ":There was an exception trying to instantiate an instance of " + clazz.getName(), e );
        } catch (InvocationTargetException e) {
            throw new JSONException(currentPath + ":There was an exception trying to instantiate an instance of " + clazz.getName(), e );
        } catch (NoSuchMethodException e) {
            throw new JSONException(currentPath + ": " + clazz.getName() + " lacks a no argument constructor.  Flexjson will instantiate any protected, private, or public no-arg constructor.", e );
        }
    }

    private Object findFieldInJson( Map map, PropertyDescriptor descriptor ) {
        Object value = map.get( descriptor.getName() );
//        if( value == null ) {
//            String field = descriptor.getName();
//            value = map.get( upperCase(field) );
//        }

        return value;
    }

    private String upperCase(String field) {
        return Character.toUpperCase( field.charAt(0) ) + field.substring(1);
    }

    public Object getTarget() {
        return objectStack.peek();
    }

    public Object getSource() {
        return jsonStack.peek();
    }

    public Object bindPrimitive(Object value, Class clazz) {
        if( value.getClass() == clazz ) {
            return value;
        } else if( value instanceof Number && clazz.equals(Double.class) ) {
            return ((Number)value).doubleValue();
        } else if( value instanceof Number && clazz.equals(Integer.class) ) {
            return ((Number)value).intValue();
        } else if( value instanceof Number && clazz.equals(Long.class) ) {
            return ((Number)value).longValue();
        } else if( value instanceof Number && clazz.equals(Short.class) ) {
            return ((Number)value).shortValue();
        } else if( value instanceof Number && clazz.equals(Byte.class) ) {
            return ((Number)value).byteValue();
        } else if( value instanceof Number && clazz.equals(Float.class) ) {
            return ((Number)value).floatValue();
        } else if( value instanceof Boolean && clazz.equals(Boolean.class) ) {
            return value;
        } else if( value instanceof Long && clazz == Date.class ) {
            return new Date( (Long)value );
        } else {
            throw new JSONException(String.format("%s: Don't know how to bind %s into class %s.  You might need to use an ObjectFactory instead of a plain class.", getCurrentPath().toString(), value, clazz.getName()) );
        }
    }

//    public Class findClassAtPath(Path currentPath) throws ClassNotFoundException {
//        ObjectFactory factory = pathFactories.get( currentPath );
//        if( factory instanceof ClassLocatorObjectFactory ) {
//            return ((ClassLocatorObjectFactory)factory).getLocator().locate( this, currentPath );
//        } else {
//            return null;
//        }
//    }
}
