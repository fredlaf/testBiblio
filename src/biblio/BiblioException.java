package biblio;

/**
 * L'exception BiblioException est levee lorsqu'une transaction est inadequate.
 * Par exemple
 *   -- livre inexistant
 */

public final class BiblioException extends Exception
{
  public BiblioException (String message)
  {
  super (message);
  }
}
