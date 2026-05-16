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
  def detectEntities(text: String, dictionary: List[NamedEntity]): List[NamedEntity] = {
    dictionary.filter(entity => entity.matches(text))
  }

  /**
   * Detecta solo las entidades relevantes que hacen match en el texto.
   *
   * @param text       texto a analizar
   * @param dictionary lista de entidades conocidas
   * @return lista de entidades relevantes (isRelevant == true) que hacen match
   */
  def detectRelevant(text: String, dictionary: List[NamedEntity]): List[NamedEntity] = {
    dictionary.filter(entity => entity.isRelevant && entity.matches(text))
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
