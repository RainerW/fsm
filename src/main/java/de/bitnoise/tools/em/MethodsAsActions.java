package de.bitnoise.tools.em;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodsAsActions
{

  Object _target;

  public MethodsAsActions(Object target)
  {
    _target = target;
  }

  public EMAction action(String name)
  {
    Method[] methods = _target.getClass().getDeclaredMethods();
    for (Method m : methods)
    {
      if (m.getName().equals(name)
          && m.getParameterTypes().length == 2 // FIXME: do type check
      )
      {
        return createEventProxy(m, _target);
      }
    }
    throw new IllegalArgumentException("Unable to find method " + name);
  }

  private EMAction createEventProxy(final Method methodToInvoke, final Object onInstance)
  {
    return new EMAction<EMState, EMEvent>()
    {
      @Override
      public void doAction(EMState currentState, EMEvent eventObj)
      {
        try
        {
          methodToInvoke.setAccessible(true);
          methodToInvoke.invoke(onInstance, currentState, eventObj);
        } catch (IllegalAccessException e)
        {
          throw new RuntimeException(e);
        } catch (IllegalArgumentException e)
        {
          throw new RuntimeException(e);
        } catch (InvocationTargetException e)
        {
          throw new RuntimeException(e);
        }
      }
    };
  }

}
