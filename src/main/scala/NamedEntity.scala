/**
 * Clase base abstracta para todas las entidades nombradas.
 *
 * Una entidad nombrada es una expresión del texto que refiere a un objeto
 * del mundo real (persona, lugar, organización, tecnología, etc.).
 *
 * @param text el texto tal como aparece en el corpus
 */
abstract class NamedEntity(val text: String) {

  /**
   * Retorna el tipo de la entidad como String.
   */
  def entityType: String

  /**
   * Retorna una línea de descripción de la entidad para el informe.
   */
  def describe: String = s"[$entityType] $text"

  /**
   * Verifica si la entidad hace match en el texto.
   * Default: case-insensitive, no puede ser parte de una palabra más larga.
   */
  def matches(text: String): Boolean = {
    val escapedText = java.util.regex.Pattern.quote(this.text)
    val regex = s"(?i)(?<!\\w)$escapedText(?!\\w)".r
    regex.findFirstIn(text).isDefined
  }

  /**
   * Indica si esta entidad es relevante para el análisis.
   * Default: todas son relevantes.
   */
  def isRelevant: Boolean = true
}

class Person(text: String) extends NamedEntity(text) {
  def entityType: String = "Person"

  /**
   * Person hace match si el nombre completo (case-sensitive) aparece en el texto.
   */
  override def matches(text: String): Boolean = {
    val escapedText = java.util.regex.Pattern.quote(this.text)
    val regex = s"(?<!\\w)$escapedText(?!\\w)".r
    regex.findFirstIn(text).isDefined
  }
}

class Organization(text: String) extends NamedEntity(text) {
  def entityType: String = "Organization"

  override def isRelevant: Boolean = false
}

class University(text: String) extends Organization(text) {
  override def entityType: String = "University"

  override def isRelevant: Boolean = true
}

class Place(text: String) extends NamedEntity(text) {
  def entityType: String = "Place"

  override def isRelevant: Boolean = false
}

class Technology(text: String) extends NamedEntity(text) {
  def entityType: String = "Technology"

  /**
   * Technology hace match si es palabra completa, case-sensitive.
   */
  override def matches(text: String): Boolean = {
    val escapedText = java.util.regex.Pattern.quote(this.text)
    val regex = s"(?<!\\w)$escapedText(?!\\w)".r
    regex.findFirstIn(text).isDefined
  }

  override def isRelevant: Boolean = false
}

class ProgrammingLanguage(text: String) extends Technology(text) {
  override def entityType: String = "ProgrammingLanguage"

  override def isRelevant: Boolean = true
}

abstract class Event(text: String) extends NamedEntity(text)

class Conference(text: String) extends Event(text) {
  def entityType: String = "Conference"

  override def isRelevant: Boolean = true
}
