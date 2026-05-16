/**
 * Responsable de convertir los resultados del análisis a texto para mostrar.
 */
object Formatters {

  /**
   * Formatea el análisis NER de un post individual.
   *
   * @param postTitle título del post analizado
   * @param entities  entidades detectadas en ese post
   * @return bloque de texto con el título y las entidades encontradas
   */
  def formatNERResult(postTitle: String, entities: List[NamedEntity]): String = {
    val header = s"""Post: "$postTitle"\nEntidades detectadas:"""
    val body =
      if (entities.isEmpty) "  (sin entidades detectadas)"
      else entities.map(e => s"  ${e.describe}").mkString("\n")
    s"$header\n$body"
  }

  /**
   * Formatea el análisis NER agrupando entidades por tipo de forma alfabética.
   *
   * @param postTitle título del post analizado
   * @param entities  entidades detectadas en ese post
   * @return bloque de texto con el título y las entidades agrupadas por tipo
   */
  def formatGroupedNERResult(postTitle: String, entities: List[NamedEntity]): String = {
    val header = s"""Post: "$postTitle"\nEntidades detectadas:"""
    
    if (entities.isEmpty) {
      s"$header\n  (sin entidades detectadas)"
    } else {
      // Agrupar por tipo y ordenar alfabéticamente por entityType
      val grouped = entities.groupBy(_.entityType).toList.sortBy(_._1)
      
      val groupedLines = grouped.map { case (entityType, entitiesOfType) =>
        val count = entitiesOfType.length
        val entitiesText = entitiesOfType
          .map(_.text)
          .sorted
          .map(text => s"  $text")
          .mkString("\n")
        s" $entityType ($count):\n$entitiesText"
      }
      
      s"$header\n${groupedLines.mkString("\n")}"
    }
  }

  /**
   * Formatea un resumen de estadísticas de entidades por tipo.
   *
   * @param counts mapa de entityType → cantidad
   * @return texto con las estadísticas ordenadas por cantidad (de mayor a menor)
   */
  def formatEntityStats(counts: Map[String, Int]): String = {
    val lines = counts.toList
      .sortBy(-_._2)
      .map { case (entityType, count) => s"$entityType: $count" }
    ("=== Estadísticas de entidades ===" :: lines).mkString("\n")
  }
}
