/* Generated by: ParserGeneratorCC: Do not edit this line. CacheQueryConstants.java */
package cacheServer.querying;


@SuppressWarnings("all")
/**
 * Token literal values and constants.
 * Generated by com.helger.pgcc.output.java.OtherFilesGenJava#start()
 */
public interface CacheQueryConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int ADD = 4;
  /** RegularExpression Id. */
  int INIT = 5;
  /** RegularExpression Id. */
  int GET = 6;
  /** RegularExpression Id. */
  int REMOVE = 7;
  /** RegularExpression Id. */
  int WITH = 8;
  /** RegularExpression Id. */
  int CACHE = 9;
  /** RegularExpression Id. */
  int REPLACEMENT = 10;
  /** RegularExpression Id. */
  int EOL = 11;
  /** RegularExpression Id. */
  int ID = 12;
  /** RegularExpression Id. */
  int LETTER = 13;
  /** RegularExpression Id. */
  int INTEGER_LITERAL = 14;
  /** RegularExpression Id. */
  int FLOATING_POINT_LITERAL = 15;
  /** RegularExpression Id. */
  int EXPONENT = 16;

  /** Lexical state. */
  int DEFAULT = 0;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\r\"",
    "\"\\t\"",
    "\"add\"",
    "\"init\"",
    "\"get\"",
    "\"remove\"",
    "\"with\"",
    "\"cache\"",
    "\"replacement by\"",
    "\"\\n\"",
    "<ID>",
    "<LETTER>",
    "<INTEGER_LITERAL>",
    "<FLOATING_POINT_LITERAL>",
    "<EXPONENT>",
    "\"=\"",
    "\"|\"",
  };

}
