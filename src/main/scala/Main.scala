object Main {
  def main(args: Array[String]): Unit = {

    val dictionary: List[NamedEntity] = Dictionary.loadAll()
    println(s"Diccionario cargado: ${dictionary.size} entidades.\n")

    val subscriptions = FileIO.readSubscriptions()

    val allDetected: List[NamedEntity] = subscriptions.flatMap { url =>
      println(s"Descargando posts de: $url")
      val json   = FileIO.downloadFeed(url)
      val titles = FileIO.extractPostTitles(json)
      println(s"\n${"=" * 60}\n$url\n${"=" * 60}")
      titles.flatMap { title =>
        val allEntities = Analyzer.detectEntities(title, dictionary)
        val relevantEntities = Analyzer.detectRelevant(title, dictionary)
        println(Formatters.formatGroupedNERResult(title, relevantEntities))
        allEntities
      }
    }

    println(s"\n${Formatters.formatEntityStats(Analyzer.countByType(allDetected))}")
  }
}
