/**
 * Responsable de detectar entidades nombradas en texto libre y
 * producir estadísticas sobre ellas.
 */
object Analyzer {

  /**
   * Detecta las entidades del diccionario que aparecen en el texto dado.
   *
   * @param text       texto a analizar (ej: título o cuerpo de un post)
   * @param dictionary lista de entidades conocidas (cargadas desde los diccionarios)
   * @return lista de entidades cuyo texto aparece en el texto analizado
   */
def detectEntities(
      text: String,
      dictionary: List[NamedEntity]
  ): List[NamedEntity] = {
    dictionary.filter { entity =>
      val escapedText = java.util.regex.Pattern.quote(entity.text)
      // (?<!\w) significa "que no tenga una letra/número a la izquierda"
      // (?!\w) significa "que no tenga una letra/número a la derecha"
      val regex = s"(?i)(?<!\\w)$escapedText(?!\\w)".r
      regex.findFirstIn(text).isDefined
    }
  }
  /**
   * Cuenta cuántas entidades de cada tipo fueron detectadas.
   *
   * @param entities lista de entidades detectadas
   * @return mapa de entityType → cantidad de apariciones
   */
  def countByType(entities: List[NamedEntity]): Map[String, Int] = {
    entities.groupBy(_.entityType).view.mapValues(_.size).toMap
  }
}