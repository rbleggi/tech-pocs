package com.rbleggi.domainllm

case class Context(domain: String, question: String)

case class LLMResponse(response: String, confidence: Double, terms: List[String])

trait DomainStrategy:
  def generate(context: Context): LLMResponse

class MedicalDomain extends DomainStrategy:
  private val knowledge = Map(
    "febre" -> "Body temperature increase, usually above 37.5C. May indicate infection.",
    "pressao alta" -> "Arterial hypertension, when blood pressure is elevated. Requires medical monitoring.",
    "diabetes" -> "Metabolic condition characterized by elevated blood glucose levels.",
    "gripe" -> "Viral respiratory infection with symptoms like fever, cough and body aches."
  )

  def generate(context: Context): LLMResponse =
    val lowerQuestion = context.question.toLowerCase
    val foundTerm = knowledge.keys.find(t => lowerQuestion.contains(t))

    foundTerm match
      case Some(term) =>
        LLMResponse(knowledge(term), 0.95, List(term))
      case None =>
        LLMResponse("Medical term not found in knowledge base.", 0.3, List.empty)

class LegalDomain extends DomainStrategy:
  private val knowledge = Map(
    "contrato" -> "Legal agreement between parties establishing reciprocal obligations with legal validity.",
    "processo" -> "Judicial procedure for conflict resolution or law enforcement.",
    "habeas corpus" -> "Constitutional guarantee protecting freedom of movement against abuse of power.",
    "testemunha" -> "Person who testifies in court about facts relevant to the case."
  )

  def generate(context: Context): LLMResponse =
    val lowerQuestion = context.question.toLowerCase
    val foundTerm = knowledge.keys.find(t => lowerQuestion.contains(t))

    foundTerm match
      case Some(term) =>
        LLMResponse(knowledge(term), 0.95, List(term))
      case None =>
        LLMResponse("Legal term not found in knowledge base.", 0.3, List.empty)

class TechDomain extends DomainStrategy:
  private val knowledge = Map(
    "api" -> "Application Programming Interface, enables integration between systems.",
    "cloud" -> "Cloud computing, IT infrastructure accessed via internet.",
    "kubernetes" -> "Container orchestration platform for deploy automation and scaling.",
    "scala" -> "Functional and object-oriented programming language that runs on JVM."
  )

  def generate(context: Context): LLMResponse =
    val lowerQuestion = context.question.toLowerCase
    val foundTerm = knowledge.keys.find(t => lowerQuestion.contains(t))

    foundTerm match
      case Some(term) =>
        LLMResponse(knowledge(term), 0.95, List(term))
      case None =>
        LLMResponse("Technology term not found in knowledge base.", 0.3, List.empty)

class DomainLLM:
  private val domains = Map(
    "medico" -> MedicalDomain(),
    "juridico" -> LegalDomain(),
    "tecnologia" -> TechDomain()
  )

  def query(context: Context): LLMResponse =
    domains.get(context.domain) match
      case Some(domain) => domain.generate(context)
      case None => LLMResponse("Domain not supported.", 0.0, List.empty)

  def addDomain(name: String, domain: DomainStrategy): Unit =
    domains + (name -> domain)

@main def run(): Unit =
  println("Domain Fine Tuned LLM")
