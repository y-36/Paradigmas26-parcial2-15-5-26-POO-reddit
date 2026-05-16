# Resumen del Laboratorio: Named Entity Recognition en Posts de Reddit

## 📋 Objetivo General
Implementar un sistema de **Reconocimiento de Entidades Nombradas (NER)** que analiza títulos de posts de Reddit para detectar y clasificar entidades nombradas (personas, organizaciones, universidades, lenguajes de programación, lugares y conferencias).

---

## 🏗️ Arquitectura del Proyecto

### Jerarquía de Clases
```
NamedEntity (clase abstracta)
├── Person
├── Organization
│   └── University (es-un Organization, pero isRelevant = true)
├── Place
├── Technology
│   └── ProgrammingLanguage (es-una Technology, pero isRelevant = true)
└── Event (clase abstracta)
    └── Conference
```

---

## 📦 Módulos Principales

### 1. **NamedEntity.scala** - Clases base para entidades nombradas

**Clase Abstracta: `NamedEntity(text: String)`**

| Método | Descripción |
|--------|------------|
| `entityType: String` | Retorna el tipo de la entidad (Person, Organization, etc.) |
| `describe: String` | Retorna una descripción formateada: `[Tipo] Texto` |
| `matches(text: String): Boolean` | Verifica si la entidad aparece en el texto usando regex case-insensitive |
| `isRelevant: Boolean` | Indica si la entidad es relevante (default: true) |

**Subclases implementadas:**

- **`Person`**: Detecta nombres de personas.
  - Override `matches()`: Case-sensitive (busca exacto)
  
- **`Organization`**: Detecta organizaciones.
  - `isRelevant = false`
  
- **`University(Organization)`**: Detecta universidades (hereda de Organization).
  - Override `isRelevant = true` (son relevantes)
  
- **`Place`**: Detecta lugares geográficos.
  - `isRelevant = false`
  
- **`Technology`**: Detecta tecnologías.
  - Override `matches()`: Case-sensitive
  - `isRelevant = false`
  
- **`ProgrammingLanguage(Technology)`**: Detecta lenguajes de programación.
  - Override `entityType = "ProgrammingLanguage"`
  - Override `isRelevant = true` (son relevantes)
  
- **`Conference`**: Detecta conferencias.
  - Hereda de `Event` (abstracta)
  - `isRelevant = true`

---

### 2. **Dictionary.scala** - Carga de diccionarios

**Responsabilidad**: Cargar colecciones de entidades desde archivos de texto.

**Métodos:**

| Función | Parámetros | Retorna | Descripción |
|---------|-----------|---------|------------|
| `loadFromFile()` | `filePath`, `entityType` | `List[NamedEntity]` | Lee un archivo línea por línea y crea entidades del tipo especificado usando un `match` statement |
| `loadAll()` | - | `List[NamedEntity]` | Carga todos los diccionarios disponibles (`people.txt`, `universities.txt`, `languages.txt`, `organizations.txt`, `places.txt`, `conferences.txt`) y los concatena |

**Archivos de entrada:**
- `data/people.txt` → Personas
- `data/universities.txt` → Universidades
- `data/languages.txt` → Lenguajes de programación
- `data/organizations.txt` → Organizaciones
- `data/places.txt` → Lugares
- `data/conferences.txt` → Conferencias

---

### 3. **Analyzer.scala** - Análisis de entidades

**Responsabilidad**: Detectar entidades en texto y generar estadísticas.

**Métodos:**

| Función | Parámetros | Retorna | Descripción |
|---------|-----------|---------|------------|
| `detectEntities()` | `text`, `dictionary` | `List[NamedEntity]` | Filtra del diccionario las entidades cuyo `matches()` devuelve true en el texto dado |
| `detectRelevant()` | `text`, `dictionary` | `List[NamedEntity]` | Similar a `detectEntities()` pero solo retorna entidades donde `isRelevant == true` |
| `countByType()` | `entities` | `Map[String, Int]` | Agrupa entidades por `entityType` y cuenta cuántas hay de cada tipo |

---

### 4. **FileIO.scala** - Entrada/Salida

**Responsabilidad**: Descargar feeds, leer archivos locales y extraer datos.

**Métodos:**

| Función | Retorna | Descripción |
|---------|---------|------------|
| `readSubscriptions()` | `List[String]` | Retorna las URLs de los subreddits a analizar (localhost:8123) |
| `downloadFeed()` | `String` | Descarga el contenido JSON de una URL usando `Source.fromURL()` |
| `extractPostTitles()` | `List[String]` | Parsea JSON usando json4s y extrae todos los campos `"title"` |
| `readLines()` | `List[String]` | Lee un archivo filtrando líneas vacías y comentarios (#) |

---

### 5. **Formatters.scala** - Formateo de salida

**Responsabilidad**: Convertir resultados del análisis a texto legible.

**Métodos:**

| Función | Parámetros | Retorna | Descripción |
|---------|-----------|---------|------------|
| `formatNERResult()` | `postTitle`, `entities` | `String` | Formatea un post con sus entidades detectadas (una por línea) |
| `formatGroupedNERResult()` | `postTitle`, `entities` | `String` | **Función principal**: agrupa entidades por tipo (alfabéticamente), cuenta cuántas hay de cada tipo, las ordena alfabéticamente dentro de cada tipo |
| `formatEntityStats()` | `counts` | `String` | Formatea estadísticas globales ordenadas por cantidad (mayor a menor) |

---

### 6. **Main.scala** - Flujo principal

**Flujo de ejecución:**

```
1. Cargar diccionario (Dictionary.loadAll())
   └─ Imprime: "Diccionario cargado: X entidades"

2. Leer subscripciones (FileIO.readSubscriptions())
   
3. Para cada subreddit:
   ├─ Descargar feed (FileIO.downloadFeed())
   ├─ Extraer títulos (FileIO.extractPostTitles())
   └─ Para cada título:
      ├─ Detectar todas las entidades (Analyzer.detectEntities())
      ├─ Detectar solo relevantes (Analyzer.detectRelevant())
      └─ Imprimir con formato agrupado (Formatters.formatGroupedNERResult())

4. Contar entidades por tipo (Analyzer.countByType())

5. Imprimir estadísticas finales (Formatters.formatEntityStats())
```

---

## 🎯 Ejercicios Resueltos

### Ejercicio 1: Jerarquía de Clases (POO)
- ✅ Crear clase abstracta `NamedEntity`
- ✅ Implementar subclases específicas (Person, Organization, University, etc.)
- ✅ Usar `override` para personalizar `matches()` e `isRelevant`
- ✅ Aplicar herencia (University hereda de Organization, ProgrammingLanguage hereda de Technology)

### Ejercicio 2: Carga de Diccionarios
- ✅ Implementar `loadFromFile()` con pattern matching (`match` statement)
- ✅ Convertir cada línea en una entidad del tipo correspondiente
- ✅ Combinar múltiples diccionarios en `loadAll()`

### Ejercicio 3: Detección de Entidades
- ✅ Implementar `detectEntities()` filtrando entidades que hace match
- ✅ Implementar `detectRelevant()` considerando `isRelevant`
- ✅ Usar expresiones regulares para búsqueda case-insensitive con límites de palabra

### Ejercicio 4: Análisis de Estadísticas
- ✅ Implementar `countByType()` usando `groupBy()` y `mapValues()`
- ✅ Retornar un `Map[String, Int]`

### Ejercicio 5: Formateo de Salida
- ✅ Implementar `formatGroupedNERResult()` agrupando y ordenando entidades
- ✅ Implementar `formatEntityStats()` ordenando por frecuencia descendente

### Ejercicio 6: Pipeline Completo
- ✅ Integrar todos los módulos en `Main.scala`
- ✅ Descargar y procesar posts de múltiples subreddits
- ✅ Generar reporte completo de entidades detectadas

---

## 🔑 Conceptos de POO Utilizados

| Concepto | Dónde | Explicación |
|----------|-------|------------|
| **Herencia** | `University extends Organization` | Reutilizar funcionalidad de la clase padre |
| **Override** | `matches()`, `isRelevant` | Personalizar comportamiento en subclases |
| **Abstracción** | `NamedEntity`, `Event` | Definir interfaz común para distintos tipos |
| **Polimorfismo** | `entity.matches()`, `entity.describe` | Llamar métodos sin saber el tipo exacto |
| **Encapsulación** | `text` como parámetro constructor | Mantener estado dentro de la clase |
| **Object/Singleton** | `Dictionary`, `Analyzer`, `FileIO`, `Formatters` | Métodos de utilidad sin estado mutable |

---

## 📊 Ejemplo de Ejecución

```
Diccionario cargado: 2847 entidades.

Descargando posts de: http://localhost:8123/r/scala/.json

============================================================
http://localhost:8123/r/scala/.json
============================================================
Post: "Getting started with Scala and Functional Programming"
Entidades detectadas:
 Conference (0):
 Organization (1):
  Apache Foundation
 Person (1):
  Martin Odersky
 ProgrammingLanguage (2):
  Scala
  Python

Post: "New MIT research on type systems"
Entidades detectadas:
 Organization (1):
  MIT
 University (1):
  MIT

...

=== Estadísticas de entidades ===
ProgrammingLanguage: 154
Person: 89
University: 45
Conference: 23
Organization: 18
Place: 12
```

---

## 🛠️ Tecnologías Utilizadas

- **Lenguaje**: Scala
- **Build**: sbt (Scala Build Tool)
- **Parsing JSON**: json4s
- **Expresiones Regulares**: `scala.util.matching.Regex`
- **E/S**: `scala.io.Source`

---

## 📁 Estructura de Archivos

```
src/main/scala/
├── Main.scala              # Punto de entrada y flujo principal
├── NamedEntity.scala       # Clases para entidades nombradas
├── Dictionary.scala        # Carga de diccionarios
├── Analyzer.scala          # Detección y estadísticas
├── FileIO.scala            # Descarga y lectura de archivos
└── Formatters.scala        # Formateo de salida

data/
├── people.txt              # Diccionario de personas
├── universities.txt        # Diccionario de universidades
├── languages.txt           # Diccionario de lenguajes
├── organizations.txt       # Diccionario de organizaciones
├── places.txt              # Diccionario de lugares
└── conferences.txt         # Diccionario de conferencias
```

---

## ✅ Verificación de Funcionalidad

- [x] El diccionario carga correctamente todas las entidades
- [x] El análisis detecta entidades relevantes e irrelevantes
- [x] Las estadísticas se generan correctamente
- [x] La salida formateada es clara y organizada
- [x] El `.gitignore` está correctamente configurado para Scala/sbt
